package com.wm.jcgl.vo;

import com.wm.jcgl.entity.Allotment;
import com.wm.jcgl.entity.Booksubmit;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AllotmentVo extends Allotment {

	/*
	 *  
	 */
	private static final long serialVersionUID = 1L;

	private Integer page = 1;
	private Integer limit = 10;

	private Integer[] ids;

}
