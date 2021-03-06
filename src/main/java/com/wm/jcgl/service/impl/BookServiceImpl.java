package com.wm.jcgl.service.impl;

import com.wm.jcgl.entity.Book;
import com.wm.jcgl.mapper.BookMapper;
import com.wm.jcgl.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wm.sys.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 在此期号内建立、修改、删除和向省店上报书目信息 服务实现类
 * </p>
 *
 * @author WOM
 * @since 2020-04-21
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    /**
     * 根据期号ID查询所有的自编书目ID
     * @param orderId
     */
    @Override
    public List<Integer> queryOrderBookIdsByOrder(Integer orderId) {
        return this.getBaseMapper().queryOrderBookIdsByOrder(orderId);
    }

    /**
     * 保存期号ID自编书目ID之间的关系
     * @param orderid
     * @param bookids
     */
    @Override
    public void saveBookOrder(Integer orderid, Integer[] bookids) {
        if(bookids!=null&&bookids.length>0) {
            for (Integer bookid : bookids) {
                this.getBaseMapper().saveBookOrder(orderid,bookid);
            }
        }

    }

    /**
     * 根据期号ID与自编书目id删除一条记录
     * @param orderid
     * @param bookids
     */
    @Override
    public void deleteBookOrderByOidAndBid(Integer orderid, Integer bookids) {
        this.getBaseMapper().deleteBookOrderByOidAndBid(orderid,bookids);

    }

    /**
     * 根据期号ID与自编书目id删除所有记录
     * @param orderid
     */
    @Override
    public void deleteBookOrderByOid(Integer orderid) {
        this.getBaseMapper().deleteBookOrderByOid(orderid);
    }

    /**
     * 保存自编书目ID与年级模型ID
     * @param bmodelid
     * @param bookids
     */
    @Override
    public void saveBookModel(Integer bmodelid, Integer[] bookids) {
        if(bookids!=null&&bookids.length>0) {
            for (Integer bookid : bookids) {
                this.getBaseMapper().saveBookModel(bmodelid,bookid);
            }
        }
    }

    /**
     * 根据年级模型ID查询所有的自编书目ID
     * @param bmodelid
     * @return
     */
    @Override
    public List<Integer> queryModelBookIdsByModel(Integer bmodelid) {
        return this.getBaseMapper().queryModelBookIdsByModel(bmodelid);
    }

    /**
     * 根据年级模型ID与自编书目id删除删除一条
     * @param bmodelid
     * @param bookids
     */
    @Override
    public void deleteBookModelByMidAndBid(Integer bmodelid, Integer bookids) {
        this.getBaseMapper().deleteBookModelByMidAndBid(bmodelid,bookids);
    }

    /**
     * 根据年级模型ID删除所有记录
     * @param bmodelid
     */
    @Override
    public void deleteBookModelByMid(Integer bmodelid) {
        this.getBaseMapper().deleteBookModelByMid(bmodelid);

    }


    @Override
    public boolean save(Book entity) {
        return super.save(entity);
    }

    @Override
    public boolean updateById(Book entity) {
        return super.updateById(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public Book getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return super.removeByIds(idList);
    }
}
