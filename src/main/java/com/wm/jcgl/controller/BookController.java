package com.wm.jcgl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Provider;
import com.wm.jcgl.service.BookService;
import com.wm.jcgl.service.ProviderService;
import com.wm.jcgl.vo.BookVo;
import com.wm.sys.common.*;
import com.wm.sys.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
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
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private ProviderService providerService;

    /**
     * 书目查询
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
            if(null!=book.getProviderId()){
                Provider provider = this.providerService.getById(book.getProviderId());
                if(null!=provider) {
                    book.setProvidername(provider.getProviderName());
                }
            }
        }
        return new DataGridView(page.getTotal(), records);
    }

    /**
     * 自编书目查询
     */
    @RequestMapping("loadAllBookByOrderId")
    public DataGridView loadAllBookByOrderId(BookVo bookVo) {
        IPage<Book> page = new Page<>(bookVo.getPage(), bookVo.getLimit());
        List<Book> records = null;
        if(bookVo.getOrderid()!=null){
            List<Integer> booksIds = this.bookService.queryOrderBookIdsByOrder(bookVo.getOrderid());
            if(booksIds.size()>0){
                QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
                queryWrapper.in("id",booksIds);
                queryWrapper.eq(bookVo.getProviderId()!=null&&bookVo.getProviderId()!=0,"provider_id",bookVo.getProviderId());
                queryWrapper.like(StringUtils.isNotBlank(bookVo.getName()), "name", bookVo.getName());
                queryWrapper.like(StringUtils.isNotBlank(bookVo.getEdition()), "edition", bookVo.getEdition());
                queryWrapper.like(bookVo.getId()!=null, "id", bookVo.getId());
                queryWrapper.like(StringUtils.isNotBlank(bookVo.getGrade()), "grade", bookVo.getGrade());
                queryWrapper.like(StringUtils.isNotBlank(bookVo.getBookNum()), "bookNum", bookVo.getBookNum());
                queryWrapper.eq(bookVo.getFree()!=null, "free", bookVo.getFree());
                queryWrapper.like(StringUtils.isNotBlank(bookVo.getOpername()), "opername", bookVo.getOpername());
                this.bookService.page(page, queryWrapper);
                records = page.getRecords();
                for (Book book : records) {
                    book.setOrderid(bookVo.getOrderid());
                    if(null!=book.getProviderId()){
                        Provider provider = this.providerService.getById(book.getProviderId());
                        if(null!=provider) {
                            book.setProvidername(provider.getProviderName());
                        }
                    }
                }
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
            if(bookVo.getOrderid()!=null){
                //保存单个期号ID与自编书目ID之间的关系
                Integer[] ids = new Integer[1];
                ids[0]=bookVo.getId();
                this.bookService.saveBookOrder(bookVo.getOrderid(),ids);
            }

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
        }catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR_DELETE;
        }
    }
    /**
     * 删除征订期号与自编书目联系
     */
    @RequestMapping("deleteRelationBookWithOrderId")
    public ResultObj deleteBookWithOrderId(@Param("orderid") Integer orderid,@Param("bookid") Integer bookid) {
        try {
            this.bookService.deleteBookOrderByOidAndBid(orderid,bookid);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteBook")
    public ResultObj batchDeleteBook(BookVo bookVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            for (Integer id : bookVo.getIds()) {
                idList.add(id);
            }
            this.bookService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR_DELETE;
        }
    }
    /**
     * 批量删除征订期号与自编书目联系
     */
    @RequestMapping("batchDeleteRelationBookWithOrderId")
    public ResultObj batchDeleteRelationBookWithOrderId(BookVo bookVo) {
        try {
//            Collection<Serializable> idList = new ArrayList<Serializable>();
//            for (Integer id : bookVo.getIds()) {
//                idList.add(id);
//            }
            for (Integer bookid : bookVo.getIds()) {
                this.bookService.deleteBookOrderByOidAndBid(bookVo.getOrderid(),bookid);
            }
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR_DELETE;
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

//    /**
//     *根据期号ID查询书目信息
//     */
//    @RequestMapping("loadBookByOrderId")
//    public DataGridView loadBookByOrderId(Integer orderid) {
//        QueryWrapper<Book> queryWrapper=new QueryWrapper<>();
//        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
//        queryWrapper.eq(orderid!=null, "order_id", orderid);
//        List<Book> list = this.bookService.listBYOrderId(queryWrapper);
//        for (Book book : list) {
//            Provider provider = this.providerService.getById(book.getProviderId());
//            if(null!=provider) {
//                book.setProvidername(provider.getProviderName());
//            }
//        }
//        return new DataGridView(list);
//    }
    /**
     *根据供应商ID查询书目信息
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

