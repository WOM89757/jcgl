package com.wm.jcgl.vo;

import com.wm.jcgl.entity.Booksubmit;
import com.wm.jcgl.entity.Provider;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = false)
public class BooksubmitVo extends Booksubmit {

	/*
	 *  
	 */
	private static final long serialVersionUID = 1L;

	private Integer page = 1;
	private Integer limit = 10;

	private Integer[] ids;
	private ArrayList<Booksubmit> submitVoList;

}
