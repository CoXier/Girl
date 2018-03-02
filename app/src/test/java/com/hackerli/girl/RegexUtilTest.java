package com.hackerli.girl;

import com.hackerli.girl.util.RegexUtil;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by lijianxin on 2018/3/2.
 */

public class RegexUtilTest {
    String testUrl = "https://github.com/SwiftyWang/AnimatePlayButton/";

    @Test
    public void parseGithubUrl() throws Exception {
        String user = RegexUtil.parse("github.com/([^/]+)/", testUrl, 1);
        assertEquals(user, "SwiftyWang");
    }
}
