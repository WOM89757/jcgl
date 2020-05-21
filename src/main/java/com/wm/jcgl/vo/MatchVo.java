package com.wm.jcgl.vo;



import com.wm.jcgl.entity.Line;
import com.wm.jcgl.entity.Match;
import com.wm.jcgl.entity.Subscription;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class MatchVo extends Match {



	private Integer page = 1;
	private Integer limit = 10;
	private Integer[] ids;

	private List<Line> chartData;



	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
}
