package by.expertsoft.phone_shop.service;


import by.expertsoft.phone_shop.entity.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    List<Order> findAll();
    Order get(Long id);
    void save(Order order);
    void changeStatusToDelivered(Long id);
}
