package codes.pmh.school.spring.guessaime.game;

import codes.pmh.school.spring.guessaime.game.api.GameAPICreationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @PostMapping()
    private GameAPICreationResult createNewGame (
            @RequestParam("type") GameType gameType,
            @RequestParam("category") GameWordCategory wordCategory) {

        GameAPICreationResult creationResult = new GameAPICreationResult();
        String gameToken = gameService.createGameToken(gameType, wordCategory);

        creationResult.setGameToken(gameToken);
        creationResult.setWordCount(gameType.getWordCount());
        creationResult.setQnaCount(gameType.getQnACount());

        return creationResult;
    }
}
