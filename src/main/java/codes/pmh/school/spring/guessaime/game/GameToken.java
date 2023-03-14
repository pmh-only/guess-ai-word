package codes.pmh.school.spring.guessaime.game;

import codes.pmh.school.spring.guessaime.ai.AIAskResult;
import codes.pmh.school.spring.guessaime.util.JWEEncryptor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.lang.JoseException;

import java.util.Date;
import java.util.List;

public class GameToken {
    private Date gameCreatedAt = new Date();

    private List<AIAskResult> askResults;

    private int nextWordIndex = 0;

    private int nextQuestionIndex = 0;

    public Date getGameCreatedAt() {
        return gameCreatedAt;
    }

    public void setGameCreatedAt(Date gameCreatedAt) {
        this.gameCreatedAt = gameCreatedAt;
    }

    public List<AIAskResult> getAskResults() {
        return askResults;
    }

    public void setAskResults(List<AIAskResult> askResults) {
        this.askResults = askResults;
    }

    public int getNextWordIndex() {
        return nextWordIndex;
    }

    public void setNextWordIndex(int nextWordIndex) {
        this.nextWordIndex = nextWordIndex;
    }

    public int getNextQuestionIndex() {
        return nextQuestionIndex;
    }

    public void setNextQuestionIndex(int nextQuestionIndex) {
        this.nextQuestionIndex = nextQuestionIndex;
    }

    public static GameToken parseString (String encrypted) throws JoseException, JsonProcessingException {
        String stringified = JWEEncryptor.getInstance().decrypt(encrypted);

        return new ObjectMapper().readValue(stringified, GameToken.class);
    }

    public String toString () {
        try {
            String stringified = new ObjectMapper().writeValueAsString(this);

            JsonWebEncryption encryted =
                    JWEEncryptor
                            .getInstance()
                            .encrypt(stringified);

            return encryted.getCompactSerialization();
        } catch (Exception e) {
            return "";
        }
    }
}
