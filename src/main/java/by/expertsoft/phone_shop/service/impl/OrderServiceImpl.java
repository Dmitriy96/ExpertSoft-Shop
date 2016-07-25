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
    private OrderDetails orderDetails;

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
    public boolean isOrderPresent() {
        Order order = this.orderDetails.getOrder();
        if (order == null || order.getOrderItems() == null || order.getOrderItems().size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public Order getCurrentOrder() {
        return orderDetails.getOrder();
    }

    @Override
    public void savePersonalData(Order order) {
        Order sessionOrder = this.orderDetails.getOrder();
        order.setOrderItems(sessionOrder.getOrderItems());
        BigDecimal deliveryCost = new BigDecimal(5);
        BigDecimal totalPrice = sessionOrder.getTotalPrice();
        order.setTotalPrice(totalPrice.add(deliveryCost));
        order.setStatus(OrderStatus.NEW);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        order.setDate(formatter.format(localDateTime));
        this.orderDetails.setOrder(order);
    }

    @Override
    public void clearCurrentOrder() {
        this.orderDetails.setOrder(null);
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }
}
