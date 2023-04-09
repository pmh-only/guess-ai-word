package codes.pmh.school.spring.guessaiword.service;

import codes.pmh.school.spring.guessaiword.dto.GameCreationDto;
import codes.pmh.school.spring.guessaiword.dto.GameTokenDto;
import codes.pmh.school.spring.guessaiword.entity.Game;
import codes.pmh.school.spring.guessaiword.repository.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                .sign(gameTokenDto.toString())
                .getCompactSerialization();
    }
}
