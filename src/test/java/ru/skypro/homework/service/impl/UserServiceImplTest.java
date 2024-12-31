package ru.skypro.homework.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.exception.NewPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapperImpl;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserMapperImpl userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;
    private UpdateUser updateUser;
    private NewPassword newPassword;
    private MultipartFile image;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");

        userDTO = new UserDTO();
        userDTO.setEmail("testUser");

        updateUser = new UpdateUser();
        updateUser.setFirstName("NewFirstName");
        updateUser.setLastName("NewLastName");
        updateUser.setPhone("1234567890");

        newPassword = new NewPassword();
        newPassword.setCurrentPassword("currentPassword");
        newPassword.setNewPassword("newPassword");

        image = mock(MultipartFile.class);
    }

    @Test
    void testCheckCurrentPassword_ValidPassword() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(encoder.matches("currentPassword", "encodedPassword")).thenReturn(true);

        assertTrue(userService.checkCurrentPassword("testUser", "currentPassword"));
    }

    @Test
    void testCheckCurrentPassword_InvalidPassword() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(encoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertFalse(userService.checkCurrentPassword("testUser", "wrongPassword"));
    }

    @Test
    void testCheckCurrentPassword_UserNotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            userService.checkCurrentPassword("unknownUser", "currentPassword");
        });
    }

    @Test
    void testUpdatePassword_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(encoder.matches("currentPassword", "encodedPassword")).thenReturn(true);

        userService.updatePassword("testUser", newPassword);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdatePassword_InvalidCurrentPassword() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(encoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);
        newPassword.setCurrentPassword("wrongPassword");

        assertThrows(NewPasswordException.class, () -> {
            userService.updatePassword("testUser", newPassword);
        });
    }

    @Test
    void testUpdatePassword_UserNotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            userService.updatePassword("unknownUser", newPassword);
        });
    }

    @Test
    void testFindUserById_UserFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDTO);

        UserDTO result = userService.findUserById(1);

        assertNotNull(result);
        assertEquals("testUser", result.getEmail());
    }

    @Test
    void testFindUserById_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserDTO result = userService.findUserById(1);

        assertNull(result);
    }

    @Test
    void testFindUserByUsername_UserFound() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDTO);

        UserDTO result = userService.findUserByUsername("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getEmail());
    }

    @Test
    void testUpdateUser_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        UpdateUser result = userService.updateUser("testUser", updateUser);

        assertNotNull(result);
        assertEquals("NewFirstName", user.getFirstName());
        assertEquals("NewLastName", user.getLastName());
        assertEquals("1234567890", user.getPhone());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser("unknownUser", updateUser);
        });
    }

    @Test
    void testUpdateUserImage_Success() throws IOException {
        Image imagePath = new Image();
        imagePath.setFilePath("path/to/image");
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(imageService.uploadImage(image)).thenReturn(imagePath);

        userService.updateUserImage("testUser", image);

        assertEquals("path/to/image", user.getImage());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserImage_IOException() throws IOException {
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(imageService.uploadImage(image)).thenThrow(IOException.class);

        assertThrows(RuntimeException.class, () -> {
            userService.updateUserImage("testUser", image);
        });
    }

    @Test
    void testUpdateUserImage_UserNotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUserImage("unknownUser", image);
        });
    }
}