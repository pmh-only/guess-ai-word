package codes.pmh.school.spring.guessaime.game;

import codes.pmh.school.spring.guessaime.ai.datatype.AIAskQnAResult;
import codes.pmh.school.spring.guessaime.ai.datatype.AIAskResult;
import codes.pmh.school.spring.guessaime.ai.AIAsker;
import codes.pmh.school.spring.guessaime.game.datatype.GameToken;
import codes.pmh.school.spring.guessaime.game.datatype.enums.GameType;
import codes.pmh.school.spring.guessaime.game.datatype.enums.GameWordCategory;
import codes.pmh.school.spring.guessaime.util.JWEEncryptor;
import codes.pmh.school.spring.guessaime.util.PromptBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.lang.JoseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service("gameService")
public class GameService {
    public GameToken createGameToken (GameType type, GameWordCategory wordCategory) {
        AIAsker aiAsker = new AIAsker();
        String prompt = createPrompt(type, wordCategory);

        List<AIAskResult> askResults = aiAsker.ask(prompt);
        GameToken gameToken = new GameToken();

        gameToken.setAskResults(askResults);

        return gameToken;
    }

    private String createPrompt (GameType type, GameWordCategory wordCategory) {
        return new PromptBuilder()
                .setWordCount(type.getWordCount())
                .setQnaCount(type.getQnACount())
                .setWordCategory(wordCategory.toString())
                .build();
    }

    public void increaseQnAIndex (GameToken gameToken, int increaseCount) {
        gameToken.setCurrentQuestionIndex(gameToken.getCurrentQuestionIndex() + increaseCount);
    }

    public void increaseWordIndex (GameToken gameToken, int increaseCount) {
        gameToken.setCurrentQuestionIndex(0);
        gameToken.setCurrentWordIndex(gameToken.getCurrentWordIndex() + increaseCount);
    }

    public AIAskQnAResult getCurrentQnAResult (GameToken gameToken) {
        int wordIndex = gameToken.getCurrentWordIndex();
        int questionIndex = gameToken.getCurrentQuestionIndex();

        List<AIAskResult> askResults = gameToken.getAskResults();

        if (askResults.size() <= wordIndex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No more words available"
            );
        }

        List<AIAskQnAResult> qnAResults = askResults.get(wordIndex).getQna();

        if (qnAResults.size() <= questionIndex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No more questions available"
            );
        }

        return qnAResults.get(questionIndex);
    }

    public GameToken parseGameToken (String encrypted) throws JoseException, JsonProcessingException {
        String stringified = JWEEncryptor.getInstance().decrypt(encrypted);

        return new ObjectMapper().readValue(stringified, GameToken.class);
    }

    public String stringifyGameToken (GameToken gameToken) {
        try {
            String stringified = new ObjectMapper().writeValueAsString(gameToken);

            JsonWebEncryption encrypted =
                    JWEEncryptor
                            .getInstance()
                            .encrypt(stringified);

            return encrypted.getCompactSerialization();
        } catch (Exception e) {
            return "";
        }
    }
}
