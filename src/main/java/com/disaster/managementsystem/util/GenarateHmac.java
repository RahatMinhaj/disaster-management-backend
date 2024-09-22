package com.disaster.managementsystem.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class GenarateHmac {
    public static String hmacSha256(String key, String data)
      throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
    sha256_HMAC.init(secret_key);
    return ByteToHex.bytesToHex(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
  }
}
