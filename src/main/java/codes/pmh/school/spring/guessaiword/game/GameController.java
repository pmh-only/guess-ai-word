package codes.pmh.school.spring.guessaiword.game;

import codes.pmh.school.spring.guessaiword.game.datatype.api.GameAPICreationResult;
import codes.pmh.school.spring.guessaiword.game.datatype.GameToken;
import codes.pmh.school.spring.guessaiword.game.datatype.api.GameAPIGetNextResult;
import codes.pmh.school.spring.guessaiword.game.datatype.enums.GameType;
import codes.pmh.school.spring.guessaiword.game.datatype.enums.GameWordCategory;
import codes.pmh.school.spring.guessaiword.game.entity.GameAIResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @PostMapping()
    private GameAPICreationResult createNewGame (
            HttpServletResponse response,
            @RequestParam(value = "type", defaultValue = "NORMAL_GAME") GameType gameType,
            @RequestParam(value = "category", defaultValue = "ANY") GameWordCategory wordCategory) {

        GameAPICreationResult creationResult = new GameAPICreationResult();
        GameToken gameToken = gameService.createNewGame(gameType, wordCategory);
        GameAIResponse aiResponse = gameService.getCurrentResponse(gameToken.getGameId());
        String stringifiedGameToken = gameService.stringifyGameToken(gameToken);

        creationResult.setWordCount(gameType.getWordCount());
        creationResult.setQnaCount(gameType.getQnACount());
        creationResult.setAiResponse(aiResponse);

        response.addCookie(new Cookie("GAME_TOKEN", stringifiedGameToken));

        return creationResult;
    }

    @GetMapping("/@current/rounds/@next")
    private GameAPIGetNextResult getNextRound (
            @CookieValue("GAME_TOKEN") String signedGameToken)
            throws Exception {

        GameToken gameToken = gameService.parseGameToken(signedGameToken);
        GameAIResponse aiResponse = gameService.getNextRound(gameToken.getGameId());
        GameAPIGetNextResult getNextResult = new GameAPIGetNextResult();

        getNextResult.setAiResponse(aiResponse);

        return getNextResult;
    }

    @GetMapping("/@current/rounds/@current/responses/@next")
    private GameAPIGetNextResult gameNextAIResponse (
            @CookieValue("GAME_TOKEN") String signedGameToken)
            throws Exception {

        GameToken gameToken = gameService.parseGameToken(signedGameToken);
        GameAIResponse aiResponse = gameService.getNextResponse(gameToken.getGameId());
        GameAPIGetNextResult getNextResult = new GameAPIGetNextResult();

        getNextResult.setAiResponse(aiResponse);

        return getNextResult;
    }
}
