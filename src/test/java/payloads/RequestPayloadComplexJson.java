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

public class RequestPayloadComplexJson {
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
        List<Integer> batterIDArrayList = new ArrayList<Integer>();
        batterIDArrayList.add(5);
        batterIDArrayList.add(9);

        HashMap<String, Object> batterHashMap1 = new HashMap<String, Object>();
        batterHashMap1.put("id", "1001");
        batterHashMap1.put("type", "Regular");

        HashMap<String, Object> batterHashMap2 = new HashMap<String, Object>();
        batterHashMap2.put("id", batterIDArrayList);
        batterHashMap2.put("type", "Chocolate");

        List<HashMap<String, Object>> batterArrayList = new ArrayList<HashMap<String, Object>>();
        batterArrayList.add(batterHashMap1);
        batterArrayList.add(batterHashMap2);

        HashMap<String, List<HashMap<String, Object>>> battersHashMap =
                new HashMap<String, List<HashMap<String, Object>>>();
        battersHashMap.put("batter", batterArrayList);

        List<String> toppingTypeArrayList = new ArrayList<String>();
        toppingTypeArrayList.add("test1");
        toppingTypeArrayList.add("test2");

        HashMap<String, Object> toppingHashMap1 = new HashMap<String, Object>();
        toppingHashMap1.put("id", "5001");
        toppingHashMap1.put("type", "None");

        HashMap<String, Object> toppingHashMap2 = new HashMap<String, Object>();
        toppingHashMap2.put("id", "5002");
        toppingHashMap2.put("type", toppingTypeArrayList);

        List<HashMap<String, Object>> toppingArrayList = new ArrayList<HashMap<String, Object>>();
        toppingArrayList.add(toppingHashMap1);
        toppingArrayList.add(toppingHashMap2);

        HashMap<String, Object> orderHashMap = new HashMap<String, Object>();
        orderHashMap.put("ppu", 0.55);
        orderHashMap.put("batters", battersHashMap);
        orderHashMap.put("name", "Cake");
        orderHashMap.put("id", "0001");
        orderHashMap.put("type", "donut");
        orderHashMap.put("topping", toppingArrayList);

        given().
                body(orderHashMap).
        when().
                post("/postComplexJson").
        then().
                spec(customResponseSpecification).
                assertThat().body("msg", equalTo("Success"));
    }
}
