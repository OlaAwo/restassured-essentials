package payloads;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RequestPayloadAsJsonArray {
    ResponseSpecification customResponseSpecification;

    @BeforeClass
    public void beforeClass() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://96f5af29-388f-4d2e-93b1-cd291b588a3f.mock.pstmn.io")
                .addHeader("x-mock-match-request-body", "true")
       //         .setConfig(config.encoderConfig(EncoderConfig.encoderConfig().
       //                 appendDefaultContentCharsetToContentTypeIfUndefined(false)))
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
    public void validate_post_request_payload_json_array() {
        HashMap<String, String> obj4003 = new HashMap<String,String>();
        obj4003.put("id", "4003");
        obj4003.put("type", "Boat");

        HashMap<String, String> obj4004 = new HashMap<String,String>();
        obj4004.put("id", "4004");
        obj4004.put("type", "Car");

        List<HashMap<String, String>> jsonList = new ArrayList<HashMap<String,String>>();
        jsonList.add(obj4003);
        jsonList.add(obj4004);

        given().
                body(jsonList).
        when().
                post("/post").
        then().
                spec(customResponseSpecification).
                assertThat().body("msg", equalTo("Success"));
    }
}
