package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.Ad;

@Mapper
public interface CreateOrUpdateAdMapper {
    CreateOrUpdateAd mapToCreateOrUpdateAd(Ad ad);
    Ad mapFromCreateOrUpdateAd(CreateOrUpdateAd dto);

}
