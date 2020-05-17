package com.wm.jcgl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Order;
import com.wm.jcgl.entity.Match;
import com.wm.jcgl.entity.Match;
import com.wm.jcgl.service.BookService;
import com.wm.jcgl.service.MatchService;
import com.wm.jcgl.service.OrderService;
import com.wm.jcgl.vo.MatchVo;
import com.wm.jcgl.vo.MatchVo;
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
 * 教材匹配信息 前端控制器
 * </p>
 *
 * @author WOM
 * @since 2020-05-15
 */
@RestController
@RequestMapping("match")
public class MatchController {

    @Resource
    private MatchService matchService;
    @Resource
    private OrderService orderService;
    @Resource
    private BookService bookService;
    @Resource
    private DeptService deptService;

    /**
     * 查询
     */
    @RequestMapping("loadAllMatch")
    public DataGridView loadAllMatch(MatchVo matchVo) {
        IPage<Match> page = new Page<>(matchVo.getPage(), matchVo.getLimit());
        QueryWrapper<Match> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(null!=matchVo.getOrderId(), "order_id",matchVo.getOrderId());
//        if(StringUtils.isNotBlank(matchVo.getSchoolname())){
//            queryWrapper.inSql("dept_id","select id from sys_dept where sys_dept.title like '%"+matchVo.getSchoolname()+"%'");
//        }
//        if(StringUtils.isNotBlank(matchVo.getOrdername())){
//            queryWrapper.inSql("order_id","select id from b_order where b_order.comment like '%"+matchVo.getOrdername()+"%'");
//        }
//        if(StringUtils.isNotBlank(matchVo.getBookname())){
//            queryWrapper.inSql("book_id","select id from b_book where b_book.name like '%"+matchVo.getBookname()+"%'");
//        }
        User user = (User) WebUtils.getSession().getAttribute("user");
        if(1!=user.getDeptid()){
            //非销售店查询
            queryWrapper.eq("dept_id",user.getDeptid());
        }
        this.matchService.page(page, queryWrapper);
        List<Match> records = page.getRecords();
        for (Match match : records) {
            //根据id查询征订期号名称
            if(null!=match.getOrderId()){
                Order order  = this.orderService.getById(match.getOrderId());
                if(null!=order) {
                    match.setOrdername(order.getComment());
                }
            }
            //根据id查询书名
            if(null!=match.getBookId()){
                Book book  = this.bookService.getById(match.getBookId());
                if(null!=book) {
                    match.setBookname(book.getName());
                    match.setGrade(book.getGrade());
                }
            }
            //根据id查询学校名
            if(null!=match.getDeptId()){
                Dept dept  = this.deptService.getById(match.getDeptId());
                if(null!=dept) {
                    match.setSchoolname(dept.getTitle());
                }
            }
        }
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 添加
     */
    @RequestMapping("addMatch")
    public ResultObj addMatch(MatchVo matchVo) {
        try {
            matchVo.setCreateTime(new Date());
            User user = (User) WebUtils.getSession().getAttribute("user");
            matchVo.setOperName(user.getName());
            matchVo.setStatus(Constast.STATUS_FALSE);

            //matchVo.setDeptId(user.getDeptid());
            this.matchService.save(matchVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }
    /**
     * 修改
     */
    @RequestMapping("updateMatch")
    public ResultObj updateMatch(MatchVo matchVo) {
        try {
            this.matchService.updateById(matchVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 提交确认
     */
    @RequestMapping("submitMatch")
    public ResultObj submitMatch(Integer id) {
        try {
            Match match = this.matchService.getById(id);
            match.setStatus(Constast.STATUS_TRUE);
            this.matchService.updateById(match);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_SUCCESS;
        }
    }
    /**
     * 批量提交确认
     */
    @RequestMapping("batchSubmitMatch")
    public ResultObj batchSubmitMatch(MatchVo matchVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            for (Integer id : matchVo.getIds()) {
                idList.add(id);
            }
            Collection<Match> matchList = this.matchService.listByIds(idList);
            for (Match match : matchList) {
                match.setStatus(Constast.STATUS_TRUE);
            }
            this.matchService.updateBatchById(matchList);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_SUCCESS;
        }
    }

    /**
     * 撤回确认
     */
    @RequestMapping("withdrawMatch")
    public ResultObj  withdrawMatch(Integer id) {
        try {
            Match match = this.matchService.getById(id);
            match.setStatus(Constast.STATUS_FALSE);
            this.matchService.updateById(match);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_SUCCESS;
        }
    }

    /**
     * 删除
     */
    @RequestMapping("deleteMatch")
    public ResultObj deleteMatch(Integer id) {
        try {
            this.matchService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteMatch")
    public ResultObj batchDeleteMatch(MatchVo matchVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            for (Integer id : matchVo.getIds()) {
                idList.add(id);
            }
            this.matchService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }




}

