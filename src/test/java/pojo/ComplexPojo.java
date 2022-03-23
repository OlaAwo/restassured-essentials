package pojo;

import example.pojo.complex.Workspace;
import example.pojo.complex.WorkspaceRoot;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class ComplexPojo {

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
    public void validate_post_request_with_complex_pojo() {
        Workspace workspace = new Workspace();
        WorkspaceRoot workspaceRoot = new WorkspaceRoot(workspace);

        workspace.setName("Another one");
        workspace.setType("personal");
        workspace.setDescription("Another one");

        WorkspaceRoot deserializePojo = given().
                body(workspaceRoot).
        when().
                post("/workspaces").
        then().
                log().all().
                extract().response().as(WorkspaceRoot.class);
        assertThat(deserializePojo.getWorkspace().getName(), equalTo(workspaceRoot.getWorkspace().getName()));
        assertThat(deserializePojo.getWorkspace().getId(), matchesPattern("^[a-z0-9-]{36}$"));
    }

    @Test (dataProvider = "workspace")
    public void validate_post_request_with_data_provider(String name, String type, String description) {
        Workspace workspace = new Workspace(name, type, description);
        WorkspaceRoot workspaceRoot = new WorkspaceRoot(workspace);

        WorkspaceRoot deserializePojo = given().
                body(workspaceRoot).
                when().post("/workspaces").
                then().log().all().
                extract().response().as(WorkspaceRoot.class);
        assertThat(deserializePojo.getWorkspace().getName(), equalTo(workspaceRoot.getWorkspace().getName()));
        assertThat(deserializePojo.getWorkspace().getId(), matchesPattern("^[a-z0-9-]{36}$"));
    }

    @DataProvider(name = "workspace")
    public Object[][] getWorkspace(){
        return new Object[][]{
                {"myWorkspace2", "personal", "Workspace 2"}
        };
    }

    @DataProvider(name = "workspace2")
    public Object[][] getWorkspace1(){
        // two-dimensional array with two entries
        return new Object[][]{
                {"myWorkspace1", "team", "Workspace 1"},
                {"myWorkspace2", "personal", "Workspace 2"}
        };
    }
}
