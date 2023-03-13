package codes.pmh.school.spring.guessaime.game;

import codes.pmh.school.spring.guessaime.game.api.GameAPICreationResult;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @PostMapping()
    private GameAPICreationResult createNewGame (
            @RequestParam("type") GameType gameType,
            @RequestParam("category") GameWordCategory wordCategory)
            throws IOException, JoseException {

        GameAPICreationResult creationResult = new GameAPICreationResult();
        String gameToken = gameService.createGameToken(gameType, wordCategory);

        creationResult.setGameToken(gameToken);

        return creationResult;
    }
}
