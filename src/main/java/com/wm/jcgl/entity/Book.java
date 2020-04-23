package com.wm.jcgl.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 在此期号内建立、修改、删除和向省店上报书目信息
 * </p>
 *
 * @author WOM
 * @since 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_book")
public class Book implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自编书目编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 供应商编号
     */
    @TableField("provider_id")
    private Integer providerId;

    /**
     * 书名
     */
    private String name;

    /**
     * 年级
     */
    private String grade;

    /**
     * 定价
     */
    private BigDecimal price;

    /**
     * 书号
     */
    @TableField("bookNum")
    private String bookNum;

    /**
     * 版别
     */
    private String edition;

    /**
     * 免费标记
     */
    private Integer free;

    /**
     * 分类
     */
    private String variety;

    /**
     * 操作员
     */
    private String opername;

    /**
     * 库存量
     */
    private Integer inportnumber;

    /**
     * 创建时间
     */
    private Date createtime;

    @TableField(exist=false)
    private String providername;
    @TableField(exist=false)
    private Integer orderid;
}
