package com.wm.jcgl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Provider;
import com.wm.jcgl.service.BookService;
import com.wm.jcgl.service.ProviderService;
import com.wm.jcgl.vo.BookVo;
import com.wm.jcgl.vo.ProviderVo;
import com.wm.sys.common.*;
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
 * 在此期号内建立、修改、删除和向省店上报书目信息 前端控制器
 * </p>
 *
 * @author WOM
 * @since 2020-04-21
 */
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ProviderService providerService;

    /**
     * 查询
     */
    @RequestMapping("loadAllBook")
    public DataGridView loadAllBook(BookVo bookVo) {
        IPage<Book> page = new Page<>(bookVo.getPage(), bookVo.getLimit());
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(bookVo.getProviderId()!=null&&bookVo.getProviderId()!=0,"provider_id",bookVo.getProviderId());
        queryWrapper.like(StringUtils.isNotBlank(bookVo.getName()), "name", bookVo.getName());
        queryWrapper.like(StringUtils.isNotBlank(bookVo.getGrade()), "grade", bookVo.getGrade());
        queryWrapper.like(StringUtils.isNotBlank(bookVo.getBookNum()), "bookNum", bookVo.getBookNum());
        queryWrapper.like(bookVo.getFree()!=null, "free", bookVo.getFree());
        queryWrapper.like(StringUtils.isNotBlank(bookVo.getOpername()), "opername", bookVo.getOpername());
        this.bookService.page(page, queryWrapper);
        List<Book> records = page.getRecords();
        for (Book book : records) {
            Provider provider = this.providerService.getById(book.getProviderId());
            if(null!=provider) {
                book.setProvidername(provider.getProviderName());
            }
        }
        return new DataGridView(page.getTotal(), records);
    }

    /**
     * 添加
     */
    @RequestMapping("addBook")
    public ResultObj addBook(BookVo bookVo) {
        try {
            bookVo.setCreatetime(new Date());
            User user = (User) WebUtils.getSession().getAttribute("user");
            bookVo.setOpername(user.getName());
            this.bookService.save(bookVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改
     */
    @RequestMapping("updateBook")
    public ResultObj updateBook(BookVo bookVo) {
        try {
            this.bookService.updateById(bookVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除
     */
    @RequestMapping("deleteBook")
    public ResultObj deleteBook(Integer id) {
        try {
            this.bookService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteProvider")
    public ResultObj batchDeleteProvider(ProviderVo providerVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            for (Integer id : providerVo.getIds()) {
                idList.add(id);
            }
            this.bookService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 加载所有可用的供应商
     */
    @RequestMapping("loadAllBookForSelect")
    public DataGridView loadAllBookForSelect() {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
        List<Book> list = this.bookService.list(queryWrapper);
        for (Book book : list) {
            Provider provider = this.providerService.getById(book.getProviderId());
            if(null!=provider) {
                book.setProvidername(provider.getProviderName());
            }
        }
        return new DataGridView(list);
    }

    /**
     *根据供应商ID查询商品信息
     */
    @RequestMapping("loadBookByProviderId")
    public DataGridView loadBookByProviderId(Integer providerid) {
        QueryWrapper<Book> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
        queryWrapper.eq(providerid!=null, "provider_id", providerid);
        List<Book> list = this.bookService.list(queryWrapper);
        for (Book book : list) {
            Provider provider = this.providerService.getById(book.getProviderId());
            if(null!=provider) {
                book.setProvidername(provider.getProviderName());
            }
        }
        return new DataGridView(list);
    }
}

