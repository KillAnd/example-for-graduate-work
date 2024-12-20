package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.exception.NewPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsersController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ImageService imageService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Тест для метода setPassword
    @Test
    public void testSetPassword_Success() throws Exception {
        // Подготовка данных
        NewPassword newPassword = new NewPassword();
        newPassword.setNewPassword("newPassword");

        // Мокирование SecurityContext
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Мокирование сервиса
        doNothing().when(userService).updatePassword(anyString(), any(NewPassword.class));

        // Выполнение запроса
        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSetPassword_Failure() throws Exception {
        // Подготовка данных
        NewPassword newPassword = new NewPassword();
        newPassword.setNewPassword("newPassword");

        // Мокирование SecurityContext
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Мокирование сервиса с исключением
        doThrow(new NewPasswordException("Invalid password")).when(userService).updatePassword(anyString(), any(NewPassword.class));

        // Выполнение запроса
        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword)))
                .andExpect(status().isBadRequest());
    }

    // Тест для метода getUser
    @Test
    public void testGetUser_Success() throws Exception {
        // Подготовка данных
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("Test User");

        // Мокирование SecurityContext
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Мокирование сервиса
        when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Выполнение запроса
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    public void testGetUser_NotFound() throws Exception {
        // Мокирование SecurityContext
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Мокирование сервиса
        when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.empty());

        // Выполнение запроса
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    // Тест для метода updateUser
    @Test
    public void testUpdateUser_Success() throws Exception {
        // Подготовка данных
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("Updated User");

        User updatedUser = new User();
        updatedUser.setEmail("test@example.com");
        updatedUser.setFirstName("Updated User");

        // Мокирование SecurityContext
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Мокирование сервиса
        when(userService.updateUser(any(), any())).thenReturn(updatedUser);

        // Выполнение запроса
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated User"));
    }

    // Тест для метода updateUserImage
    @Test
    public void testUpdateUserImage_Success() throws Exception {
        // Подготовка данных
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image".getBytes());

        // Мокирование SecurityContext
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Мокирование сервиса
        doNothing().when(userService).updateUserImage(any(), any());

        // Выполнение запроса
        mockMvc.perform(multipart("/users/me/image")
                        .file(image))
                .andExpect(status().isOk())
                .andExpect(content().string("Image updated successfully"));
    }

    @Test
    public void testUpdateUserImage_UserNotFound() throws Exception {
        // Подготовка данных
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image".getBytes());

        // Мокирование SecurityContext
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Мокирование сервиса с исключением
        doThrow(new UserNotFoundException("User not found")).when(userService).updateUserImage(any(), any());

        // Выполнение запроса
        mockMvc.perform(multipart("/users/me/image")
                        .file(image))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }
}
