package com.wm.jcgl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 匹配结果：对应书目清单数据实体类
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class matchbook {
    private String bookname;
    private Integer value;
}
