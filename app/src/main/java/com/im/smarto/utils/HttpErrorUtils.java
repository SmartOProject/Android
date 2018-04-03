package com.im.smarto.utils;

import java.io.IOException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * Created by Anatoly Chernyshev on 02.04.2018.
 */

public class HttpErrorUtils {

    public static String getHttpErrorBody(Throwable e){
        try {
            HttpException er = (HttpException) e;
            return er.response().errorBody().string();
        } catch (IOException e1) {
            e1.printStackTrace();
            return "IOException";
        }
    }
}
