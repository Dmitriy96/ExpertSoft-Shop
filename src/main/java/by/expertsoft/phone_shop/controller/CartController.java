package by.expertsoft.phone_shop.controller;


import by.expertsoft.phone_shop.entity.Order;
import by.expertsoft.phone_shop.entity.OrderDetails;
import by.expertsoft.phone_shop.service.CartService;
import by.expertsoft.phone_shop.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Map;

public class CartController extends ParameterizableViewController {

    private Logger logger = LogManager.getLogger(CartController.class.getName());
    private OrderService orderService;
    private CartService cartService;
    private OrderDetails orderDetails;

    public String getCartPage(Model model) {
        logger.debug("getCartPage");
        Order order = orderService.getCurrentOrder(orderDetails);
        model.addAttribute("order", order);
        return "cart";
    }

    public String submitCart(HttpSession session) {
        logger.debug("submitCart");
        if (!orderService.isOrderPresent(orderDetails)) {
            return "redirect:/cart";
        }
        return "redirect:/personal_data";
    }

    public ResponseEntity<String> addPhoneToCart(HttpServletRequest request, String count) {
        String urlServletPath = request.getServletPath();
        logger.debug("addPhoneToCart: urlServletPath - {}, count - {}", urlServletPath, count);
        if (count == null)
            throw new IllegalArgumentException("`Count` parameter is absent.");
        Long phoneId;
        Integer addingPhonesQuantity;
        try {
            phoneId = Long.parseLong(urlServletPath.substring(urlServletPath.lastIndexOf('/') + 1));
            addingPhonesQuantity = Integer.parseInt(count);
        } catch (NumberFormatException e) {
            return new ResponseEntity<String>("Field should contains only numbers.", HttpStatus.NOT_ACCEPTABLE);
        }
        cartService.addPhoneToCart(orderDetails, phoneId, addingPhonesQuantity);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    public ResponseEntity<String> removePhoneFromCart(HttpServletRequest request) throws IOException {
        logger.debug("removePhoneFromCart");
        String urlServletPath = request.getServletPath();
        Long phoneId = Long.parseLong(urlServletPath.substring(urlServletPath.lastIndexOf('/') + 1));
        cartService.removePhoneFromCart(orderDetails, phoneId);
        Order order = orderService.getCurrentOrder(orderDetails);
        return new ResponseEntity<String>(order.getTotalPrice().setScale(2, RoundingMode.HALF_UP).toPlainString(), HttpStatus.OK);
    }

    public String updateCart(RedirectAttributes redirectAttributes, @RequestParam Map<String, String> allParameters) {
        logger.debug("updateCart");
        for (String phoneIdParameter : allParameters.keySet()) {
            Long phoneId = null;
            try {
                phoneId = Long.parseLong(phoneIdParameter.substring(phoneIdParameter.indexOf("-") + 1));
                Integer phonesQuantity = Integer.parseInt(allParameters.get(phoneIdParameter));
                cartService.updatePhones(orderDetails, phoneId, phonesQuantity);
            } catch (NumberFormatException e) {
                redirectAttributes.addFlashAttribute("phoneIdError-" + phoneId, "Field should contains only numbers.");
            }
        }
        return "redirect:/cart";
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }
}
