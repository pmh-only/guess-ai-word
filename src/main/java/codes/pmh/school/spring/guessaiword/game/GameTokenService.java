package codes.pmh.school.spring.guessaiword.game;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service("tokenService")
public class GameTokenService {
    private static final Key TOKEN_SECRET =
            new HmacKey(ByteUtil.randomBytes(64));

    public String sign (String payload) throws JoseException {
        JsonWebSignature jws = new JsonWebSignature();

        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA512);
        jws.setPayload(payload);
        jws.setKey(TOKEN_SECRET);

        return jws.getCompactSerialization();
    }

    public String verify (String serializedJws) throws JoseException {
        JsonWebSignature jws = new JsonWebSignature();

        jws.setKey(TOKEN_SECRET);
        jws.setAlgorithmConstraints(new AlgorithmConstraints(
                AlgorithmConstraints.ConstraintType.PERMIT,
                AlgorithmIdentifiers.HMAC_SHA512));

        jws.setCompactSerialization(serializedJws);

        return jws.getPayload();
    }
}
