package com.wm.jcgl.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Allotment;
import com.wm.jcgl.entity.Allotment;
import com.wm.jcgl.entity.Order;
import com.wm.jcgl.service.AllotmentService;
import com.wm.jcgl.service.BookService;
import com.wm.jcgl.service.OrderService;
import com.wm.jcgl.service.SubscriptionService;
import com.wm.jcgl.vo.AllotmentVo;
import com.wm.jcgl.vo.BookVo;
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

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 配发信息 前端控制器
 * </p>
 *
 * @author WOM
 * @since 2020-05-15
 */
@RestController
@RequestMapping("allotment")
public class AllotmentController {

    @Resource
    private AllotmentService allotmentService;
    @Resource
    private SubscriptionService subscriptionService;
    @Resource
    private BookService bookService;
    @Resource
    private OrderService orderService;
    @Resource
    private DeptService deptService;

    /**
     * 查询所有配发信息
     *
     */
    @RequestMapping("loadALLAllotment")
    public DataGridView loadALLAllotment(AllotmentVo allotmentVo) {
        IPage<Allotment> page = new Page<>(allotmentVo.getPage(), allotmentVo.getLimit());
        QueryWrapper<Allotment> queryWrapper = new QueryWrapper<Allotment>();
        queryWrapper.like(null!=allotmentVo.getOrderId(), "order_id", allotmentVo.getOrderId());
        queryWrapper.like(null!=allotmentVo.getDeptId(), "dept_id", allotmentVo.getDeptId());
        queryWrapper.like(null!=allotmentVo.getStatus(), "status", allotmentVo.getStatus());
        queryWrapper.like(StringUtils.isNotBlank(allotmentVo.getGrade()), "grade", allotmentVo.getGrade());

        if(StringUtils.isNotBlank(allotmentVo.getSchoolname())){
            queryWrapper.inSql("dept_id","select id from sys_dept where sys_dept.title like '%"+allotmentVo.getSchoolname()+"%'");
        }
        List<Allotment> allotmentList = this.allotmentService.list(queryWrapper);
        for (Allotment allotment : allotmentList) {
            Book book = this.bookService.getById(allotment.getBookId());
            if(null!=book) {
                allotment.setBookname(book.getName());
            }
            Order order = this.orderService.getById(allotment.getOrderId());
            if(null!=order) {
                allotment.setOrdername(order.getComment());
            }
            Dept dept = this.deptService.getById(allotment.getDeptId());
            if(null!=dept) {
                allotment.setSchoolname(dept.getTitle());
            }
        }

        return new DataGridView(page.getTotal(), allotmentList);
    }

    /**
     * 查询配发信息
     *
     */
    @RequestMapping("loadAllotmentWithOrderId")
    public DataGridView loadAllotment(AllotmentVo allotmentVo) {
        IPage<Allotment> page = new Page<>(allotmentVo.getPage(), allotmentVo.getLimit());
        List<Allotment> allotmentList = this.subscriptionService.getSubscriptionForAllotment(allotmentVo.getOrderId());
        for (Allotment allotment : allotmentList) {
            Book book = this.bookService.getById(allotment.getBookId());
            if(null!=book) {
                allotment.setBookname(book.getName());
            }
            Order order = this.orderService.getById(allotment.getOrderId());
            if(null!=order) {
                allotment.setOrdername(order.getComment());
            }
            Dept dept = this.deptService.getById(allotment.getDeptId());
            if(null!=dept) {
                allotment.setSchoolname(dept.getTitle());
            }
        }

        return new DataGridView(page.getTotal(), allotmentList);
    }
    /**
     * 添加
     */
    @RequestMapping("addAllotment")
    @ResponseBody
    public ResultObj addAllotment(@RequestParam("allotmentlist") String allotmentlist) {
        try {
            User user = (User) WebUtils.getSession().getAttribute("user");
            List<Allotment> allotmentVolist = JSON.parseArray(allotmentlist,Allotment.class);
            for (Allotment allotment: allotmentVolist
            ) {
//                QueryWrapper wrapper = new QueryWrapper();
//                wrapper.eq("order_id",allotment.getOrderId());
//                wrapper.eq("book_id",allotment.getBookId());
//                Allotment temp = this.allotmentService.getOne(wrapper);
//                if(null!=temp){
//                    this.allotmentService.removeById(temp.getId());
//                }
                allotment.setOperName(user.getName());
                allotment.setCreateTime(new Date());

            }
            this.allotmentService.saveBatch(allotmentVolist);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }
    /**
     * 修改
     */
    @RequestMapping("update")
    public ResultObj updateBook(AllotmentVo allotmentVo) {
        try {
            allotmentVo.setStatus(Constast.STATUS_TRUE);
            this.allotmentService.updateById(allotmentVo);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }


    /**
     * 删除
     */
    @RequestMapping("deleteAllotment")
    public ResultObj delete(Integer id) {
        try {
            this.allotmentService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        }catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR_DELETE;
        }
    }
    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteAllotment")
    public ResultObj batchDeleteBook(AllotmentVo allotmentVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            for (Integer id : allotmentVo.getIds()) {
                idList.add(id);
            }
            this.allotmentService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }


}

