package com.rize.test.model.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema
@Data
@NoArgsConstructor
public abstract class AbstractArtistReqRes {
    @Getter
    @RequiredArgsConstructor
    public enum CategoriesEnum {
        ACTOR(0),
        PAINTER(1),
        SCULPTOR(2);
        final int index;
    }

    @Schema(required = true)
    @NotEmpty
    String firstName;

    String middleName;

    @Schema(required = true)
    @NotEmpty
    String lastName;
    @Schema(required = true)
    @NotNull
    CategoriesEnum category;
    @Schema(required = true)
    @NotNull
    LocalDate birthday;
    @Schema(required = true)
    @NotEmpty
    String email;
    String notes;
}
