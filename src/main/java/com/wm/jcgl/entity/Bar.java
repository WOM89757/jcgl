package com.wm.jcgl.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *柱形图对应实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bar {
    private String name;
    private Integer lNum;
    private Integer bNum;


}
