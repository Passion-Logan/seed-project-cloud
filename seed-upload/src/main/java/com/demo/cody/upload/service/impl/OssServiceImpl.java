package com.demo.cody.upload.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.demo.cody.upload.entity.BucketInfo;
import com.demo.cody.upload.entity.FileInfo;
import com.demo.cody.upload.entity.UploadUri;
import com.demo.cody.upload.properties.OssProperties;
import com.demo.cody.upload.service.FileService;
import com.demo.cody.upload.utils.FileProcessUtil;
import com.demo.cody.upload.utils.MockMultipartFile;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

/**
 * @author wql
 * @desc OssServiceImpl
 * @date 2021/9/29
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/9/29
 */
@RequiredArgsConstructor
@Slf4j
public class OssServiceImpl implements FileService {

    private final OssProperties ossProperties;
    private final OSS ossClient;

    @Resource(name = "fileForkJoinPool")
    private ForkJoinPool fileForkJoinPool;

    @Override
    public boolean bucketExists(String bucketName) {
        return ossClient.doesBucketExist(bucketName);
    }

    @Override
    public boolean makeBucket(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (!flag) {
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            ossClient.createBucket(createBucketRequest);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<BucketInfo> listBuckets() {
        List<BucketInfo> bucketInfoList = new ArrayList<>();
        ZoneId zone = ZoneId.systemDefault();
        ossClient.listBuckets().forEach(bucket -> {
            Instant instant = bucket.getCreationDate().toInstant();
            BucketInfo bucketInfo = new BucketInfo(bucket.getName(), LocalDateTime.ofInstant(instant, zone));
            bucketInfoList.add(bucketInfo);
        });
        return bucketInfoList;
    }

    @Override
    public boolean removeBucket(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            List<FileInfo> fileInfoList = listFiles(bucketName);
            // 有对象文件，则删除失败
            if (fileInfoList.size() > 0) {
                return false;
            }
            // 删除存储桶，注意，只有存储桶为空时才能删除成功。
            ossClient.deleteBucket(bucketName);
            flag = bucketExists(bucketName);
            return !flag;
        }
        return false;
    }

    @Override
    public List<FileInfo> listFiles(String bucketName) {
        List<FileInfo> fileInfoList = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        ZoneId zone = ZoneId.systemDefault();
        if (flag) {
            ossClient.listObjects(bucketName).getObjectSummaries().forEach(ossObjectSummary -> {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setName(ossObjectSummary.getKey());
                fileInfo.setSize(ossObjectSummary.getSize());
                if (null != ossObjectSummary.getOwner() && StrUtil.isNotBlank(ossObjectSummary.getOwner().getDisplayName())) {
                    fileInfo.setOwner(ossObjectSummary.getOwner().getDisplayName());
                }
                if (null != ossObjectSummary.getLastModified()) {
                    Instant instant = ossObjectSummary.getLastModified().toInstant();
                    fileInfo.setLastModified(LocalDateTime.ofInstant(instant, zone));
                }
                fileInfoList.add(fileInfo);
            });
        }
        return fileInfoList;
    }

    @Override
    public List<FileInfo> listFiles() {
        return listFiles(ossProperties.getBucketName());
    }

    @Override
    public FileInfo getFileInfo(String fileId) {
        return getFileInfo(ossProperties.getBucketName(), fileId);
    }

    @Override
    public FileInfo getFileInfo(String bucketName, String fileId) {
        return Optional.ofNullable(ossClient.getObjectMetadata(bucketName, fileId)).map(objectMetadata -> {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setSize(objectMetadata.getContentLength());
            Instant instant = objectMetadata.getLastModified().toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            fileInfo.setLastModified(instant.atZone(zoneId).toLocalDateTime());
            return fileInfo;
        }).orElse(null);
    }

    @Override
    @SneakyThrows
    public Map<String, FileInfo> getFileInfos(String bucketName, List<String> fileIdList) {
        Map<String, FileInfo> resultMap = Maps.newConcurrentMap();
        fileForkJoinPool.submit(() ->
                fileIdList.parallelStream()
                        .forEach(id ->
                                Optional.ofNullable(getFileInfo(bucketName, id))
                                        .ifPresent((in -> resultMap.put(id, in))))).get();
        return resultMap;
    }

    @Override
    public Map<String, FileInfo> getFileInfos(List<String> fileIdList) {
        return getFileInfos(ossProperties.getBucketName(), fileIdList);
    }

    @Override
    @SneakyThrows
    public String uploadFile(String bucketName, String folderName, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new RuntimeException("文件为空");
        }
        // 文件类型
        String type = FileTypeUtil.getType(multipartFile.getInputStream());
        // UUID重命名
        String fileName = FileProcessUtil.reName(type);
        // 年/月/日/file
        String finalPath = "";
        if (StrUtil.isNotEmpty(folderName)) {
            finalPath = finalPath + folderName + "/";
        } else {
            finalPath = finalPath + "default/";
        }
        finalPath = finalPath + FileProcessUtil.getDateFolder() + "/" + fileName;
        ossClient.putObject(bucketName, finalPath, new ByteArrayInputStream(multipartFile.getBytes()));
        return finalPath;
    }

