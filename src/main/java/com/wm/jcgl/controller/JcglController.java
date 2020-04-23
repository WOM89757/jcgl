package com.wm.jcgl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 业务管理的路由器
 * @author WOM
 *
 */
@Controller
@RequestMapping("bus")
public class JcglController {

    /**
     * 跳转到期号维护
     */
    @RequestMapping("toOrderManager")
    public String toOrderManager() {
        return "business/order/orderManager";
    }

    /**
     * 跳转到供应商管理
     */
    @RequestMapping("toProviderManager")
    public String toProviderManager() {
        return "business/provider/providerManager";
    }
    /**
     * 跳转到书目管理
     */
    @RequestMapping("toBooksManager")
    public String toBooksManager() {
        return "business/books/booksManager";
    }
    /**
     * 跳转到自编书目管理
     */
    @RequestMapping("toOrderBooksManager")
    public String toOrderBooksManager() {
        return "business/books/orderBooksManager";
    }
    /**
     * 跳转到年级套订模型
     */
    @RequestMapping("toBookModelManager")
    public String toBookModelManager() {
        return "business/bookModel/bookModelManager";
    }
    /**
     * 跳转到进货管理
     */
    @RequestMapping("toInportManager")
    public String toInportManager() {
        return "business/inport/inportManager";
    }
    /**
     * 跳转到退货查询管理
     */
    @RequestMapping("toOutportManager")
    public String toOutportManager() {
        return "business/outport/outportManager";
    }
}

