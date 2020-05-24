package com.wm.jcgl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class matchResult {
    private String fromName;
    private String ToName;
    private List<matchbook> books;
}
