package com.rize.test.model.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexArtistRes {
    @Schema(required = true)
    private List<ArtistRes> artists;
    @Schema(required = true)
    private boolean hasMore;
}
