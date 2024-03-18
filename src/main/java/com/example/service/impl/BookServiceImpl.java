package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.BookMapper;
import com.example.pojo.dto.Book;
import com.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper,Book> implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public Book findBookByName(String bookName) {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("bkName", bookName);
        return bookMapper.selectOne(wrapper);
    }

    @Override
    public void insertBook(Book book) {
        System.out.println("==========");
        System.out.println(book);
        bookMapper.insert(book);
    }

    @Override
    public void updateBook(Book book) {
        LambdaUpdateWrapper<Book> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper
                .eq(Book::getId,book.getId())
                .set(Book::getCode,book.getCode())
                .set(Book::getBookName,book.getBookName())
                .set(Book::getAuthor,book.getAuthor())
                .set(Book::getPublisher,book.getPublisher())
                .set(Book::getPublishDay,book.getPublishDay())
                .set(Book::getIsbnId,book.getIsbnId())
                .set(Book::getCategoryId,book.getCategoryId())
                .set(Book::getLanguageId,book.getLanguageId())
                .set(Book::getPages,book.getPages())
                .set(Book::getPrice,book.getPrice())
                .set(Book::getDateIn,book.getDateIn())
                .set(Book::getBrief,book.getBrief())
                .set(Book::getBookCover,book.getBookCover())
                .set(Book::getStatus,book.getStatus());
        bookMapper.update(null,lambdaUpdateWrapper);
    }

    @Override
    public void deleteBookByName(String bookName) {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("bkName", bookName);
        bookMapper.delete(wrapper);
    }

    @Override
    public List<Book> selectAllBooks() {
        LambdaQueryWrapper<Book> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        return bookMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void deleteById(Integer id) {
        LambdaQueryWrapper<Book> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Book::getId,id);

        bookMapper.delete(lambdaQueryWrapper);
    }


}
