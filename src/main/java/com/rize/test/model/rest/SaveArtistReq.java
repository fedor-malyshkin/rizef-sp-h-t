package com.rize.test.model.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Schema
@Data
@NoArgsConstructor
public class SaveArtistReq extends AbstractArtistReqRes {
}
