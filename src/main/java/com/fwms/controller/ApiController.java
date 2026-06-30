package com.fwms.controller;

import com.fwms.common.Result;
import com.fwms.entity.*;
import com.fwms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired private AuthService authService;
    @Autowired private DashboardService dashboardService;
    @Autowired private PurchaseService purchaseService;
    @Autowired private InboundService inboundService;
    @Autowired private OutboundService outboundService;
    @Autowired private ProductionService productionService;
    @Autowired private ShipmentService shipmentService;
    @Autowired private com.fwms.mapper.SupplierMapper supplierMapper;
    @Autowired private com.fwms.mapper.MaterialMapper materialMapper;
    @Autowired private com.fwms.mapper.ProductMapper productMapper;
    @Autowired private com.fwms.mapper.CustomerMapper customerMapper;
    @Autowired private com.fwms.mapper.WarehouseLocationMapper locationMapper;
    @Autowired private com.fwms.mapper.MaterialStockMapper materialStockMapper;
    @Autowired private com.fwms.mapper.ProductStockMapper productStockMapper;
    @Autowired private com.fwms.mapper.QualityInspectionMapper qcMapper;
    @Autowired private com.fwms.mapper.ProductionMapper productionMapper;
    @Autowired private com.fwms.mapper.SalesOrderMapper salesOrderMapper;
    @Autowired private com.fwms.mapper.UserMapper userMapper;

    @PostMapping("/auth/login")
    public Result<User> apiLogin(@RequestBody Map<String, String> body, HttpSession session) {
        User user = authService.login(body.get("username"), body.get("password"));
        if (user != null) {
            session.setAttribute("user", user);
            return Result.success(user);
        }
        return Result.error("用户名或密码错误");
    }

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        return Result.success(dashboardService.getDashboardData());
    }

    // ===== 供应商 =====
    @GetMapping("/suppliers")
    public Result<List<Supplier>> suppliers(@RequestParam(required = false) String keyword) {
        return Result.success(supplierMapper.findAll(keyword));
    }

    @PostMapping("/suppliers")
    public Result<Supplier> addSupplier(@RequestBody Supplier supplier) {
        supplier.setStatus(1);
        supplierMapper.insert(supplier);
        return Result.success(supplier);
    }

    @PutMapping("/suppliers/{id}")
    public Result<Void> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
        supplier.setId(id);
        supplierMapper.update(supplier);
        return Result.success();
    }

    @DeleteMapping("/suppliers/{id}")
    public Result<Void> deleteSupplier(@PathVariable Long id) {
        supplierMapper.deleteById(id);
        return Result.success();
    }

    // ===== 原料 =====
    @GetMapping("/materials")
    public Result<List<Material>> materials(@RequestParam(required = false) String keyword) {
        return Result.success(materialMapper.findAll(keyword));
    }

    @PostMapping("/materials")
    public Result<Material> addMaterial(@RequestBody Material material) {
        material.setStatus(1);
        materialMapper.insert(material);
        return Result.success(material);
    }

    @PutMapping("/materials/{id}")
    public Result<Void> updateMaterial(@PathVariable Long id, @RequestBody Material material) {
        material.setId(id);
        materialMapper.update(material);
        return Result.success();
    }

    @DeleteMapping("/materials/{id}")
    public Result<Void> deleteMaterial(@PathVariable Long id) {
        materialMapper.deleteById(id);
        return Result.success();
    }

    // ===== 采购 =====
    @GetMapping("/purchases")
    public Result<List<PurchaseOrder>> purchases(@RequestParam(required = false) String status) {
        return Result.success(purchaseService.findAll(status));
    }

    @PostMapping("/purchases")
    public Result<PurchaseOrder> createPurchase(@RequestBody Map<String, Object> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        PurchaseOrder order = new PurchaseOrder();
        order.setSupplierId(Long.valueOf(body.get("supplierId").toString()));
        order.setPurchaseDate(LocalDate.parse(body.get("purchaseDate").toString()));
        if (body.get("expectedDate") != null) {
            order.setExpectedDate(LocalDate.parse(body.get("expectedDate").toString()));
        }
        order.setPurchaserId(user.getId());
        order.setRemark((String) body.get("remark"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> itemMaps = (List<Map<String, Object>>) body.get("items");
        List<PurchaseOrderItem> items = new java.util.ArrayList<>();
        for (Map<String, Object> m : itemMaps) {
            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setMaterialId(Long.valueOf(m.get("materialId").toString()));
            item.setQuantity(new BigDecimal(m.get("quantity").toString()));
            if (m.get("unitPrice") != null) {
                item.setUnitPrice(new BigDecimal(m.get("unitPrice").toString()));
            }
            items.add(item);
        }
        return Result.success(purchaseService.create(order, items));
    }

    @PutMapping("/purchases/{id}/arrived")
    public Result<Void> markArrived(@PathVariable Long id) {
        purchaseService.markArrived(id);
        return Result.success();
    }

    // ===== 入库 =====
    @GetMapping("/inbounds")
    public Result<List<MaterialInbound>> inbounds() {
        return Result.success(inboundService.findAll());
    }

    @PostMapping("/inbounds")
    public Result<MaterialInbound> processInbound(@RequestBody Map<String, Object> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        try {
            MaterialInbound inbound = inboundService.processInbound(
                    Long.valueOf(body.get("supplierId").toString()),
                    Long.valueOf(body.get("materialId").toString()),
                    new BigDecimal(body.get("quantity").toString()),
                    Long.valueOf(body.get("locationId").toString()),
                    body.get("purchaseOrderId") != null ? Long.valueOf(body.get("purchaseOrderId").toString()) : null,
                    Long.valueOf(body.get("inspectorId").toString()),
                    user.getId(),
                    body.get("qcResult").toString(),
                    (String) body.get("remark")
            );
            return Result.success(inbound);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    // ===== 库存 =====
    @GetMapping("/material-stocks")
    public Result<List<MaterialStock>> materialStocks() {
        return Result.success(materialStockMapper.findAll());
    }

    @GetMapping("/product-stocks")
    public Result<List<ProductStock>> productStocks() {
        return Result.success(productStockMapper.findAll());
    }

    // ===== 领料 =====
    @GetMapping("/requisitions")
    public Result<List<MaterialOutbound>> requisitions(@RequestParam(required = false) String status) {
        return Result.success(outboundService.findAll(status));
    }

    @PostMapping("/requisitions")
    public Result<MaterialOutbound> createRequisition(@RequestBody Map<String, Object> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        MaterialOutbound outbound = outboundService.createRequisition(
                body.get("workshop").toString(),
                user.getId(),
                Long.valueOf(body.get("materialId").toString()),
                new BigDecimal(body.get("quantity").toString()),
                (String) body.get("remark")
        );
        return Result.success(outbound);
    }

    @PutMapping("/requisitions/{id}/approve")
    public Result<Void> approveRequisition(@PathVariable Long id, @RequestBody Map<String, Object> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        try {
            outboundService.approveAndIssue(id, user.getId(), Boolean.TRUE.equals(body.get("approved")));
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    // ===== 生产 =====
    @GetMapping("/productions")
    public Result<List<Production>> productions(@RequestParam(required = false) String status) {
        return Result.success(productionService.findAll(status));
    }

    @PostMapping("/productions")
    public Result<Production> createProduction(@RequestBody Production production, HttpSession session) {
        User user = (User) session.getAttribute("user");
        production.setLeaderId(user.getId());
        return Result.success(productionService.create(production));
    }

    @PutMapping("/productions/{id}/submit-qc")
    public Result<Void> submitQc(@PathVariable Long id) {
        productionService.submitForQc(id);
        return Result.success();
    }

    @PostMapping("/productions/{id}/qc")
    public Result<Void> productQc(@PathVariable Long id, @RequestBody Map<String, Object> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        productionService.productQc(id, user.getId(), body.get("result").toString(), (String) body.get("remark"));
        return Result.success();
    }

    @PostMapping("/productions/{id}/store")
    public Result<ProductStock> storeProduct(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            LocalDate deliveryDate = body.get("deliveryDate") != null ?
                    LocalDate.parse(body.get("deliveryDate").toString()) : LocalDate.now();
            ProductStock stock = productionService.storeProduct(id,
                    Long.valueOf(body.get("locationId").toString()), deliveryDate);
            return Result.success(stock);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    // ===== 质检 =====
    @GetMapping("/quality-inspections")
    public Result<List<QualityInspection>> qualityInspections(@RequestParam(required = false) String type) {
        return Result.success(qcMapper.findAll(type));
    }

    // ===== 产品 =====
    @GetMapping("/products")
    public Result<List<Product>> products(@RequestParam(required = false) String keyword) {
        return Result.success(productMapper.findAll(keyword));
    }

    @PostMapping("/products")
    public Result<Product> addProduct(@RequestBody Product product) {
        product.setStatus(1);
        productMapper.insert(product);
        return Result.success(product);
    }

    // ===== 客户 =====
    @GetMapping("/customers")
    public Result<List<Customer>> customers(@RequestParam(required = false) String keyword) {
        return Result.success(customerMapper.findAll(keyword));
    }

    @PostMapping("/customers")
    public Result<Customer> addCustomer(@RequestBody Customer customer) {
        customer.setStatus(1);
        customerMapper.insert(customer);
        return Result.success(customer);
    }

    @PutMapping("/customers/{id}")
    public Result<Void> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        customer.setId(id);
        customerMapper.update(customer);
        return Result.success();
    }

    @DeleteMapping("/customers/{id}")
    public Result<Void> deleteCustomer(@PathVariable Long id) {
        customerMapper.deleteById(id);
        return Result.success();
    }

    // ===== 销售订单 =====
    @GetMapping("/sales-orders")
    public Result<List<SalesOrder>> salesOrders(@RequestParam(required = false) String status) {
        return Result.success(salesOrderMapper.findAll(status));
    }

    @PostMapping("/sales-orders")
    public Result<SalesOrder> createSalesOrder(@RequestBody SalesOrder order, HttpSession session) {
        User user = (User) session.getAttribute("user");
        order.setOrderNo(com.fwms.common.OrderNoGenerator.generate("SO"));
        order.setStatus("PENDING");
        order.setSalespersonId(user.getId());
        salesOrderMapper.insert(order);
        return Result.success(order);
    }

    // ===== 发货 =====
    @GetMapping("/shipments")
    public Result<List<Shipment>> shipments() {
        return Result.success(shipmentService.findAll());
    }

    @PostMapping("/shipments")
    public Result<Shipment> ship(@RequestBody Map<String, Object> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        try {
            Shipment shipment = shipmentService.ship(
                    Long.valueOf(body.get("salesOrderId").toString()),
                    Long.valueOf(body.get("locationId").toString()),
                    user.getId()
            );
            return Result.success(shipment);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    // ===== 库位 =====
    @GetMapping("/locations")
    public Result<List<WarehouseLocation>> locations(@RequestParam(required = false) String zone) {
        if (zone != null && !zone.isEmpty()) {
            return Result.success(locationMapper.findByZone(zone));
        }
        return Result.success(locationMapper.findAll());
    }

    @PostMapping("/locations")
    public Result<WarehouseLocation> addLocation(@RequestBody WarehouseLocation location) {
        location.setStatus(1);
        locationMapper.insert(location);
        return Result.success(location);
    }

    // ===== 用户 =====
    @GetMapping("/users/qc")
    public Result<List<User>> qcUsers() {
        return Result.success(userMapper.findAll().stream()
                .filter(u -> "QC".equals(u.getRole()))
                .peek(u -> u.setPassword(null))
                .collect(java.util.stream.Collectors.toList()));
    }
}
