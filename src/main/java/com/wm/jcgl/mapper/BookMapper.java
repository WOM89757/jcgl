package com.wm.jcgl.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wm.jcgl.entity.Book;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 在此期号内建立、修改、删除和向省店上报书目信息 Mapper 接口
 * </p>
 *
 * @author WOM
 * @since 2020-04-21
 */
public interface BookMapper extends BaseMapper<Book> {
    /**
     * 根据期号ID查询所有的自编书目ID
     * @param ordierid
     * @return
     */
    List<Integer> queryOrderBookIdsByOrder(Integer ordierid);
    /**
     * 保存期号ID自编书目ID之间的关系
     * @param order_id
     * @param book_id
     */
    void saveBookOrder(@Param("order_id")Integer order_id, @Param("book_id")Integer book_id);
    /**
     * 根据期号ID删除book_order
     * @param id
     */
    void deleteBookOrderByOid(Serializable id);

    /**
     * 根据期号ID与自编书目id 删除book_order
     * @param order_id
     * @param book_id
     */
    void deleteBookOrderByOidAndBid(@Param("order_id")Integer order_id, @Param("book_id")Integer book_id);

}
