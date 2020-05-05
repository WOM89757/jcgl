package com.wm.jcgl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.Booksubmit;
import com.wm.jcgl.service.BooksubmitService;
import com.wm.jcgl.service.SubscriptionService;
import com.wm.jcgl.vo.BooksubmitVo;
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

        return new DataGridView(page.getTotal(), submitList);
    }
    /**
     * 查询
     */
    @RequestMapping("loadAllBooksubmit")
    public DataGridView loadAllBooksubmit(BooksubmitVo submitVo) {
        IPage<Booksubmit> page = new Page<>(submitVo.getPage(), submitVo.getLimit());
        QueryWrapper<Booksubmit> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like(StringUtils.isNotBlank(submitVo.getBooksubmitName()), "submitName",
//                submitVo.getBooksubmitName());
//        queryWrapper.like(StringUtils.isNotBlank(submitVo.getPhone()), "phone", submitVo.getPhone());
//        queryWrapper.like(StringUtils.isNotBlank(submitVo.getConnectionperson()), "connectionperson",
//                submitVo.getConnectionperson());
        this.submitService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 添加
     */
    @RequestMapping("addBooksubmit")
    public ResultObj addBooksubmit(BooksubmitVo submitVo) {
        try {
            this.submitService.save(submitVo);
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

