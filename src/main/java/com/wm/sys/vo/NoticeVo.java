package com.wm.sys.vo;

import com.wm.sys.entity.Notice;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
public class NoticeVo extends Notice{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private Integer page=1;
	private Integer limit=5;
	
	private Integer[] ids;//接收多个ID
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
}
