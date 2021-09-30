package com.demo.upload.service;

import com.demo.upload.entity.BucketInfo;
import com.demo.upload.entity.FileInfo;
import com.demo.upload.entity.UploadUri;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author wql
 * @desc OssService
 * @date 2021/9/29
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/9/29
 */
public interface FileService {

//--------------------------------------------------------------桶操作---------------------------------------------------------------------------

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    boolean bucketExists(String bucketName);

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    boolean makeBucket(String bucketName);

    /**
     * 列出所有存储桶
     *
     * @return List<Bucket>
     */
    List<BucketInfo> listBuckets();

    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    boolean removeBucket(String bucketName);

//--------------------------------------------------------------文件列表---------------------------------------------------------------------------

    /**
     * 查询列表
     *
     * @param bucketName 桶
     * @return List<FileInfo>
     */
    List<FileInfo> listFiles(String bucketName);

    /**
     * 查询列表
     *
     * @return List<FileInfo>
     */
    List<FileInfo> listFiles();

//--------------------------------------------------------------文件信息---------------------------------------------------------------------------

    /**
     * 查询文件信息
     *
     * @param fileId 文件id
     * @return FileInfo
     */
    FileInfo getFileInfo(String fileId);

    /**
     * 查询文件信息
     *
     * @param bucketName 桶
     * @param fileId     文件id
     * @return FileInfo
     */
    FileInfo getFileInfo(String bucketName, String fileId);

    /**
     * 批量获取文件信息
     *
     * @param bucketName 桶
     * @param fileIdList 文件id列表
     * @return Map<String, FileInfo>
     */
    Map<String, FileInfo> getFileInfos(String bucketName, List<String> fileIdList);

    /**
     * 批量获取文件信息
     *
     * @param fileIdList 文件id列表
     * @return Map<String, FileInfo>
     */
    Map<String, FileInfo> getFileInfos(List<String> fileIdList);

//--------------------------------------------------------------上传---------------------------------------------------------------------------

    /**
     * 文件信息上传
     *
     * @param bucketName    存储桶名称
     * @param multipartFile 文件
     * @param folderName    自定义文件夹
     * @return String
     * @throws IOException IO异常
     */
    String uploadFile(String bucketName, String folderName, MultipartFile multipartFile) throws IOException;

    /**
     * 文件信息上传
     *
     * @param bucketName       存储桶名称
     * @param multipartFileMap 文件map
     * @param folderName       自定义文件夹
     * @return Map<String, String>
     * @throws IOException IO异常
     */
    Map<String, String> uploadFiles(String bucketName, String folderName, Map<String, MultipartFile> multipartFileMap) throws IOException;

    /**
     * 文件信息上传
     *
     * @param multipartFile 文件
     * @param folderName    自定义文件夹
     * @return String
     */
    String uploadFile(String folderName, MultipartFile multipartFile);

    /**
     * 文件信息上传
     *
     * @param bufferedImage 图片文件
     * @param formatName    图片格式
     * @param folderName    自定义文件夹
     * @return String
     */
    String uploadFile(String folderName, BufferedImage bufferedImage, String formatName);

    /**
     * 文件信息上传
     *
     * @param multipartFileMap 文件
     * @param folderName       自定义文件夹
     * @return Map<String, String>
     */
    Map<String, String> uploadFiles(String folderName, Map<String, MultipartFile> multipartFileMap);

    /**
     * 文件信息上传
     *
     * @param multipartFileMap 文件
     * @return Map<String, String>
     */
    Map<String, String> uploadFiles(Map<String, MultipartFile> multipartFileMap);

//--------------------------------------------------------------下载--------------------------------------------------------------------------

    /**
     * 文件下载 写入到流
     *
     * @param fileId 文件id
     * @return InputStream
     * @throws IOException IOException
     */
    InputStream downloadFile(String fileId) throws IOException;

    /**
     * 文件下载 写入到流
     *
     * @param bucketName 存储桶
     * @param fileId     文件id
     * @return InputStream
     * @throws IOException IOException
     */
    InputStream downloadFile(String bucketName, String fileId) throws IOException;

    /**
     * 文件批量下载不处理
     *
     * @param fileIdList 文件id列表
     * @return InputStream
     */
    Map<String, InputStream> downloadFiles(List<String> fileIdList);

    /**
     * 文件批量下载不处理
     *
     * @param bucketName 存储桶
     * @param fileIdList 文件id列表
     * @return InputStream
     */
    Map<String, InputStream> downloadFiles(String bucketName, List<String> fileIdList);

//--------------------------------------------------------------删除--------------------------------------------------------------------------

    /**
     * 删除文件
     *
     * @param bucketName 存储桶
     * @param fileId     文件id
     */
    void removeFile(String bucketName, String fileId);

    /**
     * 删除指定桶的多个文件对象,返回删除错误的对象列表，全部删除成功，返回空列表
     *
     * @param bucketName 存储桶
     * @param fileIdList 文件ids
     * @return List<String>
     */
    List<String> removeFiles(String bucketName, List<String> fileIdList);

    /**
     * 删除文件
     *
     * @param fileId 文件id
     */
    void removeFile(String fileId);

    /**
     * 删除默认桶的多个文件对象,返回删除错误的对象列表，全部删除成功，返回空列表
     *
     * @param fileIdList 文件ids
     * @return List<String>
     */
    List<String> removeFiles(List<String> fileIdList);

//--------------------------------------------------------------复制--------------------------------------------------------------------------

    /**
     * 复制文件
     *
     * @param sourceBucket 源桶
     * @param sourceFile   源文件id
     * @param targetBucket 目标桶
     * @param targetFile   目标文件id
     */
    void copy(String sourceBucket, String sourceFile, String targetBucket, String targetFile);

    /**
     * 复制文件
     *
     * @param sourceFile 源文件id
     * @param targetFile 目标文件id
     */
    void copy(String sourceFile, String targetFile);

//--------------------------------------------------------------访问地址--------------------------------------------------------------------------

    /**
     * 获取文件访问地址
     *
     * @param fileId 文件id
     * @return String
     */
    String getFileUrl(String fileId);

    /**
     * 获取文件访问地址
     *
     * @param bucketName 存储桶
     * @param fileId     文件id
     * @return String
     */
    String getFileUrl(String bucketName, String fileId);

    /**
     * 批量获取文件访问地址
     *
     * @param bucketName 存储桶
     * @param fileIdList 文件id列表
     * @return Map<String, String>
     */
    Map<String, String> getFileUrls(String bucketName, List<String> fileIdList);

    /**
     * 批量获取文件访问地址
     *
     * @param fileIdList 文件id列表
     * @return Map<String, String>
     */
    Map<String, String> getFileUrls(List<String> fileIdList);


//--------------------------------------------------------------获取上传地址--------------------------------------------------------------------------

    /**
     * 获取上传链接
     *
     * @param folderName 文件夹名称
     * @param extName    文件扩展名
     * @return UploadUri
     */
    UploadUri getUploadUrl(String folderName, String extName);

}
