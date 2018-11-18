package com.kmk.imageboard.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@PropertySource("classpath:banklink.yml")
public class BankService {

    @Value("${VK_SERVICE}")
    private String VK_SERVICE;
    @Value("${VK_VERSION}")
    private String VK_VERSION;
    @Value("${VK_SND_ID}")
    private String VK_SND_ID;
    @Value("${VK_STAMP}")
    private String VK_STAMP;
    @Value("${VK_AMOUNT}")
    private String VK_AMOUNT;
    @Value("${VK_CURR}")
    private String VK_CURR;
    @Value("${VK_ACC}")
    private String VK_ACC;
    @Value("${VK_NAME}")
    private String VK_NAME;
    @Value("${VK_REF}")
    private String VK_REF;
    @Value("${VK_LANG}")
    private String VK_LANG;
    @Value("${VK_MSG}")
    private String VK_MSG;
    @Value("${VK_RETURN}")
    private String VK_RETURN;
    @Value("${VK_CANCEL}")
    private String VK_CANCEL;
    @Value("${VK_ENCODING}")
    private String VK_ENCODING;

    private String VK_DATETIME;

    public static String padLeft(String s, int n) {
        return StringUtils.leftPad(s, n, '0');
    }

    public static String currentISODate() {
        TimeZone tz = TimeZone.getTimeZone("GMT");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    public String generateData() {
//        String VK_DATETIME = "2018-11-18T19:37:16+0000";
        VK_DATETIME = currentISODate();

        StringBuilder sb = new StringBuilder();
        sb.append(padLeft(String.valueOf(VK_SERVICE.length()), 3));
        sb.append(VK_SERVICE);
        sb.append(padLeft(String.valueOf(VK_VERSION.length()), 3));
        sb.append(VK_VERSION);
        sb.append(padLeft(String.valueOf(VK_SND_ID.length()), 3));
        sb.append(VK_SND_ID);
        sb.append(padLeft(String.valueOf(VK_STAMP.length()), 3));
        sb.append(VK_STAMP);
        sb.append(padLeft(String.valueOf(VK_AMOUNT.length()), 3));
        sb.append(VK_AMOUNT);
        sb.append(padLeft(String.valueOf(VK_CURR.length()), 3));
        sb.append(VK_CURR);
        sb.append(padLeft(String.valueOf(VK_ACC.length()), 3));
        sb.append(VK_ACC);
        sb.append(padLeft(String.valueOf(VK_NAME.length()), 3));
        sb.append(VK_NAME);
        sb.append(padLeft(String.valueOf(VK_REF.length()), 3));
        sb.append(VK_REF);
        sb.append(padLeft(String.valueOf(VK_MSG.length()), 3));
        sb.append(VK_MSG);
        sb.append(padLeft(String.valueOf(VK_RETURN.length()), 3));
        sb.append(VK_RETURN);
        sb.append(padLeft(String.valueOf(VK_CANCEL.length()), 3));
        sb.append(VK_CANCEL);
        sb.append(padLeft(String.valueOf(VK_DATETIME.length()), 3));
        sb.append(VK_DATETIME);
        System.out.println(sb.toString());
        return sb.toString();
    }



    public String sign(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA1withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return Base64.getEncoder().encodeToString(signature);
    }


    public PrivateKey privateKeyFromString() throws Exception {

        // use "openssl pkcs8 -topk8 -inform PEM -outform PEM -in priv1.pem -out priv8.pem -nocrypt" to make pangalink.net pkcs1 -> pkcs8

        String privateKeyContent = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("priv8.pem").toURI())));
        privateKeyContent = privateKeyContent.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");

        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
        return kf.generatePrivate(keySpecPKCS8);
    }

    public String getVK_SERVICE() {
        return VK_SERVICE;
    }

    public String getVK_VERSION() {
        return VK_VERSION;
    }

    public String getVK_SND_ID() {
        return VK_SND_ID;
    }

    public String getVK_STAMP() {
        return VK_STAMP;
    }

    public String getVK_AMOUNT() {
        return VK_AMOUNT;
    }

    public String getVK_CURR() {
        return VK_CURR;
    }

    public String getVK_ACC() {
        return VK_ACC;
    }

    public String getVK_NAME() {
        return VK_NAME;
    }

    public String getVK_REF() {
        return VK_REF;
    }

    public String getVK_LANG() {
        return VK_LANG;
    }

    public String getVK_MSG() {
        return VK_MSG;
    }

    public String getVK_RETURN() {
        return VK_RETURN;
    }

    public String getVK_CANCEL() {
        return VK_CANCEL;
    }

    public String getVK_ENCODING() {
        return VK_ENCODING;
    }

    public String getVK_DATETIME() {
        return VK_DATETIME;
    }
}
