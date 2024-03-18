package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.dto.Book;

import java.util.List;

public interface BookService extends IService<Book> {

    Book findBookByName(String bookName);
    void insertBook(Book book);
    void updateBook(Book book);
    void deleteBookByName(String bookName);

    //查询所有图书
    List<Book> selectAllBooks();

    //根据图书id删除图书
    void deleteById(Integer id);
}
