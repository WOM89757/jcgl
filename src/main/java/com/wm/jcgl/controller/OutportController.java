package com.wm.jcgl.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Outport;
import com.wm.jcgl.entity.Provider;
import com.wm.jcgl.service.BookService;
import com.wm.jcgl.service.OutportService;
import com.wm.jcgl.service.ProviderService;
import com.wm.jcgl.vo.OutportVo;
import com.wm.sys.common.DataGridView;
import com.wm.sys.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 库存退货信息 前端控制器
 * </p>
 *
 * @author WOM
 * @since 2020-05-07
 */
@RestController
@RequestMapping("outport")
public class OutportController {

    @Autowired
    private OutportService outportService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private BookService bookService;

    /**
     * 查询
     */
    @RequestMapping("loadAllOutport")
    public DataGridView loadAllOutport(OutportVo outportVo) {
        IPage<Outport> page = new Page<>(outportVo.getPage(), outportVo.getLimit());
        QueryWrapper<Outport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(outportVo.getProviderId()!=null&&outportVo.getProviderId()!=0,"provider_id",outportVo.getProviderId());
        queryWrapper.eq(outportVo.getBookId()!=null&&outportVo.getBookId()!=0,"book_id",outportVo.getBookId());
        queryWrapper.ge(outportVo.getStartTime()!=null, "inportTime", outportVo.getStartTime());
        queryWrapper.le(outportVo.getEndTime()!=null, "inportTime", outportVo.getEndTime());
        queryWrapper.like(StringUtils.isNotBlank(outportVo.getOperateperson()), "operatePerson", outportVo.getOperateperson());
        queryWrapper.like(StringUtils.isNotBlank(outportVo.getRemark()), "remark", outportVo.getRemark());
        queryWrapper.orderByDesc("outputTime");
        this.outportService.page(page, queryWrapper);
        List<Outport> records = page.getRecords();
        for (Outport outport : records) {
            Provider provider = this.providerService.getById(outport.getProviderId());
            if(null!=provider) {
                outport.setProvidername(provider.getProviderName());
            }
            Book book = this.bookService.getById(outport.getBookId());
            if(null!=book) {
                outport.setBookname(book.getName());

            }
        }
        return new DataGridView(page.getTotal(), records);
    }

    /**
     * 添加退货信息
     */
    @RequestMapping("addOutport")
    public ResultObj addOutport(Integer id, Integer number, String remark) {
        try {
            this.outportService.addOutPort(id,number,remark);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }
}

