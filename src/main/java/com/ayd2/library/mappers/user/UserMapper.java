package com.ayd2.library.mappers.user;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.ayd2.library.dto.users.UserResponseDTO;
import com.ayd2.library.dto.users.UpdateUserAccountRequestDTO;
import com.ayd2.library.models.user.UserAccount;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseDTO toUserResponseDTO(UserAccount userAccount);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, ignoreByDefault = true)
    @Mapping(target = "email")
    void updateUserFromDTO(UpdateUserAccountRequestDTO dto, @MappingTarget UserAccount userAccount);

}
