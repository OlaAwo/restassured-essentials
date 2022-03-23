package authentication.formbased;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static java.util.Arrays.asList;
import static org.hamcrest.core.IsEqual.equalTo;

public class FormAuthentication {

    /*
    To execute:
    1) open CMD
    2) navigate to target folder
    3) run: java -jar springboot-security-forms-0.0.1-SNAPSHOT
     */

    @BeforeClass
    public void beforeClass(){
        RestAssured.requestSpecification = new RequestSpecBuilder().
                setRelaxedHTTPSValidation().
                setBaseUri("https://localhost:8443").
                build();
    }

    @Test
    public void form_authentication_using_csrf_token(){
        SessionFilter filter = new SessionFilter(); // used to capture session ID

        given().
                auth().form("dan", "dan123", new FormAuthConfig
                ("/signin", "txtUsername", "txtPassword").withAutoDetectionOfCsrf()). // get this info from dev tools
                filter(filter). // stores the session ID
                log().all().
        when().
                get("/login").
        then().
                log().all().
                assertThat().statusCode(200);

        System.out.println("Session ID is " + filter.getSessionId());

        given().
                sessionId(filter.getSessionId()). // pass the session ID
                log().all().
        when().
                get("/profile/index").
        then().
                log().all().
                assertThat().statusCode(200).
                body("html.body.div.p", equalTo("This is User Profile\\Index. Only authenticated people can see this"));
    }

    @Test
    public void form_authentication_using_cookie(){
        SessionFilter filter = new SessionFilter();

        given().
                auth().form("dan", "dan123", new FormAuthConfig
                ("/signin", "txtUsername", "txtPassword").withAutoDetectionOfCsrf()).
                filter(filter).
                log().all().
        when().
                get("/login").
        then().
                log().all().
                assertThat().statusCode(200);

        System.out.println("Session ID is " + filter.getSessionId());

        given().
                cookie("JSESSIONID", filter.getSessionId()). // provide the cookie name "JSESSIONID"
                log().all().
        when().
                get("/profile/index").
        then().
                log().all().
                assertThat().statusCode(200).
                body("html.body.div.p", equalTo("This is User Profile\\Index. Only authenticated people can see this"));
    }

    @Test
    public void form_authentication_using_cookie_builder(){
        SessionFilter filter = new SessionFilter();

        given().
                auth().form("dan", "dan123", new FormAuthConfig
                        ("/signin", "txtUsername", "txtPassword").withAutoDetectionOfCsrf()).
                filter(filter).
                log().all().
        when().
                get("/login").
        then().
                log().all().
                assertThat().statusCode(200);

        System.out.println("Session ID is " + filter.getSessionId());

        Cookie cookie = new Cookie.Builder("JSESSIONID", filter.getSessionId()).
                setSecured(true). // added attributes
                setHttpOnly(true). // added attributes
                setComment("my cookie").build(); // added attributes

        given().
                cookie(cookie).
                log().all().
        when().
                get("/profile/index").
        then().
                log().all().
                assertThat().statusCode(200).
                body("html.body.div.p", equalTo("This is User Profile\\Index. Only authenticated people can see this"));
    }

    @Test
    public void form_authentication_using_multiple_cookies(){
        SessionFilter filter = new SessionFilter();

        given().
                auth().form("dan", "dan123", new FormAuthConfig
                        ("/signin", "txtUsername", "txtPassword").withAutoDetectionOfCsrf()).
                filter(filter).
                log().all().
        when().
                get("/login").
        then().
                log().all().
                assertThat().statusCode(200);

        System.out.println("Session ID is " + filter.getSessionId());

        Cookie cookie = new Cookie.Builder("JSESSIONID", filter.getSessionId()).
                setSecured(true).
                setHttpOnly(true).
                setComment("my cookie").build();

        Cookie cookie1 = new Cookie.Builder("dummy", "dummy value").build();

        Cookies cookies = new Cookies(cookie, cookie1);

        given().
                cookies(cookies).
                log().all().
        when().
                get("/profile/index").
        then().
                log().all().
                assertThat().statusCode(200).
                body("html.body.div.p", equalTo("This is User Profile\\Index. Only authenticated people can see this"));
    }

    @Test
    public void fetch_a_single_cookie(){
        Response response = given().
                log().all().
        when().
                get("/profile/index").
        then().
                log().all().
                assertThat().
                statusCode(200).
                extract().response();

        System.out.println(response.getCookie("JSESSIONID"));
        System.out.println(response.getDetailedCookie("JSESSIONID"));
    }

    @Test
    public void fetch_multiple_cookies(){
        Response response = given().
                log().all().
        when().
                get("/profile/index").
        then().
                log().all().
                assertThat().
                statusCode(200).
                extract().response();

        Map<String, String> cookies =  response.getCookies();

        // loop through all the cookies
        for(Map.Entry<String, String> entry: cookies.entrySet()){
            System.out.println("cookie name = " + entry.getKey()); // get the cookie name
            System.out.println("cookie name = " + entry.getValue()); // get the cookie value
        }

        // get a list of cookies and details (more comprehensive)
        Cookies cookies1 = response.getDetailedCookies();
        List<Cookie> cookieList = cookies1.asList();

        for(Cookie cookie: cookieList){
            System.out.println("cookie = " + cookie.toString());
        }
    }
}
