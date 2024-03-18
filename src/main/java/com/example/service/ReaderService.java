package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.dto.Book;
import com.example.pojo.dto.Reader;
import com.example.pojo.vo.BookVO;
import com.example.pojo.vo.PasswordVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReaderService extends IService<Reader> {

    //根据读者id查询读者
    Reader findReaderById(Integer id);

    //根据读者id删除读者信息
    void deleteReaderById(Integer id);

    //添加读者信息
    void addReader(Reader reader);

    //更新读者信息
    void updateReader(Reader reader);

    //读者登录
    boolean login(String readerName, String password);

    @Transactional
    //读者借书
    void lendBook(String readerName,Integer bookId, String operatorBorrowName);

    @Transactional
    //读者还书
    void returnBook(String readerName,Integer bookId, String operatorReturnName);

    //根据读者姓名查询读者信息
    Reader findReaderByName(String readerName);

    //查询所有图书操作员
    List<Reader> getAllBookOperator();

    //查询指定读者借阅的书籍
    List<BookVO> getMyBooks(String readerName);

    //查询所有读者和图书管理员
    List<Reader> findLowLevelReader();

    void updateReader2(Reader reader);

    //改密码
    Integer changePassword(PasswordVO passwordVO);
}
