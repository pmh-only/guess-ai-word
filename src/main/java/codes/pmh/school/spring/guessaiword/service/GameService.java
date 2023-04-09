package codes.pmh.school.spring.guessaiword.service;

import codes.pmh.school.spring.guessaiword.dto.*;
import codes.pmh.school.spring.guessaiword.entity.Game;
import codes.pmh.school.spring.guessaiword.repository.GameRepository;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service("gameService")
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TokenService tokenService;

    public GameCreationDto createGame (GameCreationDto gameCreationDto) throws Exception {
        gameCreationDto.setGame(createGameEntity(gameCreationDto));
        gameCreationDto.setGameToken(createGameToken(gameCreationDto));

        return gameCreationDto;
    }

    private Game createGameEntity (GameCreationDto gameCreationDto) {
        Game game = new Game();

        game.setGameType(gameCreationDto.getGameType());
        game.setDictionaryCategory(gameCreationDto.getDictionaryCategory());

        return gameRepository.save(game);
    }

    private String createGameToken (GameCreationDto gameCreationDto) throws JoseException {
        Game game = gameCreationDto.getGame();
        GameTokenDto gameTokenDto = new GameTokenDto();

        gameTokenDto.setGameId(game.getId());

        return tokenService
                .sign(gameTokenDto.toString());
    }

    public void updatePlayerName (GameUpdatePlayerNameDto updatePlayerNameDto) throws Exception {
        getGameIdByToken(updatePlayerNameDto);
        getGameById(updatePlayerNameDto);

        if (!isValidUpdatePlayerNameRequest(updatePlayerNameDto))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        updateGameEntityPlayerName(updatePlayerNameDto);
    }

    private boolean isValidUpdatePlayerNameRequest (GameUpdatePlayerNameDto updatePlayerNameDto) {
        Game game = updatePlayerNameDto.getGame();

        return game.isFinished() && game.getPlayerName() != null;
    }

    private void updateGameEntityPlayerName (GameUpdatePlayerNameDto updatePlayerNameDto) {
        Game game = updatePlayerNameDto.getGame();
        game.setPlayerName(updatePlayerNameDto.getPlayerName());

        gameRepository.save(game);
    }

//    -- Utils --

    private void getGameIdByToken (GameIdFetchableDto gameIdFetchableDto) throws Exception {
        String gameToken = gameIdFetchableDto.getGameToken();
        GameTokenDto tokenDto = new GameTokenDto(tokenService.verify(gameToken));

        gameIdFetchableDto.setGameId(tokenDto.getGameId());
    }

    private void getGameById (GameFetchableDto gameFetchableDto) {
        Game game = gameRepository.getReferenceById(gameFetchableDto.getGameId());

        gameFetchableDto.setGame(game);
    }
}
