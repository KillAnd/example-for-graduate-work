package ru.skypro.homework.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UpdateUserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

@Service
public class MyUserDetailsManager implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public MyUserDetailsManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(phone);
        if (user == null) {
            throw new UserNotFoundException(phone);
        }
        return new MyUserDetails(user);
    }

    public void createUser(UserDetails userDetails) {
        // Преобразуем UserDetails в сущность User
        User user = new User();
        user.setEmail(userDetails.getUsername());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        user.setRole(Role.valueOf(userDetails.getAuthorities().iterator().next().getAuthority()));
        userRepository.save(user);
    }

    public boolean userExists(String username) {
        // Проверка, существует ли пользователь
        return userRepository.findByEmail(username).isPresent();
    }
}
