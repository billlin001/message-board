package tw.bill.java101.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import tw.bill.java101.domain.User;
import tw.bill.java101.service.security.UserDetailsImpl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jt on 12/29/15.
 */
@Component
public class UserToUserDetails implements Converter<User, UserDetails> {
    @Override
    public UserDetails convert(User user) {
        UserDetailsImpl userDetails = new UserDetailsImpl();

        if (user != null) {
            userDetails.setUsername(user.getEmail());
            userDetails.setPassword(user.getPasswordHash());
            userDetails.setEnabled(user.getEnabled());

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getRole()));
            });
            userDetails.setAuthorities(authorities);
        }

        return userDetails;
    }
}
