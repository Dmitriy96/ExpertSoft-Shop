package by.expertsoft.phone_shop.support;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UrlHandlerMapping extends AbstractHandlerMapping {

    private Logger logger = LogManager.getLogger(UrlHandlerMapping.class.getName());
    private String defaultMapping;
    private Map<String, String> getMethodMapper = new HashMap<>();
    private Map<String, String> postMethodMapper = new HashMap<>();
    private Map<String, String> putMethodMapper = new HashMap<>();

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        HandlerMethod defaultHandlerMethod = this.getDefaultHandlerMethod();
        String servletPath = request.getServletPath();
        String requestMethod = request.getMethod();
        String handlerMapping = null;
        String handlerMethodName;
        String handlerBeanName;
        if (HttpMethod.GET.name().equals(requestMethod)) {
            handlerMapping = this.getHandlerMapping(servletPath, getMethodMapper);
        } else
        if (HttpMethod.POST.name().equals(requestMethod)) {
            handlerMapping = this.getHandlerMapping(servletPath, postMethodMapper);
        } else
        if (HttpMethod.PUT.name().equals(requestMethod)) {
            handlerMapping = this.getHandlerMapping(servletPath, putMethodMapper);
        }
        if (handlerMapping != null) {
            handlerBeanName = handlerMapping.trim().split("#")[0];
            handlerMethodName = handlerMapping.trim().split("#")[1];
        } else {
            return defaultHandlerMethod;
        }
        Method method = this.getMethod(handlerBeanName, handlerMethodName);
        if (method == null) {
            return defaultHandlerMethod;
        }
        HandlerMethod handlerMethod = new HandlerMethod(getApplicationContext().getBean(handlerBeanName), method);
        //InvocableHandlerMethod invocableHandlerMethod = new InvocableHandlerMethod(handlerMethod);
        //invocableHandlerMethod.setHandlerMethodArgumentResolvers();
        //invocableHandlerMethod.setDataBinderFactory();
        return handlerMethod;
    }

    private Method getMethod(String handlerBeanName, String handlerMethodName) {
        Class beanClass = getApplicationContext().getBean(handlerBeanName).getClass();
        Method method = null;
        Method[] methods = beanClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(handlerMethodName)) {
                method = methods[i];
            }
        }
        return method;
    }

    private HandlerMethod getDefaultHandlerMethod() {
        String defaultHandlerBeanName = defaultMapping.split("#")[0];
        String defaultHandlerMethodName = defaultMapping.split("#")[1];
        Method defaultMethod = this.getMethod(defaultHandlerBeanName, defaultHandlerMethodName);
        HandlerMethod defaultHandlerMethod = new HandlerMethod(defaultHandlerBeanName, defaultMethod);
        return defaultHandlerMethod;
    }

    private String getHandlerMapping(String servletPath, Map<String, String> requestMethodMapping) {
        for (String urlRegex : requestMethodMapping.keySet()) {
            Pattern pattern = Pattern.compile(urlRegex);
            Matcher matcher = pattern.matcher(servletPath);
            if (matcher.matches()) {
                return requestMethodMapping.get(urlRegex);
            }
        }
        return null;
    }

    public void setPostMethodMapper(Map<String, String> postMethodMapper) {
        this.postMethodMapper = postMethodMapper;
    }

    public void setGetMethodMapper(Map<String, String> getMethodMapper) {
        this.getMethodMapper = getMethodMapper;
    }

    public void setPutMethodMapper(Map<String, String> putMethodMapper) {
        this.putMethodMapper = putMethodMapper;
    }

    public void setDefaultMapping(String defaultMapping) {
        this.defaultMapping = defaultMapping;
    }
}
