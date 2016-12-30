package tw.bill.java101.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import tw.bill.java101.dto.UserForm;
import tw.bill.java101.service.UserService;

/**
 * Created by bill33 on 2016/2/14.
 */

@Component
public class UserCreateFormValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserCreateFormValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserForm form = (UserForm) target;
        validatePasswords(errors, form);
        validateEmail(errors, form);
    }

    private void validatePasswords(Errors errors, UserForm form) {
        if (!form.getPassword().equals(form.getPasswordRepeated())) {
            errors.reject("password.no_match", "Passwords do not match");
        }
    }

    private void validateEmail(Errors errors, UserForm form) {
        if (userService.findByUserName(form.getEmail()) != null) {
//            errors.rejectValue("email", "email.exists", "User with this email already exists");
            errors.reject("email.exists", "User with this email already exists");
        }
    }
}