    @Override
    @SneakyThrows
    public Map<String, String> uploadFiles(String bucketName, String folderName, Map<String, MultipartFile> multipartFileMap) {
        Map<String, String> retVal = CollectionUtil.createMap(HashMap.class);
        for (String key : multipartFileMap.keySet()) {
            MultipartFile multipartFile = multipartFileMap.get(key);
            String finalPath = uploadFile(bucketName, folderName, multipartFile);
            retVal.put(key, finalPath);
        }

        return retVal;
    }

    @Override
    public String getFileUrl(String bucketName, String fileId) {
        return getFileUrl(bucketName, fileId);
    }

    @Override
    @SneakyThrows
    public Map<String, String> getFileUrls(String bucketName, List<String> fileIdList) {
        Map<String, String> resultMap = Maps.newConcurrentMap();
        fileForkJoinPool.submit(() -> fileIdList.parallelStream().forEach(id -> {
            String url = getFileUrl(bucketName, id);
            resultMap.put(id, url);
        })).get();
        return resultMap;
    }

    @Override
    public Map<String, String> getFileUrls(List<String> fileIdList) {
        return getFileUrls(ossProperties.getBucketName(), fileIdList);
    }

    @Override
    public UploadUri getUploadUrl(String folderName, String extName) {
        return null;
    }

    @Override
    public void removeFile(String bucketName, String fileId) {
        ossClient.deleteObject(bucketName, fileId);
    }

    @Override
    public List<String> removeFiles(String bucketName, List<String> fileIdList) {
        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(fileIdList));
        return deleteObjectsResult.getDeletedObjects();
    }

    @Override
    public void copy(String sourceBucket, String sourceFile, String targetBucket, String targetFile) {
        ossClient.copyObject(sourceBucket, sourceFile, targetBucket, targetFile);
    }

    @Override
    public String uploadFile(String folderName, MultipartFile multipartFile) {
        return uploadFile(ossProperties.getBucketName(), folderName, multipartFile);
    }

    @Override
    @SneakyThrows
    public String uploadFile(String folderName, BufferedImage bufferedImage, String formatName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, formatName, outputStream);
        return uploadFile(ossProperties.getBucketName(), folderName, new MockMultipartFile(folderName, outputStream.toByteArray()));
    }

    @Override
    public Map<String, String> uploadFiles(String folderName, Map<String, MultipartFile> multipartFileMap) {
        return uploadFiles(ossProperties.getBucketName(), folderName, multipartFileMap);
    }

    @Override
    public Map<String, String> uploadFiles(Map<String, MultipartFile> multipartFileMap) {
        return uploadFiles(ossProperties.getBucketName(), null, multipartFileMap);
    }

    @Override
    public InputStream downloadFile(String fileId) {
        return downloadFile(ossProperties.getBucketName(), fileId);
    }

    @Override
    public InputStream downloadFile(String bucketName, String fileId) {
        return downloadFile(bucketName, fileId);
    }

    @Override
    public Map<String, InputStream> downloadFiles(List<String> fileIdList) {
        return downloadFiles(ossProperties.getBucketName(), fileIdList);
    }

    @Override
    @SneakyThrows
    public Map<String, InputStream> downloadFiles(String bucketName, List<String> fileIdList) {
        Map<String, InputStream> resultMap = Maps.newConcurrentMap();
        fileForkJoinPool.submit(() -> fileIdList.parallelStream().forEach(id -> {
            try {
                Optional.ofNullable(downloadFile(bucketName, id))
                        .ifPresent((in -> resultMap.put(id, in)));
            } catch (Exception e) {
                log.warn("获取文件失败！文件id:{}", id, e);
            }
        })).get();
        return resultMap;
    }

    @Override
    public String getFileUrl(String fileId) {
        return getFileUrl(ossProperties.getBucketName(), fileId);
    }

    @Override
    public void removeFile(String fileId) {
        removeFile(ossProperties.getBucketName(), fileId);
    }

    @Override
    public List<String> removeFiles(List<String> fileIdList) {
        return removeFiles(ossProperties.getBucketName(), fileIdList);
    }

    @Override
    public void copy(String sourceFile, String targetFile) {
        copy(ossProperties.getBucketName(), sourceFile, ossProperties.getBucketName(), targetFile);
    }

}
