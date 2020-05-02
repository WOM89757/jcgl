package com.wm.jcgl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Provider;
import com.wm.jcgl.entity.Subscription;
import com.wm.jcgl.service.SubscriptionService;
import com.wm.jcgl.vo.SubscriptionVo;
import com.wm.sys.common.Constast;
import com.wm.sys.common.DataGridView;
import com.wm.sys.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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

    @Autowired
    private SubscriptionService subscriptionService;

    /**
     * 查询
     */
    @RequestMapping("loadAllSubscription")
    public DataGridView loadAllSubscription(SubscriptionVo subscriptionVo) {
        IPage<Subscription> page = new Page<>(subscriptionVo.getPage(), subscriptionVo.getLimit());
        QueryWrapper<Subscription> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like(StringUtils.isNotBlank(subscriptionVo.getSubscriptionName()), "subscriptionName",
//                subscriptionVo.getSubscriptionName());
//        queryWrapper.like(StringUtils.isNotBlank(subscriptionVo.getPhone()), "phone", subscriptionVo.getPhone());
//        queryWrapper.like(StringUtils.isNotBlank(subscriptionVo.getConnectionperson()), "connectionperson",
//                subscriptionVo.getConnectionperson());
        this.subscriptionService.page(page, queryWrapper);
//        List<Book> records = page.getRecords();
//        for (Book book : records) {
//            book.setOrderid(bookVo.getOrderid());
//            if(null!=book.getProviderId()){
//                Provider provider = this.providerService.getById(book.getProviderId());
//                if(null!=provider) {
//                    book.setProvidername(provider.getProviderName());
//                }
//            }
//        }
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 添加
     */
    @RequestMapping("addSubscription")
    public ResultObj addSubscription(SubscriptionVo subscriptionVo) {
        try {
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

