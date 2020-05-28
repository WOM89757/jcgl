package com.wm.jcgl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Order;
import com.wm.jcgl.service.BookService;
import com.wm.jcgl.service.OrderService;
import com.wm.jcgl.service.SubscriptionService;
import com.wm.jcgl.vo.OrderVo;
import com.wm.jcgl.vo.SubscriptionVo;
import com.wm.sys.common.Constast;
import com.wm.sys.common.DataGridView;
import com.wm.sys.common.ResultObj;
import com.wm.sys.common.WebUtils;
import com.wm.sys.entity.Dept;
import com.wm.sys.entity.User;
import com.wm.sys.service.DeptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 征订期号信息 前端控制器
 * </p>
 *
 * @author WOM
 * @since 2020-04-21
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private BookService bookService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private SubscriptionService subscriptionService;


    /**
     * 根据规则生成期号id
     * @param orderVo
     * @return
     */
    @RequestMapping("loadOrderId")
    public DataGridView loadOrderId(OrderVo orderVo){
        return new DataGridView(this.orderService.loadOrderId(orderVo));
    }

    /**
     * 根据年级套订模型添加征订期号与自编书目
     */
    @RequestMapping("pushOrder")
    public ResultObj addBookOrder(OrderVo orderVo) {

        List<Integer> booksIds = this.bookService.queryOrderBookIdsByOrder(orderVo.getId());
        if(booksIds.size()>0){
            User user = (User) WebUtils.getSession().getAttribute("user");
            //根据征订期号查询所有书目
            QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id",booksIds);
            //queryWrapper.eq("variety","小学");
            List<Book> booksList = this.bookService.list(queryWrapper);
            //根据学校id查询所有学校
            QueryWrapper<Dept> schoolWrapper = new QueryWrapper<>();
            schoolWrapper.in("id",orderVo.getIds());
            List<Dept> schoolList = this.deptService.list(schoolWrapper);
            //创建征订信息列表
            for (Dept school:schoolList) {
                for (Book book : booksList) {
                    if(school.getTitle().contains(book.getVariety())){
                        SubscriptionVo subscriptionVo = new SubscriptionVo() ;
                        subscriptionVo.setOrderId(orderVo.getId());
                        subscriptionVo.setBookId(book.getId());
                        subscriptionVo.setGrade(book.getGrade());
                        subscriptionVo.setDeptId(school.getId());
                        subscriptionVo.setOperName(user.getName());
                        subscriptionVo.setCreateTime(new Date());
                        subscriptionVo.setStatus(Constast.STATUS_FALSE);
                        subscriptionService.save(subscriptionVo);
                    }
                }
            }



        }
        if(booksIds.size()==0){
            return ResultObj.ADD_ERROR_Order;
        }


        return ResultObj.ADD_SUCCESS;

    }


    /**
     * 查询
     */
    @RequestMapping("loadAllOrder")
    public DataGridView loadAllOrder(OrderVo orderVo) {
        IPage<Order> page = new Page<>(orderVo.getPage(), orderVo.getLimit());
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(null!=orderVo.getId(), "id",orderVo.getId());
        queryWrapper.like(orderVo.getYear()!=null, "year", orderVo.getYear());
        queryWrapper.like(StringUtils.isNotBlank(orderVo.getOpername()), "opername",orderVo.getOpername());
        queryWrapper.like(orderVo.getQihao()!=null, "qihao", orderVo.getQihao());
        queryWrapper.ge(orderVo.getStartTime()!=null, "createtime", orderVo.getStartTime());
        queryWrapper.le(orderVo.getEndTime()!=null, "createtime", orderVo.getEndTime());
        queryWrapper.orderByDesc("createtime");
        this.orderService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 添加
     */
    @RequestMapping("addOrder")
    public ResultObj addOrder(OrderVo orderVo) {
        try {
            orderVo.setCreatetime(new Date());
            User user = (User) WebUtils.getSession().getAttribute("user");
            orderVo.setOpername(user.getName());
            this.orderService.save(orderVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改
     */
    @RequestMapping("updateOrder")
    public ResultObj updateOrder(OrderVo orderVo) {
        try {
            this.orderService.updateById(orderVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除
     */
    @RequestMapping("deleteOrder")
    public ResultObj deleteOrder(Integer id) {
        try {
            this.orderService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteOrder")
    public ResultObj batchDeleteOrder(OrderVo orderVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            for (Integer id : orderVo.getIds()) {
                idList.add(id);
            }
            this.orderService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }


    /**
     * 加载所有征订期号
     */
    @RequestMapping("loadAllOrderForSelect")
    public DataGridView loadAllOrderForSelect() {
        QueryWrapper<Order> queryWrapper=new QueryWrapper<>();
        List<Order> list = this.orderService.list(queryWrapper);
        return new DataGridView(list);
    }

}

