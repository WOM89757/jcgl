package com.wm.jcgl.service.impl;

import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Inport;
import com.wm.jcgl.mapper.BookMapper;
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

    @Override
    public boolean save(Inport entity) {
        //根据商品编号查询商品
        Book book=bookMapper.selectById(entity.getBookId());
        if(null!=book.getInportnumber()){
            book.setInportnumber(book.getInportnumber()+entity.getNumber());
        }else{
            book.setInportnumber(entity.getNumber());
        }
        bookMapper.updateById(book);
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
        return super.removeById(id);
    }
}
