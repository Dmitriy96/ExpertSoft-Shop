package by.expertsoft.phone_shop.controller;


import by.expertsoft.phone_shop.entity.Order;
import by.expertsoft.phone_shop.entity.OrderItem;
import by.expertsoft.phone_shop.service.PhoneService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CartController extends ParameterizableViewController {

    private Logger logger = LogManager.getLogger(CartController.class.getName());
    private PhoneService phoneService;

    public String getCartPage(Model model, HttpSession session) {
        logger.debug("getCartPage");
        Order order = (Order) session.getAttribute("order");
        model.addAttribute("order", order);
        return "cart";
    }

    public String submitCart(HttpSession session) {
        logger.debug("submitCart");
        Order order = (Order) session.getAttribute("order");
        if (order == null || order.getOrderItems() == null || order.getOrderItems().size() == 0) {
            return "redirect:/cart";
        }
        return "redirect:/personal_data";
    }

    public ResponseEntity<String> addPhoneToCart(HttpServletRequest request, HttpSession session) {
        String urlServletPath = request.getServletPath();
        String countParameter = request.getParameter("count");
        logger.debug("addPhoneToCart: urlServletPath - {}, count - {}", urlServletPath, countParameter);
        Long phoneId = Long.parseLong(urlServletPath.substring(urlServletPath.lastIndexOf('/') + 1));
        if (countParameter == null)
            throw new IllegalArgumentException("`Count` parameter is absent.");
        Integer count = Integer.parseInt(countParameter);                               // TODO parse in try catch
        Order order = (Order) session.getAttribute("order");
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
        session.setAttribute("order", order);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    public ResponseEntity<String> removePhoneFromCart(HttpServletRequest request, HttpSession session) throws IOException {
        logger.debug("removePhoneFromCart");
        String urlServletPath = request.getServletPath();
        Long phoneId = Long.parseLong(urlServletPath.substring(urlServletPath.lastIndexOf('/') + 1));
        Order order = (Order) session.getAttribute("order");
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
        return new ResponseEntity<String>(order.getTotalPrice().setScale(2, RoundingMode.HALF_UP).toPlainString(), HttpStatus.OK);
    }

    public void setPhoneService(PhoneService phoneService) {
        this.phoneService = phoneService;
    }
}
