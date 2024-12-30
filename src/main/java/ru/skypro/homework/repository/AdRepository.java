package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.dto.Ads;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Integer> {

    @Query(value = "UPDATE ad SET description=:description, price=:price, title=:title WHERE pk=:id", nativeQuery = true)
    int updateInfoAboutAdByPk(int id, String description, Integer price, String title);

    Ad findAdByPk(int id);

    Ad findAdEntityByImage(String filePath);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM ad WHERE pk = :id)", nativeQuery = true)
    boolean existsAdByPk(int id);


}
