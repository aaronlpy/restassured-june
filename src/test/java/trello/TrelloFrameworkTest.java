package trello;

import api.BaseRequest;
import models.HttpMethod;
import org.testng.annotations.Test;

public class TrelloFrameworkTest extends BaseRequest {
    @Test
    public void getCurrentUser() throws Exception {
        response = sendRequest(HttpMethod.GET, "/members/me", null);
        response.then().log().all().statusCode(200);
    }
}
