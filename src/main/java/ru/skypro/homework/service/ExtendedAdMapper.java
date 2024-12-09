package ru.skypro.homework.service;

import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.ExtendedAd;

public interface ExtendedAdMapper {
    ExtendedAd mapToExtendedAd(Ad ad);
}
