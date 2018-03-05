package com.hackerli.girl;

import com.hackerli.girl.network.api.BaseUrl;

import org.junit.Test;


/**
 * Created by lijianxin on 2018/3/4.
 */

public class BaseUrlTest {
    @Test
    public void check_baseUrl() {
        try {
            Class c = Class.forName("com.hackerli.girl.network.api.GankIoService");

            if (c.isAnnotationPresent(BaseUrl.class)) {
                BaseUrl baseUrl = (BaseUrl) c.getAnnotation(BaseUrl.class);
                assertEquals("http://gank.io/api/", baseUrl.value());
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
