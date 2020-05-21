package com.wm.jcgl.service;

import com.wm.jcgl.entity.Allotment;
import com.wm.jcgl.entity.Match;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 教材匹配信息 服务类
 * </p>
 *
 * @author WOM
 * @since 2020-05-15
 */
public interface MatchService extends IService<Match> {
    /**
     * 通过期号id 得到各学校匹配信息
     * @param orderId
     * @return
     */
    List<Match> getMatchForLine(Integer orderId);

}
