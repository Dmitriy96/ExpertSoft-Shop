package by.expertsoft.phone_shop.entity;


public class OrderDetails {
    private Order order;
    private Double fixedCostDelivery;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Double getFixedCostDelivery() {
        return fixedCostDelivery;
    }

    public void setFixedCostDelivery(Double fixedCostDelivery) {
        this.fixedCostDelivery = fixedCostDelivery;
    }
}
