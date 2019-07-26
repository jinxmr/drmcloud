package com.ddl.poiTest;

import org.apache.shiro.crypto.hash.SimpleHash;

public class convertImgTest {

    public static void main(String[] args) {
        SimpleHash sh = new SimpleHash("MD5", "111111", "df9cfe", 2);
        System.out.println(sh.toString());
    }

}
