package serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class JacksonAPI_JSONArrayNode {
    ResponseSpecification customResponseSpecification;

    @BeforeClass
    public void beforeClass() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://96f5af29-388f-4d2e-93b1-cd291b588a3f.mock.pstmn.io")
                .addHeader("x-mock-match-request-body", "true")
                .setContentType("application/json;charset=utf-8")
                .log(LogDetail.ALL);
        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL);
        customResponseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void serialize_json_array_using_jackson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNodeList = objectMapper.createArrayNode();

        ObjectNode obj4003Array = objectMapper.createObjectNode();
        obj4003Array.put("id", "4003");
        obj4003Array.put("type", "Boat");

        ObjectNode obj4004Array = objectMapper.createObjectNode();
        obj4004Array.put("id", "4004");
        obj4004Array.put("type", "Car");

       arrayNodeList.add(obj4003Array);
       arrayNodeList.add(obj4004Array);

        given().
                body(arrayNodeList).
        when().
                post("/post").
        then().
                spec(customResponseSpecification).
                assertThat().body("msg", equalTo("Success"));
    }
}
