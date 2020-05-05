package com.wm.jcgl.mapper;

import com.wm.jcgl.entity.Booksubmit;
import com.wm.jcgl.entity.Subscription;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 教材征订信息 Mapper 接口
 * </p>
 *
 * @author WOM
 * @since 2020-04-27
 */
public interface SubscriptionMapper extends BaseMapper<Subscription> {

    List<Booksubmit> getSubscriptionWithOrderId(Integer orderId);
}
