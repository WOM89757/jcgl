package com.wm.jcgl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Order;
import com.wm.jcgl.entity.Subscription;
import com.wm.jcgl.service.BookService;
import com.wm.jcgl.service.OrderService;
import com.wm.jcgl.service.SubscriptionService;
import com.wm.jcgl.vo.SubscriptionVo;
import com.wm.sys.common.Constast;
import com.wm.sys.common.DataGridView;
import com.wm.sys.common.ResultObj;
import com.wm.sys.common.WebUtils;
import com.wm.sys.entity.Dept;
import com.wm.sys.entity.User;
import com.wm.sys.service.DeptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 教材征订信息 前端控制器
 * </p>
 *
 * @author WOM
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    @Resource
    private SubscriptionService subscriptionService;
    @Resource
    private OrderService orderService;
    @Resource
    private BookService bookService;
    @Resource
    private DeptService deptService;

    /**
     * 查询
     */
    @RequestMapping("loadAllSubscription")
    public DataGridView loadAllSubscription(SubscriptionVo subscriptionVo) {
        IPage<Subscription> page = new Page<>(subscriptionVo.getPage(), subscriptionVo.getLimit());
        QueryWrapper<Subscription> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(subscriptionVo.getGrade()), "grade",subscriptionVo.getGrade());
        queryWrapper.eq(null!=subscriptionVo.getOrderId(), "order_id",subscriptionVo.getOrderId());

        if(StringUtils.isNotBlank(subscriptionVo.getSchoolname())){
            queryWrapper.inSql("dept_id","select id from sys_dept where sys_dept.title like '%"+subscriptionVo.getSchoolname()+"%'");
        }
        if(StringUtils.isNotBlank(subscriptionVo.getOrdername())){
            queryWrapper.inSql("order_id","select id from b_order where b_order.comment like '%"+subscriptionVo.getOrdername()+"%'");
        }
        if(StringUtils.isNotBlank(subscriptionVo.getBookname())){
            queryWrapper.inSql("book_id","select id from b_book where b_book.name like '%"+subscriptionVo.getBookname()+"%'");
        }
        User user = (User) WebUtils.getSession().getAttribute("user");
        if(1!=user.getDeptid()){
            //非销售店查询
            queryWrapper.eq("dept_id",user.getDeptid());
        }
        this.subscriptionService.page(page, queryWrapper);
        List<Subscription> records = page.getRecords();
        for (Subscription subscription : records) {
            //根据id查询征订期号名称
            if(null!=subscription.getOrderId()){
                Order order  = this.orderService.getById(subscription.getOrderId());
                if(null!=order) {
                    subscription.setOrdername(order.getComment());
                }
            }
            //根据id查询书名
            if(null!=subscription.getBookId()){
                    Book book  = this.bookService.getById(subscription.getBookId());
                if(null!=book) {
                    subscription.setBookname(book.getName());
                }
            }
            //根据id查询学校名
            if(null!=subscription.getDeptId()){
                Dept dept  = this.deptService.getById(subscription.getDeptId());
                if(null!=dept) {
                    subscription.setSchoolname(dept.getTitle());
                }
            }
        }
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 添加
     */
    @RequestMapping("addSubscription")
    public ResultObj addSubscription(SubscriptionVo subscriptionVo) {
        try {
            subscriptionVo.setCreateTime(new Date());
            User user = (User) WebUtils.getSession().getAttribute("user");
            subscriptionVo.setOperName(user.getName());
            subscriptionVo.setStatus(Constast.STATUS_FALSE);
            subscriptionVo.setGrade(this.bookService.getById(subscriptionVo.getBookId()).getGrade());
            this.subscriptionService.save(subscriptionVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

      /**
     * 修改
     */
    @RequestMapping("updateSubscription")
    public ResultObj updateSubscription(SubscriptionVo subscriptionVo) {
        try {
            this.subscriptionService.updateById(subscriptionVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 提交确认
     */
    @RequestMapping("submitSubscription")
    public ResultObj submitSubscription(Integer id) {
        try {
            Subscription subscription = this.subscriptionService.getById(id);
            subscription.setStatus(Constast.STATUS_TRUE);
            this.subscriptionService.updateById(subscription);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_SUCCESS;
        }
    }
    /**
     * 批量提交确认
     */
    @RequestMapping("batchSubmitSubscription")
    public ResultObj batchSubmitSubscription(SubscriptionVo subscriptionVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            for (Integer id : subscriptionVo.getIds()) {
                idList.add(id);
            }
            Collection<Subscription> subscriptionList = this.subscriptionService.listByIds(idList);
            for (Subscription subscription : subscriptionList) {
                subscription.setStatus(Constast.STATUS_TRUE);
            }
            this.subscriptionService.updateBatchById(subscriptionList);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_SUCCESS;
        }
    }

    /**
     * 撤回确认
     */
    @RequestMapping("withdrawSubscription")
    public ResultObj  withdrawSubscription(Integer id) {
        try {
            Subscription subscription = this.subscriptionService.getById(id);
            subscription.setStatus(Constast.STATUS_FALSE);
            this.subscriptionService.updateById(subscription);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_SUCCESS;
        }
    }

    /**
     * 删除
     */
    @RequestMapping("deleteSubscription")
    public ResultObj deleteSubscription(Integer id) {
        try {
            this.subscriptionService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteSubscription")
    public ResultObj batchDeleteSubscription(SubscriptionVo subscriptionVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            for (Integer id : subscriptionVo.getIds()) {
                idList.add(id);
            }
            this.subscriptionService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }


    /**
     * 加载所有可用的征订教材信息
     */
    @RequestMapping("loadAllSubscriptionForSelect")
    public DataGridView loadAllSubscriptionForSelect() {
        QueryWrapper<Subscription> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
        List<Subscription> list = this.subscriptionService.list(queryWrapper);
        return new DataGridView(list);
    }
}

