package com.demo.cody.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.cody.model.entity.SysDictItem;

import java.util.List;

public interface ISysDictItemService extends IService<SysDictItem> {

    public List<SysDictItem> selectItemsByMainId(String mainId);

}
