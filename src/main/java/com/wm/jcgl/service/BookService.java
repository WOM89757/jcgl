package com.wm.jcgl.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wm.jcgl.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 在此期号内建立、修改、删除和向省店上报书目信息 服务类
 * </p>
 *
 * @author WOM
 * @since 2020-04-21
 */
public interface BookService extends IService<Book> {

   // List<Book> listBYOrderId(QueryWrapper<Book> queryWrapper);

    /**
     * 根据期号ID查询所有的自编书目ID
     * @param orderId
     * @return
     */
   List<Integer> queryOrderBookIdsByOrder(Integer orderId);

    /**
     * 保存期号ID自编书目ID之间的关系
     * @param orderid
     * @param bookids
     */
    void saveBookOrder(Integer orderid, Integer[] bookids);

    /**
     * 根据期号ID与自编书目id 删除book_order
     * @param orderid
     * @param bookids
     */
    void deleteBookOrderByOidAndBid(Integer orderid, Integer bookids);
}
