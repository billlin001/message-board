package tw.bill.java101.converter;

import org.springframework.core.convert.converter.Converter;
import tw.bill.java101.domain.User;
import tw.bill.java101.dto.UserForm;


/**
 * Created by bill33 on 2016/3/27.
 */
public class UserToUserForm implements Converter<User, UserForm> {

    @Override
    public UserForm convert(User source) {
        UserForm result = new UserForm();
        result.setEmail(source.getEmail());
        return result;
    }
}
