package com.fwms.controller;

import com.fwms.entity.User;
import com.fwms.enums.UserRole;
import com.fwms.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class PageController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("roleLabel", UserRole.fromCode(user.getRole()).getLabel());
        Map<String, Object> data = dashboardService.getDashboardData();
        model.addAllAttributes(data);
        return "dashboard";
    }

    @GetMapping("/supplier")
    public String supplier(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("title", "供应商管理");
        return "supplier/list";
    }

    @GetMapping("/material")
    public String material(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("title", "原料管理");
        return "material/list";
    }

    @GetMapping("/purchase")
    public String purchase(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("title", "采购管理");
        return "purchase/list";
    }

    @GetMapping("/inbound")
    public String inbound(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("title", "原料入库");
        return "inbound/list";
    }

    @GetMapping("/material-stock")
    public String materialStock(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("title", "原料库存");
        return "stock/material";
    }

    @GetMapping("/requisition")
    public String requisition(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("title", "生产领料");
        return "outbound/list";
    }

    @GetMapping("/production")
    public String production(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("title", "生产管理");
        return "production/list";
    }

    @GetMapping("/qc")
    public String qc(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("title", "质检管理");
        return "qc/list";
    }

    @GetMapping("/product-stock")
    public String productStock(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("title", "成品库存");
        return "stock/product";
    }

    @GetMapping("/customer")
    public String customer(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("title", "客户管理");
        return "customer/list";
    }

    @GetMapping("/sales")
    public String sales(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("title", "客户订单");
        return "sales/list";
    }

    @GetMapping("/shipment")
    public String shipment(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("title", "发货出库");
        return "shipment/list";
    }

    @GetMapping("/location")
    public String location(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("title", "库位管理");
        return "location/list";
    }
}
