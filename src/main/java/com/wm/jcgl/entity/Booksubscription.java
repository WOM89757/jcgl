package com.wm.jcgl.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 教材征订信息
 * </p>
 *
 * @author WOM
 * @since 2020-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Booksubscription implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 教材征订编号
     */
    private Integer id;

    /**
     * 学生订数
     */
    private Integer studnum;

    /**
     * 教师订数
     */
    private Integer teanum;

    /**
     * 免费订数
     */
    private Integer freenum;

    /**
     * 操作员
     */
    private String opername;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 学校编号
     */
    private Integer deptId;

    /**
     * 创建时间
     */
    private Date createtime;


}
