package imgur;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.hamcrest.Matchers.*;

public abstract class BaseTest {
    static Properties properties = new Properties();
    static String token;

    static String username;

    public final static String PROPERTIES = "src/main/resources/application.properties";
    public final static String PATH_TO_IMAGE = "src/main/resources/horse.jpg";
    public final static String PATH_TO_IMAGE_12MB = "src/main/resources/photo12MB.jpg";
    public final static String PATH_TO_GIF = "src/main/resources/heart.gif";
    public final static String PATH_TO_MP3 = "src/main/resources/audio.mp3";
    public final static String PATH_TO_VIDEO = "src/main/resources/video.mp4";

    static ResponseSpecification PositiveResponseSpecification;
    static ResponseSpecification positiveAccountResponseSpecification;
    static ResponseSpecification NegativeResponseSpecification;
    static ResponseSpecification negative401ResponseSpecification;
    static ResponseSpecification negative417ResponseSpecification;
    static RequestSpecification requestSpecificationWithAuth;
    static RequestSpecification requestSpecUploadImage;
    static ResponseSpecification uploadFileImageResponseSpecification;
    static RequestSpecification requestSpecUploadImage12MB;
    static ResponseSpecification uploadFileImage12MBResponseSpecification;
    static RequestSpecification requestSpecUploadImageGIF;
    static ResponseSpecification uploadFileImageGIFResponseSpecification;
    static RequestSpecification requestSpecUploadImageVideo;
    static ResponseSpecification uploadFileImageVideoResponseSpecification;
    static RequestSpecification requestSpecUploadImageMP3;
    static RequestSpecification requestSpecUploadImageBase64;
    static ResponseSpecification uploadFileImageBase64ResponseSpecification;
    static RequestSpecification requestSpecDeleteImageBase64;
    static ResponseSpecification deleteFileImageBase64ResponseSpecification;

    static String encodedFile;
    static MultiPartSpecification base64MultiPartSpec;

    @BeforeAll
    static void beforeAll() {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://api.imgur.com/3";
        getProperties();
        token = properties.getProperty("token");
        username = properties.getProperty("username");

        PositiveResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("data.id", is(notNullValue()))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        positiveAccountResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(PositiveResponseSpecification)
                .expectBody("data.url", is(username))
                .expectBody("data.url", is(notNullValue()))
                .build();

        NegativeResponseSpecification = new ResponseSpecBuilder()
                .expectBody("success", equalTo(false))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        negative401ResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(401))
                .expectBody("data.error", equalTo("Authentication required"))
                .addResponseSpecification(NegativeResponseSpecification)
                .expectStatusCode(401)
                .build();

        negative417ResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(417))
                .addResponseSpecification(NegativeResponseSpecification)
                .expectBody(("data.error"), equalTo("Internal expectation failed"))
                .expectStatusCode(417)
                .build();

        requestSpecificationWithAuth = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .build();

        requestSpecUploadImage = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addMultiPart("image", new File(PATH_TO_IMAGE))
                .build();

        uploadFileImageResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(PositiveResponseSpecification)
                .expectBody("data.type", equalTo("image/jpeg"))
                .build();

        requestSpecUploadImage12MB = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addMultiPart("image", new File(PATH_TO_IMAGE_12MB))
                .build();

        uploadFileImage12MBResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(PositiveResponseSpecification)
                .expectBody("data.type", equalTo("image/jpeg"))
                .build();

        requestSpecUploadImageGIF = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addMultiPart("image", new File(PATH_TO_GIF))
                .build();

        uploadFileImageGIFResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(PositiveResponseSpecification)
                .expectBody("data.type", equalTo("image/gif"))
                .build();

        requestSpecUploadImageVideo = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addMultiPart("image", new File(PATH_TO_VIDEO))
                .build();

        uploadFileImageVideoResponseSpecification = new ResponseSpecBuilder()
                .addResponseSpecification(PositiveResponseSpecification)
                .expectBody("data.type", equalTo("video/mp4"))
                .build();


        requestSpecUploadImageMP3 = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addMultiPart("image", new File(PATH_TO_MP3))
                .build();

    }



    private static void getProperties() {
        try (InputStream output = new FileInputStream(PROPERTIES)) {
            properties.load(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}