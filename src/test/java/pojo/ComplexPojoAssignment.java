package pojo;

import assignment.pojo.Address;
import assignment.pojo.BodyRoot;
import assignment.pojo.Geo;
import example.pojo.complex.WorkspaceRoot;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ComplexPojoAssignment {

    @BeforeClass
    public void beforeClass() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://jsonplaceholder.typicode.com")
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL);
        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL);
        RestAssured.responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void validate_post_request_with_complex_pojo_assignment() {
        Geo geo = new Geo();
        Address address = new Address();
        BodyRoot bodyRoot = new BodyRoot("Leanne Graham", "Bret", "Sincere@april.biz", address,geo);

        address.setStreet("Kulas Light");
        address.setSuite("Apt. 556");
        address.setCity("Gwenborough");
        geo.setLat("-37.3159");
        geo.setLng("81.149");

        BodyRoot deserializePojo = given().
                body(bodyRoot).
        when().
                post("/users").
        then().
                log().all().
                extract().response().as(BodyRoot.class);
        //assertThat(deserializePojo.getId(), matchesPattern("^(?!\\s*$).+"));
    }

}
