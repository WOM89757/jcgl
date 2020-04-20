package com.wm.jcgl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 业务管理的路由器
 * @author WOM
 *
 */
@Controller
@RequestMapping("/bus")
public class JcglController {

    /**
     * 跳转到期号维护
     */
    @RequestMapping("toOrderManager")
    public String toOrderManager() {
        return "business/order/orderManager";
    }

    /**
     * 跳转到客户管理
     */
    @RequestMapping("toCustomerManager")
    public String toCustomerManager() {
        return "business/customer/customerManager";
    }
    /**
     * 跳转到供应商管理
     */
    @RequestMapping("toProviderManager")
    public String toProviderManager() {
        return "business/provider/providerManager";
    }
    /**
     * 跳转到自编书目管理
     */
    @RequestMapping("toBooksManager")
    public String toBooksManager() {
        return "business/books/booksManager";
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

