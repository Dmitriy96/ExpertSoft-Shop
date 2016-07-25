package by.expertsoft.phone_shop.controller;


import by.expertsoft.phone_shop.entity.Order;
import by.expertsoft.phone_shop.entity.PhoneIdWrapper;
import by.expertsoft.phone_shop.entity.PhoneQuantityListWrapper;
import by.expertsoft.phone_shop.entity.PhoneQuantityWrapper;
import by.expertsoft.phone_shop.service.CartService;
import by.expertsoft.phone_shop.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.validation.Valid;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartController extends ParameterizableViewController {

    private Logger logger = LogManager.getLogger(CartController.class.getName());
    private OrderService orderService;
    private CartService cartService;
    private Validator phoneQuantityValidator;
    private Validator phoneIdValidator;

    @InitBinder("phoneQuantityWrapper")
    private void phoneQuantityBinder(WebDataBinder binder) {
        logger.debug("phoneQuantityBinder: {}", binder.getTarget(), binder.getObjectName());
        binder.setValidator(phoneQuantityValidator);
    }

    @InitBinder("phoneIdWrapper")
    private void phoneIdBinder(WebDataBinder binder) {
        logger.debug("phoneIdBinder: {}", binder.getTarget(), binder.getObjectName());
        binder.setValidator(phoneIdValidator);
    }


    public String getCartPage(Model model) {
        logger.debug("getCartPage");
        Order order = orderService.getCurrentOrder();
        model.addAttribute("order", order);
        model.addAttribute("phoneQuantityList", new PhoneQuantityListWrapper());
        return "cart";
    }

    public String submitCart() {
        logger.debug("submitCart");
        if (!orderService.isOrderPresent()) {
            return "redirect:/cart";
        }
        return "redirect:/personal_data";
    }

    public ResponseEntity<String> addPhoneToCart(Model model, @Valid PhoneQuantityWrapper phoneQuantityWrapper, BindingResult bindingResult) {
        logger.debug("addPhoneToCart: {}", phoneQuantityWrapper);
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            if (bindingResult.getFieldError().isBindingFailure()) {
                errorMessage = "Phones quantity field should contain only numbers.";
            }
            model.addAttribute("phoneIdError-" + phoneQuantityWrapper.getId(), bindingResult.getFieldError().getRejectedValue());
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_ACCEPTABLE);
        }
        cartService.addPhoneToCart(phoneQuantityWrapper.getId(), phoneQuantityWrapper.getQuantity());
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    public ResponseEntity<String> removePhoneFromCart(@Valid PhoneIdWrapper phoneIdWrapper, BindingResult bindingResult) throws IOException {
        logger.debug("removePhoneFromCart: {}", phoneIdWrapper);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        cartService.removePhoneFromCart(phoneIdWrapper.getId());
        Order order = orderService.getCurrentOrder();
        return new ResponseEntity<String>(order.getTotalPrice().setScale(2, RoundingMode.HALF_UP).toPlainString(), HttpStatus.OK);
    }

    public String updateCart(Model model, @ModelAttribute("phoneQuantityList") @Valid PhoneQuantityListWrapper phoneQuantityList, BindingResult bindingResult) {
        logger.debug("updateCart: {}", phoneQuantityList);
        Set<Integer> incorrectFieldsIndexSet = new HashSet<>();
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                String fieldName = fieldError.getField();
                fieldName = fieldName.substring(fieldName.indexOf("[") + 1);
                fieldName = fieldName.substring(0, fieldName.indexOf("]"));
                incorrectFieldsIndexSet.add(Integer.parseInt(fieldName));
                model.addAttribute("phoneQuantity-" + fieldName,
                        fieldError.getRejectedValue() == null ? " " : fieldError.getRejectedValue());
            }
        }
        if (phoneQuantityList.getPhonesQuantityList() != null) {
            List<PhoneQuantityWrapper> phoneQuantityWrapperList = phoneQuantityList.getPhonesQuantityList();
            for (int i = 0; i < phoneQuantityWrapperList.size(); i++) {
                if (!incorrectFieldsIndexSet.contains(i)) {
                    PhoneQuantityWrapper phonesQuantity = phoneQuantityWrapperList.get(i);
                    cartService.updatePhones(phonesQuantity.getId(), phonesQuantity.getQuantity());
                }
            }
        }
        if (!bindingResult.hasErrors())
            model.addAttribute("phoneQuantityList", new PhoneQuantityListWrapper());
        model.addAttribute("order", orderService.getCurrentOrder());
        return "/cart";
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void setPhoneQuantityValidator(Validator phoneQuantityValidator) {
        this.phoneQuantityValidator = phoneQuantityValidator;
    }

    public void setPhoneIdValidator(Validator phoneIdValidator) {
        this.phoneIdValidator = phoneIdValidator;
    }
}
