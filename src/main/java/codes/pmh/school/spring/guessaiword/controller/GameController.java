package codes.pmh.school.spring.guessaiword.controller;

import codes.pmh.school.spring.guessaiword.dto.GameCreationDto;
import codes.pmh.school.spring.guessaiword.model.GameCreationRequestBody;
import codes.pmh.school.spring.guessaiword.service.GameService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
