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
 * 配发信息
 * </p>
 *
 * @author WOM
 * @since 2020-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_allotment")
public class Allotment implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 配发编号
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
    private LocalDateTime createTime;

    /**
     * 年级
     */
    private String grade;

    /**
     * 应配发数量
     */
    @TableField("orderNum")
    private Integer orderNum;

    /**
     * 实配发数量
     */
    @TableField("realNum")
    private Integer realNum;

    /**
     * 配发状态
     */
    private Integer status;


}
