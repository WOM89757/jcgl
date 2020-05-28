package com.wm.jcgl.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wm.jcgl.entity.Order;
import com.wm.jcgl.mapper.OrderMapper;
import com.wm.jcgl.service.OrderService;
import com.wm.jcgl.vo.OrderVo;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 根据规则生成期号id
     * @param orderVo
     * @return
     */
    @Override
    public Integer loadOrderId(OrderVo orderVo) {
//00120101 01:课本 20:年份 1/2:春/秋 01:编号
        String type = '0'+orderVo.getYwtpye();
        String year = orderVo.getYear().toString().substring(orderVo.getYear().toString().length()-2);
        String qihao = orderVo.getQihao().toString();
        String orderIdStr = type.concat(year).concat(qihao);

        //查询当前前缀下最大编号
//        SELECT id
//        FROM b_order
//        where id LIKE '2201%'
//        ORDER BY id desc
        QueryWrapper<Order> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.select("id");
        queryWrapper.like("id",Integer.valueOf(orderIdStr)+"%");
        IPage<Order> page=new Page<>(1, 1);
        List<Order> list = this.baseMapper.selectPage(page, queryWrapper).getRecords();
        Integer orderId;
        if (list.size()>0){
             orderId = list.get(0).getId()+1;
        }else{
            orderId = Integer.valueOf(orderIdStr+"01");
        }
        return orderId;
    }
}
