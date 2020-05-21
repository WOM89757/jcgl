package com.wm.jcgl.mapper;

import com.wm.jcgl.entity.Allotment;
import com.wm.jcgl.entity.Match;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 教材匹配信息 Mapper 接口
 * </p>
 *
 * @author WOM
 * @since 2020-05-15
 */
public interface MatchMapper extends BaseMapper<Match> {

    List<Match> getMatchForLine(Integer orderId);
}
