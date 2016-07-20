package by.expertsoft.phone_shop.service.impl;


import by.expertsoft.phone_shop.dao.OrderDao;
import by.expertsoft.phone_shop.dao.PhoneDao;
import by.expertsoft.phone_shop.entity.*;
import by.expertsoft.phone_shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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
    public Long save(Order order) {
        return orderDao.save(order);
    }

    @Override
    public void changeStatusToDelivered(Long id) {
        Order order = orderDao.get(id);
        order.setStatus(OrderStatus.DELIVERED);
        orderDao.update(order);
    }

    @Override
    public boolean isOrderPresent(OrderDetails orderDetails) {
        Order order = orderDetails.getOrder();
        if (order == null || order.getOrderItems() == null || order.getOrderItems().size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public Order getCurrentOrder(OrderDetails orderDetails) {
        return orderDetails.getOrder();
    }

    @Override
    public void savePersonalData(OrderDetails orderDetails, Order order) {
        Order sessionOrder = orderDetails.getOrder();
        order.setOrderItems(sessionOrder.getOrderItems());
        BigDecimal deliveryCost = new BigDecimal(5);
        BigDecimal totalPrice = sessionOrder.getTotalPrice();
        order.setTotalPrice(totalPrice.add(deliveryCost));
        order.setStatus(OrderStatus.NEW);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        order.setDate(formatter.format(localDateTime));
        orderDetails.setOrder(order);
    }

    @Override
    public void clearCurrentOrder(OrderDetails orderDetails) {
        orderDetails.setOrder(null);
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }
}
