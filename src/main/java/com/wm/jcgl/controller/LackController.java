package com.wm.jcgl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Lack;
import com.wm.jcgl.entity.Order;
import com.wm.jcgl.service.BookService;
import com.wm.jcgl.service.LackService;
import com.wm.jcgl.service.OrderService;
import com.wm.jcgl.vo.LackVo;
import com.wm.sys.common.DataGridView;
import com.wm.sys.entity.Dept;
import com.wm.sys.service.DeptService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 学校缺货信息 前端控制器
 * </p>
 *
 * @author WOM
 * @since 2020-05-15
 */
@RestController
@RequestMapping("lack")
public class LackController {
    @Resource
    private LackService lackService;
    @Resource
    private OrderService orderService;
    @Resource
    private BookService bookService;
    @Resource
    private DeptService deptService;
    /**
     * 查询
     */
    @RequestMapping("loadAllLack")
    public DataGridView loadAllLack(LackVo lackVo) {
        IPage<Lack> page = new Page<>(lackVo.getPage(), lackVo.getLimit());
        QueryWrapper<Lack> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(lackVo.getOrderId()!=null,"order_id",lackVo.getOrderId());
        queryWrapper.eq(lackVo.getDeptId()!=null,"dept_id",lackVo.getDeptId());
        this.lackService.page(page, queryWrapper);
        List<Lack> records = page.getRecords();
        for (Lack lack : records) {
            //根据id查询学校名
            if(null!=lack.getDeptId()){
                Dept dept  = this.deptService.getById(lack.getDeptId());
                if(null!=dept) {
                    lack.setSchoolname(dept.getTitle().replace(" ", ""));
                }
            }
            if(null!=lack.getBookId()){
                Book book  = this.bookService.getById(lack.getBookId());
                if(null!=book) {
                    lack.setBookname(book.getName());
                }
            }
            if(null!=lack.getOrderId()){
                Order order  = this.orderService.getById(lack.getOrderId());
                if(null!=order) {
                    lack.setOrdername(order.getComment());
                }
            }
        }
        return new DataGridView(page.getTotal(), records);
    }

}

