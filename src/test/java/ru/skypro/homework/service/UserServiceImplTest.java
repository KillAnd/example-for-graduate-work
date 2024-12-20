package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.exception.NewPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapperImpl;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private UserMapperImpl userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    // Тест для метода checkCurrentPassword
    @Test
    public void testCheckCurrentPassword_Success() {
        // Подготовка данных
        String email = "test@example.com";
        String password = "hashedPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        // Мокирование репозитория
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Выполнение теста
        boolean result = userService.checkCurrentPassword(email, password);

        // Проверка результата
        assertTrue(result);
    }

    @Test
    public void testCheckCurrentPassword_UserNotFound() {
        // Подготовка данных
        String email = "test@example.com";
        String password = "hashedPassword";

        // Мокирование репозитория
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Выполнение теста и проверка исключения
        assertThrows(UserNotFoundException.class, () -> {
            userService.checkCurrentPassword(email, password);
        });
    }

    @Test
    public void testCheckCurrentPassword_IncorrectPassword() {
        // Подготовка данных
        String email = "test@example.com";
        String correctPassword = "hashedPassword";
        String incorrectPassword = "wrongPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword(correctPassword);

        // Мокирование репозитория
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Выполнение теста
        boolean result = userService.checkCurrentPassword(email, incorrectPassword);

        // Проверка результата
        assertFalse(result);
    }

    // Тест для метода updatePassword
    @Test
    public void testUpdatePassword_Success() throws NewPasswordException {
        // Подготовка данных
        String email = "test@example.com";
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("hashedPassword");
        newPassword.setNewPassword("newHashedPassword");
        User user = new User();
        user.setEmail(email);
        user.setPassword("hashedPassword");

        // Мокирование репозитория
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Выполнение теста
        userService.updatePassword(email, newPassword);

        // Проверка, что пароль обновлен
        assertEquals("newHashedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdatePassword_UserNotFound() {
        // Подготовка данных
        String email = "test@example.com";
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("hashedPassword");
        newPassword.setNewPassword("newHashedPassword");

        // Мокирование репозитория
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Выполнение теста и проверка исключения
        assertThrows(UserNotFoundException.class, () -> {
            userService.updatePassword(email, newPassword);
        });
    }

    @Test
    public void testUpdatePassword_IncorrectCurrentPassword() {
        // Подготовка данных
        String email = "test@example.com";
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("wrongPassword");
        newPassword.setNewPassword("newHashedPassword");
        User user = new User();
        user.setEmail(email);
        user.setPassword("hashedPassword");

        // Мокирование репозитория
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Выполнение теста и проверка исключения
        assertThrows(NewPasswordException.class, () -> {
            userService.updatePassword(email, newPassword);
        });
    }

    // Тест для метода findUserByEmail
    @Test
    public void testFindUserByEmail_Success() {
        // Подготовка данных
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        // Мокирование репозитория
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Выполнение теста
        Optional<User> result = userService.findUserByEmail(email);

        // Проверка результата
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    public void testFindUserByEmail_NotFound() {
        // Подготовка данных
        String email = "test@example.com";

        // Мокирование репозитория
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Выполнение теста
        Optional<User> result = userService.findUserByEmail(email);

        // Проверка результата
        assertFalse(result.isPresent());
    }

    // Тест для метода updateUser
    @Test
    public void testUpdateUser_Success() {
        // Подготовка данных
        String email = "test@example.com";
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("Updated");
        updateUser.setLastName("User");
        updateUser.setPhone("1234567890");

        User existingUser = new User();
        existingUser.setEmail(email);
        existingUser.setFirstName("Old");
        existingUser.setLastName("User");
        existingUser.setPhone("0987654321");

        User updatedUser = new User();
        updatedUser.setEmail(email);
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        updatedUser.setPhone("1234567890");

        // Мокирование репозитория и маппера
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(userMapper.mapFromUpdateUser(updateUser)).thenReturn(updatedUser);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        // Выполнение теста
        User result = userService.updateUser(email, updateUser);

        // Проверка результата
        assertEquals("Updated", result.getFirstName());
        assertEquals("User", result.getLastName());
        assertEquals("1234567890", result.getPhone());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        // Подготовка данных
        String email = "test@example.com";
        UpdateUser updateUser = new UpdateUser();

        // Мокирование репозитория
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Выполнение теста и проверка исключения
        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(email, updateUser);
        });
    }

    // Тест для метода updateUserImage
    @Test
    public void testUpdateUserImage_Success() throws IOException {
        // Подготовка данных
        String email = "test@example.com";
        MultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());
        User user = new User();
        user.setEmail(email);

        // Мокирование репозитория и ImageService
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        doNothing().when(imageService).uploadImage(email, image);

        // Выполнение теста
        userService.updateUserImage(email, image);

        // Проверка, что метод uploadImage вызван
        verify(imageService, times(1)).uploadImage(email, image);
    }

    @Test
    public void testUpdateUserImage_UserNotFound() {
        // Подготовка данных
        String email = "test@example.com";
        MultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());

        // Мокирование репозитория
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Выполнение теста и проверка исключения
        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUserImage(email, image);
        });
    }

    @Test
    public void testUpdateUserImage_IOException() throws IOException {
        // Подготовка данных
        String email = "test@example.com";
        MultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());
        User user = new User();
        user.setEmail(email);

        // Мокирование репозитория и ImageService
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        doThrow(IOException.class).when(imageService).uploadImage(email, image);

        // Выполнение теста и проверка исключения
        assertThrows(RuntimeException.class, () -> {
            userService.updateUserImage(email, image);
        });
    }
}
