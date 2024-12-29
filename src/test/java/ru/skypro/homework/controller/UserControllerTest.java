package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.exception.NewPasswordException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private MultipartFile image;

    @InjectMocks
    private UsersController usersController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void setPassword_Success() {
        NewPassword newPassword = new NewPassword();
        when(authentication.getName()).thenReturn("user@example.com");

        ResponseEntity<Void> response = usersController.setPassword(newPassword);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).updatePassword("user@example.com", newPassword);
    }

    @Test
    void setPassword_NewPasswordException() {
        NewPassword newPassword = new NewPassword();
        when(authentication.getName()).thenReturn("user@example.com");
        doThrow(new NewPasswordException("неверный пароль"))
                .when(userService).updatePassword("user@example.com", newPassword);

        ResponseEntity<Void> response = usersController.setPassword(newPassword);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getUser_Success() {
        UserDTO user = new UserDTO();
        when(authentication.getName()).thenReturn("user@example.com");
        when(userService.findUserByUsername("user@example.com")).thenReturn(user);

        ResponseEntity<UserDTO> response = usersController.getUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void getUser_UnauthorizedException() {
        when(authentication.getName()).thenReturn("user@example.com");
        when(userService.findUserByUsername("user@example.com")).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> usersController.getUser());
    }

    @Test
    void updateUser_Success() {
        UpdateUser updateUser = new UpdateUser();
        UpdateUser updatedUser = new UpdateUser();
        when(authentication.getName()).thenReturn("user@example.com");
        when(userService.updateUser("user@example.com", updateUser)).thenReturn(updatedUser);

        ResponseEntity<UpdateUser> response = usersController.updateUser(updateUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }

    @Test
    void updateUserImage_Success() {
        when(authentication.getName()).thenReturn("user@example.com");

        ResponseEntity<?> response = usersController.updateUserImage(image);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).updateUserImage("user@example.com", image);
    }

    @Test
    void updateUserImage_Unauthorized() {
        when(securityContext.getAuthentication()).thenReturn(null);

        ResponseEntity<?> response = usersController.updateUserImage(image);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}