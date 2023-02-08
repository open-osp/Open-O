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

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public final class EncryptionUtils {
    private static final QueueCacheValueCloner<byte[]> byteArrayCloner = new QueueCacheValueCloner<byte[]>() {
        public byte[] cloneBean(byte[] original) {
            return (byte[])original.clone();
        }
    };
    private static Logger logger = MiscUtils.getLogger();
    private static final MessageDigest messageDigest = initMessageDigest();
    private static final QueueCache<String, byte[]> sha1Cache;
    private static final int MAX_SHA_KEY_CACHE_SIZE = 2048;

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

    protected static byte[] getSha1NoCache(String s) {
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

    public static SecretKey generateEncryptionKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;
    }

    public static SecretKeySpec generateEncryptionKey(String seed) {
        byte[] sha1 = getSha1(seed);
        SecretKeySpec secretKey = new SecretKeySpec(sha1, 0, 16, "AES");
        return secretKey;
    }

    public static byte[] encrypt(SecretKey secretKey, byte[] plainData) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        if (secretKey == null) {
            return plainData;
        } else {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(1, secretKey);
            byte[] results = cipher.doFinal(plainData);
            return results;
        }
    }

    public static byte[] decrypt(SecretKey secretKey, byte[] encryptedData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (secretKey == null) {
            return encryptedData;
        } else {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2, secretKey);
            byte[] results = cipher.doFinal(encryptedData);
            return results;
        }
    }

    static {
        sha1Cache = new QueueCache(4, 2048, byteArrayCloner);
    }
}

