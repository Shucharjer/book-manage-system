package com.example.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 借阅记录实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_borrow")
public class Borrow {
    //借阅顺序号
    @TableId("BorrowID")
    Integer borrowId;

    //读者序号
    @TableField("rdID")
    Integer readerId;

    //图书序号
    @TableField("bkID")
    Integer bookId;

    //续约次数
    @TableField("idContinueTimes")
    Integer refreshNum;

    //借书日期
    @TableField("idDateOut")
    Date lendDay;

    //应还日期
    @TableField("idDateRetPlan")
    Date preReturnDay;

    //实际还的日期
    @TableField("idDateRetAct")
    Date actReturnDay;

    //超期天数
    @TableField("idOverDay")
    Integer overDays;

    //超期金额
    @TableField("idOverMoney")
    Double overMoney;

    //罚款金额
    @TableField("idPunishMoney")
    Double punishMoney;

    //是否还书
    @TableField("isHasReturn")
    Integer isReturn;

    //借书操作员名字
    @TableField("OperatorBorrow")
    String lendOperationPersonName;

    //还书操作员
    @TableField("OperatorReturn")
    String returnOperationPersonName;

}
