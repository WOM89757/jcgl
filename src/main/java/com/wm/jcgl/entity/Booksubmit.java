package com.wm.jcgl.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 教材报订信息
 * </p>
 *
 * @author WOM
 * @since 2020-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_booksubmit")
public class Booksubmit implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 教材报订编号
     */
    private Integer id;

    /**
     * 00120101 01:课本 20:年份 1/2:春/秋 01:编号
     */
    private Integer orderId;

    /**
     * 自编书目编号
     */
    private Integer bookId;

    /**
     * 年级
     */
    private String grade;

    /**
     * 待配订数
     */
    @TableField("sumordNum")
    private Integer sumordNum;

    /**
     * 库存量
     */
    @TableField("inportNum")
    private Integer inportNum;

    /**
     * 报订未到货
     */
    @TableField("lackNum")
    private Integer lackNum;

    /**
     * 进货折扣
     */
    private BigDecimal discount;


}
