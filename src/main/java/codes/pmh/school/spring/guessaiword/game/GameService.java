package codes.pmh.school.spring.guessaiword.game;

import codes.pmh.school.spring.guessaiword.ai.datatype.AIAskQnAResult;
import codes.pmh.school.spring.guessaiword.ai.datatype.AIAskResult;
import codes.pmh.school.spring.guessaiword.ai.AIAsker;
import codes.pmh.school.spring.guessaiword.game.datatype.GameToken;
import codes.pmh.school.spring.guessaiword.game.datatype.enums.GameType;
import codes.pmh.school.spring.guessaiword.game.datatype.enums.GameWordCategory;
import codes.pmh.school.spring.guessaiword.game.entity.Game;
import codes.pmh.school.spring.guessaiword.game.entity.GameAIResponse;
import codes.pmh.school.spring.guessaiword.game.entity.GameRound;
import codes.pmh.school.spring.guessaiword.game.repository.GameRepository;
import codes.pmh.school.spring.guessaiword.util.JWSSigner;
import codes.pmh.school.spring.guessaiword.util.PromptBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("gameService")
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public GameToken createNewGame(GameType type, GameWordCategory wordCategory) {
        List<AIAskResult> askResults = createPromptAndAsk(type, wordCategory);
        String gameId = createGameFromAskResults(type, wordCategory, askResults);

        return createGameToken(gameId);
    }

    public List<AIAskResult> createPromptAndAsk (GameType type, GameWordCategory wordCategory) {
        AIAsker aiAsker = new AIAsker();
        String prompt = createPrompt(type, wordCategory);

        return aiAsker.ask(prompt);
    }

    private String createPrompt (GameType type, GameWordCategory wordCategory) {
        return new PromptBuilder()
                .setWordCount(type.getWordCount())
                .setQnaCount(type.getQnACount())
                .setWordCategory(wordCategory.toString())
                .build();
    }

    private String createGameFromAskResults (GameType type, GameWordCategory wordCategory, List<AIAskResult> askResults) {
       Game game = new Game();

       game.setType(type);
       game.setWordCategory(wordCategory);

       for (AIAskResult askResult : askResults) {
           GameRound round = createGameRoundFromAskResult(askResult);
           game.appendRound(round);
       }

       return gameRepository.save(game).getId();
    }

    private GameRound createGameRoundFromAskResult (AIAskResult askResult) {
        GameRound round = new GameRound();
        round.setAnswer(askResult.getWord());

        for (AIAskQnAResult qnAResult : askResult.getQna()) {
            GameAIResponse aiResponse = createAIResponseFromQnAResult(qnAResult);
            round.appendAiResponse(aiResponse);
        }

        return round;
    }

    private GameAIResponse createAIResponseFromQnAResult (AIAskQnAResult qnAResult) {
        GameAIResponse aiResponse = new GameAIResponse();

        aiResponse.setQuestion(qnAResult.getQuestion());
        aiResponse.setResponse(qnAResult.getAnswer());

        return aiResponse;
    }

    private GameToken createGameToken (String gameId) {
        GameToken gameToken = new GameToken();
        gameToken.setGameId(gameId);

        return gameToken;
    }

    public GameToken parseGameToken (String signed) throws JoseException, JsonProcessingException {
        String stringified = JWSSigner.getInstance().verify(signed);

        return new ObjectMapper().readValue(stringified, GameToken.class);
    }

    public String stringifyGameToken (GameToken gameToken) {
        try {
            String stringified = new ObjectMapper().writeValueAsString(gameToken);

            JsonWebSignature signed =
                    JWSSigner
                            .getInstance()
                            .sign(stringified);

            return signed.getCompactSerialization();
        } catch (Exception e) {
            return "";
        }
    }
}
