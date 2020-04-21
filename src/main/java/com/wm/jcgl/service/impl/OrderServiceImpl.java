package com.wm.jcgl.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wm.jcgl.entity.Order;
import com.wm.jcgl.mapper.OrderMapper;
import com.wm.jcgl.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 征订期号信息 服务实现类
 * </p>
 *
 * @author WOM
 * @since 2020-04-21
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
