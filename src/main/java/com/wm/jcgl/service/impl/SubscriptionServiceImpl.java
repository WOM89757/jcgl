package com.wm.jcgl.service.impl;

import com.wm.jcgl.entity.Allotment;
import com.wm.jcgl.entity.Booksubmit;
import com.wm.jcgl.entity.Subscription;
import com.wm.jcgl.mapper.SubscriptionMapper;
import com.wm.jcgl.service.SubscriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 教材征订信息 服务实现类
 * </p>
 *
 * @author WOM
 * @since 2020-04-27
 */
@Service
@Transactional
public class SubscriptionServiceImpl extends ServiceImpl<SubscriptionMapper, Subscription> implements SubscriptionService {

    @Override
    public List<Booksubmit> getSubscriptionWithOrderId(Integer orderId) {
        return this.baseMapper.getSubscriptionWithOrderId(orderId);
    }

    @Override
    public List<Allotment> getSubscriptionForAllotment(Integer orderId) {
        return this.baseMapper.getSubscriptionForAllotment(orderId);
    }
}
