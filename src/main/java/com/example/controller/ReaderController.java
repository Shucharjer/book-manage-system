package com.example.controller;

import com.example.pojo.RestBean;
import com.example.pojo.dto.Book;
import com.example.pojo.dto.Reader;

import com.example.pojo.vo.BookVO;
import com.example.pojo.vo.PasswordVO;
import com.example.service.ReaderService;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/readers")
@CrossOrigin
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/findReaderById/{id}")
    public RestBean<Reader> findReaderById(@PathVariable Integer id){
        Reader reader = readerService.findReaderById(id);
        return RestBean.success(reader);
    }

    @GetMapping("/getAllBookOperator")
    public RestBean<List<Reader>> getAllBookOperator(){
        List<Reader> readers = readerService.getAllBookOperator();
        return RestBean.success(readers);
    }

    @GetMapping("/getMyBooks")
    public RestBean<List<BookVO>> getMyBooks(@RequestParam String readerName){
        List<BookVO> books = readerService.getMyBooks(readerName);
        return RestBean.success(books);
    }

    @GetMapping("/findReaderByName")
    public RestBean<Reader> findReaderByName(@RequestParam String readerName){
        Reader reader = readerService.findReaderByName(readerName);
        return RestBean.success(reader);
    }
    
    @GetMapping("/findLowLevelReader")
    public RestBean<List<Reader>> findLowLevelReader(){
        List<Reader> readers = readerService.findLowLevelReader();
        return RestBean.success(readers);
    }


    @DeleteMapping("/deleteReaderById/{id}")
    public RestBean<Void> deleteReaderById(@PathVariable Integer id){
        readerService.deleteReaderById(id);
        return RestBean.success();
    }

    @PostMapping("/addReader")
    public RestBean<Void> addReader(@RequestBody Reader reader){
        readerService.addReader(reader);
        return RestBean.success();
    }



    @PostMapping ("/login")
    public RestBean login(@RequestBody Reader reader){
        String readerName = reader.getName();
        String password = reader.getPassword();
        boolean loginInfo = readerService.login(readerName,password);

        if(loginInfo){
            return RestBean.success();
        }else {
            return RestBean.failure(401,"用户名或密码错误");
        }

    }

    @PostMapping("/lendBook")
    public RestBean lendBook(@RequestParam String readerName, @RequestParam Integer bookId, @RequestParam String operatorBorrowName){
        readerService.lendBook(readerName,bookId,operatorBorrowName);
        return RestBean.success();
    }

    @PostMapping("/returnBook")
    public RestBean returnBook(@RequestParam String readerName,@RequestParam Integer bookId,@RequestParam String operatorReturnName){
        readerService.returnBook(readerName,bookId,operatorReturnName);
        return RestBean.success();
    }

    @PostMapping("/changePassword")
    public RestBean changePassword(@RequestBody PasswordVO passwordVO){
        Integer resCode = readerService.changePassword(passwordVO);
        if(resCode == -1){
            return RestBean.failure(-1,"旧密码输入错误");
        }else {
            return RestBean.success();
        }

    }

    @PutMapping("/updateReader")
    public RestBean<Void> updateReader(@RequestBody Reader reader){
        readerService.updateReader(reader);
        return RestBean.success();
    }

    @PutMapping("/updateReader2")
    public RestBean<Void> updateReader2(@RequestBody Reader reader){
        readerService.updateReader2(reader);
        return RestBean.success();
    }




}
