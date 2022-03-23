package logging;

import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import static io.restassured.RestAssured.given;

public class Filters {
    @Test
    public void loggingFilters() {
        given().
                baseUri("https://postman-echo.com").
                filter(new RequestLoggingFilter(LogDetail.HEADERS)).
                filter(new ResponseLoggingFilter(LogDetail.STATUS)).
        when().
                get("/get").
        then().
                assertThat().statusCode(200);
    }

    @Test
    public void log_to_file() throws FileNotFoundException {
        PrintStream FileOutPutStream = new PrintStream("restAssured.log");

        given().
                baseUri("https://postman-echo.com").
                filter(new RequestLoggingFilter(FileOutPutStream)). // file output stream
                filter(new ResponseLoggingFilter(FileOutPutStream)). // file output stream
        when().
                get("/get").
        then().
                assertThat().statusCode(200);
    }

    @Test
    public void log_to_file_details() throws FileNotFoundException {
        PrintStream FileOutPutStream = new PrintStream("restAssured.log");

        given().
                baseUri("https://postman-echo.com").
                filter(new RequestLoggingFilter(LogDetail.BODY, FileOutPutStream)). // log specific details
                filter(new ResponseLoggingFilter(LogDetail.STATUS, FileOutPutStream)). // log specific details
        when().
                get("/get").
        then().
                assertThat().statusCode(200);
    }

}
