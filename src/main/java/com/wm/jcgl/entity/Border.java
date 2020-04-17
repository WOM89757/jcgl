package com.wm.jcgl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 征订期号信息
 * </p>
 *
 * @author WOM
 * @since 2020-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Border implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 征订编号
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    /**
     * 征订期号
     */
    private Integer orderDnumber;

    /**
     * 年份
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderYear;

    /**
     * 卷期号
     */
    private Integer orderQihao;

    /**
     * 结算类型
     */
    @TableField("order_JStype")
    private String orderJstype;

    /**
     * 业务类型
     */
    @TableField("order_YWtpye")
    private String orderYwtpye;

    /**
     * 备注
     */
    private String orderComment;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 操作员
     */
    private String opername;

    @TableField(exist=false)
    private int[] ids;

}
