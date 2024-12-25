package ru.skypro.homework.mapper;

//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.MappingConstants;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.User;

//@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExtendedAdMapper {

//    @Mapping(source = "user.firstName", target = "authorFirstName")
//    @Mapping(source = "user.lastName", target = "authorLastName")
//    @Mapping(source = "user.email", target = "email")
//    @Mapping(source = "user.phone", target = "phone")
//    @Mapping(source = "user.imageUser", target = "image")
//    @Mapping(source = "ad.adId", target = "adId")
//    @Mapping(source = "ad.price", target = "price")
//    @Mapping(source = "ad.title", target = "title")
//    @Mapping(source = "ad.description", target = "description")
    ExtendedAd mapToExtendedAd(User user, Ad ad);

//    @Mapping(source = "extendedAd.image", target = "imageAd")
//    @Mapping(source = "extendedAd.adId", target = "adId")
//    @Mapping(source = "extendedAd.price", target = "price")
//    @Mapping(source = "extendedAd.title", target = "title")
//    @Mapping(source = "extendedAd.description", target = "description")
    Ad mapFromExtendedAd(ExtendedAd extendedAd);
}
