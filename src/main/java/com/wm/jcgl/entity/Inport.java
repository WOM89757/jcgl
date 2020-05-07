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
 * 库存信息
 * </p>
 *
 * @author WOM
 * @since 2020-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_inport")
public class Inport implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 库存编号
     */
    private Integer id;

    /**
     * 进货时间
     */
    @TableField("inportTime")
    private Date inportTime;

    /**
     * 操作员管理员
     */
    @TableField("operatePerson")
    private String operatePerson;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 注释
     */
    private String remark;

    /**
     * 进货价格
     */
    @TableField("inportPrice")
    private Double inportPrice;

    /**
     * 自编书目编号
     */
    private Integer bookId;

    /**
     * 供应商编号
     */
    private Integer providerId;

    @TableField(exist = false)
    private String providername;

    @TableField(exist = false)
    private String bookname;


}
