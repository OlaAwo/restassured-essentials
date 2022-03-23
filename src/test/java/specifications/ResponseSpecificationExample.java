package specifications;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

// NON BDD STYLE
public class ResponseSpecificationExample {
    // ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://api.getpostman.com").
                addHeader("X-Api-Key", "");
        RestAssured.requestSpecification = requestSpecBuilder.build();
        requestSpecBuilder.log(LogDetail.ALL);

        // option 1 - recommended
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL);
        RestAssured.responseSpecification = responseSpecBuilder.build(); // use static method

        /* option 2
        responseSpecification = RestAssured.expect()
                .statusCode(200)
                .contentType(ContentType.JSON).logDetail(LogDetail.ALL);*/
    }

    @Test
    public void validate_status_code() {
        get("/workspaces");
    }

    @Test
    public void validate_response_body() {
        Response response = get("/workspaces")
                .then()
                .extract().response();
        assertThat(response.path("workspaces[0].name"), equalTo("Jobs"));
    }


}

