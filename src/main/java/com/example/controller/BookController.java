package com.example.controller;

import com.example.pojo.dto.Book;
import com.example.pojo.RestBean;
import com.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books")
@CrossOrigin
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/findBookByName")
    public RestBean<Book> findBookByName(@RequestParam String bookName) {
        Book book =  bookService.findBookByName(bookName);
        return RestBean.success(book);
    }

    @PostMapping("/insertBook")
    public RestBean<Void> insertBook(@RequestBody Book book) {
        bookService.insertBook(book);
        return RestBean.success();
    }

    @PutMapping("/update")
    public RestBean<Void> updateBook(@RequestBody Book book) {
        bookService.updateBook(book);
        return RestBean.success();
    }

    @DeleteMapping("/deleteByName")
    public RestBean<Void> deleteBookByName(String bookName) {
        bookService.deleteBookByName(bookName);
        return RestBean.success();
    }

    @DeleteMapping("/deleteById")
    public RestBean<Void> deleteById(@RequestParam Integer id){
        bookService.deleteById(id);
        return RestBean.success();
    }

    @GetMapping("/selectAllBooks")
    public RestBean<List<Book>> selectAllBooks(){
        List<Book> books = bookService.selectAllBooks();
        return RestBean.success(books);
    }

}
