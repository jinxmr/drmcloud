package com.ddl.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordEncryptionUtil {

    /**
     * 用户密码加密
     * @param password 密码
     * @param salt     盐（用户名称）
     * @return
     */
    public static String encryptPassword(String password, String salt) {
        return new SimpleHash("md5", password,
                ByteSource.Util.bytes(salt), 2).toHex();
    }
}
