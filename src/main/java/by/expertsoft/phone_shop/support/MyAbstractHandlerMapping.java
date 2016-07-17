package by.expertsoft.phone_shop.support;

import by.expertsoft.phone_shop.controller.MainController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dmitriy on 17.7.16.
 */
public class MyAbstractHandlerMapping extends AbstractHandlerMapping {

    @Override
    protected Object getHandlerInternal(HttpServletRequest httpServletRequest) throws Exception {
        HandlerMethod handlerMethod = new HandlerMethod(getApplicationContext().getBean("mainController"),
                MainController.class.getDeclaredMethod("getMainPage", HttpServletRequest.class, HttpServletResponse.class));
        InvocableHandlerMethod invocableHandlerMethod = new InvocableHandlerMethod(handlerMethod);
        invocableHandlerMethod.setHandlerMethodArgumentResolvers();
        invocableHandlerMethod.setDataBinderFactory();
        return invocableHandlerMethod;
    }
}
