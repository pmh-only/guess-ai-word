package codes.pmh.school.spring.guessaime.game;

import codes.pmh.school.spring.guessaime.ai.datatype.AIAskQnAResult;
import codes.pmh.school.spring.guessaime.game.datatype.api.GameAPICreationResult;
import codes.pmh.school.spring.guessaime.game.datatype.api.GameAPINextQnaResult;
import codes.pmh.school.spring.guessaime.game.datatype.GameToken;
import codes.pmh.school.spring.guessaime.game.datatype.enums.GameType;
import codes.pmh.school.spring.guessaime.game.datatype.enums.GameWordCategory;
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
        GameToken gameToken = gameService.createGameToken(gameType, wordCategory);
        String stringifiedGameToken = gameService.stringifyGameToken(gameToken);

        creationResult.setWordCount(gameType.getWordCount());
        creationResult.setQnaCount(gameType.getQnACount());

        response.addCookie(new Cookie("GAME_TOKEN", stringifiedGameToken));

        return creationResult;
    }

    @GetMapping("/nextQna")
    private GameAPINextQnaResult nextQnaResult (
            HttpServletResponse response,
            @CookieValue("GAME_TOKEN") String stringifiedGameToken)
            throws Exception {

        GameAPINextQnaResult nextQnaResult = new GameAPINextQnaResult();
        GameToken gameToken = gameService.parseGameToken(stringifiedGameToken);
        GameToken nextGameToken = gameService.increaseQnAIndex(gameToken, 1);
        String stringifiedNextGameToken = gameService.stringifyGameToken(nextGameToken);

        AIAskQnAResult askQnAResult = gameService.getCurrentQnAResult(nextGameToken);

        nextQnaResult.setAskQnAResult(askQnAResult);

        response.addCookie(new Cookie("GAME_TOKEN", stringifiedNextGameToken));

        return nextQnaResult;
    }
}
