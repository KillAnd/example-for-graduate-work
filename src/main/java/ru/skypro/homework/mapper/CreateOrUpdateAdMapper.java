package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.Ad;

public interface CreateOrUpdateAdMapper {

    CreateOrUpdateAd mapToCreateOrUpdateAd(Ad ad);
    Ad mapFromCreateOrUpdateAd(CreateOrUpdateAd dto);

}
