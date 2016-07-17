package by.expertsoft.phone_shop.entity;


public class OrderItem {
    private Phone phone;
    private Integer quantity;

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem)) return false;

        OrderItem orderItem = (OrderItem) o;

        if (!phone.equals(orderItem.phone)) return false;
        return quantity.equals(orderItem.quantity);

    }

    @Override
    public int hashCode() {
        int result = phone.hashCode();
        result = 31 * result + quantity.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "phone=" + phone +
                ", quantity=" + quantity +
                '}';
    }
}
