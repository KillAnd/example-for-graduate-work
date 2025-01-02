package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private UsersController usersController;

    @Test
    void testSetPassword_Success() {
        // Arrange
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("currentPassword");
        newPassword.setNewPassword("newPassword");

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com");

        // Act
        ResponseEntity<Void> response = usersController.setPassword(newPassword);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).updatePassword("user@example.com", newPassword);
    }

    @Test
    void testSetPassword_NewPasswordException() {
        // Arrange
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("wrongPassword");
        newPassword.setNewPassword("newPassword");

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com");

        doThrow(new NewPasswordException("Current password is incorrect"))
                .when(userService).updatePassword("user@example.com", newPassword);

        // Act
        ResponseEntity<Void> response = usersController.setPassword(newPassword);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService).updatePassword("user@example.com", newPassword);
    }

    @Test
    void testGetUser_Success() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com");

        when(userService.findUserByUsername("user@example.com")).thenReturn(userDTO);

        // Act
        ResponseEntity<UserDTO> response = usersController.getUser();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
        verify(userService).findUserByUsername("user@example.com");
    }

    @Test
    void testGetUser_UnauthorizedException() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com");

        when(userService.findUserByUsername("user@example.com")).thenReturn(null);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> usersController.getUser());
        verify(userService).findUserByUsername("user@example.com");
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("John");
        updateUser.setLastName("Doe");

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com");

        when(userService.updateUser("user@example.com", updateUser)).thenReturn(updateUser);

        // Act
        ResponseEntity<UpdateUser> response = usersController.updateUser(updateUser);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateUser, response.getBody());
        verify(userService).updateUser("user@example.com", updateUser);
    }

    @Test
    void testUpdateUserImage_Success() {
        // Arrange
        MultipartFile image = mock(MultipartFile.class);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com");

        // Act
        ResponseEntity<?> response = usersController.updateUserImage(image);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).updateUserImage("user@example.com", image);
    }

    @Test
    void testUpdateUserImage_Unauthorized() {
        // Arrange
        MultipartFile image = mock(MultipartFile.class);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        // Act
        ResponseEntity<?> response = usersController.updateUserImage(image);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, never()).updateUserImage(any(), any());
    }
}
