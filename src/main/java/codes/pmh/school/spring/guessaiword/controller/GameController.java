package codes.pmh.school.spring.guessaiword.controller;

import codes.pmh.school.spring.guessaiword.dto.GameCreationDto;
import codes.pmh.school.spring.guessaiword.dto.GameUpdatePlayerNameDto;
import codes.pmh.school.spring.guessaiword.model.GameCreationRequestBody;
import codes.pmh.school.spring.guessaiword.model.GameUpdatePlayerNameRequestBody;
import codes.pmh.school.spring.guessaiword.service.GameService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @PostMapping("/createNewGame")
    public void createGame (
            HttpServletResponse response,
            @RequestBody @Valid GameCreationRequestBody requestBody)
            throws Exception {

        GameCreationDto gameCreationDto = new GameCreationDto();

        gameCreationDto.setGameType(requestBody.getGameType());
        gameCreationDto.setDictionaryCategory(requestBody.getDictionaryCategory());

        gameService.createGame(gameCreationDto);

        response.addCookie(new Cookie(
                "GAME_TOKEN", gameCreationDto.getGameToken()));
    }

    @PostMapping("/updatePlayerName")
    public void updatePlayerName (
            @CookieValue("GAME_TOKEN") String gameToken,
            @RequestBody @Valid GameUpdatePlayerNameRequestBody requestBody)
            throws Exception {

        GameUpdatePlayerNameDto updatePlayerNameDto = new GameUpdatePlayerNameDto();

        updatePlayerNameDto.setGameToken(gameToken);
        updatePlayerNameDto.setPlayerName(requestBody.getPlayerName());

        gameService.updatePlayerName(updatePlayerNameDto);
    }
}
