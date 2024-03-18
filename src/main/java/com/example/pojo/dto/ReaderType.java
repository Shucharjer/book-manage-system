package com.example.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 读者类型实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_readertype")
public class ReaderType {

    //读者类别
    @TableId("rdType")
    Integer type;

    //读者类别名称
    @TableField("rdTypeName")
    String typeName;

    //可借书数量
    @TableField("CanLendQty")
    Integer canLendNum;

    //可书天数
    @TableField("CanLendDay")
    Integer canLendDays;

    //可续借的次数
    @TableField("CanContinueTimes")
    Integer canContinueTimes;

    //罚款率
    @TableField("PunishRate")
    Double punishRate;

    //证书有效期
    @TableField("DateValid")
    Integer accessValid;
}
