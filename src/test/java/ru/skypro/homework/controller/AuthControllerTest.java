package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testLogin_Success() {
        // Arrange
        Login login = new Login();
        login.setUsername("user");
        login.setPassword("password");

        when(authService.login(login.getUsername(), login.getPassword())).thenReturn(true);

        // Act
        ResponseEntity<?> response = authController.login(login);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService).login(login.getUsername(), login.getPassword());
    }

    @Test
    void testLogin_Failure() {
        // Arrange
        Login login = new Login();
        login.setUsername("user");
        login.setPassword("wrongPassword");

        when(authService.login(login.getUsername(), login.getPassword())).thenReturn(false);

        // Act
        ResponseEntity<?> response = authController.login(login);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authService).login(login.getUsername(), login.getPassword());
    }

    @Test
    void testRegister_Success() {
        // Arrange
        Register register = new Register();
        register.setUsername("newUser");
        register.setPassword("password");

        when(authService.register(register)).thenReturn(true);

        // Act
        ResponseEntity<?> response = authController.register(register);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(authService).register(register);
    }

    @Test
    void testRegister_Failure() {
        // Arrange
        Register register = new Register();
        register.setUsername("existingUser");
        register.setPassword("password");

        when(authService.register(register)).thenReturn(false);

        // Act
        ResponseEntity<?> response = authController.register(register);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(authService).register(register);
    }
}