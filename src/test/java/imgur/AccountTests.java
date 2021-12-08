package imgur;

import malinina.dto.PostImageResponse;
import org.junit.jupiter.api.Test;

import static malinina.Endpoints.GET_ACCOUNT;
import static io.restassured.RestAssured.given;

class AccountTests extends BaseTest {

    @Test
    void getAccountInfoTest() {
        given(requestSpecificationWithAuth)
                .get(GET_ACCOUNT, username).prettyPeek().then()
                .spec(positiveAccountResponseSpecification)
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData();
    }

    @Test
    void getAccountInfoWithoutTokenTest() {
        given()
                .get(GET_ACCOUNT, username).prettyPeek().then()
                .spec(negative401ResponseSpecification)
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData();
    }
}
