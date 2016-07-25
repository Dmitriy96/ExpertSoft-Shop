package by.expertsoft.phone_shop.service;


public interface CartService {
    void addPhoneToCart(Long phoneId, Integer quantity);
    void removePhoneFromCart(Long phoneId);
    void updatePhones(Long phoneId, Integer quantity);
}
