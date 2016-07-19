package by.expertsoft.phone_shop.support;

import by.expertsoft.phone_shop.controller.CartController;
import by.expertsoft.phone_shop.controller.MainController;
import by.expertsoft.phone_shop.controller.OrderController;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;


public class Interceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        /*System.out.println("Interceptor postHandle: " + modelAndView);
        System.out.println("Interceptor postHandle: " + httpServletResponse.getStatus() + "  " + httpServletResponse.isCommitted());*/

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //System.out.println("Interceptor afterCompletion: " + httpServletResponse.getStatus() + "  " + httpServletResponse.isCommitted());
        /*System.out.println("Interceptor afterCompletion: " + httpServletResponse.getStatus());
        if (httpServletResponse.getStatus() == HttpServletResponse.SC_NOT_FOUND) {
            HandlerMethod handlerMethod = (HandlerMethod) o;
            System.out.println("Interceptor afterCompletion: method name - " + handlerMethod.getMethod().getName());
            System.out.println("Interceptor afterCompletion: method has annotation - " + handlerMethod.hasMethodAnnotation(AjaxMethod.class));
            System.out.println("Interceptor afterCompletion: method return type Void - " + handlerMethod.getReturnType().getParameterType().equals(Void.class));
            MethodParameter methodParameter = handlerMethod.getReturnType();
            System.out.println("Interceptor afterCompletion: methodParameter - " + methodParameter);
            System.out.println("Interceptor afterCompletion: methodParameter - " + methodParameter.getParameterType());
            System.out.println("Interceptor afterCompletion: methodParameter - " + methodParameter.getParameterName());
            System.out.println("Interceptor afterCompletion: methodParameter - " + methodParameter.getClass());
            System.out.println("Interceptor afterCompletion: methodParameter - " + methodParameter.getContainingClass());
            System.out.println("Interceptor afterCompletion: methodParameter - " + methodParameter.getDeclaringClass());
            if (handlerMethod.hasMethodAnnotation(AjaxMethod.class) && handlerMethod.getReturnType().getParameterType().equals(void.class)) {
                System.out.println("setting status to 200");
                httpServletResponse.setStatus(200*//*HttpServletResponse.SC_OK*//*);
                httpServletResponse.setIntHeader();
                System.out.println("status: " + httpServletResponse.getStatus());
            }
        }*/
    }
}
