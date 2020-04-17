package com.wm.jcgl.entity;

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
 * @since 2020-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Bookmodel implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 年级套订编号
     */
    @TableId(value = "bmodel_id", type = IdType.AUTO)
    private Integer bmodelId;

    /**
     * 套订名称
     */
    private String bmodelName;

    /**
     * 操作员
     */
    private String opername;

    /**
     * 创建时间
     */
    private Date createtime;


}
