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
     * 数量
     */
    private Integer number;

    /**
     * 退货数量
     */
    @TableField("back_Num")
    private Integer backNum;

    /**
     * 缺货数量
     */
    @TableField("lack_Num")
    private Integer lackNum;

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
     * 【0未提交1提交】
     */
    private Integer status;


}
