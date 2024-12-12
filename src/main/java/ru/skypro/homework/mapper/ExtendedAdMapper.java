package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExtendedAdMapper {

    ExtendedAd mapToExtendedAd(User user, Ad ad);
    Ad mapFromExtendedAd(ExtendedAd extendedAd);
}
