package com.ddl.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordEncryptionUtil {

    public static String encryptPassword(String username, String password, String salt) {
        return new SimpleHash("md5", password,
                ByteSource.Util.bytes(salt), 2).toHex();
    }
}
