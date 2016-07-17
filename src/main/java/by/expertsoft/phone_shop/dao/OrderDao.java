package by.expertsoft.phone_shop.dao;


import by.expertsoft.phone_shop.entity.Order;
import by.expertsoft.phone_shop.entity.Phone;

import java.util.List;

public interface OrderDao {
    List<Order> findAll();
    Order get(long id);
    void save(Order order);
    void update(Order order);
}
