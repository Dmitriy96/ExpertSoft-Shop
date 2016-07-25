package by.expertsoft.phone_shop.support;

import by.expertsoft.phone_shop.entity.PhoneQuantityListWrapper;
import by.expertsoft.phone_shop.entity.PhoneQuantityWrapper;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;


public class PhoneQuantityListValidator implements Validator {

    private PhoneQuantityValidator phoneQuantityValidator;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(PhoneQuantityListWrapper.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PhoneQuantityListWrapper phoneQuantityListWrapper = (PhoneQuantityListWrapper) o;
        List<PhoneQuantityWrapper> phoneQuantityList = phoneQuantityListWrapper.getPhonesQuantityList();
        int idx = 0;
        for (PhoneQuantityWrapper phoneQuantity : phoneQuantityList) {
            errors.pushNestedPath("phonesQuantityList[" + idx + "]");
            ValidationUtils.invokeValidator(this.phoneQuantityValidator, phoneQuantity, errors);
            errors.popNestedPath();
            idx++;
        }
    }

    public void setPhoneQuantityValidator(PhoneQuantityValidator phoneQuantityValidator) {
        this.phoneQuantityValidator = phoneQuantityValidator;
    }
}
