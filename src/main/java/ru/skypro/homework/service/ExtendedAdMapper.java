package ru.skypro.homework.service;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExtendedAdMapper {
    ExtendedAd mapToExtendedAd(User user, Ad ad);
    Ad mapFromExtendedAd(ExtendedAd extendedAd);
}
