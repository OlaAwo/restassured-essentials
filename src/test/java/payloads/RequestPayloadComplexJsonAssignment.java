package payloads;

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

public class RequestPayloadComplexJsonAssignment {
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
    public void validate_post_request_payload_json_array() {
        List<Integer> rgbaColor1 = new ArrayList<Integer>();
        rgbaColor1.add(255);
        rgbaColor1.add(255);
        rgbaColor1.add(255);
        rgbaColor1.add(1);

        HashMap<String, Object> codeHashMap1 = new HashMap<String, Object>();
        codeHashMap1.put("rgba", rgbaColor1);
        codeHashMap1.put("hex", "#000");

        HashMap<String, Object> colorHashMap1 = new HashMap<String, Object>();
        colorHashMap1.put("color", "black");
        colorHashMap1.put("category", "hue");
        colorHashMap1.put("type", "primary");
        colorHashMap1.put("code", codeHashMap1);

        List<Integer> rgbaColor2 = new ArrayList<Integer>();
        rgbaColor2.add(0);
        rgbaColor2.add(0);
        rgbaColor2.add(0);
        rgbaColor2.add(1);

        HashMap<String, Object> codeHashMap2 = new HashMap<String, Object>();
        codeHashMap2.put("rgba", rgbaColor2);
        codeHashMap2.put("hex", "#FFF");

        HashMap<String, Object> colorHashMap2 = new HashMap<String, Object>();
        colorHashMap2.put("color", "white");
        colorHashMap2.put("category", "value");
        colorHashMap2.put("code", codeHashMap2);

        List<Object> colors = new ArrayList<Object>();
        colors.add(colorHashMap1);
        colors.add(colorHashMap2);

        HashMap<String, Object> colorsMain = new HashMap<String, Object>();
        colorsMain.put("colors", colors);

        given().
                body(colorsMain).
        when().
                post("/postComplexJsonAssignment").
        then().
                spec(customResponseSpecification).
                assertThat().body("msg", equalTo("Success"));
    }
}
