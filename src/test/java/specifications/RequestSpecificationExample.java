package specifications;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

// NON BDD STYLE
public class RequestSpecificationExample {
    // RequestSpecification requestSpecification;

    @BeforeClass
    public void beforeClass() {

        // option 1 - recommended
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://api.getpostman.com").
                addHeader("X-Api-Key", "");

        requestSpecBuilder.log(LogDetail.ALL);
        RestAssured.requestSpecification = requestSpecBuilder.build(); // use static method

        // option 2
        /*requestSpecification = with().
                baseUri("https://api.getpostman.com").
                header("X-Api-Key", "").
                log().all();*/
    }

    // option 2
    /*@Test
    public void validate_status_code() {
        Response response = given(requestSpecification). // you have to add "given to use the spec builder"
                header("headerKey", "headerValue"). // adding a header for specific test
                get("/workspaces").then().log().all().extract().response();
        assertThat(response.statusCode(), equalTo(200));
    }*/

    @Test
    public void queryTest() {
        QueryableRequestSpecification queryableRequestSpecification = SpecificationQuerier.
                query(requestSpecification);
        System.out.println(queryableRequestSpecification.getBaseUri());
        System.out.println(queryableRequestSpecification.getHeaders());
    }

    @Test
    public void validate_status_code() {
        Response response = get("/workspaces").
                then().log().all().extract().response();
        assertThat(response.statusCode(), equalTo(200));
    }

    @Test
    public void validate_response_body() {
        Response response = get("/workspaces").
                then().log().all().extract().response();
        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.path("workspaces[0].name"), equalTo("Jobs"));
    }
}
