package codes.pmh.school.spring.guessaiword.util;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;

import java.security.Key;

public class JWSSigner {
    private static JWSSigner instance;

    private final Key key;

    private JWSSigner() {
        this.key = new HmacKey(ByteUtil.randomBytes(64));
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public static JWSSigner getInstance () {
        if (JWSSigner.instance == null)
            JWSSigner.instance = new JWSSigner();

        return JWSSigner.instance;
    }

    public JsonWebSignature sign (String payload) {
        JsonWebSignature jws = new JsonWebSignature();

        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA512);
        jws.setPayload(payload);
        jws.setKey(key);

        return jws;
    }

    public String verify (String serializedJws) throws JoseException {
        JsonWebSignature jws = new JsonWebSignature();

        jws.setKey(key);
        jws.setAlgorithmConstraints(new AlgorithmConstraints(
                AlgorithmConstraints.ConstraintType.PERMIT,
                AlgorithmIdentifiers.HMAC_SHA512));

        jws.setCompactSerialization(serializedJws);

        return jws.getPayload();
    }
}
