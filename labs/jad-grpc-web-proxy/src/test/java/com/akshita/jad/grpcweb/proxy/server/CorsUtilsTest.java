package com.akshita.jad.grpcweb.proxy.server;

import com.akshita.jad.grpcweb.proxy.CorsUtils;
import io.netty.handler.codec.http.*;
import org.junit.Test;


public class CorsUtilsTest {

    @Test
    public void test(){
        DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        CorsUtils.updateCorsHeader(response.headers());
        System.out.println(response.headers());
    }
}
