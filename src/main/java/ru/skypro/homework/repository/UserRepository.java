package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
    @Modifying
    @Transactional
    @Query(value = "UPDATE app_user SET image =:filePath WHERE username=:username", nativeQuery = true)
    void saveImagePath(String filePath, String username);
}
