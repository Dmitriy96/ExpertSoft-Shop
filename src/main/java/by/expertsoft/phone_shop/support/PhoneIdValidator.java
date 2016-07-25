package by.expertsoft.phone_shop.support;


import by.expertsoft.phone_shop.entity.PhoneIdWrapper;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PhoneIdValidator implements Validator {

    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(PhoneIdWrapper.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PhoneIdWrapper phoneQuantityWrapper = (PhoneIdWrapper) o;
        if (phoneQuantityWrapper.getId() == null) {
            errors.rejectValue("id", "requireNumbers", null, messageSource.getMessage("requireNumbers", null, null));
        } else
        if (phoneQuantityWrapper.getId() < 1) {
            errors.rejectValue("id", "negativeValue", new Object[]{"'id'"}, messageSource.getMessage("negativeValue", null, null));
        }
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
