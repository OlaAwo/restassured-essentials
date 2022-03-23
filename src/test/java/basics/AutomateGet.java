package basics;

import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AutomateGet {
    @Test
    public void validate_status_code() {
        given().
                baseUri("https://api.getpostman.com").
                header("X-Api-Key", "").
        when().
                get("/workspaces").
        then().
                assertThat().statusCode(200).
                log().all();
    }

    @Test
    public void validate_response_body() {
        given().
                baseUri("https://api.getpostman.com").
                header("X-Api-Key", "").
        when().
                get("/workspaces").
        then().
                assertThat().statusCode(200).
                body("workspaces.name", hasItems("New Workspace", "Tutorials", "Push Live", "Jobs"),
                        "workspaces.type", hasItems("personal"),
                        "workspaces[0].name", equalTo("Jobs"),
                        "workspaces[0].name", is(equalTo("Jobs")),
                        "workspaces.size()", equalTo(4),
                        "workspaces.name", hasItem("Tutorials")).
                log().all();
    }

    @Test
    public void extract_entire_json_response() {
        Response res = given(). // use the Response interface
                baseUri("https://api.getpostman.com").
                header("X-Api-Key", "").
        when().
                get("/workspaces").
        then().
                assertThat().statusCode(200).
                extract().response(); // extract response

        System.out.println("response = " + res.asString()); // converts json resonse to string
    }

    @Test
    public void extract_single_value_from_response() {
        Response res = given().
                baseUri("https://api.getpostman.com").
                header("X-Api-Key", "").
        when().
                get("/workspaces").
        then().
                assertThat().statusCode(200).
                extract().response();

        // OPTION 1: JsonPath + response as json
        /*JsonPath jsonPath = new JsonPath(res.asString());
        System.out.println("workspace name = " + jsonPath.getString("workspaces[0].name"));*/

        // OPTION 2
        // System.out.println("workspace name = " + res.path("workspaces[0].name"));
    }

    @Test
    public void extract_single_value_from_response_2() {
        String res = given().
                baseUri("https://api.getpostman.com").
                header("X-Api-Key", "").
        when().
                get("/workspaces").
        then().
                assertThat().statusCode(200).
                extract().response().path("workspaces[0].name"); // OPTION 3

        System.out.println("workspace name = " + res);

        // OPTION 4
        /* JsonPath.from(res).getString("workspaces[0].name");
        System.out.println("workspace name = " + JsonPath.from(res).getString("workspaces[0].name"));*/
    }

    @Test
    public void assert_on_extracted_response() {
        String name = given().
                baseUri("https://api.getpostman.com").
                header("X-Api-Key", "").
        when().
                get("/workspaces").
        then().
                assertThat().statusCode(200).
                extract().response().path("workspaces[0].name");

        // assertThat(name, equalTo("Jobs")); // using hamcrest assertion
        Assert.assertEquals(name, "Jobs"); // using testng assertion
    }

    @Test
    public void validate_response_with_hamcrest() {
        given().
                baseUri("https://api.getpostman.com").
                header("X-Api-Key", "").
        when().
                get("/workspaces").
        then().
                assertThat().
                statusCode(200).
                body("workspaces.name", contains("Jobs", "Push Live", "Tutorials"),
                        "workspaces.name", is(not(empty())),
                        "workspaces.name", hasSize(3), // has 3 items
                        "workspaces.name", everyItem(startsWith("M")),
                        "workspaces[0]", hasKey("id"), // check json key
                        "workspaces[0]", hasValue("Jobs"),
                        "workspaces[0]", hasEntry("id", "8a9270ea-48bc-4ad5-8cb8-5a607203ce7b"),
                        "workspaces[1].name", allOf(startsWith("Push"), containsString("Live"))
                );
    }

    @Test
    public void request_response_logging() {
        given().
                baseUri("https://api.getpostman.com").
                header("X-Api-Key", "").
                log().all(). // log request
        when().
                get("/workspaces").
        then().
                log().all(). //log response
                assertThat().statusCode(200);
    }

    @Test
    public void log_only_if_error() {
        given().
                baseUri("https://api.getpostman.com").
                header("X-Api-Key", "").
                log().all().
        when().
                get("/workspaces").
        then().
                log().ifError(). // only logs if there's an error
                assertThat().
                statusCode(200);
    }

    @Test
    public void log_only_if_validation_fails() {
        given().
                baseUri("https://api.getpostman.com").
                header("X-Api-Key", "").
                config(config.logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails())).
        when().
                get("/workspaces").
        then().
                assertThat().statusCode(200); // only logs if there's a validation error
    }

    @Test
    public void hide_header_information() {
        Set<String> headers = new HashSet<String>();
        headers.add("X-Api-Key");
        headers.add("Accept");

        given().
                baseUri("https://api.getpostman.com").
                header("X-Api-Key", "").
               //config(config.logConfig(LogConfig.logConfig().blacklistHeader("X-Api-Key"))). // single header value
                config(config.logConfig(LogConfig.logConfig().blacklistHeaders(headers))). // multiple headers
                log().all().
        when().
                get("/workspaces").
        then().
                assertThat().statusCode(200);
    }
}
