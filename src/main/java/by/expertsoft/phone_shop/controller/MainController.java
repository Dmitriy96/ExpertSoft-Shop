package by.expertsoft.phone_shop.controller;


import by.expertsoft.phone_shop.service.PhoneService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController extends ParameterizableViewController {

    private Logger logger = LogManager.getLogger(MainController.class.getName());
    private PhoneService phoneService;

    public String getMainPage(Model model) {
        logger.debug("getMainPage");
        model.addAttribute("phones", phoneService.findAll());
        return "main";
    }

    public void setPhoneService(PhoneService phoneService) {
        this.phoneService = phoneService;
    }
}
