package wolox.training.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wolox.training.repository.UserRepository;

import java.util.Collections;

public class CustomAuthenticationProvider implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(userFound -> new User(userFound.getUsername(), userFound.getPassword(), Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username"));
    }
}
