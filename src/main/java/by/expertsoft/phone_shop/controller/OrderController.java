package by.expertsoft.phone_shop.controller;


import by.expertsoft.phone_shop.entity.Order;
import by.expertsoft.phone_shop.entity.OrderItem;
import by.expertsoft.phone_shop.entity.OrderStatus;
import by.expertsoft.phone_shop.entity.Phone;
import by.expertsoft.phone_shop.service.OrderService;
import by.expertsoft.phone_shop.service.PhoneService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractUrlViewController;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderController extends ParameterizableViewController {

    private Logger logger = LogManager.getLogger(OrderController.class.getName());
    private OrderService orderService;
    private PhoneService phoneService;
    private Map<String, String> urlRegexToGetMethodNameMap = new HashMap<>();
    private Map<String, String> urlRegexToPostMethodNameMap = new HashMap<>();
    private Map<String, String> urlRegexToPutMethodNameMap = new HashMap<>();

    private ModelAndView getOrderPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("order");
        return modelAndView;
    }

    private ModelAndView addPersonalData(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String phoneNumber = request.getParameter("phoneNumber");
        String deliveryCost = request.getParameter("deliveryCost");
        logger.debug("=================addPersonalData: {}, {}, {}, {}", name, surname, phoneNumber, deliveryCost);
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
        }
        HttpSession session = request.getSession();
        Map<Long, Integer> phonesIdToQuantityMap = (Map<Long, Integer>) session.getAttribute("phonesInCart");
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(5);
        if (phonesIdToQuantityMap != null) {
            for (Long phoneId : phonesIdToQuantityMap.keySet()) {
                Phone phone = phoneService.get(phoneId);
                OrderItem orderItem = new OrderItem();
                orderItem.setPhone(phone);
                orderItem.setQuantity(phonesIdToQuantityMap.get(phoneId));
                orderItems.add(orderItem);
                totalPrice = totalPrice.add(new BigDecimal(orderItem.getQuantity() * phone.getPrice()));
            }
        }
        Order order = new Order();
        order.setName(name);
        order.setSurname(surname);
        order.setPhoneNo(phoneNumber);
        order.setStatus(OrderStatus.NEW);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        order.setDate(formatter.format(localDateTime));
        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);
        session.setAttribute("order", order);
        modelAndView.setViewName("orderOverview");
        modelAndView.addObject("order", order);
        return modelAndView;
    }

    private ModelAndView getOrdersPage(HttpServletRequest request, HttpServletResponse response) {
        List<Order> orders = orderService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("orders");
        modelAndView.addObject("orders", orders);
        return modelAndView;
    }

    private void changeStatusToDelivered(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("========================changeStatusToDelivered");
        String servletPath = request.getServletPath();
        Long phoneId = Long.parseLong(servletPath.split("/")[2]);
        orderService.changeStatusToDelivered(phoneId);
    }


    private ModelAndView saveOrder(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute("order");
        orderService.save(order);
        session.removeAttribute("phonesInCart");
        session.removeAttribute("order");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/orders");
        return modelAndView;
    }


    private ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, Map<String, String> urlRegexToMethodNameMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String urlServletPath = request.getServletPath();
        for (String urlRegex : urlRegexToMethodNameMap.keySet()) {
            Pattern pattern = Pattern.compile(urlRegex);
            Matcher matcher = pattern.matcher(urlServletPath);
            if (matcher.matches()) {
                Method method = this.getClass().getDeclaredMethod(urlRegexToMethodNameMap.get(urlRegex),
                        HttpServletRequest.class, HttpServletResponse.class);
                if (method.getReturnType().equals(ModelAndView.class))
                    return (ModelAndView) method.invoke(this, request, response);
                else {
                    method.invoke(this, request, response);
                    return null;
                }
            }
        }
        throw new NoSuchMethodException("Method not found.");
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String requestMethod = request.getMethod();
        if (HttpMethod.GET.name().equals(requestMethod)) {
            return processRequest(request, response, urlRegexToGetMethodNameMap);
        }
        if (HttpMethod.POST.name().equals(requestMethod)) {
            return processRequest(request, response, urlRegexToPostMethodNameMap);
        }
        if (HttpMethod.PUT.name().equals(requestMethod)) {
            return processRequest(request, response, urlRegexToPutMethodNameMap);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        return modelAndView;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void setPhoneService(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    public void setUrlRegexToGetMethodNameMap(Map<String, String> urlRegexToGetMethodNameMap) {
        this.urlRegexToGetMethodNameMap = urlRegexToGetMethodNameMap;
    }

    public void setUrlRegexToPostMethodNameMap(Map<String, String> urlRegexToPostMethodNameMap) {
        this.urlRegexToPostMethodNameMap = urlRegexToPostMethodNameMap;
    }

    public void setUrlRegexToPutMethodNameMap(Map<String, String> urlRegexToPutMethodNameMap) {
        this.urlRegexToPutMethodNameMap = urlRegexToPutMethodNameMap;
    }
}
