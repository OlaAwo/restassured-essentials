package serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class JacksonAPI_JSONObjectNode {
    private static ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://api.getpostman.com").
                addHeader("X-Api-Key", "")
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL);
        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL);
        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void serialize_json_object_using_jackson() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode nestedObjectNode = objectMapper.createObjectNode();

        nestedObjectNode.put("name", "Yet Another Workspace");
        nestedObjectNode.put("type", "personal");
        nestedObjectNode.put("description", "Test workspace");

        ObjectNode mainObjectNode = objectMapper.createObjectNode();

        mainObjectNode.set("workspace", nestedObjectNode);

        given().
                body(mainObjectNode).
        when().
                post("/workspaces").
        then().
                spec(responseSpecification).
                assertThat().body("workspace.name", equalTo("Yet Another Workspace"),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));
    }
}
