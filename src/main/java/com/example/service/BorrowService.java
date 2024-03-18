package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.dto.Borrow;
import org.springframework.transaction.annotation.Transactional;

public interface BorrowService extends IService<Borrow> {

    //用户续借
    boolean updatePreReturnDay(Integer bookId);
}
