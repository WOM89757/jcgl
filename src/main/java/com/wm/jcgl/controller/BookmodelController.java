package com.wm.jcgl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.Bookmodel;
import com.wm.jcgl.service.BookmodelService;
import com.wm.jcgl.vo.BookmodelVo;
import com.wm.sys.common.Constast;
import com.wm.sys.common.DataGridView;
import com.wm.sys.common.ResultObj;
import com.wm.sys.common.WebUtils;
import com.wm.sys.entity.User;
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
 * 年级套订信息 前端控制器
 * </p>
 *
 * @author WOM
 * @since 2020-04-23
 */
@RestController
@RequestMapping("bookmodel")
public class BookmodelController {

    @Autowired
    private BookmodelService bookmodelService;

    /**
     * 查询
     */
    @RequestMapping("loadAllBookmodel")
    public DataGridView loadAllBookmodel(BookmodelVo bookmodelVo) {
        IPage<Bookmodel> page = new Page<>(bookmodelVo.getPage(), bookmodelVo.getLimit());
        QueryWrapper<Bookmodel> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(bookmodelVo.getId()!=null, "id",bookmodelVo.getId());
        queryWrapper.like(StringUtils.isNotBlank(bookmodelVo.getName()), "name", bookmodelVo.getName());
        queryWrapper.like(StringUtils.isNotBlank(bookmodelVo.getOpername()), "opername",bookmodelVo.getOpername());
        this.bookmodelService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 添加
     */
    @RequestMapping("addBookmodel")
    public ResultObj addBookmodel(BookmodelVo bookmodelVo) {
        try {
            bookmodelVo.setCreatetime(new Date());
            User user = (User) WebUtils.getSession().getAttribute("user");
            bookmodelVo.setOpername(user.getName());
            this.bookmodelService.save(bookmodelVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改
     */
    @RequestMapping("updateBookmodel")
    public ResultObj updateBookmodel(BookmodelVo bookmodelVo) {
        try {
            this.bookmodelService.updateById(bookmodelVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除
     */
    @RequestMapping("deleteBookmodel")
    public ResultObj deleteBookmodel(Integer id) {
        try {
            this.bookmodelService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteBookmodel")
    public ResultObj batchDeleteBookmodel(BookmodelVo bookmodelVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            for (Integer id : bookmodelVo.getIds()) {
                idList.add(id);
            }
            this.bookmodelService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }


    /**
     * 加载所有可用的年级模型
     */
    @RequestMapping("loadAllBookmodelForSelect")
    public DataGridView loadAllBookmodelForSelect() {
        return new DataGridView(this.bookmodelService.list(null));
    }

}

