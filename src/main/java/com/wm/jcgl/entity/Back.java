package com.wm.jcgl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 学校退货信息
 * </p>
 *
 * @author WOM
 * @since 2020-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_back")
public class Back implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 退货编号
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
     * 退货数量
     */
    private Integer number;

    @TableField(exist = false)
    private String bookname;
    @TableField(exist = false)
    private String ordername;
    @TableField(exist = false)
    private String schoolname;


}
