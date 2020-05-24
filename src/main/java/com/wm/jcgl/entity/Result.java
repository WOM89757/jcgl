package com.wm.jcgl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 匹配结果信息
 * </p>
 *
 * @author WOM
 * @since 2020-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_result")
public class Result implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 匹配结果编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 00120101 01:课本 20:年份 1/2:春/秋 01:编号
     */
    private Integer orderId;

    /**
     * 退货学校id
     */
    private Integer bdeptId;

    /**
     * 缺货学校id
     */
    private Integer ldeptId;

    /**
     * 书目id
     */
    private Integer bookId;

    /**
     * 剩余书目可给当前缺货学校分配的数量
     */
    private Integer number;

    @TableField(exist = false)
    private String bDeptName;
    @TableField(exist = false)
    private String lDeptName;
    @TableField(exist = false)
    private String bookName;


}
