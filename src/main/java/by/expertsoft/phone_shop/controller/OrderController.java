package by.expertsoft.phone_shop.controller;


import by.expertsoft.phone_shop.entity.DeliveryCostWrapper;
import by.expertsoft.phone_shop.entity.Order;
import by.expertsoft.phone_shop.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

public class OrderController extends ParameterizableViewController {

    private Logger logger = LogManager.getLogger(OrderController.class.getName());
    private OrderService orderService;

    public String getPersonalDataPage() {
        logger.debug("getPersonalDataPage");
        return "personalData";
    }

    public String getOrderPage(Model model) {
        logger.debug("getOrderPage");
        Order order = orderService.getCurrentOrder();
        model.addAttribute("order", order);
        return "order";
    }

    public String addPersonalData(Model model, @Valid Order order, BindingResult orderBindingResult,
                                  @Valid DeliveryCostWrapper deliveryCostWrapper, BindingResult deliveryCostBindingResult) {
        logger.debug("addPersonalData: {}, {}", order, deliveryCostWrapper);
        if (orderBindingResult.hasErrors()) {
            for (FieldError fieldError : orderBindingResult.getFieldErrors()) {
                model.addAttribute(fieldError.getField() + "Error", fieldError.getDefaultMessage());
            }
        }
        if (deliveryCostBindingResult.hasErrors()) {
            FieldError fieldError = deliveryCostBindingResult.getFieldError();
            model.addAttribute(fieldError.getField() + "Error", fieldError.getDefaultMessage());
            return "personalData";
        }
        if (orderBindingResult.hasErrors())
            return "personalData";
        orderService.savePersonalData(order);
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

    public String getOrderOverviewPage(Model model, HttpServletRequest request) {
        logger.debug("getOrderOverviewPage");
        String servletPath = request.getServletPath();
        Long orderId = Long.parseLong(servletPath.split("/")[2]);
        Order order = orderService.get(orderId);
        model.addAttribute("order", order);
        return "summary";
    }

    public String saveOrder() {
        logger.debug("saveOrder");
        Order order = orderService.getCurrentOrder();
        Long orderId = orderService.save(order);
        orderService.clearCurrentOrder();
        return "redirect:/order/" + orderId;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
