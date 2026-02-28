package com.lms.lmssystem.mapper;

import com.lms.lmssystem.dto.response.AuthResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Map;
@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(source = "map", target = "accessToken", qualifiedByName = "getAccessToken")
    @Mapping(source = "map", target = "refreshToken", qualifiedByName = "getRefreshToken")
    AuthResponseDto toAuthResponseDto(Map<String, Object> map);

    @Named("getAccessToken")
    default String getAccessToken(Map<String, Object> map) {
        return (String) map.get("access_token");
    }

    @Named("getRefreshToken")
    default String getRefreshToken(Map<String, Object> map) {
        return (String) map.get("refresh_token");
    }

}

