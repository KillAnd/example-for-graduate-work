package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.config.MyUserDetails;
import ru.skypro.homework.config.MyUserDetailsManager;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.mapper.UserMapperImpl;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private MyUserDetailsManager manager;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapperImpl userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testLogin_Success() {
        // Arrange
        String username = "user@example.com";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");
        UserDetails userDetails = new MyUserDetails(user);

        when(manager.userExists(username)).thenReturn(true);
        when(manager.loadUserByUsername(username)).thenReturn(userDetails);
        when(encoder.matches(password, userDetails.getPassword())).thenReturn(true);

        // Act
        boolean result = authService.login(username, password);

        // Assert
        assertTrue(result);
        verify(manager).userExists(username);
        verify(manager).loadUserByUsername(username);
        verify(encoder).matches(password, userDetails.getPassword());
    }

    @Test
    void testLogin_Failure_UserDoesNotExist() {
        // Arrange
        String username = "user@example.com";
        String password = "password";

        when(manager.userExists(username)).thenReturn(false);

        // Act
        boolean result = authService.login(username, password);

        // Assert
        assertFalse(result);
        verify(manager).userExists(username);
        verify(manager, never()).loadUserByUsername(any());
        verify(encoder, never()).matches(any(), any());
    }

    @Test
    void testLogin_Failure_PasswordMismatch() {
        // Arrange
        String username = "user@example.com";
        String password = "wrongPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");
        UserDetails userDetails = new MyUserDetails(user);

        when(manager.userExists(username)).thenReturn(true);
        when(manager.loadUserByUsername(username)).thenReturn(userDetails);
        when(encoder.matches(password, userDetails.getPassword())).thenReturn(false);

        // Act
        boolean result = authService.login(username, password);

        // Assert
        assertFalse(result);
        verify(manager).userExists(username);
        verify(manager).loadUserByUsername(username);
        verify(encoder).matches(password, userDetails.getPassword());
    }

    @Test
    void testRegister_Success() {
        // Arrange
        Register register = new Register();
        register.setUsername("user@example.com");
        register.setPassword("password");
        User user = new User();

        when(manager.userExists(register.getUsername())).thenReturn(false);
        when(encoder.encode(register.getPassword())).thenReturn("password");
        when(userMapper.toUserEntity(register)).thenReturn(user);

        // Act
        boolean result = authService.register(register);

        // Assert
        assertTrue(result);
        verify(manager).userExists(register.getUsername());
        verify(encoder).encode(register.getPassword());
        verify(userMapper).toUserEntity(register);
        verify(userRepository).save(user);
    }

    @Test
    void testRegister_Failure_UserAlreadyExists() {
        // Arrange
        Register register = new Register();
        register.setUsername("user@example.com");
        register.setPassword("password");

        when(manager.userExists(register.getUsername())).thenReturn(true);

        // Act
        boolean result = authService.register(register);

        // Assert
        assertFalse(result);
        verify(manager).userExists(register.getUsername());
        verify(encoder, never()).encode(any());
        verify(userMapper, never()).toUserEntity(any());
        verify(userRepository, never()).save(any());
    }
}