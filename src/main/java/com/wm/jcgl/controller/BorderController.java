package com.wm.jcgl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.Border;
import com.wm.jcgl.service.BorderService;
import com.wm.jcgl.vo.BorderVo;
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
 * 征订期号信息 前端控制器
 * </p>
 *
 * @author WOM
 * @since 2020-04-17
 */
@RestController
@RequestMapping("/border")
public class BorderController {

    @Autowired
    private BorderService borderService;

    /**
     * 查询
     */
    @RequestMapping("loadAllBorder")
    public DataGridView loadAllBorder(BorderVo orderVo) {
        IPage<Border> page = new Page<>(orderVo.getPage(), orderVo.getLimit());
        QueryWrapper<Border> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(null!=orderVo.getOrderDnumber(), "order_dnumber",orderVo.getOrderDnumber());
       // queryWrapper.like(orderVo.getOrderYear()!=null, "orderYear", orderVo.getOrderYear());
        queryWrapper.like(StringUtils.isNotBlank(orderVo.getOpername()), "opername",orderVo.getOpername());
        this.borderService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 添加
     */
    @RequestMapping("addBorder")
    public ResultObj addBorder(BorderVo orderVo) {
        try {
            this.borderService.save(orderVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改
     */
    @RequestMapping("updateBorder")
    public ResultObj updateBorder(BorderVo orderVo) {
        try {
            this.borderService.updateById(orderVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除
     */
    @RequestMapping("deleteBorder")
    public ResultObj deleteBorder(Integer id) {
        try {
            this.borderService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteBorder")
    public ResultObj batchDeleteBorder(BorderVo orderVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            for (Integer id : orderVo.getIds()) {
                idList.add(id);
            }
            this.borderService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }


    /**
     * 加载所有可用的供应商
     */
    @RequestMapping("loadAllBorderForSelect")
    public DataGridView loadAllBorderForSelect() {
        QueryWrapper<Border> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
        List<Border> list = this.borderService.list(queryWrapper);
        return new DataGridView(list);
    }

}

