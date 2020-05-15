package com.wm.jcgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Booksubmit;
import com.wm.jcgl.entity.Inport;
import com.wm.jcgl.mapper.BookMapper;
import com.wm.jcgl.mapper.BooksubmitMapper;
import com.wm.jcgl.mapper.InportMapper;
import com.wm.jcgl.service.InportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * <p>
 * 库存信息 服务实现类
 * </p>
 *
 * @author WOM
 * @since 2020-05-07
 */
@Service
@Transactional
public class InportServiceImpl extends ServiceImpl<InportMapper, Inport> implements InportService {

    @Resource
    private BookMapper bookMapper;

    @Resource
    private BooksubmitMapper booksubmitMapper;

    @Override
    public boolean save(Inport entity) {
        //根据商品编号查询商品,并保存商品入库数量
        Book book=bookMapper.selectById(entity.getBookId());
        if(null!=book.getInportnumber()){
            book.setInportnumber(book.getInportnumber()+entity.getNumber());
        }else{
            book.setInportnumber(entity.getNumber());
        }
        bookMapper.updateById(book);
        //根据期号id和书目id 修改报订信息的库存数量
        QueryWrapper<Booksubmit> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("order_id",entity.getOrderId());
        queryWrapper.eq("book_id",entity.getBookId());
        Booksubmit booksubmit=booksubmitMapper.selectOne(queryWrapper);
        if(null!=booksubmit.getInportNum()){
            booksubmit.setInportNum(booksubmit.getInportNum()+entity.getNumber());
        }else{
            booksubmit.setInportNum(entity.getNumber());
        }
        booksubmitMapper.updateById(booksubmit);
        //保存进货信息
        return super.save(entity);
    }

    @Override
    public boolean updateById(Inport entity) {
        //根据进货ID查询进货
        Inport inport = this.baseMapper.selectById(entity.getId());
        //根据商品ID查询商品信息
        Book book = this.bookMapper.selectById(entity.getBookId());
        //库存的算法  当前库存-进货单修改之前的数量+修改之后的数量
        book.setInportnumber(book.getInportnumber()-inport.getNumber()+entity.getNumber());
        this.bookMapper.updateById(book);
        //根据期号id和书目id 修改报订信息的库存数量
        QueryWrapper<Booksubmit> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("order_id",entity.getOrderId());
        queryWrapper.eq("book_id",entity.getBookId());
        Booksubmit booksubmit=booksubmitMapper.selectOne(queryWrapper);
        //库存的算法  当前库存-进货单修改之前的数量+修改之后的数量
        booksubmit.setInportNum(booksubmit.getInportNum()-inport.getNumber()+entity.getNumber());
        this.booksubmitMapper.updateById(booksubmit);
        //更新进货单
        return super.updateById(entity);
    }



    @Override
    public boolean removeById(Serializable id) {
        //根据进货ID查询进货
        Inport inport = this.baseMapper.selectById(id);
        //根据商品ID查询商品信息
        Book book = this.bookMapper.selectById(inport.getBookId());
        //库存的算法  当前库存-进货单数量
        book.setInportnumber(book.getInportnumber()-inport.getNumber());
        this.bookMapper.updateById(book);
        //根据期号id和书目id 修改报订信息的库存数量
        QueryWrapper<Booksubmit> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("order_id",inport.getOrderId());
        queryWrapper.eq("book_id",inport.getBookId());
        Booksubmit booksubmit=booksubmitMapper.selectOne(queryWrapper);
        booksubmit.setInportNum(booksubmit.getInportNum()-inport.getNumber());
        this.booksubmitMapper.updateById(booksubmit);
        return super.removeById(id);
    }
}
