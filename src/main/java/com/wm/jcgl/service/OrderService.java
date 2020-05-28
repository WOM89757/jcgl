package com.wm.jcgl.service;

import com.wm.jcgl.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wm.jcgl.vo.OrderVo;

/**
 * <p>
 * 征订期号信息 服务类
 * </p>
 *
 * @author WOM
 * @since 2020-04-21
 */
public interface OrderService extends IService<Order> {

    /**
     * 根据规则生成期号id
     * @param orderVo
     * @return
     */
    Integer loadOrderId(OrderVo orderVo);
}
