package by.expertsoft.phone_shop.validation;


import by.expertsoft.phone_shop.entity.PhoneIdWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PhoneIdValidator implements Validator {

    private Logger logger = LogManager.getLogger(PhoneQuantityValidator.class.getName());
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(PhoneIdWrapper.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PhoneIdWrapper phoneIdWrapper = (PhoneIdWrapper) o;
        logger.debug("validate: {}", phoneIdWrapper);
        if (phoneIdWrapper.getId() == null) {
            errors.rejectValue("id", "requireNumbers", null, messageSource.getMessage("requireNumbers", null, null));
        } else
        if (phoneIdWrapper.getId() < 1) {
            errors.rejectValue("id", "negativeValue", new Object[]{"'id'"}, messageSource.getMessage("negativeValue", null, null));
        }
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
