package by.expertsoft.phone_shop.service.impl;


import by.expertsoft.phone_shop.entity.Order;
import by.expertsoft.phone_shop.entity.OrderDetails;
import by.expertsoft.phone_shop.entity.OrderItem;
import by.expertsoft.phone_shop.service.CartService;
import by.expertsoft.phone_shop.service.PhoneService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CartServiceImpl implements CartService {

    private Logger logger = LogManager.getLogger(CartServiceImpl.class.getName());
    private PhoneService phoneService;

    @Override
    public void addPhoneToCart(OrderDetails orderDetails, Long phoneId, Integer count) {
        Order order = orderDetails.getOrder();
        if (order != null) {
            OrderItem orderItem = null;
            for (OrderItem existingOrderItem : order.getOrderItems()) {
                if (existingOrderItem.getPhone().getId().equals(phoneId)) {
                    orderItem = existingOrderItem;
                }
            }
            if (orderItem != null) {
                BigDecimal addingPhonesPrice = new BigDecimal(orderItem.getPhone().getPrice() * count);
                BigDecimal totalPrice = order.getTotalPrice().add(addingPhonesPrice);
                order.setTotalPrice(totalPrice);
                Integer quantity = orderItem.getQuantity();
                quantity += count;
                orderItem.setQuantity(quantity);
            } else {
                OrderItem newOrderItem = new OrderItem();
                newOrderItem.setQuantity(count);
                newOrderItem.setPhone(phoneService.get(phoneId));
                order.getOrderItems().add(newOrderItem);
                BigDecimal addingPhonesPrice = new BigDecimal(newOrderItem.getPhone().getPrice() * count);
                BigDecimal totalPrice = order.getTotalPrice().add(addingPhonesPrice);
                order.setTotalPrice(totalPrice);
            }
        } else {
            order = new Order();
            List<OrderItem> orderItems = new ArrayList<>();
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(count);
            orderItem.setPhone(phoneService.get(phoneId));
            orderItems.add(orderItem);
            order.setOrderItems(orderItems);
            BigDecimal addingPhonesPrice = new BigDecimal(orderItem.getPhone().getPrice() * count);
            order.setTotalPrice(addingPhonesPrice);
        }
        orderDetails.setOrder(order);
    }

    @Override
    public void removePhoneFromCart(OrderDetails orderDetails, Long phoneId) {
        Order order = orderDetails.getOrder();
        Iterator<OrderItem> iterator = order.getOrderItems().iterator();
        while (iterator.hasNext()) {
            OrderItem orderItem = iterator.next();
            if (orderItem.getPhone().getId().equals(phoneId)) {
                iterator.remove();
                BigDecimal totalPrice = order.getTotalPrice();
                totalPrice = totalPrice.subtract(new BigDecimal(orderItem.getQuantity() * orderItem.getPhone().getPrice()));
                order.setTotalPrice(totalPrice);
            }
        }
    }

    @Override
    public void updatePhones(OrderDetails orderDetails, Long phoneId, Integer quantity) {
        Order order = orderDetails.getOrder();
        for (OrderItem orderItem : order.getOrderItems()) {
            if (orderItem.getPhone().getId().equals(phoneId)) {
                BigDecimal totalPrice = order.getTotalPrice();
                totalPrice = totalPrice.add(new BigDecimal(orderItem.getPhone().getPrice() * (quantity - orderItem.getQuantity())));
                order.setTotalPrice(totalPrice);
                orderItem.setQuantity(quantity);
            }
        }
    }

    public void setPhoneService(PhoneService phoneService) {
        this.phoneService = phoneService;
    }
}
