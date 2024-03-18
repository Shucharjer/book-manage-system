package com.example.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 读者实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_reader")
public class Reader {
    //读者id
    @TableId("rdID")
    Integer id;

    //读者姓名
    @TableField("rdName")
    String name;

    //性别
    @TableField("rdSex")
    String sex;

    //读者类别
    @TableField("rdType")
    Integer type;

    //单位代码
    @TableField("rdDept")
    String dept;

    //电话号码
    @TableField("rdPhone")
    String phone;

    //电子邮箱
    @TableField("rdEmail")
    String email;

    //登记日期
    @TableField("rdDateReg")
    String registerTime;

    //读者照片
    @TableField("rdPhoto")
    String photo;

    //状态
    @TableField("rdStatus")
    String status;

    //已借书数量
    @TableField("rdBorrowQty")
    Integer borrowNum;

    //密码
    @TableField("rdPwd")
    String password;

    //角色
    @TableField("rdAdminRoles")
    Integer role;
}
