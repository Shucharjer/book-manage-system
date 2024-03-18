package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.dto.ReaderType;

public interface ReaderTypeService extends IService<ReaderType> {

    //添加读者类别
    void addReaderType(ReaderType readerType);

    //查询读者类别信息
    ReaderType findReaderType(String typeName);

    //删除读者类别
    void deleteByName(String tyName);

    //更新读者类别信息
    void updateByTypeId(ReaderType readerType);
}
