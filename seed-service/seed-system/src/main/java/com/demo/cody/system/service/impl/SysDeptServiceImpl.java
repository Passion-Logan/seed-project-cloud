package com.demo.cody.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.cody.common.entity.SysDept;
import com.demo.cody.system.mapper.SysDeptMapper;
import com.demo.cody.system.service.ISysDeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysDeptServiceImpl.class);

}
