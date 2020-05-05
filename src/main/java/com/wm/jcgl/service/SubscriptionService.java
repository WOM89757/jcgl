package com.wm.jcgl.service;

import com.wm.jcgl.entity.Booksubmit;
import com.wm.jcgl.entity.Subscription;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 教材征订信息 服务类
 * </p>
 *
 * @author WOM
 * @since 2020-04-27
 */
public interface SubscriptionService extends IService<Subscription> {
    /**
     * 通过期号id 得到所有已提交的征订信息
     * @param orderId
     * @return
     */
    List<Booksubmit> getSubscriptionWithOrderId(Integer orderId);
}
