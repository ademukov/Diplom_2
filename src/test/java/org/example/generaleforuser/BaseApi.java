package org.example.generaleforuser;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.BeforeClass;

public class BaseApi {

    @BeforeClass
    public static void setupRAStaticParams() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        RestAssured.filters(
                new RequestLoggingFilter(LogDetail.ALL),
                new ResponseLoggingFilter(LogDetail.ALL)
        );
    }
}
