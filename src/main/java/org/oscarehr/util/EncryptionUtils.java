/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.util;

import org.apache.logging.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.Objects;


public final class EncryptionUtils extends PasswordHash {
    private static final QueueCacheValueCloner<byte[]> byteArrayCloner = new QueueCacheValueCloner<byte[]>() {
        public byte[] cloneBean(byte[] original) {
            return (byte[])original.clone();
        }
    };
    private static Logger logger = MiscUtils.getLogger();
    private static final MessageDigest messageDigest = initMessageDigest();
    private static final QueueCache<String, byte[]> sha1Cache;
    private static final int MAX_SHA_KEY_CACHE_SIZE = 2048;
    public static final String SECRET_KEY_ENV_VAR = "encryption.util.secret.key";
    private static SecretKeySpec SECRET_KEY_SPEC;
    private static final String ENCRYPTION_PREFIX = "{ENC}";

    public EncryptionUtils() {
    }

    private static MessageDigest initMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException var1) {
            logger.error("Error", var1);
            return null;
        }
    }

    /**
     * @deprecated
     * weak: do not use for generating password hashes.
     * use the hash(String password) method below.
     */
    @Deprecated
    public static byte[] getSha1(String s) {
        byte[] b = (byte[])sha1Cache.get(s);
        if (b == null) {
            b = getSha1NoCache(s);
            if (s.length() < 2048) {
                sha1Cache.put(s, b);
            }
        }

        return b;
    }

    /**
     * @deprecated
     * weak: do not use for generating password hashes.
     * use the hash(String password) method below.
     */
    @Deprecated
    private static byte[] getSha1NoCache(String s) {
        if (s == null) {
            return null;
        } else {
            try {
                synchronized(Objects.requireNonNull(messageDigest)) {
                    return messageDigest.digest(s.getBytes("UTF-8"));
                }
            } catch (Exception var4) {
                logger.error("Unexpected error.", var4);
                return null;
            }
        }
    }

    /**
     * uses Javax.crypto utils to generate a random AES key.
     * Considered a weak method.
     */
    public static SecretKey generateEncryptionKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    /**
     * uses Javax.crypto utils to generate a secret key spec
     * Considered a weak method.
     */
    public static SecretKeySpec generateEncryptionKey(String seed) {
        byte[] sha1 = getSha1(seed);
        return new SecretKeySpec(sha1, 0, 16, "AES");
    }

    /**
     * Use only to store encrypted data.
     * Do NOT use for authentication.
     * Considered a weak encryption.
     */
    public static byte[] encrypt(SecretKey secretKey, byte[] plainData) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        if (secretKey == null) {
            return plainData;
        } else {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(1, secretKey);
            return cipher.doFinal(plainData);
        }
    }

    /**
     * Use only to store encrypted data.
     * Do NOT use for authentication.
     * Considered a weak encryption.
     */
    public static byte[] decrypt(SecretKey secretKey, byte[] encryptedData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (secretKey == null) {
            return encryptedData;
        } else {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2, secretKey);
            return cipher.doFinal(encryptedData);
        }
    }


    /**
     * Encrypts a plain text string.
     *
     * @param plainText The plain text string to encrypt.
     * @return The encrypted string, prefixed with an encryption marker and encoded in Base64.
     * @throws Exception If the secret key is not initialized, or if there is an error during encryption.
     */
    public static String encrypt(String plainText) throws Exception {

        /*
         * null will fail and empty string will be encrypted as a valid password.
         * Exit this method in these cases.
         */
        if (plainText == null || plainText.isEmpty()) {
            return plainText;
        }

        if (Objects.isNull(SECRET_KEY_SPEC)) {
            throw new Exception("Secret key not found in environment variables.");
        }

        byte[] iv = new byte[12];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY_SPEC, gcmSpec);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encryptedBytes.length);
        byteBuffer.put(iv);
        byteBuffer.put(encryptedBytes);

        // Return the encrypted string with a prefix, encoded in Base64
        return ENCRYPTION_PREFIX + Base64.getEncoder().encodeToString(byteBuffer.array());
    }

    /**
     * Decrypts an encrypted string.
     *
     * @param encryptedText The encrypted string to decrypt.
     * @return The decrypted string, or the original string if it was not encrypted.
     * @throws Exception If the secret key is not initialized, or if there is an error during decryption.
     */
    public static String decrypt(String encryptedText) throws Exception {
        if (Objects.isNull(SECRET_KEY_SPEC)) {
            throw new Exception("Secret key not found in environment variables.");
        }
        
        /*
         * avoid nulls and empty strings.
         */
        if (encryptedText == null || encryptedText.isEmpty()) {
            return encryptedText;
        }

        if (isEncrypted(encryptedText)) {
            String base64Encoded = encryptedText.substring(ENCRYPTION_PREFIX.length());

            byte[] cipherBytes = Base64.getDecoder().decode(base64Encoded);

            ByteBuffer byteBuffer = ByteBuffer.wrap(cipherBytes);
            byte[] iv = new byte[12];
            byteBuffer.get(iv);

            byte[] encryptedBytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(encryptedBytes);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY_SPEC, gcmSpec);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }

        // If the text is not encrypted, return it as-is
        return encryptedText;
    }

    /**
     * Checks if a provided string is encrypted.
     * <p>
     * This method determines if the input string starts with the encryption prefix,
     * indicating that it has been encrypted.
     *
     * NULL and empty strings are returned as encrypted = true
     *
     * @param input The string to check for encryption.
     * @return True if the string is encrypted (starts with the encryption prefix), False otherwise.
     */
    public static boolean isEncrypted(String input) {
        return input == null || input.isEmpty() || input.startsWith(ENCRYPTION_PREFIX);
    }

    /**
     * Generates a new secret key for encryption/decryption.
     * <p>
     * This method uses the AES algorithm to generate a 256-bit secret key.
     * The generated key is then Base64 encoded for storage and retrieval.
     *
     * @return The Base64 encoded string representation of the generated secret key.
     * @throws NoSuchAlgorithmException If the AES algorithm is not available.
     */
    public static String generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(256, new SecureRandom());
        SecretKey key = keygen.generateKey();

        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * Prepares the SecretKeySpec object used for encryption and decryption.
     * <p>
     * This method retrieves the secret key from the OscarProperties, decodes it from Base64,
     * and initializes the SECRET_KEY_SPEC with the decoded key.
     * If the secret key is not found in the properties, an error is logged.
     */
    public static void prepareSecretKeySpec() {
        String key = oscar.OscarProperties.getInstance().getProperty(SECRET_KEY_ENV_VAR);
        if (Objects.isNull(key)) {
            logger.error("Secret key not found in environment variables.");
            return;
        }

        byte[] keyBytes = Base64.getDecoder().decode(key);
        SECRET_KEY_SPEC = new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * A one way PBKDF2 With Hmac SHA1 hash of given password string.
     * The verify method, should be used to validate the hash against
     * passwords.
     * @param password string
     * @return hashed password
     * @throws CannotPerformOperationException failed encrypt
     */
    public static String hash(String password) throws CannotPerformOperationException {
        return createHash(password);
    }

    /**
     * Validate a given password phrase against the stored hash password.
     * This is a boolean validation. No password values are returned
     * @param password  plain password string
     * @param hashedPassword hashed password string usually stored in the database.
     */
    public static boolean verify(String password, String hashedPassword) throws InvalidHashException, CannotPerformOperationException {
        return verifyPassword(password, hashedPassword);
    }

    static {
        sha1Cache = new QueueCache(4, 2048, byteArrayCloner);
        prepareSecretKeySpec();
    }
}

