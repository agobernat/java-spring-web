package hu.ponte.hr.services;


import org.springframework.stereotype.Service;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Service
public class SignService {


    public String SignByteArray(byte[] dataToSign, byte[] rawPK) throws Exception {

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(rawPK);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        Signature privateSignature = Signature.getInstance("SHA256withRSA");

        privateSignature.initSign(keyFactory.generatePrivate(spec));
        privateSignature.update(dataToSign);
        byte[] s = privateSignature.sign();

        return Base64.getEncoder().encodeToString(s);
    }

    public byte[] ReadPrivateKeyFromFile(String path) {
        byte[] privateKey;
        try (FileInputStream fis = new FileInputStream(path);)
            { privateKey = fis.readAllBytes();}
        catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        return privateKey;
    }
}
