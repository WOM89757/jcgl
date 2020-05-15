package com.wm.jcgl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Inport;
import com.wm.jcgl.entity.Provider;
import com.wm.jcgl.service.BookService;
import com.wm.jcgl.service.InportService;
import com.wm.jcgl.service.ProviderService;
import com.wm.jcgl.vo.InportVo;
import com.wm.sys.common.DataGridView;
import com.wm.sys.common.ResultObj;
import com.wm.sys.common.WebUtils;
import com.wm.sys.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 库存信息 前端控制器
 * </p>
 *
 * @author WOM
 * @since 2020-05-07
 */
@RestController
@RequestMapping("/inport")
public class InportController {
    @Autowired
    private InportService inportService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private BookService bookService;

    /**
     * 查询
     */
    @RequestMapping("loadAllInport")
    public DataGridView loadAllInport(InportVo inportVo) {
        IPage<Inport> page = new Page<>(inportVo.getPage(), inportVo.getLimit());
        QueryWrapper<Inport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(inportVo.getProviderId()!=null&&inportVo.getProviderId()!=0,"provider_id",inportVo.getProviderId());
        queryWrapper.eq(inportVo.getBookId()!=null&&inportVo.getBookId()!=0,"book_id",inportVo.getBookId());
        queryWrapper.ge(inportVo.getStartTime()!=null, "inportTime", inportVo.getStartTime());
        queryWrapper.le(inportVo.getEndTime()!=null, "inportTime", inportVo.getEndTime());
        queryWrapper.like(StringUtils.isNotBlank(inportVo.getOperatePerson()), "operatePerson", inportVo.getOperatePerson());
        queryWrapper.like(StringUtils.isNotBlank(inportVo.getRemark()), "remark", inportVo.getRemark());
        queryWrapper.orderByDesc("inportTime");
        this.inportService.page(page, queryWrapper);
        List<Inport> records = page.getRecords();
        for (Inport inport : records) {
            Provider provider = this.providerService.getById(inport.getProviderId());
            if(null!=provider) {
                inport.setProvidername(provider.getProviderName());
            }
            Book book = this.bookService.getById(inport.getBookId());
            if(null!=book) {
                inport.setBookname(book.getName());
                inport.setGrade(book.getGrade());


            }
        }
        return new DataGridView(page.getTotal(), records);
    }

    /**
     * 添加
     */
    @RequestMapping("addInport")
    public ResultObj addInport(InportVo inportVo) {
        try {
            inportVo.setInportTime(new Date());
            User user=(User) WebUtils.getSession().getAttribute("user");
            inportVo.setOperatePerson(user.getName());
            this.inportService.save(inportVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改
     */
    @RequestMapping("updateInport")
    public ResultObj updateInport(InportVo inportVo) {
        try {
            this.inportService.updateById(inportVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }
    /**
     * 删除
     */
    @RequestMapping("deleteInport")
    public ResultObj deleteInport(Integer id) {
        try {
            this.inportService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

}

