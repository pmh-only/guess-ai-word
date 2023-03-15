package codes.pmh.school.spring.guessaiword.util;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;

import java.security.Key;

public class JWEEncryptor {
    private static JWEEncryptor instance;

    private Key key;

    private JWEEncryptor () {
        this.key = new AesKey(ByteUtil.randomBytes(32));
    }

    public static JWEEncryptor getInstance () {
        if (JWEEncryptor.instance == null)
            JWEEncryptor.instance = new JWEEncryptor();

        return JWEEncryptor.instance;
    }

    public JsonWebEncryption encrypt (String payload) {
        JsonWebEncryption jwe = new JsonWebEncryption();

        jwe.setPayload(payload);
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A256GCMKW);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);
        jwe.setKey(key);

        return jwe;
    }

    public String decrypt (String serializedJwe) throws JoseException {
        JsonWebEncryption jwe = new JsonWebEncryption();

        jwe.setAlgorithmConstraints(
                new AlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.PERMIT,
                        KeyManagementAlgorithmIdentifiers.A256GCMKW));

        jwe.setContentEncryptionAlgorithmConstraints(
                new AlgorithmConstraints(
                    AlgorithmConstraints.ConstraintType.PERMIT,
                    ContentEncryptionAlgorithmIdentifiers.AES_256_GCM));

        jwe.setKey(key);
        jwe.setCompactSerialization(serializedJwe);

        return jwe.getPayload();
    }
}
