package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ReaderMapper;
import com.example.mapper.ReaderTypeMapper;
import com.example.pojo.dto.Book;
import com.example.pojo.dto.ReaderType;
import com.example.service.ReaderTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReaderTypeServiceImpl extends ServiceImpl<ReaderTypeMapper, ReaderType> implements ReaderTypeService {

    @Autowired
    private ReaderTypeMapper readerTypeMapper;

    @Override
    public void addReaderType(ReaderType readerType) {
        readerTypeMapper.insert(readerType);
    }

    @Override
    public ReaderType findReaderType(String typeName) {
        LambdaQueryWrapper<ReaderType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ReaderType::getTypeName,typeName);
        return readerTypeMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public void deleteByName(String tyName) {
        LambdaQueryWrapper<ReaderType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ReaderType::getTypeName,tyName);
        readerTypeMapper.delete(lambdaQueryWrapper);
    }

    @Override
    public void updateByTypeId(ReaderType readerType) {
        LambdaUpdateWrapper<ReaderType> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper
                .eq(ReaderType::getType, readerType.getType())
                .set(ReaderType::getTypeName, readerType.getTypeName())
                .set(ReaderType::getCanLendNum, readerType.getCanLendNum())
                .set(ReaderType::getCanLendDays, readerType.getCanLendDays())
                .set(ReaderType::getCanContinueTimes, readerType.getCanContinueTimes())
                .set(ReaderType::getPunishRate, readerType.getPunishRate())
                .set(ReaderType::getAccessValid, readerType.getAccessValid());

        readerTypeMapper.update(null, lambdaUpdateWrapper);
    }
}
