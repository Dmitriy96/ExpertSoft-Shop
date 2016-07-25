package by.expertsoft.phone_shop.validation;

import by.expertsoft.phone_shop.entity.PhoneQuantityWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class PhoneQuantityValidator implements Validator {

    private MessageSource messageSource;
    private Logger logger = LogManager.getLogger(PhoneQuantityValidator.class.getName());

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(PhoneQuantityWrapper.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PhoneQuantityWrapper phoneQuantityWrapper = (PhoneQuantityWrapper) o;
        logger.debug("validate: {}", phoneQuantityWrapper);
        if (phoneQuantityWrapper.getId() == null) {
            errors.rejectValue("id", "requireNumbers", null, messageSource.getMessage("required", null, null));
        } else
        if (phoneQuantityWrapper.getId() < 1) {
            errors.rejectValue("id", "negativeValue", new Object[]{"'id'"}, messageSource.getMessage("negativeValue", null, null));
        }
        if (phoneQuantityWrapper.getQuantity() == null) {
            errors.rejectValue("quantity", "requireNumbers", null, messageSource.getMessage("required", null, null));
        } else
        if (phoneQuantityWrapper.getQuantity() < 1) {
            errors.rejectValue("quantity", "negativeValue", new Object[]{"'quantity'"}, messageSource.getMessage("negativeValue", null, null));
        }
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
