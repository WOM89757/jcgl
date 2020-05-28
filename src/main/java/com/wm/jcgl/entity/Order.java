package com.wm.jcgl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 征订期号信息
 * </p>
 *
 * @author WOM
 * @since 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_order")
public class Order implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 00120101 01:课本 20:年份 1/2:春/秋 01:编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;


    /**
     * 年份
     */
    private Integer year;

    /**
     * 卷期号
     */
    private Integer qihao;

    /**
     * 春课、秋课、春辅、秋辅
     */
    @TableField("JCtype")
    private String jctype;

    /**
     * 教材、教辅
     */
    @TableField("YWtpye")
    private String ywtpye;

    /**
     * 备注
     */
    private String comment;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 操作员
     */
    private String opername;




}
