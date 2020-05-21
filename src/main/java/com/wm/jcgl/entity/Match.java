package com.wm.jcgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 教材匹配信息
 * </p>
 *
 * @author WOM
 * @since 2020-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_match")
public class Match implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 匹配编号
     */
    private Integer id;

    /**
     * 自编书目编号
     */
    private Integer bookId;

    /**
     * 00120101 01:课本 20:年份 1/2:春/秋 01:编号
     */
    private Integer orderId;

    /**
     * 0：剩余 1：缺少
     */
    private Integer type;

    /**
     * 缺少数量
     */
    @TableField("lNum")
    private Integer lNum;

    /**
     *剩余数量
     */
    @TableField("bNum")
    private Integer bNum;


    /**
     * 操作员
     */
    @TableField("operName")
    private String operName;

    /**
     * 学校编号
     */
    private Integer deptId;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private Date createTime;

    /**
     * 【0未提交1提交】
     */
    private Integer status;
    /**
     * 年级
     */
    private String grade;
    @TableField(exist = false)
    private String bookname;
    @TableField(exist = false)
    private String ordername;
    @TableField(exist = false)
    private String schoolname;
    @TableField(exist = false)
    private Integer number;



}
