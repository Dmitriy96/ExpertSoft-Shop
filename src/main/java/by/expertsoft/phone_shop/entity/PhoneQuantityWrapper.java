package by.expertsoft.phone_shop.entity;


public class PhoneQuantityWrapper {
    private Long id;
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(o instanceof PhoneQuantityWrapper)) return false;

        PhoneQuantityWrapper that = (PhoneQuantityWrapper) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return quantity != null ? quantity.equals(that.quantity) : that.quantity == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PhoneQuantityWrapper{" +
                "id=" + id +
                ", quantity=" + quantity +
                '}';
    }
}
