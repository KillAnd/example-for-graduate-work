package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.exception.NewPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapperImpl;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapperImpl userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkCurrentPassword_UserExists_ReturnsTrue() {
        // Arrange
        String username = "testUser";
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(user);

        // Act
        boolean result = userService.checkCurrentPassword(username, "password");

        // Assert
        assertTrue(result);
    }

    @Test
    void checkCurrentPassword_UserNotFound_ThrowsUserNotFoundException() {
        // Arrange
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            userService.checkCurrentPassword(username, "password");
        });
    }

    @Test
    void updatePassword_ValidCurrentPassword_UpdatesPassword() {
        // Arrange
        String username = "testUser";
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("oldPassword");
        newPassword.setNewPassword("newPassword");

        User user = new User();
        user.setPassword("encodedOldPassword");

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(passwordEncoder.matches(newPassword.getCurrentPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword.getNewPassword())).thenReturn("encodedNewPassword");

        // Act
        userService.updatePassword(username, newPassword);

        // Assert
        verify(userRepository).save(user);
        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    void updatePassword_InvalidCurrentPassword_ThrowsNewPasswordException() {
        // Arrange
        String username = "testUser";
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("wrongPassword");
        newPassword.setNewPassword("newPassword");

        User user = new User();
        user.setPassword("encodedOldPassword");

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(passwordEncoder.matches(newPassword.getCurrentPassword(), user.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(NewPasswordException.class, () -> {
            userService.updatePassword(username, newPassword);
        });

        // Проверяем, что пароль не был изменен
        verify(userRepository, never()).save(user);
    }

    @Test
    void findUserById_UserExists_ReturnsUser() {
        // Arrange
        Integer id = 1;
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findUserById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findUserById_UserNotFound_ReturnsEmptyOptional() {
        // Arrange
        Integer id = 1;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findUserById(id);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void updateUser_UserExists_UpdatesUserAndReturnsUpdateUserDto() {
        // Arrange
        String username = "testUser";
        UpdateUser updateUserDto = new UpdateUser();
        updateUserDto.setFirstName("NewFirstName");
        updateUserDto.setLastName("NewLastName");
        updateUserDto.setPhone("NewPhone");

        User user = new User();
        user.setUsername(username);
        user.setFirstName("OldFirstName");
        user.setLastName("OldLastName");
        user.setPhone("OldPhone");

        when(userRepository.findByUsername(username)).thenReturn(user);

        // Act
        UpdateUser result = userService.updateUser(username, updateUserDto);

        // Assert
        assertNotNull(result);
        assertEquals(updateUserDto.getFirstName(), user.getFirstName());
        assertEquals(updateUserDto.getLastName(), user.getLastName());
        assertEquals(updateUserDto.getPhone(), user.getPhone());
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_UserNotFound_ThrowsUserNotFoundException() {
        // Arrange
        String username = "nonExistentUser";
        UpdateUser updateUserDto = new UpdateUser();
        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(username, updateUserDto);
        });
    }

    @Test
    void updateUserImage_UserExists_UpdatesImage() throws IOException {
        // Arrange
        String username = "testUser";
        MultipartFile imageFile = mock(MultipartFile.class);
        Image image = new Image();
        image.setFilePath("path/to/image");

        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(imageService.uploadImage(imageFile)).thenReturn(image);

        // Act
        userService.updateUserImage(username, imageFile);

        // Assert
        assertEquals(image.getFilePath(), user.getImage());
        verify(userRepository).save(user);
    }

    @Test
    void updateUserImage_UserNotFound_ThrowsUserNotFoundException() {
        // Arrange
        String username = "nonExistentUser";
        MultipartFile imageFile = mock(MultipartFile.class);
        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUserImage(username, imageFile);
        });
    }

    @Test
    void updateUserImage_IOException_ThrowsRuntimeException() throws IOException {
        // Arrange
        String username = "testUser";
        MultipartFile imageFile = mock(MultipartFile.class);
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(imageService.uploadImage(imageFile)).thenThrow(new IOException());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userService.updateUserImage(username, imageFile);
        });
    }
}