package codes.pmh.school.spring.guessaime.game;

import codes.pmh.school.spring.guessaime.ai.AIAskResult;
import codes.pmh.school.spring.guessaime.ai.AIAsker;
import codes.pmh.school.spring.guessaime.util.JWEEncryptor;
import codes.pmh.school.spring.guessaime.util.PromptBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("gameService")
public class GameService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String createGameToken (GameType type, GameWordCategory wordCategory) throws JsonProcessingException, JoseException {
        AIAsker aiAsker = new AIAsker();
        String prompt = createPrompt(type, wordCategory);

        List<AIAskResult> askResults = aiAsker.ask(prompt);
        String askResultsString = objectMapper.writeValueAsString(askResults);

        JsonWebEncryption gameToken =
                JWEEncryptor
                        .getInstance()
                        .encrypt(askResultsString);

        return gameToken.getCompactSerialization();
    }

    private String createPrompt (GameType type, GameWordCategory wordCategory) {
        return new PromptBuilder()
                .setWordCount(type.getWordCount())
                .setQnaCount(type.getQnACount())
                .setWordCategory(wordCategory.toString())
                .build();
    }
}
