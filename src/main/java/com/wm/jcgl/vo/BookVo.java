package com.wm.jcgl.vo;



import com.wm.jcgl.entity.Book;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class BookVo extends Book {

	/*
	 *  
	 */
	private static final long serialVersionUID = 1L;

	private Integer page = 1;
	private Integer limit = 10;
	private int[] ids;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
}
