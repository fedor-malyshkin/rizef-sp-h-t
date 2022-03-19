package com.rize.test.model.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Schema
@Data
@NoArgsConstructor
public class ArtistRes extends AbstractArtistReqRes {

    @Schema(required = true)
    private Integer id;

    @Schema(required = true)
    private Instant createdAt;

    @Schema(required = true)
    private Instant updatedAt;
}
