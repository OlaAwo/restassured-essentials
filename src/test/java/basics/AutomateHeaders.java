package basics;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;

public class AutomateHeaders {
    @Test
    public void add_multiple_headers() {
        given().
                baseUri("https://96f5af29-388f-4d2e-93b1-cd291b588a3f.mock.pstmn.io").
                header("Header-Value1", "value1").header("x-mock-match-request-headers", "Header-Value2").
        when().
                get("/get").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void add_multiple_headers_with_the_headers_class() {
        Header header = new Header("Header-Value1", "value1");
        Header header2 = new Header("x-mock-match-request-headers", "Header-Value2");
        Headers headers = new Headers(header, header2);

        given().
                baseUri("https://96f5af29-388f-4d2e-93b1-cd291b588a3f.mock.pstmn.io").
                headers(headers).
        when().
                get("/get").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void add_multiple_headers_with_Hashmap() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Header-Value2", "value2");
        headers.put("x-mock-match-request-headers", "Header-Value2");

        given().
                baseUri("https://96f5af29-388f-4d2e-93b1-cd291b588a3f.mock.pstmn.io").
                headers(headers).
        when().
                get("/get").
        then().
                log().all().
                assertThat().statusCode(200);
    }

    @Test
    public void request_with_multivalue_header() {
        Header header1 = new Header("multivalueHeader", "value1"); // same header name
        Header header2 = new Header("multivalueHeader", "value1"); // same header name

        Headers headers = new Headers(header1, header2);

        given().
                baseUri("https://96f5af29-388f-4d2e-93b1-cd291b588a3f.mock.pstmn.io").
                headers(headers).
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void assertion_on_response_headers() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Header-Value2", "value2");
        headers.put("x-mock-match-request-headers", "Header-Value2");

        given().
                baseUri("https://96f5af29-388f-4d2e-93b1-cd291b588a3f.mock.pstmn.io").
                headers(headers).
        when().
                get("/get").
        then().
                log().all().
                assertThat().statusCode(200).
                headers("Response-Header", "resValue2",
                        "Content-Type", "application/json; charset=utf-8"); // expected key & value
    }

    @Test
    public void extract_response_headers() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Header-Value2", "value2");
        headers.put("x-mock-match-request-headers", "Header-Value2");

        Headers headerResponse = given(). // use header class
                baseUri("https://96f5af29-388f-4d2e-93b1-cd291b588a3f.mock.pstmn.io").
                headers(headers).
        when().
                get("/get").
        then().
                assertThat().statusCode(200).
                extract().headers(); // extract headers method

        // print all headers
        for (Header header : headerResponse) {
            System.out.print("Header name: " + header.getName() + ", ");
            System.out.println("Header value: " + header.getValue());
        }

        /* get individual header name + value
        System.out.println("*************************************************************");
        System.out.println("Header name: " + headerResponse.get("Response-Header").getName());
        System.out.println("Header value: " + headerResponse.get("Response-Header").getValue());
        System.out.println("Header value: " + headerResponse.getValue("Response-Header"));
         */
    }

    @Test
    public void extract_multivalue_response_header() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Header-Value1", "value1");
        headers.put("x-mock-match-request-headers", "Header-Value1");

        Headers headerResponse = given().
                baseUri("https://96f5af29-388f-4d2e-93b1-cd291b588a3f.mock.pstmn.io").
                headers(headers).
        when().
                get("/get").
        then().
                assertThat().statusCode(200).
                extract().headers();

        List<String> values = headerResponse.getValues("Multi-Response-Header");
        for (String value : values) {
            System.out.println(value);
        }
    }
}
