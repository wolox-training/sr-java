package wolox.training.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import wolox.training.repository.UserRepository;

import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements UserDetailsService {

    private final
    UserRepository userRepository;

    @Autowired
    public CustomAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(userFound -> new User(userFound.getUsername(), userFound.getPassword(), Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username"));
    }
}
