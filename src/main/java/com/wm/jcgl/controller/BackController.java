package com.wm.jcgl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Back;
import com.wm.jcgl.entity.Order;
import com.wm.jcgl.service.BookService;
import com.wm.jcgl.service.BackService;
import com.wm.jcgl.service.OrderService;
import com.wm.jcgl.vo.BackVo;
import com.wm.sys.common.DataGridView;
import com.wm.sys.entity.Dept;
import com.wm.sys.service.DeptService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 学校退货信息 前端控制器
 * </p>
 *
 * @author WOM
 * @since 2020-05-15
 */
@RestController
@RequestMapping("back")
public class BackController {
    @Resource
    private BackService backService;
    @Resource
    private OrderService orderService;
    @Resource
    private BookService bookService;
    @Resource
    private DeptService deptService;
    /**
     * 查询
     */
    @RequestMapping("loadAllBack")
    public DataGridView loadAllBack(BackVo backVo) {
        IPage<Back> page = new Page<>(backVo.getPage(), backVo.getLimit());
        QueryWrapper<Back> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(backVo.getOrderId()!=null,"order_id",backVo.getOrderId());
        queryWrapper.eq(backVo.getDeptId()!=null,"dept_id",backVo.getDeptId());
        this.backService.page(page, queryWrapper);
        List<Back> records = page.getRecords();
        for (Back back : records) {
            //根据id查询学校名
            if(null!=back.getDeptId()){
                Dept dept  = this.deptService.getById(back.getDeptId());
                if(null!=dept) {
                    back.setSchoolname(dept.getTitle().replace(" ", ""));
                }
            }
            if(null!=back.getBookId()){
                Book book  = this.bookService.getById(back.getBookId());
                if(null!=book) {
                    back.setBookname(book.getName());
                }
            }
            if(null!=back.getOrderId()){
                Order order  = this.orderService.getById(back.getOrderId());
                if(null!=order) {
                    back.setOrdername(order.getComment());
                }
            }
        }
        return new DataGridView(page.getTotal(), records);
    }


}

