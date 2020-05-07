package com.wm.jcgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 库存退货信息
 * </p>
 *
 * @author WOM
 * @since 2020-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_outport")
public class Outport implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 退货编号
     */
    private Integer id;

    /**
     * 退货时间
     */
    @TableField("outputTime")
    private LocalDateTime outputTime;

    /**
     * 操作员
     */
    private String operateperson;

    /**
     * 价格
     */
    private Double outportprice;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 注释
     */
    private String remark;

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
