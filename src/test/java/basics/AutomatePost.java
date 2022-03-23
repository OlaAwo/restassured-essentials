package basics;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class AutomatePost {

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
        RestAssured.responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void validate_post_request() {
        String payload = "{\n" +
                "    \"workspace\": {\n" +
                "        \"name\": \"New Workspace\",\n" +
                "        \"type\": \"personal\",\n" +
                "        \"description\": \"Rest Assured Tutorial\"\n" +
                "    }\n" +
                "}";

        given().
                body(payload).
        when().
                post("/workspaces").
        then().
                assertThat().body("workspace.name", equalTo("New Workspace"),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));
    }

    @Test
    public void validate_post_request_non_bdd() {
        String payload = "{\n" +
                "    \"workspace\": {\n" +
                "        \"name\": \"New Workspace1\",\n" +
                "        \"type\": \"personal\",\n" +
                "        \"description\": \"Rest Assured Tutorial\"\n" +
                "    }\n" +
                "}";

        Response response = with().body(payload).
                post("/workspaces");
        assertThat(response.path("workspace.name"), equalTo("New Workspace1"));
        assertThat(response.path("workspace.id"), matchesPattern("^[a-z0-9-]{36}$"));
    }

    @Test
    public void validate_post_request_payload_from_file() {
        File file = new File("src/main/resources/CreateWorkspacePayload.json");

        given().
                body(file).
        when().
                post("/workspaces").
        then().
                assertThat().body("workspace.name", equalTo("New Workspace"),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));
    }

    @Test
    public void validate_post_request_payload_as_map() {
        HashMap<String, Object> mainObject = new HashMap<String, Object>();
        HashMap<String, String> nestedObject = new HashMap<String, String>();
        nestedObject.put("name", "First Workspace");
        nestedObject.put("type", "personal");
        nestedObject.put("description", "Test workspace");

        mainObject.put("workspace", nestedObject);

        given().
                body(mainObject).
        when().post("/workspaces").
        then().
                assertThat().body("workspace.name", equalTo("First Workspace"),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));
    }
}
