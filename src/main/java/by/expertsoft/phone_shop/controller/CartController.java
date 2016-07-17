package by.expertsoft.phone_shop.controller;


import by.expertsoft.phone_shop.entity.OrderItem;
import by.expertsoft.phone_shop.entity.Phone;
import by.expertsoft.phone_shop.service.OrderService;
import by.expertsoft.phone_shop.service.PhoneService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CartController extends ParameterizableViewController {

    private Logger logger = LogManager.getLogger(CartController.class.getName());
    private PhoneService phoneService;
    private Map<String, String> urlRegexToGetMethodNameMap = new HashMap<>();
    private Map<String, String> urlRegexToPostMethodNameMap = new HashMap<>();
    private Map<String, String> urlRegexToPutMethodNameMap = new HashMap<>();

    private ModelAndView getCartPage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Map<Long, Integer> phonesIdToQuantityMap = (Map<Long, Integer>) session.getAttribute("phonesInCart");
        List<OrderItem> orderItems = new ArrayList<>();
        Double subtotal = 0.0;
        if (phonesIdToQuantityMap != null) {
            for (Long phoneId : phonesIdToQuantityMap.keySet()) {
                Phone phone = phoneService.get(phoneId);
                OrderItem orderItem = new OrderItem();
                orderItem.setPhone(phone);
                orderItem.setQuantity(phonesIdToQuantityMap.get(phoneId));
                orderItems.add(orderItem);
                subtotal += phone.getPrice() * phonesIdToQuantityMap.get(phoneId);
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cart");
        modelAndView.addObject("orderItems", orderItems);
        modelAndView.addObject("subtotal", subtotal);
        return modelAndView;
    }

    private ModelAndView submitCart(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Map<Long, Integer> phonesIdToQuantityMap = (Map<Long, Integer>) session.getAttribute("phonesInCart");
        ModelAndView modelAndView = new ModelAndView();
        if (phonesIdToQuantityMap == null || phonesIdToQuantityMap.keySet().size() == 0) {
            modelAndView.setViewName("redirect:/cart");
        } else
            modelAndView.setViewName("redirect:/order");
        return modelAndView;
    }

    private void addPhoneToCart(HttpServletRequest request, HttpServletResponse response) {
        String urlServletPath = request.getServletPath();
        Long phoneId = Long.parseLong(urlServletPath.substring(urlServletPath.lastIndexOf('/') + 1));
        String count = request.getParameter("count");
        if (count == null)
            throw new IllegalStateException("`Count` parameter is absent.");
        HttpSession session = request.getSession();
        Map<Long, Integer> phonesIdToQuantityMap = (Map<Long, Integer>) session.getAttribute("phonesInCart");
        if (phonesIdToQuantityMap != null) {
            Integer totalCount = phonesIdToQuantityMap.get(phoneId);
            if (totalCount != null) {
                totalCount += Integer.parseInt(count);
            } else {
                totalCount = Integer.parseInt(count);
            }
            phonesIdToQuantityMap.put(phoneId, totalCount);
            session.setAttribute("phonesInCart", phonesIdToQuantityMap);
        } else {
            phonesIdToQuantityMap = new HashMap<>();
            phonesIdToQuantityMap.put(phoneId, Integer.parseInt(count));
            session.setAttribute("phonesInCart", phonesIdToQuantityMap);
        }
    }

    private void removePhoneFromCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String urlServletPath = request.getServletPath();
        Long phoneId = Long.parseLong(urlServletPath.substring(urlServletPath.lastIndexOf('/') + 1));
        HttpSession session = request.getSession();
        Map<Long, Integer> phonesIdToQuantityMap = (Map<Long, Integer>) session.getAttribute("phonesInCart");
        if (phonesIdToQuantityMap != null) {
            phonesIdToQuantityMap.remove(phoneId);
            Double subtotal = 0.0;
            for (Long id : phonesIdToQuantityMap.keySet()) {
                Phone phone = phoneService.get(id);
                subtotal += phone.getPrice() * phonesIdToQuantityMap.get(id);
            }
            response.getWriter().write(subtotal.toString());
        }
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
