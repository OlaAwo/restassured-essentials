package queryparameters;

import io.restassured.config.EncoderConfig;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

public class RequestParameters {

    @Test
    public void single_query_parameter() {
        given().
                baseUri("https://postman-echo.com").
                queryParam("foo1", "bar1").
                log().all().
        when().
                get("/get").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void multiple_query_parameters() {
        HashMap<String, String> queryParam = new HashMap<String, String>();
        queryParam.put("foo1", "bar1");
        queryParam.put("foo2", "bar2");

        given().
                baseUri("https://postman-echo.com").
                queryParams(queryParam).
                log().all().
        when().
                get("/get").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void multiple_value_query_parameter() {

        given().
                baseUri("https://postman-echo.com").
                queryParam("foo1", "bar1", "bar2", "bar3").
                log().all().
        when().
                get("/get").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void path_parameter() {
        given().
                baseUri("https://reqres.in").
                pathParam("user", "2").
                log().all().
        when().
                get("/api/users/{user}").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void multipart_form_data() {
        given().
                baseUri("https://postman-echo.com").
                multiPart("foo1", "bar1").
                multiPart("foo2", "bar2").
                log().all().
        when().
                post("/post").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void upload_file_multipart_form_data() {
        String attributes = "{\"name\":\"sample.txt\",\"parent\":{\"id\":\"12345\"}}";

        given().
                baseUri("https://postman-echo.com").
                multiPart("file", new File("sample.txt")).
                multiPart("attributes", attributes, "application/json").
                log().all().
        when().
                post("/post").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void download_file_multipart_form_data() throws IOException {

        byte[] bytes = given().
                baseUri("https://raw.githubusercontent.com").
                log().all().
        when().
                get("/appium/appium/master/sample-code/apps/ApiDemos-debug.apk").
        then().
                log().all().
                extract().response().asByteArray();

        OutputStream os = new FileOutputStream(new File("ApiDemos-debug.apk"));
        os.write(bytes);
        os.close();
    }

    @Test
    public void form_urlencoded() {
        given().
                baseUri("https://postman-echo.com").
                config(config().encoderConfig(EncoderConfig.encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false))).
                formParam("key1", "value1").
                formParam("key 2", "value 2").
                log().all().
        when().
                post("/post").
        then().
                log().all().
                assertThat().statusCode(200);
    }
}
