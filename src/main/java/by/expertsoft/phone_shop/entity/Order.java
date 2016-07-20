package by.expertsoft.phone_shop.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;


public class Order {
    private Long id;
    @NotBlank(message = "Field cannot be empty.")
    private String name;
    @NotBlank(message = "Field cannot be empty.")
    private String surname;
    @NotBlank(message = "Field cannot be empty.")
    @Size(min = 11, message = "Please, input full phone number.")
    @Pattern(regexp = "\\d+-\\d+-\\d+", message = "Phone number should be in format: X-X-XXXXXXX.")
    private String phoneNumber;
    private String date;
    private OrderStatus status;
    private List<OrderItem> orderItems;
    private BigDecimal totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;

        Order order = (Order) o;

        if (!name.equals(order.name)) return false;
        if (!surname.equals(order.surname)) return false;
        if (!phoneNumber.equals(order.phoneNumber)) return false;
        if (!date.equals(order.date)) return false;
        if (status != order.status) return false;
        return totalPrice.equals(order.totalPrice);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + surname.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        if (date != null)
            result = 31 * result + date.hashCode();
        if (status != null)
            result = 31 * result + status.hashCode();
        if (totalPrice != null)
            result = 31 * result + totalPrice.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", date='" + date + '\'' +
                ", status=" + status +
                ", orderItems=" + orderItems +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
