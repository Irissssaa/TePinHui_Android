package com.example.tepinhui.dto;

/**
 * 订单统计DTO
 */
public class OrderStatsDTO {
    private Long unpaid;    // 待付款
    private Long unship;    // 待发货
    private Long shipped;   // 待收货
    private Long unreview;  // 待评价
    private Long refund;    // 退款售后

    // 构造器
    public OrderStatsDTO() {}

    public OrderStatsDTO(Long unpaid, Long unship, Long shipped, Long unreview, Long refund) {
        this.unpaid = unpaid;
        this.unship = unship;
        this.shipped = shipped;
        this.unreview = unreview;
        this.refund = refund;
    }

    // Getter和Setter
    public Long getUnpaid() { return unpaid; }
    public void setUnpaid(Long unpaid) { this.unpaid = unpaid; }

    public Long getUnship() { return unship; }
    public void setUnship(Long unship) { this.unship = unship; }

    public Long getShipped() { return shipped; }
    public void setShipped(Long shipped) { this.shipped = shipped; }

    public Long getUnreview() { return unreview; }
    public void setUnreview(Long unreview) { this.unreview = unreview; }

    public Long getRefund() { return refund; }
    public void setRefund(Long refund) { this.refund = refund; }
}