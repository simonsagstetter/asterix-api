package com.spring.asterix.models;

import lombok.Builder;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@With
@Builder
@Document("characters")
public record Character(
        @Id String id,
        String name,
        String description,
        List<String> attributes,
        int age,
        String nationality,
        String occupation,
        String firstAppearance,
        String village,
        boolean isMainCharacter,
        Instant createdAt,
        Instant updatedAt
) {
}
