package com.wm.jcgl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.*;
import com.wm.jcgl.service.BookService;
import com.wm.jcgl.service.MatchService;
import com.wm.jcgl.service.OrderService;
import com.wm.jcgl.vo.MatchVo;
import com.wm.sys.common.*;
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

    //SELECT dept_id FROM b_match WHERE order_id=5 GROUP BY dept_id
    /**
     * 根据期号id加载部门管理左边的部门树的json
     */
    @RequestMapping("loadDeptTreeJsonByOrderId")
    public DataGridView loadDeptTreeJsonByOrderId(MatchVo matchVo) {
        QueryWrapper<Match> queryWrapper = new QueryWrapper<>();
//        添加部门条件
        if(null!=matchVo.getOrderId()){
            queryWrapper.groupBy("dept_id");
            queryWrapper.eq("order_id",matchVo.getOrderId());
            queryWrapper.select("dept_id");
            List<Match> list = this.matchService.list(queryWrapper);
            if(list.size()>0){
                List<TreeNode> treeNodes=new ArrayList<>();
                Dept deptManger  = this.deptService.getById(1);
                treeNodes.add(new TreeNode(deptManger.getId(), deptManger.getPid(), deptManger.getTitle(), deptManger.getOpen()==1?true:false));
                for (Match match : list) {
                    //根据id查询学校名
                    if(null!=match.getDeptId()){
                        Dept dept  = this.deptService.getById(match.getDeptId());
                        if(null!=dept) {
                            match.setSchoolname(dept.getTitle().replace(" ", ""));
                            Boolean spread=dept.getOpen()==1?true:false;
                            treeNodes.add(new TreeNode(dept.getId(), dept.getPid(), dept.getTitle(), spread));
                        }
                    }
                }
                return new DataGridView(treeNodes);
            }
            else{
                Integer code = Constast.ERROR;
                String msg = "无数据";

                return  new DataGridView(code,msg);
            }
        }

        return new DataGridView(null);
    }

    /**
     * 查询各学校余缺量
     */
    @RequestMapping("loadSchoolMatch")
    public List<Line> loadSchoolMatch(MatchVo matchVo) {
        QueryWrapper<Match> queryWrapper = new QueryWrapper<>();
//        添加部门条件
        queryWrapper.select("dept_id","SUM(lNum)AS lNum","SUM(bNum)AS bNum");
        if(null!=matchVo.getDeptId()){
            queryWrapper.select("grade","dept_id","SUM(lNum)AS lNum","SUM(bNum)AS bNum");
            queryWrapper.eq("dept_id",matchVo.getDeptId());
            queryWrapper.groupBy("grade");
        }
        queryWrapper.eq("order_id",matchVo.getOrderId());
        queryWrapper.groupBy("dept_id");
        List<Match> matchList = this.matchService.list(queryWrapper);
        List<Line> list = new ArrayList<Line>();
        for (Match match:matchList) {
            if(null!=matchVo.getDeptId()){
                list.add(new Line(match.getGrade(),match.getLNum(),match.getBNum()));
            }else {
                //根据id查询学校名
                if(null!=match.getDeptId()){
                    Dept dept  = this.deptService.getById(match.getDeptId());
                    if(null!=dept) {
                        match.setSchoolname(dept.getTitle().replace(" ", ""));
                    }
                }
                list.add(new Line(match.getSchoolname(),match.getLNum(),match.getBNum()));
            }
        }
        return list;
    }
    /**
     * 查询各书目余缺量
     */
    @RequestMapping("loadBookMatch")
    public List<Bar> loadBookMatch(MatchVo matchVo) {
//        SELECT book_id,SUM(lNum)AS lNum,SUM(bNum)AS bNum
//        FROM b_match
//        WHERE order_id=5
//        GROUP BY book_id
        QueryWrapper<Match> queryWrapper = new QueryWrapper<>();

        if(null!=matchVo.getDeptId()){
            //添加部门条件
            queryWrapper.select("dept_id");
            queryWrapper.eq("dept_id",matchVo.getDeptId());
            //添加年级条件
            if(StringUtils.isNotBlank(matchVo.getGrade())){
                queryWrapper.select("grade");
                queryWrapper.eq("grade",matchVo.getGrade());
                queryWrapper.groupBy("grade");
            }
        }
        queryWrapper.select("book_id","SUM(lNum)AS lNum","SUM(bNum)AS bNum");

        queryWrapper.eq("order_id",matchVo.getOrderId());
        queryWrapper.groupBy("book_id");
        List<Match> matchList = this.matchService.list(queryWrapper);
        List<Bar> list = new ArrayList<Bar>();
        for (Match match:matchList) {
             //根据id查询书名
             if(null!=match.getBookId()){
                 Book book  = this.bookService.getById(match.getBookId());
                 if(null!=book) {
                     match.setBookname(book.getName());
                     match.setGrade(book.getGrade());
                 }
             }
             list.add(new Bar(match.getBookname(),match.getLNum(),match.getBNum()));

        }
        return list;
    }

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

            match.setNumber(match.getLNum()==0?match.getBNum():match.getLNum());
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
            if(matchVo.getType()==1){
                matchVo.setLNum(matchVo.getNumber());
            }else
            {
                matchVo.setBNum(matchVo.getNumber());
            }
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
//            Match temp = this.matchService.getById(matchVo.getId());
//            if(temp.getType()==matchVo.getType()){
//                if(matchVo.getType()==1){
//                    matchVo.setLNum(matchVo.getNumber());
//                }else {
//                    matchVo.setBNum(matchVo.getNumber());
//                }
//            }else{
//                if(temp.getType()==1){
//                    matchVo.setLimit()
//                }
//            }

            if(matchVo.getType()==1){
                matchVo.setLNum(matchVo.getNumber());
                matchVo.setBNum(0);
            }else {
                matchVo.setBNum(matchVo.getNumber());
                matchVo.setLNum(0);
            }
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

