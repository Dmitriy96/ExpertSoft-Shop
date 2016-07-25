package by.expertsoft.phone_shop.entity;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class DeliveryCostWrapper {
    @NotNull(message = "Fixed delivery cost should be accepted!")
    @Pattern(regexp = "on", message = "Fixed delivery cost should be accepted!")
    private String deliveryCost;

    public String getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(String deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryCostWrapper)) return false;

        DeliveryCostWrapper that = (DeliveryCostWrapper) o;

        return deliveryCost != null ? deliveryCost.equals(that.deliveryCost) : that.deliveryCost == null;

    }

    @Override
    public int hashCode() {
        return deliveryCost != null ? deliveryCost.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DeliveryCostWrapper{" +
                "deliveryCost='" + deliveryCost + '\'' +
                '}';
    }
}
