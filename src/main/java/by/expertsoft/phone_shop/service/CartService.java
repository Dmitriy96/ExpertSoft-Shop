package by.expertsoft.phone_shop.service;


import by.expertsoft.phone_shop.entity.OrderDetails;

public interface CartService {
    void addPhoneToCart(OrderDetails orderDetails, Long phoneId, Integer quantity);
    void removePhoneFromCart(OrderDetails orderDetails, Long phoneId);
    void updatePhones(OrderDetails orderDetails, Long phoneId, Integer quantity);
}
