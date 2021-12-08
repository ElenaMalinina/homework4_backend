package imgur;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import malinina.dto.PostImageResponse;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static malinina.Endpoints.*;
import static org.hamcrest.Matchers.equalTo;

public class ImageTests extends BaseTest {
    String uploadedImageId;

    @BeforeEach
    void beforeTest() {
        byte[] byteArray = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(byteArray);

        base64MultiPartSpec = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();

        requestSpecUploadImageBase64 = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .build();

        uploadFileImageBase64ResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(PositiveResponseSpecification)
                .expectBody("data.type", equalTo("image/jpeg"))
                .build();

        requestSpecDeleteImageBase64 = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addMultiPart(base64MultiPartSpec)
                .build();

        deleteFileImageBase64ResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectContentType(ContentType.JSON)
                .expectBody("data", equalTo(true))
                .expectBody("success", equalTo(true))
                .expectStatusCode(200)
                .build();
    }
    @Test
    void uploadFileImage() {
        given(requestSpecUploadImage)
                .post(UPLOAD).prettyPeek().then()
                .spec(uploadFileImageResponseSpecification)
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData();
    }

    @Test
    void uploadFileImage12MBTests() {
        given(requestSpecUploadImage12MB)
                .post(UPLOAD).prettyPeek().then()
                .spec(uploadFileImage12MBResponseSpecification)
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData();
    }

    @Test
    void uploadFileGIFTests() {
        given(requestSpecUploadImageGIF)
                .post(UPLOAD).prettyPeek().then()
                .spec(uploadFileImageGIFResponseSpecification)
                .extract().body().as(PostImageResponse.class);
    }

    @Test
    void uploadFileVideoToImageTests() {
        given(requestSpecUploadImageVideo)
                .post(UPLOAD).prettyPeek().then()
                .spec(uploadFileImageVideoResponseSpecification)
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData();
    }


    @Test
    void uploadFileMP3ToImageTests() {
        given(requestSpecUploadImageMP3)
                .post(UPLOAD).prettyPeek().then()
                .spec(negative417ResponseSpecification)
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData();
    }
    @Test
    void uploadFileBase64Test() {
        uploadedImageId = given(requestSpecUploadImageBase64)
                .post(UPLOAD_BASE64).prettyPeek().then()
                .spec(uploadFileImageBase64ResponseSpecification)
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();
    }

    @AfterEach
    void tearDown() {
        given(requestSpecDeleteImageBase64)
                .delete(GET_DELETE, username, uploadedImageId).prettyPeek().then()
                .spec(deleteFileImageBase64ResponseSpecification)
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData();
    }
    private byte[] getFileContent() {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}