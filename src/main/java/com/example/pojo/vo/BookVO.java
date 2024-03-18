package com.example.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class BookVO {
    Date dateBorrow;
    Date preReturnDay;
    Integer bookId;
    String bookName;
}
