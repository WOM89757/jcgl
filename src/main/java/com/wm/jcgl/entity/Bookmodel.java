package com.wm.jcgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 年级套订信息
 * </p>
 *
 * @author WOM
 * @since 2020-04-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_bookmodel")
public class Bookmodel implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 年级套订编号
     */
    @TableId( type = IdType.AUTO)
    private Integer id;

    /**
     * 套订名称
     */
    private String name;

    /**
     * 操作员
     */
    private String opername;

    /**
     * 创建时间
     */
    private Date createtime;


}
