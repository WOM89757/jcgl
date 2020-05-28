package com.wm.jcgl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 匹配结果实体类
 * @Autor WOM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class matchResult {
    private String fromName;
    private String ToName;
    private List<matchbook> books;
}
