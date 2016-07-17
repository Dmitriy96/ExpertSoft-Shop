package by.expertsoft.phone_shop.controller;


import by.expertsoft.phone_shop.service.PhoneService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
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
    private Map<String, String> urlRegexToGetMethodNameMap = new HashMap<>();

    private ModelAndView getMainPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("phones", phoneService.findAll());
        modelAndView.setViewName("main");
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
                return (ModelAndView) method.invoke(this, request, response);
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
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        return modelAndView;
    }


    /*logger.debug("ContextPath: " + request.getContextPath());         // TODO remove comment
            logger.debug("PathInfo: " + request.getPathInfo());
            logger.debug("PathTranslated: " + request.getPathTranslated());
            logger.debug("QueryString: " + request.getQueryString());
            logger.debug("RequestURI: " + request.getRequestURI());
            logger.debug("RequestURL: " + request.getRequestURL().toString());
            logger.debug("ServletPath: " + request.getServletPath());
            Enumeration<String> enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements()) {
                logger.debug("enumeration: " + enumeration.nextElement());
            }*/

    public void setPhoneService(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    public void setUrlRegexToGetMethodNameMap(Map<String, String> urlRegexToGetMethodNameMap) {
        this.urlRegexToGetMethodNameMap = urlRegexToGetMethodNameMap;
    }
}
