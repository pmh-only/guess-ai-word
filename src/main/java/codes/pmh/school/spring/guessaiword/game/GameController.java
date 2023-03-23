package codes.pmh.school.spring.guessaiword.game;

import codes.pmh.school.spring.guessaiword.game.datatype.api.*;
import codes.pmh.school.spring.guessaiword.game.datatype.GameToken;
import codes.pmh.school.spring.guessaiword.game.datatype.enums.GameType;
import codes.pmh.school.spring.guessaiword.game.datatype.enums.GameWordCategory;
import codes.pmh.school.spring.guessaiword.game.entity.GameAIResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @PostMapping("/createNewGame")
    private GameAPICreationResult createNewGame (
            HttpServletResponse response,
            @RequestParam(value = "type", defaultValue = "NORMAL_GAME") GameType gameType,
            @RequestParam(value = "category", defaultValue = "ANY") GameWordCategory wordCategory) {

        GameAPICreationResult creationResult = new GameAPICreationResult();
        GameToken gameToken = gameService.createNewGame(gameType, wordCategory);
        GameAIResponse aiResponse = gameService.getCurrentResponse(gameToken.getGameId());
        String stringifiedGameToken = gameService.stringifyGameToken(gameToken);

        creationResult.setRoundCount(gameType.getRoundCount());
        creationResult.setAiResultCount(gameType.getAIResultCount());
        creationResult.setAiResponse(aiResponse);

        response.addCookie(new Cookie("GAME_TOKEN", stringifiedGameToken));

        return creationResult;
    }

    @PostMapping("/nextRound")
    private GameAPIGetNextResult getNextRound (
            @CookieValue("GAME_TOKEN") String signedGameToken)
            throws Exception {

        GameToken gameToken = gameService.parseGameToken(signedGameToken);
        GameAIResponse aiResponse = gameService.getNextRound(gameToken.getGameId());
        GameAPIGetNextResult getNextResult = new GameAPIGetNextResult();

        getNextResult.setAiResponse(aiResponse);

        return getNextResult;
    }

    @PostMapping("/nextAIResponse")
    private GameAPIGetNextResult getNextAIResponse (
            @CookieValue("GAME_TOKEN") String signedGameToken)
            throws Exception {

        GameToken gameToken = gameService.parseGameToken(signedGameToken);
        GameAIResponse aiResponse = gameService.getNextResponse(gameToken.getGameId());
        GameAPIGetNextResult getNextResult = new GameAPIGetNextResult();

        getNextResult.setAiResponse(aiResponse);

        return getNextResult;
    }

    @PostMapping("/submitAnswer")
    private GameAPISubmitAnswerResult submitAnswer (
            @CookieValue("GAME_TOKEN") String signedGameToken,
            @RequestBody GameAPISubmitAnswerBody answerBody)
            throws Exception {

        GameToken gameToken = gameService.parseGameToken(signedGameToken);
        GameAPISubmitAnswerResult answerResult = new GameAPISubmitAnswerResult();

        boolean isCorrect = gameService.submitAnswer(gameToken.getGameId(), answerBody.getAnswer());
        answerResult.setCorrect(isCorrect);

        return answerResult;
    }

    @PostMapping("/calculateScore")
    private GameAPICalculateScoreResult calculateScore (
            @CookieValue("GAME_TOKEN") String signedGameToken)
            throws Exception {

        GameToken gameToken = gameService.parseGameToken(signedGameToken);
        GameAPICalculateScoreResult calculateScoreResult = new GameAPICalculateScoreResult();

        float score = gameService.calculateGameScore(gameToken.getGameId());
        calculateScoreResult.setScore(score);

        return calculateScoreResult;
    }
}
