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
 * 在此期号内建立、修改、删除和向省店上报书目信息
 * </p>
 *
 * @author WOM
 * @since 2020-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Book implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自编书目编号
     */
    @TableId(value = "book_id", type = IdType.AUTO)
    private Integer bookId;

    /**
     * 书名
     */
    private String bookName;

    /**
     * 年级
     */
    private String bookGrade;

    /**
     * 版别
     */
    private String bookEdition;

    /**
     * 免费品种
     */
    private String bookFree;

    /**
     * 供货单位
     */
    private String bookPublic;

    /**
     * 分类
     */
    private String bookVariety;

    /**
     * 操作员
     */
    private String opername;

    /**
     * 创建时间
     */
    private Date createtime;


}
