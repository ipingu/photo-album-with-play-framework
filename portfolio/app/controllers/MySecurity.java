package controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.jce.provider.JCEMac.MD5;

import play.Logger;
import play.Play;
import play.libs.Crypto;

public class MySecurity extends Secure.Security {

	static boolean authentify(String username, String password) {
		String cryptedPassword = Crypto.passwordHash(password);

		return Play.configuration.getProperty("admin.login").equals(username) 
			&& Play.configuration.getProperty("admin.password").equals(cryptedPassword);
	}
}
