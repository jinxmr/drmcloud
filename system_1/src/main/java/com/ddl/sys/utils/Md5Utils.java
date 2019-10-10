package com.ddl.sys.utils;

import java.security.MessageDigest;

public class Md5Utils {

	private static final String ALGORITHM = "MD5";
	private static MessageDigest md;

	private Md5Utils() {
	}

	private static char[] hexDump(byte[] src) {
		char[] buf = new char[src.length * 2];
		for (int b = 0; b < src.length; b++) {
			String byt = Integer.toHexString(src[b] & 0xFF);
			if (byt.length() < 2) {
				buf[(b * 2 + 0)] = '0';
				buf[(b * 2 + 1)] = byt.charAt(0);
			} else {
				buf[(b * 2 + 0)] = byt.charAt(0);
				buf[(b * 2 + 1)] = byt.charAt(1);
			}
		}

		return buf;
	}

	public static void smudge(char[] pwd) {
		if (pwd != null) {
			for (int b = 0; b < pwd.length; b++) {
				pwd[b] = '\000';
			}
		}
	}

	public static void smudge(byte[] pwd) {
		if (pwd != null) {
			for (int b = 0; b < pwd.length; b++) {
				pwd[b] = 0;
			}
		}
	}

	public static char[] cryptPassword(char[] pwd) throws Exception {
		if (md == null)
			md = MessageDigest.getInstance("MD5");
		md.reset();
		byte[] pwdb = new byte[pwd.length];
		for (int b = 0; b < pwd.length; b++) {
			pwdb[b] = ((byte) pwd[b]);
		}
		char[] crypt = hexDump(md.digest(pwdb));
		smudge(pwdb);
		return crypt;
	}

	public static String getMD5Str(String password) {
		char[] pw = new char[password.length()];
		for (int i = 0; i < pw.length; i++) {
			pw[i] = password.charAt(i);
		}
		try {
			return new String(cryptPassword(pw));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
