package by.expertsoft.phone_shop.service.impl;


import by.expertsoft.phone_shop.dao.OrderDao;
import by.expertsoft.phone_shop.dao.PhoneDao;
import by.expertsoft.phone_shop.entity.Order;
import by.expertsoft.phone_shop.entity.OrderItem;
import by.expertsoft.phone_shop.entity.OrderStatus;
import by.expertsoft.phone_shop.entity.Phone;
import by.expertsoft.phone_shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {

    private OrderDao orderDao;

    @Override
    public List<Order> findAll() {
        return orderDao.findAll();
    }

    @Override
    public Order get(Long id) {
        return orderDao.get(id);
    }

    @Override
    public void save(Order order) {
        orderDao.save(order);
    }

    @Override
    public void changeStatusToDelivered(Long id) {
        Order order = orderDao.get(id);
        order.setStatus(OrderStatus.DELIVERED);
        orderDao.update(order);
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }
}
