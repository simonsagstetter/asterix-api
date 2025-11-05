package com.spring.asterix.dtos;

import com.mongodb.lang.Nullable;
import lombok.Builder;
import lombok.With;

import java.util.List;

@With
@Builder
public record CharacterPatchDTO( @Nullable String name,
                                 @Nullable String description,
                                 @Nullable List<String> attributes,
                                 @Nullable Integer age,
                                 @Nullable String nationality,
                                 @Nullable String occupation,
                                 @Nullable String firstAppearance,
                                 @Nullable String village,
                                 @Nullable Boolean isMainCharacter ) {
}
