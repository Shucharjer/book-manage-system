package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.dto.Borrow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BorrowMapper extends BaseMapper<Borrow> {

    //添加数据
    void add(@Param("readerId") Integer readerId,@Param("bookId") Integer bookId, @Param("operatorBorrowName") String operatorBorrowName);
}
