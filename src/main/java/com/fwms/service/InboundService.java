package com.fwms.service;

import com.fwms.common.OrderNoGenerator;
import com.fwms.entity.*;
import com.fwms.enums.OrderStatus;
import com.fwms.enums.QcResult;
import com.fwms.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InboundService {

    @Autowired
    private MaterialInboundMapper inboundMapper;
    @Autowired
    private MaterialStockMapper stockMapper;
    @Autowired
    private QualityInspectionMapper qcMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private DashboardService dashboardService;

    public List<MaterialInbound> findAll() {
        return inboundMapper.findAll();
    }

    /**
     * 原料入库流程：验收 -> 质检 -> 合格则入库增加库存
     */
    @Transactional
    public MaterialInbound processInbound(Long supplierId, Long materialId, BigDecimal quantity,
            Long locationId, Long purchaseOrderId, Long inspectorId, Long operatorId,
            String qcResult, String remark) {
        Material material = materialMapper.findById(materialId);
        if (material == null) {
            throw new RuntimeException("原料不存在");
        }

        // 创建质检记录
        QualityInspection qc = new QualityInspection();
        qc.setInspectionNo(OrderNoGenerator.generate("QC"));
        qc.setType("MATERIAL");
        qc.setMaterialId(materialId);
        qc.setQuantity(quantity);
        qc.setResult(qcResult);
        qc.setInspectorId(inspectorId);
        qc.setRemark(remark);
        qcMapper.insert(qc);

        if (!QcResult.PASS.name().equals(qcResult)) {
            throw new RuntimeException("质检不合格，无法入库，请办理退货");
        }

        // 生成入库单
        MaterialInbound inbound = new MaterialInbound();
        inbound.setInboundNo(OrderNoGenerator.generate("IN"));
        inbound.setPurchaseOrderId(purchaseOrderId);
        inbound.setSupplierId(supplierId);
        inbound.setMaterialId(materialId);
        inbound.setQuantity(quantity);
        inbound.setLocationId(locationId);
        inbound.setQcResult(qcResult);
        inbound.setQcInspectorId(inspectorId);
        inbound.setOperatorId(operatorId);
        inbound.setRemark(remark);
        inboundMapper.insert(inbound);

        // 更新库存
        MaterialStock existing = stockMapper.findByMaterialAndLocation(materialId, locationId);
        if (existing != null) {
            stockMapper.increaseStock(materialId, locationId, quantity);
            existing.setQuantity(existing.getQuantity().add(quantity));
        } else {
            MaterialStock stock = new MaterialStock();
            stock.setMaterialId(materialId);
            stock.setLocationId(locationId);
            stock.setQuantity(quantity);
            stock.setLastInboundTime(LocalDateTime.now());
            stockMapper.insert(stock);
            existing = stock;
        }

        // 更新采购单状态
        if (purchaseOrderId != null) {
            purchaseOrderMapper.updateStatus(purchaseOrderId, OrderStatus.COMPLETED.name());
        }

        // 检查库存预警
        dashboardService.checkAndAlertMaterial(materialId, material.getName(),
                existing.getQuantity(), material.getAlertThreshold(), material.getUnit());

        return inbound;
    }
}
