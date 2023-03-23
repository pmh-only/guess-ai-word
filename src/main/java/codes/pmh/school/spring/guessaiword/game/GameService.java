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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
                .setWordCount(type.getRoundCount())
                .setQnaCount(type.getAIResultCount())
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

    public GameAIResponse getNextRound (String gameId) {
        Game game = getGameById(gameId);
        int currentRound = game.getCurrentRound();

        if (game.getRounds().size() <= currentRound + 1)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Next round not found");

        game.setCurrentRound(currentRound + 1);
        GameRound round = game.getRounds().get(currentRound + 1);

        round.setResponseUsedDate(new Date());
        round.setStartedAt(new Date());
        gameRepository.save(game);

        return round.getAiResponses().get(0);
    }

    public GameAIResponse getNextResponse (String gameId) {
        Game game = getGameById(gameId);
        GameRound round = game.getRounds().get(game.getCurrentRound());

        int usedResponseCount = round.getUsedResponseCount();

        if (round.getAiResponses().size() <= usedResponseCount + 1)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Next ai response not found");

        Date responseUseDate = round.getResponseUsedDate();
        Date responseUsableDate = Date.from(responseUseDate.toInstant().plusSeconds(5));

        if (responseUsableDate.after(new Date()))
            throw new ResponseStatusException(HttpStatus.TOO_EARLY, "Wait 5s to get next ai response");

        round.setUsedResponseCount(usedResponseCount + 1);
        round.setResponseUsedDate(new Date());
        gameRepository.save(game);

        return round.getAiResponses().get(usedResponseCount + 1);
    }

    public GameAIResponse getCurrentResponse (String gameId) {
        Game game = getGameById(gameId);
        GameRound round = game.getRounds().get(game.getCurrentRound());

        round.setResponseUsedDate(new Date());
        round.setStartedAt(new Date());
        gameRepository.save(game);

        return round.getAiResponses().get(round.getUsedResponseCount());
    }

    public boolean submitAnswer (String gameId, String answer) {
        Game game = getGameById(gameId);
        GameRound round = game.getRounds().get(game.getCurrentRound());

        Date answerSubmittedDate = round.getAnswerSubmittedDate();
        Date answerSubmittableDate = Date.from(answerSubmittedDate.toInstant().plusSeconds(5));

        if (answerSubmittableDate.after(new Date()))
            throw new ResponseStatusException(HttpStatus.TOO_EARLY, "Wait 5s to submit answer");

        boolean isCorrect = round.getAnswer().equals(answer);
        if (isCorrect) {
            round.setPlayerWin(true);
            round.setWinAt(new Date());
        }

        round.setAnswerSubmittedDate(new Date());
        gameRepository.save(game);
        return isCorrect;
    }

    public float calculateGameScore (String gameId) {
        Game game = getGameById(gameId);
        return (calculateRoundsScore(game) + calculateBonusScore(game)) / 10000f;
    }

    private long calculateRoundsScore (Game game) {
        long score = 0;

        for (GameRound round : game.getRounds())
            score += calculateRoundScore(round);

        return score;
    }

    private long calculateRoundScore (GameRound round) {
        if (!round.isPlayerWin())
            return 0;

        long timeSpend = round.getWinAt().getTime() - round.getStartedAt().getTime();

        return (10 * 60 * 1000 - timeSpend) - ((long) round.getUsedResponseCount() * 10 * 1000);
    }

    private long calculateBonusScore (Game game) {
        int roundCount = game.getType().getRoundCount();
        int winRoundCount = 0;

        for (GameRound round : game.getRounds())
            winRoundCount += round.isPlayerWin() ? 1 : 0;

        return winRoundCount >= roundCount ? 10000 : 0;
    }

    private Game getGameById (String gameId) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);

        if (gameOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");

        return gameOptional.get();
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
