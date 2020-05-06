package com.wm.jcgl.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Booksubmit;
import com.wm.jcgl.entity.Order;
import com.wm.jcgl.entity.Provider;
import com.wm.jcgl.service.BookService;
import com.wm.jcgl.service.BooksubmitService;
import com.wm.jcgl.service.OrderService;
import com.wm.jcgl.service.SubscriptionService;
import com.wm.jcgl.vo.BooksubmitVo;
import com.wm.sys.common.Constast;
import com.wm.sys.common.DataGridView;
import com.wm.sys.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 教材报订信息 前端控制器
 * </p>
 *
 * @author WOM
 * @since 2020-04-27
 */
@RestController
@RequestMapping("/booksubmit")
public class BooksubmitController {
    @Autowired
    private BooksubmitService submitService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private BookService bookService;
    @Autowired
    private OrderService orderService;

    /**
     * 查询已提交的征订信息
     *
     */
    @RequestMapping("loadBooksubmit")
    public DataGridView loadBooksubmit(BooksubmitVo submitVo) {
        IPage<Booksubmit> page = new Page<>(submitVo.getPage(), submitVo.getLimit());
        QueryWrapper<Booksubmit> queryWrapper = new QueryWrapper<>();
//
//        queryWrapper.eq("order_id",submitVo.getOrderId());
//        this.submitService.page(page, queryWrapper);
        List<Booksubmit> submitList = this.subscriptionService.getSubscriptionWithOrderId(submitVo.getOrderId());
        for (Booksubmit submit : submitList) {
            Book book = this.bookService.getById(submit.getBookId());
            if(null!=book) {
                submit.setBookname(book.getName());
            }
            Order order = this.orderService.getById(submit.getOrderId());
            if(null!=order) {
                submit.setOrdername(order.getComment());
            }
        }

        return new DataGridView(page.getTotal(), submitList);
    }
    /**
     * 查询
     */
    @RequestMapping("loadAllBooksubmit")
    public DataGridView loadAllBooksubmit(BooksubmitVo submitVo) {
        if(null!=submitVo.getOrderId()){

            IPage<Booksubmit> page = new Page<>(submitVo.getPage(), submitVo.getLimit());
            QueryWrapper<Booksubmit> queryWrapper = new QueryWrapper<>();
            queryWrapper.like(null!=submitVo.getOrderId(), "order_id",submitVo.getOrderId());
            queryWrapper.like(StringUtils.isNotBlank(submitVo.getGrade()), "grade", submitVo.getGrade());
            if(StringUtils.isNotBlank(submitVo.getBookname())){
                queryWrapper.inSql("book_id","select id from b_book where b_book.name like '%"+submitVo.getBookname()+"%'");
            }
            this.submitService.page(page, queryWrapper);
            for (Booksubmit submit : page.getRecords()) {
                Book book = this.bookService.getById(submit.getBookId());
                if(null!=book) {
                    submit.setBookname(book.getName());
                }
                Order order = this.orderService.getById(submit.getOrderId());
                if(null!=order) {
                    submit.setOrdername(order.getComment());
                }
            }
            return new DataGridView(page.getTotal(), page.getRecords());
        }
        return new DataGridView(null);
    }

    /**
     * 添加
     */
    @RequestMapping("addBooksubmit")
    @ResponseBody
    public ResultObj addBooksubmit(@RequestParam("submitlist") String submitlist) {
        try {
            List<Booksubmit> submitVolist = JSON.parseArray(submitlist,Booksubmit.class);
            for (Booksubmit submit: submitVolist
                 ) {
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("order_id",submit.getOrderId());
                wrapper.eq("book_id",submit.getBookId());
                Booksubmit temp = this.submitService.getOne(wrapper);
                if(null!=temp){
                    this.submitService.removeById(temp.getId());
                }

            }

            this.submitService.saveBatch(submitVolist);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改
     */
    @RequestMapping("updateBooksubmit")
    public ResultObj updateBooksubmit(BooksubmitVo submitVo) {
        try {
            this.submitService.updateById(submitVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除
     */
    @RequestMapping("deleteBooksubmit")
    public ResultObj deleteBooksubmit(Integer id) {
        try {
            this.submitService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteBooksubmit")
    public ResultObj batchDeleteBooksubmit(BooksubmitVo submitVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            for (Integer id : submitVo.getIds()) {
                idList.add(id);
            }
            this.submitService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }


    /**
     * 加载所有可用的供应商
     */
    @RequestMapping("loadAllBooksubmitForSelect")
    public DataGridView loadAllBooksubmitForSelect() {
        QueryWrapper<Booksubmit> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
        List<Booksubmit> list = this.submitService.list(queryWrapper);
        return new DataGridView(list);
    }

}

