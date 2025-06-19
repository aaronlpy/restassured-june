package trello;

import api.BaseRequest;
import models.HttpMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.DataManager;

@Listeners(listeners.ExtentReportListener.class)
public class TrelloFrameworkTest extends BaseRequest {
    @Test
    public void getCurrentUser() throws Exception {
        response = sendRequest(HttpMethod.GET, "/members/me", null);
        response.then().log().all().statusCode(200);
        String userId = response.then().extract().jsonPath().get("id");
        DataManager.save("userId", userId);
        System.out.println("UserId was saved successfully in data properties: " + DataManager.get("userId"));
    }
}
