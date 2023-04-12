package codes.pmh.school.spring.guessaiword.game;

import codes.pmh.school.spring.guessaiword.ai.dto.AIAskDto;
import codes.pmh.school.spring.guessaiword.game.dto.*;
import codes.pmh.school.spring.guessaiword.game.model.*;
import codes.pmh.school.spring.guessaiword.game.GameService;
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

    @PostMapping("/createNewRound")
    public void createNewGameRound (
            @CookieValue("GAME_TOKEN") String gameToken)
            throws Exception {

        GameRoundCreationDto roundCreationDto = new GameRoundCreationDto();

        roundCreationDto.setGameToken(gameToken);
        gameService.createGameRound(roundCreationDto);
    }

    @PostMapping("/createAskCandidate")
    public GameAskCandidateCreationResponseBody createGameAskPromptCandidate (
            @CookieValue("GAME_TOKEN") String gameToken)
            throws Exception {

        GameAskCandidateCreationDto candidateCreationDto = new GameAskCandidateCreationDto();
        GameAskCandidateCreationResponseBody responseBody = new GameAskCandidateCreationResponseBody();

        candidateCreationDto.setGameToken(gameToken);
        gameService.createAskCandidate(candidateCreationDto);

        responseBody.setCandidates(candidateCreationDto.getCandidates());
        responseBody.setCandidateSecret(candidateCreationDto.getCandidateSecret());

        return responseBody;
    }

    @PostMapping("/askToAI")
    public GameAskToAIResponseBody askToAIWithCandidate (
            @CookieValue("GAME_TOKEN") String gameToken,
            @RequestBody @Valid GameAskToAIRequestBody requestBody)
            throws Exception {

        GameAskToAIDto askToAIDto = new GameAskToAIDto();
        GameAskToAIResponseBody responseBody = new GameAskToAIResponseBody();

        askToAIDto.setGameToken(gameToken);
        askToAIDto.setCandidateId(requestBody.getCandidateId());
        askToAIDto.setCandidateSecret(requestBody.getCandidateSecret());

        gameService.askToAI(askToAIDto);

        AIAskDto aiAskDto = askToAIDto.getAiDto();

        responseBody.setResponse(aiAskDto.getResponse());

        return responseBody;
    }

    @PostMapping("/submitAnswer")
    public GameSubmitAnswerResponseBody submitAnswer (
            @CookieValue("GAME_TOKEN") String gameToken,
            @RequestBody @Valid GameSubmitAnswerRequestBody requestBody)
            throws Exception {

        GameSubmitAnswerDto submitAnswerDto = new GameSubmitAnswerDto();
        GameSubmitAnswerResponseBody responseBody = new GameSubmitAnswerResponseBody();

        submitAnswerDto.setGameToken(gameToken);
        gameService.submitGameAnswer(submitAnswerDto);
        responseBody.setCorrectAnswer(submitAnswerDto.isCorrect());

        return responseBody;
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
