package org.carl.auth.utils;

import io.smallrye.jwt.util.KeyUtils;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class EncryptionUtils {
  static Cipher encryptCipher;
  static PrivateKey privateKey;
  static PublicKey publicKey;

  static {
    try {
      privateKey = KeyUtils.readPrivateKey("/privateKey.pem");
    } catch (IOException | GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
    try {
      publicKey = KeyUtils.readPublicKey("/publicKey.pem");
    } catch (IOException | GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
    try {
      encryptCipher = Cipher.getInstance("RSA");
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new RuntimeException(e);
    }
    try {
      encryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);
    } catch (InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  public static String encrypt(String plainText) {

    byte[] cipherText;
    try {
      cipherText = encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    } catch (IllegalBlockSizeException | BadPaddingException e) {
      throw new RuntimeException(e);
    }

    return Base64.getEncoder().encodeToString(cipherText);
  }

  public static String passwordEncoder(String password) {
    return EncryptionUtils.encrypt(password);
  }
}
