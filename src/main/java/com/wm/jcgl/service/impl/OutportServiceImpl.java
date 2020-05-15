package com.wm.jcgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Booksubmit;
import com.wm.jcgl.entity.Inport;
import com.wm.jcgl.entity.Outport;
import com.wm.jcgl.mapper.BookMapper;
import com.wm.jcgl.mapper.BooksubmitMapper;
import com.wm.jcgl.mapper.InportMapper;
import com.wm.jcgl.mapper.OutportMapper;
import com.wm.jcgl.service.OutportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wm.sys.common.WebUtils;
import com.wm.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 库存退货信息 服务实现类
 * </p>
 *
 * @author WOM
 * @since 2020-05-07
 */
@Service
@Transactional
public class OutportServiceImpl extends ServiceImpl<OutportMapper, Outport> implements OutportService {

    @Resource
    private BookMapper bookMapper;
    @Resource
    private InportMapper inportMapper;

    @Resource
    private BooksubmitMapper booksubmitMapper;

    /**
     * 添加退货信息
     * @param id
     * @param number
     * @param remark
     */
    @Override
    public void addOutPort(Integer id, Integer number, String remark) {
        //1,根据进货单ID查询进货单信息
        Inport inport = this.inportMapper.selectById(id);
        inport.setNumber(inport.getNumber()-number);
        this.inportMapper.updateById(inport);
        //2,根据商品ID查询商品信息
        Book book = this.bookMapper.selectById(inport.getBookId());
        book.setInportnumber(book.getInportnumber()-number);
        this.bookMapper.updateById(book);

        //根据期号id和书目id 修改报订信息的库存数量
        QueryWrapper<Booksubmit> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("order_id",inport.getOrderId());
        queryWrapper.eq("book_id",inport.getBookId());
        Booksubmit booksubmit=booksubmitMapper.selectOne(queryWrapper);
        booksubmit.setInportNum(booksubmit.getInportNum()-number);
        this.booksubmitMapper.updateById(booksubmit);

        //3,添加退货单信息
        Outport entity=new Outport();
        entity.setBookId(inport.getBookId());
        entity.setNumber(number);
        User user=(User) WebUtils.getSession().getAttribute("user");
        entity.setOperateperson(user.getName());
        entity.setOutportprice(inport.getInportPrice());
        entity.setOutputTime(new Date());
        entity.setProviderId(inport.getProviderId());
        entity.setRemark(remark);
        this.getBaseMapper().insert(entity);
    }
}
