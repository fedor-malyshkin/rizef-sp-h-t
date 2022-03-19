package com.rize.test.controller;

import com.rize.test.model.mapper.ArtistMapper;
import com.rize.test.model.rest.AbstractArtistReqRes;
import com.rize.test.model.rest.ArtistRes;
import com.rize.test.model.rest.IndexArtistRes;
import com.rize.test.model.rest.SaveArtistReq;
import com.rize.test.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping(value = "/artists",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;
    private final ArtistMapper artistMapper;

    static final HttpHeaders responseJsonHeader = new HttpHeaders();

    static {
        responseJsonHeader.setContentType(MediaType.APPLICATION_JSON);
    }


    @Operation(summary = "Get specific artist by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<ArtistRes> show(@PathVariable Integer id) {
        var artist = artistService.get(id);
        return artist
                .map(artistMapper::fromEntityToRes)
                .map(a -> new ResponseEntity<>(a, responseJsonHeader, HttpStatus.OK))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get a list of artists")
    @GetMapping
    public ResponseEntity<IndexArtistRes> index(
            @Parameter(description = "Filter by category", in = ParameterIn.QUERY)
            @RequestParam(required = false) AbstractArtistReqRes.CategoriesEnum category,
            @Parameter(description = "Filter by the month of birthday", in = ParameterIn.QUERY)
            @RequestParam(required = false) Integer birthdayMonth,
            @Parameter(description = "Filter by substring (in first and last names)", in = ParameterIn.QUERY)
            @RequestParam(required = false) String searchTexts
    ) {

        var result = artistService.index(
                Objects.nonNull(category) ? category.getIndex() : null,
                birthdayMonth,
                searchTexts);
        var indexRes = artistMapper.fromEntitiesToIndexRes(result.getFirst(), result.getSecond());
        return new ResponseEntity<>(indexRes, responseJsonHeader, HttpStatus.OK);
    }

    @Operation(summary = "Create an artist")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArtistRes> create(@Valid
                                            @RequestBody SaveArtistReq body) {
        var response = createOrUpdateArtist(body);
        return new ResponseEntity<>(response, responseJsonHeader, HttpStatus.CREATED);
    }

    private ArtistRes createOrUpdateArtist(SaveArtistReq body) {
        var artistEntity = artistMapper.fromSaveReqToEntity(body);
        artistEntity = artistService.save(artistEntity);
        return artistMapper.fromEntityToRes(artistEntity);
    }

    @Operation(summary = "Delete specific artist by their ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (artistService.delete(id))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }
}
