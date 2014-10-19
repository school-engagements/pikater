package org.pikater.shared.database.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.pikater.shared.logging.database.PikaterDBLogger;

public class Hash {
	public static String getMD5Hash(File file) throws IOException {
		if(file == null)
		{
			throw new NullPointerException("Who has ever heard of feeding nulls to a hashing function?");
		}
		else
		{
			String res = null;
			try (FileInputStream fis = new FileInputStream(file)){
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] buf = new byte[2048];
				int s;
				while ((s = fis.read(buf, 0, 2048)) > 0) {
					md.update(buf, 0, s);
				}
				byte[] dig = md.digest();
				res = DatatypeConverter.printHexBinary(dig).toLowerCase();
			} catch (NoSuchAlgorithmException nsae) {
				PikaterDBLogger.logThrowable("Unexpected error occured:", nsae);
			}
			return res;
		}
	}
}