package by.expertsoft.phone_shop.service;


import by.expertsoft.phone_shop.entity.Order;
import by.expertsoft.phone_shop.entity.OrderDetails;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface OrderService {
    List<Order> findAll();
    Order get(Long id);
    Long save(Order order);
    void changeStatusToDelivered(Long id);
    boolean isOrderPresent();
    Order getCurrentOrder();
    void savePersonalData(Order order);
    void clearCurrentOrder();
}
