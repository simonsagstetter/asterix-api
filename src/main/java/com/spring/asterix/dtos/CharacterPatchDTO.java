package com.spring.asterix.dtos;

import com.mongodb.lang.Nullable;
import lombok.Builder;
import lombok.With;

@With
@Builder
public record CharacterPatchDTO( @Nullable String name,
                                 @Nullable String description,
                                 @Nullable Integer age,
                                 @Nullable String nationality,
                                 @Nullable String occupation,
                                 @Nullable String firstAppearance,
                                 @Nullable String village,
                                 @Nullable Boolean isMainCharacter ) {
}
