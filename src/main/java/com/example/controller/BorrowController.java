package com.example.controller;

import com.example.pojo.RestBean;
import com.example.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrow")
@CrossOrigin
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @PutMapping("/updatePreReturnDay")
    public RestBean<Void> updatePreReturnDay(@RequestParam Integer bookId){
        boolean b = borrowService.updatePreReturnDay(bookId);
        if(b){
            return RestBean.success();
        }else {
            return RestBean.failure(500,"没有续借次数了");
        }
    }

}
