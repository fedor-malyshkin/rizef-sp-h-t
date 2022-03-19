package com.rize.test;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.parsing.Parser;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql("/db/h2/operations/clean_data.sql"),
        @Sql("/db/h2/fixtures/init.sql"),
})
@ContextConfiguration(initializers = TestApplicationTests.InnerInitializer.class)
class TestApplicationTests {

    public static class InnerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("spring.profiles.active=test")
                    .applyTo(applicationContext);
        }
    }

    private static ResponseSpecification restSuccessSpec;
    private static ResponseSpecification restErrorSpec;

    private String address(String address) {
        return "http://localhost:" + localPort + address;
    }

    @LocalServerPort
    private int localPort;

    @BeforeAll
    public static void setUpClass() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(200);
        restSuccessSpec = builder.build();
        builder = new ResponseSpecBuilder();
        builder.expectStatusCode(400);
        restErrorSpec = builder.build();
    }


    @Test
    void shouldCreateAndGetIt() {
        var body = "{\n" +
                "  \"firstName\": \"fnXXX\",\n" +
                "  \"lastName\": \"lnXXX\",\n" +
                "  \"category\": \"PAINTER\",\n" +
                "  \"birthday\": \"2020-01-01\",\n" +
                "  \"email\": \"email\"\n" +
                "}";
        var newId = given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                // ---
                .post(address("/artists"))
                // ---
                .then()
                // ---
                .statusCode(HttpStatus.CREATED.value())
                .extract().path("id");

        given()
                .param("category", "XXXXXXXXX")
                .when()
                // ---
                .get(address("/artists/" + newId))
                // ---
                .then()
                // ---
                .spec(restSuccessSpec)
                .body("firstName", equalTo("fnXXX"))
                .body("lastName", equalTo("lnXXX"));
    }

    @Test
    void shouldGetErrorWithWrongFilter() {
        given()
                .param("category", "XXXXXXXXX")
                .when()
                // ---
                .get(address("/artists"))
                // ---
                .then()
                // ---
                .spec(restErrorSpec);
    }

    @Test
    void shouldFilterByCategory() {
        given()
                .param("category", "ACTOR")
                .when()
                // ---
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get(address("/artists"))
                // ---
                .then()
                // ---
                .spec(restSuccessSpec)
                .body("artists", hasSize(2))
                .body("hasMore", equalTo(false));
    }

    @Test
    void shouldFilterByBirthMonth() {
        given()
                .param("birthdayMonth", "4")
                .when()
                // ---
                .get(address("/artists"))
                // ---
                .then()
                // ---
                .spec(restSuccessSpec)
                .body("artists", hasSize(1))
                .body("hasMore", equalTo(false));
    }

    @Test
    void shouldFilterBySearch() {
        given()
                .param("searchTexts", "vin")
                .when()
                // ---
                .get(address("/artists"))
                // ---
                .then()
                // ---
                .spec(restSuccessSpec)
                .body("artists", hasSize(3))
                .body("hasMore", equalTo(false));
    }

    @Test
    void shouldFilterBySearchAndCategory() {
        given()
                .param("searchTexts", "vin")
                .param("category", "ACTOR")
                .when()
                // ---
                .get(address("/artists"))
                // ---
                .then()
                // ---
                .spec(restSuccessSpec)
                .body("artists", hasSize(1))
                .body("hasMore", equalTo(false));
    }

/*
    @Tag(MapleApiTestUtils.TEST_TAG_SMOKE)
    @Test
    void shouldGetAffiliations() {
        when()
                // ---
                .get(address("/nrd/all-affiliations"))
                // ---
                .then()
                // ---
                .spec(restSuccessSpec)
                .body(".", hasItems("None specified", "Neutral", "Hostile", "Joker", "Pending", "Faker", "Friend", "Assumed friend", "Suspect", "Unknown"));
    }

    @Tag(MapleApiTestUtils.TEST_TAG_SMOKE)
    @Test
    void shouldGetCountries() {
        when()
                .get(address("/nrd/countries"))
                // ---
                .then()
                .spec(restSuccessSpec)
                .body(".", hasSize(12))
                .body("[1].id", equalTo("c_12"));
    }

    @Tag(MapleApiTestUtils.TEST_TAG_SMOKE)
    @Test
    void shouldGetUnits() {
        when()
                .get(address("/nrd/units"))
                // ---
                .then()
                .spec(restSuccessSpec)
                .body(".", hasSize(0));

    }


    @Disabled("not ready yet")
    @Tag(MapleApiTestUtils.TEST_TAG_SMOKE)
    @Test
    void shouldGetRadars() {
        when()
                .get(address("/nrd/radars"))
                // ---
                .then()
                .spec(restSuccessSpec)
                .body("[0].name", equalTo("RM1070A_HENDIJAN"))
                .body("[0].radarFunction", equalTo("NAVIGATION (NA)"))
                .body("[0].id", equalTo("r_1"));
    }


    @Disabled("not ready yet")
    @Tag(MapleApiTestUtils.TEST_TAG_LOGIC)
    @Test
    void shouldGetAlarmsFromRadar() {
        given()
                .param("from", "2000-01-01T00:00:00.000-00:00")
                .param("to", "2000-01-02T00:00:00.000-00:00")
                // ---
                .when()
                .get(address("/alarm/radars"))
                // ---
                .then()
                .spec(restSuccessSpec)
                .body(".", hasSize(1))
                .body("[0].englishName", equalTo("Test unit"));
    }

    @Tag(MapleApiTestUtils.TEST_TAG_LOGIC)
    @Test
    void shouldSearchRefFrequency() {
        given()
                .param("fMinFrom", 100)
                .param("fMinTo", 0)
                // ---
                .when()
                .get(address("/reference/frequency/search"))
                // ---
                .then()
                .spec(restSuccessSpec)
                .body(".", hasSize(0));
    }

    */
}
