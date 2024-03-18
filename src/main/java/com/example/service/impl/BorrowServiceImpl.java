package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.BorrowMapper;
import com.example.mapper.ReaderMapper;
import com.example.mapper.ReaderTypeMapper;
import com.example.pojo.dto.Borrow;
import com.example.pojo.dto.Reader;
import com.example.pojo.dto.ReaderType;
import com.example.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements BorrowService {

    @Autowired
    private BorrowMapper borrowMapper;

    @Autowired
    private ReaderMapper readerMapper;

    @Autowired
    private ReaderTypeMapper readerTypeMapper;

    @Override
    @Transactional
    public boolean updatePreReturnDay(Integer bookId) {
        // 查询读者序号
        LambdaQueryWrapper<Borrow> borrowQueryWrapper = new LambdaQueryWrapper<>();
        borrowQueryWrapper.eq(Borrow::getBookId, bookId);
        Borrow borrow = borrowMapper.selectOne(borrowQueryWrapper);

        if (borrow != null) {
            Integer readerId = borrow.getReaderId();

            // 查询读者类别
            LambdaQueryWrapper<Reader> readerQueryWrapper = new LambdaQueryWrapper<>();
            readerQueryWrapper.eq(Reader::getId, readerId);
            Reader reader = readerMapper.selectOne(readerQueryWrapper);

            if (reader != null) {
                Integer readerType = reader.getType();

                // 查找该类别可续借次数
                LambdaQueryWrapper<ReaderType> readerTypeQueryWrapper = new LambdaQueryWrapper<>();
                readerTypeQueryWrapper.eq(ReaderType::getType, readerType);
                ReaderType readerType1 = readerTypeMapper.selectOne(readerTypeQueryWrapper);

                if (readerType != null) {
                    Integer canContinueTimes = readerType1.getCanContinueTimes();

                    if (canContinueTimes > 0) {
                        // 续借次数-1，该书籍应还时间加30天
                        LambdaUpdateWrapper<ReaderType> readerTypeUpdateWrapper = new LambdaUpdateWrapper<>();
                        readerTypeUpdateWrapper
                                .eq(ReaderType::getType, readerType)
                                .setSql("CanContinueTimes = CanContinueTimes - 1");
                        readerTypeMapper.update(null, readerTypeUpdateWrapper);

                        LambdaUpdateWrapper<Borrow> borrowUpdateWrapper = new LambdaUpdateWrapper<>();
                        borrowUpdateWrapper
                                .eq(Borrow::getBookId, bookId)
                                .setSql("idDateRetPlan = DATE_ADD(idDateRetPlan, INTERVAL 30 DAY)");
                        borrowMapper.update(null, borrowUpdateWrapper);

                        return true;
                    } else {
                        // 续借失败
                        return false;
                    }
                }
            }
        }

        // 失败，返回false
        return false;
    }
}
