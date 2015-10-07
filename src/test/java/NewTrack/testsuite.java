package NewTrack;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Cookies;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;


public class testsuite {
    Cookies cookies;

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = "https://gorest.myjetbrains.com/youtrack/rest/";

        Response response =
                given().
            param("login", "oksana.granovska@gmail.com").
            param("password", "GoIT-GoAQA2").
        when().
                post("user/login");

        cookies = response.getDetailedCookies();
    }

    @Test
    public void testCreateNewIssue() throws Exception {
        given().
                cookies(cookies).
                param("project", "GAQA2").
                param("summary", "Sum_summary").
                param("description", "Some desc").
                when().
                put("/issue").
                then().
                statusCode(201);
    }

    private String createtestIssue() throws Exception{
    Response response =
            given().
                    cookies(cookies).
                    param("project", "GAQA2").
                    param("summary", "Sum_summary").
                    param("description", "Some_new_desc").
                    when().
                    put("/issue");

        String location = response.getHeader("Location");
        String issueId = location.substring(location.lastIndexOf('/') + 1);
        return issueId;
    }

    @Test
    public void testDeleteIssue() throws Exception {
        String issueID = createtestIssue();

        given().
                cookies(cookies).
                when().
                delete("/issue/" + issueID).
                then().
                statusCode(200);
    }

    @Test
    public void testIssue() throws Exception {
        String issueID = createtestIssue();
        Response response =
        given().
                cookies(cookies).
                when().
                get("/issue/" + issueID).
        then().
                statusCode(200).
                body("issue id", not(equalTo(issueID))).
                extract().response();

        System.out.print(response.asString());
    }

    @Test
    public void testIssueExists() throws Exception {
        String issueID = createtestIssue();

        given().
                        cookies(cookies).
                when().
                        get("/issue/" + issueID + "/exists").
                        then().
                        statusCode(200);
    }

    @Test
    public void testIssueExists1() throws Exception {
        String issueID = "12345";

        given().
                cookies(cookies).
                when().
                get("/issue/" + issueID + "/exists").
                then().
                statusCode(200);
    }


}
