package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.dto.Ads;

public interface AdRepository extends JpaRepository<Ad, Long> {

    Ads getAllAds();
}
