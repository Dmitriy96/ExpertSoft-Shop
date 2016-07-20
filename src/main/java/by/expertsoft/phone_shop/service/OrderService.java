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
    boolean isOrderPresent(OrderDetails orderDetails);
    Order getCurrentOrder(OrderDetails orderDetails);
    void savePersonalData(OrderDetails orderDetails, Order order);
    void clearCurrentOrder(OrderDetails orderDetails);
}
