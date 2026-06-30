package com.fwms.service;

import com.fwms.common.OrderNoGenerator;
import com.fwms.entity.*;
import com.fwms.enums.OrderStatus;
import com.fwms.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OutboundService {

    @Autowired
    private MaterialOutboundMapper outboundMapper;
    @Autowired
    private MaterialStockMapper stockMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private DashboardService dashboardService;

    public List<MaterialOutbound> findAll(String status) {
        return outboundMapper.findAll(status);
    }

    public MaterialOutbound createRequisition(String workshop, Long applicantId, Long materialId,
            BigDecimal quantity, String remark) {
        MaterialOutbound outbound = new MaterialOutbound();
        outbound.setOutboundNo(OrderNoGenerator.generate("MO"));
        outbound.setWorkshop(workshop);
        outbound.setApplicantId(applicantId);
        outbound.setMaterialId(materialId);
        outbound.setQuantity(quantity);
        outbound.setStatus(OrderStatus.PENDING.name());
        outbound.setRemark(remark);
        outboundMapper.insert(outbound);
        return outbound;
    }

    @Transactional
    public void approveAndIssue(Long id, Long approverId, boolean approved) {
        MaterialOutbound outbound = outboundMapper.findById(id);
        if (outbound == null) {
            throw new RuntimeException("领料单不存在");
        }
        if (!OrderStatus.PENDING.name().equals(outbound.getStatus())) {
            throw new RuntimeException("领料单状态不正确");
        }

        if (!approved) {
            outboundMapper.updateStatus(id, OrderStatus.REJECTED.name(), approverId);
            return;
        }

        // 检查库存
        List<MaterialStock> stocks = stockMapper.findAll();
        BigDecimal totalQty = BigDecimal.ZERO;
        for (MaterialStock s : stocks) {
            if (s.getMaterialId().equals(outbound.getMaterialId())) {
                totalQty = totalQty.add(s.getQuantity());
            }
        }
        if (totalQty.compareTo(outbound.getQuantity()) < 0) {
            throw new RuntimeException("库存不足，无法发料");
        }

        // 扣减库存
        int rows = stockMapper.decreaseStock(outbound.getMaterialId(), outbound.getQuantity());
        if (rows == 0) {
            throw new RuntimeException("库存扣减失败");
        }

        outboundMapper.updateStatus(id, OrderStatus.COMPLETED.name(), approverId);

        // 检查预警
        Material material = materialMapper.findById(outbound.getMaterialId());
        if (material != null) {
            BigDecimal remainQty = BigDecimal.ZERO;
            for (MaterialStock s : stockMapper.findAll()) {
                if (s.getMaterialId().equals(outbound.getMaterialId())) {
                    remainQty = remainQty.add(s.getQuantity());
                }
            }
            dashboardService.checkAndAlertMaterial(material.getId(), material.getName(),
                    remainQty, material.getAlertThreshold(), material.getUnit());
        }
    }
}
