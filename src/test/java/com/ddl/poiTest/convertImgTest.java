package com.ddl.poiTest;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class convertImgTest {

    public static void main(String[] args) {
        String newPassword = new SimpleHash("md5", "111111",
                ByteSource.Util.bytes("admin"), 2).toHex();
        System.out.println(newPassword);
    }


}
