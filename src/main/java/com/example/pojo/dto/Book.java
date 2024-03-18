package com.example.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 图书实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_book")
public class Book {
    //图书序号
    @TableId(value = "bkID",type = IdType.AUTO)
    Integer id;
    //图书条码号
    @TableField("bkCode")
    String code;
    //书名
    @TableField("bkName")
    String bookName;
    //作者
    @TableField("bkAuthor")
    String author;
    //出版社
    @TableField("bkPress")
    String publisher;
    //出版日期
    @TableField("bkDatePress")
    Date publishDay;
    //ISBN书号
    @TableField("bkISBN")
    String isbnId;
    //分类号
    @TableField("bkCatalog")
    String categoryId;
    //语言
    @TableField("bkLanguage")
    Integer languageId;
    //页数
    @TableField("bkPages")
    Integer pages;
    //价格
    @TableField("bkPrice")
    Double price;
    //入馆日期
    @TableField("bkDateIn")
    Date dateIn;
    //内容简介
    @TableField("bkBrief")
    String brief;
    //图书封面
    @TableField("bkCover")
    String bookCover;
    //图书状态
    @TableField("bkStatus")
    String status;
}
