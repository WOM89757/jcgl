package com.wm.jcgl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供货商信息
 * </p>
 *
 * @author WOM
 * @since 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_provider")
public class Provider implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 供应商编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 供应商全称
     */
    @TableField("providerName")
    private String providerName;

    /**
     * 供应商邮编
     */
    private String zip;

    /**
     * 公司地址
     */
    private String address;

    /**
     * 公司电话
     */
    private String telephone;

    /**
     * 联系人
     */
    private String connectionperson;

    /**
     * 联系人电话
     */
    private String phone;

    /**
     * 开户银行
     */
    private String bank;

    /**
     * 银行账号
     */
    private String account;

    /**
     * 联系人邮箱
     */
    private String email;

    /**
     * 公司传真
     */
    private String fax;

    /**
     * 【0不可用1可用】
     */
    private Integer available;


}
