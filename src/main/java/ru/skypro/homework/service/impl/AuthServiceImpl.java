package ru.skypro.homework.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.config.MyUserDetailsManager;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.mapper.UserMapperImpl;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final MyUserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapperImpl userMapper;

    public AuthServiceImpl(MyUserDetailsManager manager, PasswordEncoder encoder, UserRepository userRepository, UserMapperImpl userMapper) {
        this.manager = manager;
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public boolean login(String username, String password) {
        if (!manager.userExists(username)) {
            logger.info("Пользователя не существует");
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(username);
        logger.info("Информация о загруженном пользователе: {} | {}", userDetails.getUsername(), userDetails.getPassword());
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(Register register) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }
//        manager.createUser(
//                User.builder()
//                        .passwordEncoder(this.encoder::encode)
//                        .password(register.getPassword())
//                        .username(register.getUsername())
//                        .roles(register.getRole().name())
//                        .build());
        register.setPassword(encoder.encode(register.getPassword()));
        userRepository.save(userMapper.toUserEntity(register));
        return true;
    }

}
