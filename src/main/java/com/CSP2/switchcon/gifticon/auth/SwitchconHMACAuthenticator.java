package com.CSP2.switchcon.gifticon.auth;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SwitchconHMACAuthenticator {
    public static String getAuthStringRegister(String barcodeNumber, String serverID, byte[] keyBytes)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException
    {
        Security.addProvider(new BouncyCastleProvider());
        SecureRandom sr = SecureRandom.getInstanceStrong();
        byte[] nonce16 = new byte[16];
        sr.nextBytes(nonce16);
        byte[] barcodeBytes = barcodeNumber.getBytes(StandardCharsets.UTF_8);
        byte[] serverIDBytes = serverID.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "HmacSHA256");
        Mac macBnum = Mac.getInstance("HmacSHA256");
        macBnum.init(key);
        byte[] bNumMac = macBnum.doFinal(barcodeBytes);
        Mac macSID = Mac.getInstance("HmacSHA256");
        macSID.init(key);
        macSID.update(nonce16);
        macSID.update(serverIDBytes);
        byte[] sIDMac = macSID.doFinal();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(nonce16); baos.write(bNumMac); baos.write(sIDMac); baos.write(serverIDBytes);
        byte[] finalValue = baos.toByteArray();
        return new String(Base64.getEncoder().encode(finalValue));
    }
    public static String getAuthStringQRUse(String barcodeNumber, String serverID, byte[] keyBytes)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException
    {
        Security.addProvider(new BouncyCastleProvider());
        long unixTime = System.currentTimeMillis() / 1000;
        SecureRandom sr = SecureRandom.getInstanceStrong();
        byte[] nonce16 = new byte[16];
        sr.nextBytes(nonce16);
        byte[] T =
                {
                        (byte) (unixTime >> 24),
                        (byte) (unixTime >> 16),
                        (byte) (unixTime >> 8),
                        (byte) unixTime
                };
        byte[] barcodeBytes = barcodeNumber.getBytes(StandardCharsets.UTF_8);
        byte[] serverIDBytes = serverID.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "HmacSHA256");
        Mac macBnum = Mac.getInstance("HmacSHA256");
        macBnum.init(key);
        byte[] bNumMac = macBnum.doFinal(barcodeBytes);
        Mac macSID = Mac.getInstance("HmacSHA256");
        macSID.init(key);
        macSID.update(T);
        macSID.update(nonce16);
        macSID.update(serverIDBytes);
        byte[] sIDMac = macSID.doFinal();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(T); baos.write(nonce16);baos.write(bNumMac); baos.write(sIDMac); baos.write(serverIDBytes);
        byte[] finalValue = baos.toByteArray();
        return "swc:" + new String(Base64.getEncoder().encode(finalValue));
    }

}