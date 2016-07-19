package by.expertsoft.phone_shop.controller;


import by.expertsoft.phone_shop.entity.Order;
import by.expertsoft.phone_shop.entity.OrderStatus;
import by.expertsoft.phone_shop.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderController extends ParameterizableViewController {

    private Logger logger = LogManager.getLogger(OrderController.class.getName());
    private OrderService orderService;

    public String getPersonalDataPage() {
        logger.debug("getPersonalDataPage");
        return "personalData";
    }

    public String getOrderPage(Model model, HttpSession session) {
        logger.debug("getOrderPage");
        Order order = (Order) session.getAttribute("order");
        model.addAttribute("order", order);
        return "order";
    }

    public String addPersonalData(HttpSession session, Order order) {
        logger.debug("addPersonalData: {}", order);
        /*String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String phoneNumber = request.getParameter("phoneNumber");
        String deliveryCost = request.getParameter("totalPrice");               // TODO delivery cost is checkbox value!
        if (name == null || surname == null || phoneNumber == null || deliveryCost == null) {
            modelAndView.setViewName("order");
            modelAndView.addObject("error", "Please, fill all fields.");
            return modelAndView;
        }
        if (name.trim().length() < 2 || surname.trim().length() < 2 || phoneNumber.trim().length() < 10) {
            modelAndView.setViewName("order");
            modelAndView.addObject("error", "Please, input correct data.");
            return modelAndView;
        }
        if (!"on".equals(deliveryCost.trim())) {
            modelAndView.setViewName("order");
            modelAndView.addObject("error", "Fixed cost delivery should be accepted.");
            return modelAndView;
        }*/
        Order sessionOrder = (Order) session.getAttribute("order");
        order.setOrderItems(sessionOrder.getOrderItems());
        BigDecimal deliveryCost = new BigDecimal(5);
        BigDecimal totalPrice = sessionOrder.getTotalPrice();
        order.setTotalPrice(totalPrice.add(deliveryCost));
        order.setStatus(OrderStatus.NEW);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        order.setDate(formatter.format(localDateTime));
        session.setAttribute("order", order);
        return "redirect:/order";
    }

    public String getOrdersPage(Model model) {
        logger.debug("getOrdersPage");
        model.addAttribute("orders", orderService.findAll());
        return "orders";
    }

    public ResponseEntity<String> changeStatusToDelivered(HttpServletRequest request) {
        logger.debug("changeStatusToDelivered");
        String servletPath = request.getServletPath();
        Long phoneId = Long.parseLong(servletPath.split("/")[2]);
        orderService.changeStatusToDelivered(phoneId);
        return new ResponseEntity<String>(HttpStatus.OK);
    }


    public String saveOrder(HttpSession session) {
        logger.debug("saveOrder");
        Order order = (Order) session.getAttribute("order");
        orderService.save(order);
        session.removeAttribute("order");
        return "redirect:/orders";
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
