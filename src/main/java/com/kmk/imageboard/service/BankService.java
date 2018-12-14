package com.kmk.imageboard.service;

import net.oauth.signature.pem.PEMReader;
import net.oauth.signature.pem.PKCS1EncodedKeySpec;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
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

    public static String padLeft(String s, int n) {
        return StringUtils.leftPad(s, n, '0');
    }

    public static String currentISODate() {
        TimeZone tz = TimeZone.getTimeZone("GMT");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    public String generateServerSideData() {
        return generateData(VK_SERVICE,
                VK_VERSION,
                VK_SND_ID,
                VK_STAMP,
                VK_AMOUNT,
                VK_CURR,
                VK_ACC,
                VK_NAME,
                VK_REF,
                VK_MSG,
                VK_RETURN,
                VK_CANCEL,
                currentISODate());
    }

    public String generateData(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(padLeft(String.valueOf(string.length()), 3));
            sb.append(string);
        }
        return sb.toString();
    }


    public String sign(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA1withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF_8));
        byte[] signature = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    public boolean verifyResponse(PublicKey publicKey, String signedMessage, String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(publicKey);
        signature.update(message.getBytes());
        return signature.verify(Base64.getDecoder().decode(signedMessage.getBytes()));
    }


    public PrivateKey privateKeyFromResources() throws Exception {
        PEMReader reader = new PEMReader(ClassLoader.getSystemResourceAsStream("user_key.pem"));
        PKCS1EncodedKeySpec keySpecPKCS1 = new PKCS1EncodedKeySpec(reader.getDerBytes());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpecPKCS1.getKeySpec());
    }

    public PublicKey publicKeyFromCertificate() throws Exception {
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) f.generateCertificate(ClassLoader.getSystemResourceAsStream("bank_cert.pem"));
        return certificate.getPublicKey();
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
        return currentISODate();
    }
}
