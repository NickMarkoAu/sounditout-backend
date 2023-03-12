package com.staticvoid.user.domain;

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
        entity.setTokens(this.getTokens());
        entity.setFreeTokens(this.getFreeTokens());
        return entity;
    }
}
