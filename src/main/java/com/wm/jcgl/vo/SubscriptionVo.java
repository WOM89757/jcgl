package com.wm.jcgl.vo;



import com.wm.jcgl.entity.Subscription;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class SubscriptionVo extends Subscription {



	private Integer page = 1;
	private Integer limit = 10;
	private Integer[] ids;


	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
}
