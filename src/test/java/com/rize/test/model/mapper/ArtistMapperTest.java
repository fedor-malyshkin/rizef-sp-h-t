package com.rize.test.model.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rize.test.model.rest.AbstractArtistReqRes;
import com.rize.test.model.rest.SaveArtistReq;
import com.rize.test.model.storage.ArtistEntity;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;


class ArtistMapperTest {

    static ObjectMapper objectMapper;
    ArtistMapperImpl testable = new ArtistMapperImpl();

    @BeforeAll
    public static void setUpClass() {
        objectMapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(new JavaTimeModule());

    }


    @Test
    void fromSaveReqToEntity() throws JsonProcessingException, JSONException {
        var req = new SaveArtistReq();
        req.setFirstName("fn");
        req.setMiddleName("mn");
        req.setLastName("ln");
        req.setCategory(AbstractArtistReqRes.CategoriesEnum.SCULPTOR);
        req.setBirthday(LocalDate.of(2020, 01, 01));
        req.setEmail("email");
        req.setNotes("notes");

        var result = testable.fromSaveReqToEntity(req);
        var actual = objectMapper.writeValueAsString(result);
        var expected = getStringFromClasspath("com/rize/test/model/mapper/ArtistMapperTest.fromSaveReqToEntity.expected.json");
        assertEquals(expected, actual);
    }

    @Test
    void fromEntityToRes() throws JsonProcessingException, JSONException {
        final LocalDate bd = LocalDate.of(2020, 01, 01);
        var entity = new ArtistEntity();
        entity.setFirstName("fn");
        entity.setMiddleName("mn");
        entity.setLastName("ln");
        entity.setCategoryId(1);
        entity.setBirthday(bd);
        entity.setEmail("email");
        entity.setNotes("notes");
        entity.setUpdatedAt(Instant.parse("2007-12-03T10:15:30.00Z"));
        entity.setCreatedAt(Instant.parse("2007-12-03T10:15:30.00Z"));

        var result = testable.fromEntityToRes(entity);
        var actual = objectMapper.writeValueAsString(result);
        var expected = getStringFromClasspath("com/rize/test/model/mapper/ArtistMapperTest.fromEntityToRes.expected.json");
        assertEquals(expected, actual);
    }

    @SneakyThrows
    private String getStringFromClasspath(String path) {
        final InputStream resourceAsStream = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(path);
        return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
    }

    private void assertEquals(String expected, String actual) throws JSONException {
        JSONAssert.assertEquals(new JSONObject(expected), new JSONObject(actual),
                true);
    }
}