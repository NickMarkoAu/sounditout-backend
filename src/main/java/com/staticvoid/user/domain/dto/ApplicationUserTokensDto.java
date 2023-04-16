package com.staticvoid.user.domain.dto;

import com.staticvoid.user.domain.ApplicationUserTokens;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApplicationUserTokensDto implements Serializable {

    private Long tokens;
    private Long freeTokens;

    public static ApplicationUserTokensDto toDto(ApplicationUserTokens entity) {
        ApplicationUserTokensDto tokensDto = new ApplicationUserTokensDto();
        tokensDto.setTokens(entity.getTokens());
        tokensDto.setFreeTokens(entity.getFreeTokens());
        return tokensDto;
    }

    public ApplicationUserTokens toEntity() {
        ApplicationUserTokens entity = new ApplicationUserTokens();
        entity.setTokens(tokens);
        entity.setFreeTokens(freeTokens);
        return entity;
    }
}
