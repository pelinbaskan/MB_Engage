import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.mortbay.jetty.HttpStatus.Accepted;

public class PostScenario {


    public static ExtentReports extent;
    public ExtentTest test;
    public static ExtentHtmlReporter htmlReporter;
    //private final String ContentPath = "/v3/devices/2136046442/events";


    @BeforeTest
    public void _Before() {

        //create extent report
        htmlReporter = new ExtentHtmlReporter("D:\\Users\\porhan\\Documents\\IntellijProjects\\MobileEngage_Automation" + "/report1.html");
        //tried to use user directory but could not create the file
        // htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/MobileEngageTestReport.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Host Name:", "Mobile Engage");

        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setDocumentTitle("Mobile Engage Test Report");
        htmlReporter.config().setReportName("Mobile Engage Test Report");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.STANDARD);
        baseURI = "http://ems-me-deviceevent.herokuapp.com";


    }

    @Test(description = "Receiving Message from MESegment Campaign")
    public void _TC1() throws Exception {
        try {

            test = extent.createTest("Receiving Message from MESegment Campaign");

            //Click details
            Map<String, Object> clickDetails = new HashMap<String, Object>();
            clickDetails.put("button_id", "0");
            clickDetails.put("message_id", "55166");
            clickDetails.put("timestamp", "2018-10-01T17:32:28Z");

            //viewed message details
            Map<String, Object> viewedMessageDetails = new HashMap<String, Object>();
            viewedMessageDetails.put("message_id", "55166");
            viewedMessageDetails.put("timestamp", "2018-10-01T17:32:28Z");

            //event details
            Map<String, Object> eventDetails = new HashMap<String, Object>();
            eventDetails.put("type", "custom");
            eventDetails.put("name", "mesegmentpb");
            eventDetails.put("timestamp", "2018-10-01T17:32:28Z");

            //full request details
            Map<String, Object> requestDetails = new HashMap<String, Object>();
            requestDetails.put("hardware_id", "HWID-MESeg-Blue000");
            requestDetails.put("clicks", clickDetails);
            requestDetails.put("viewed_messages", viewedMessageDetails);
            requestDetails.put("events", eventDetails);


            //create the response
            Response response = given().
                    auth().preemptive().basic("EMS35-14B3E", "YZkKpIlkhg4mrQ52mlSdQQL6Q5h1sKrQ").
                    contentType("application/json; charset=utf-8").
                    header("Host", "ems-me-deviceevent.herokuapp.com").
                    accept("application/json").
                    body(requestDetails).
                    when().
                    post(baseURI + "/v3/devices/2136046442/events").
                    then().
                    contentType("text/plain; charset=utf-8").
                    extract().
                    response();


            int statusCode = response.getStatusCode();
            if (statusCode != 202) {
                test.log(Status.FAIL, "Status code is not 202");
                Assert.assertEquals(statusCode, 202);

            } else {
                test.log(Status.PASS, "Status code is 202");
            }


            String responseString = response.toString();
            //assertNotNull(responseString);
            if (!responseString.contains("MeSegmentPB")) {
                test.log(Status.FAIL, "Message does not received from MeSegmentPB");
            } else {
                test.log(Status.PASS, "Message received from MeSegmentPB");
            }

            System.out.println(response.toString());
        } catch (Exception e) {
            String errorMessage = e.toString();
            test.log(Status.ERROR, "General error on  " + errorMessage);
        }
    }

    @AfterTest
    public void _After() {
        extent.close();
        extent.flush();
    }

}
