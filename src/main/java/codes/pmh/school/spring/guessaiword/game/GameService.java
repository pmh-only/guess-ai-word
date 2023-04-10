package codes.pmh.school.spring.guessaiword.game;

import codes.pmh.school.spring.guessaiword.dictionary.DictionaryServiceAskPromptImpl;
import codes.pmh.school.spring.guessaiword.dictionary.DictionaryServiceWordImpl;
import codes.pmh.school.spring.guessaiword.dictionary.enums.DictionaryCategory;
import codes.pmh.school.spring.guessaiword.game.dto.*;
import codes.pmh.school.spring.guessaiword.game.entity.Game;
import codes.pmh.school.spring.guessaiword.game.entity.GameAskCandidate;
import codes.pmh.school.spring.guessaiword.game.entity.GameAskRecord;
import codes.pmh.school.spring.guessaiword.game.entity.GameRound;
import codes.pmh.school.spring.guessaiword.game.enums.GameType;
import codes.pmh.school.spring.guessaiword.game.repository.GameAskCandidateRepository;
import codes.pmh.school.spring.guessaiword.game.repository.GameRepository;
import codes.pmh.school.spring.guessaiword.game.repository.GameRoundRepository;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("gameService")
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameRoundRepository gameRoundRepository;

    @Autowired
    private GameAskCandidateRepository askCandidateRepository;

    @Autowired
    private GameTokenService gameTokenService;

    @Autowired
    private DictionaryServiceWordImpl wordDictionaryService;

    @Autowired
    private DictionaryServiceAskPromptImpl askPromptDictionaryService;

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

        return gameTokenService
                .sign(gameTokenDto.toString());
    }

    public void createGameRound (GameRoundCreationDto roundCreationDto) throws Exception {
        getGameIdByToken(roundCreationDto);
        getGameById(roundCreationDto);

        if (isGameRoundCreatable(roundCreationDto))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        getRandomAnswerWord(roundCreationDto);
        createGameRoundEntity(roundCreationDto);
    }

    public boolean isGameRoundCreatable (GameRoundCreationDto roundCreationDto) {
        Game game = roundCreationDto.getGame();
        List<GameRound> rounds = game.getRounds();

        return rounds.size() < game.getGameType().getMaxRoundCount();
    }

    private void getRandomAnswerWord (GameRoundCreationDto roundCreationDto) {
        DictionaryCategory category = roundCreationDto.getGame().getDictionaryCategory();
        String answerWord = wordDictionaryService.getRandom(category);

        roundCreationDto.setAnswerWord(answerWord);
    }

    private void createGameRoundEntity (GameRoundCreationDto roundCreationDto) {
        Game game = roundCreationDto.getGame();
        GameRound gameRound = new GameRound();

        gameRound.setGame(game);
        gameRound.setAnswer(roundCreationDto.getAnswerWord());

        gameRoundRepository.save(gameRound);
    }

    public GameAskCandidateCreationDto createAskCandidate (GameAskCandidateCreationDto candidateCreationDto) throws Exception {
        getGameIdByToken(candidateCreationDto);
        getGameById(candidateCreationDto);
        getGameRoundByGame(candidateCreationDto);

        if (!isAskCandidateCreatable(candidateCreationDto))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        createAskCandidateList(candidateCreationDto);
        saveCandidateList(candidateCreationDto);
        saveAskedAt(candidateCreationDto);

        return candidateCreationDto;
    }

    private boolean isAskCandidateCreatable (GameAskCandidateCreationDto candidateCreationDto) {
        Game game = candidateCreationDto.getGame();
        GameRound gameRound = candidateCreationDto.getGameRound();
        List<GameAskRecord> askRecords = gameRound.getAsks();
        Date lastAskedAt = gameRound.getLastAskedAt();

        if (lastAskedAt == null)
            return true;

        if (askRecords.size() >= game.getGameType().getMaxAskableCount())
            return false;

        Duration throttleSecond = Duration.ofSeconds(game.getGameType().getAskThrottleSecond());
        Instant throttleExpireAt = lastAskedAt.toInstant().plus(throttleSecond);

        return throttleExpireAt.isBefore(new Date().toInstant());
    }

    private void createAskCandidateList (GameAskCandidateCreationDto candidateCreationDto) {
        DictionaryCategory dictionaryCategory = candidateCreationDto.getGame().getDictionaryCategory();
        GameType gameType = candidateCreationDto.getGame().getGameType();

        List<GameAskCandidateDto> candidateList = candidateCreationDto.getCandidates();

        while (candidateList.size() < gameType.getCandidateCount()) {
            String askPrompt = this.askPromptDictionaryService.getRandom(dictionaryCategory);
            GameAskCandidateDto candidateDto = new GameAskCandidateDto();

            candidateDto.setAskPrompt(askPrompt);
            candidateList.add(candidateDto);
        }

        candidateCreationDto.setCandidates(candidateList);
    }

    private void saveCandidateList (GameAskCandidateCreationDto candidateCreationDto) {
        Game game = candidateCreationDto.getGame();
        GameRound gameRound = game.getRounds().get(game.getRounds().size() - 1);
        String candidateSecret = UUID.randomUUID().toString();

        for (GameAskCandidateDto candidate : candidateCreationDto.getCandidates()) {
            GameAskCandidate candidateEntity = new GameAskCandidate();

            candidateEntity.setCandidateSecret(candidateSecret);
            candidateEntity.setAskPrompt(candidate.getAskPrompt());
            candidateEntity.setRound(gameRound);

            GameAskCandidate savedCandidate = askCandidateRepository.save(candidateEntity);

            candidate.setId(savedCandidate.getId());
        }

        candidateCreationDto.setCandidateSecret(candidateSecret);
    }

    private void saveAskedAt (GameAskCandidateCreationDto candidateCreationDto) {
        GameRound round = candidateCreationDto.getGameRound();
        round.setLastAskedAt(new Date());

        gameRoundRepository.save(round);
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
        GameTokenDto tokenDto = new GameTokenDto(gameTokenService.verify(gameToken));

        gameIdFetchableDto.setGameId(tokenDto.getGameId());
    }

    private void getGameById (GameFetchableDto gameFetchableDto) {
        Game game = gameRepository.getReferenceById(gameFetchableDto.getGameId());

        gameFetchableDto.setGame(game);
    }

    private void getGameRoundByGame (GameRoundFetchableDto roundFetchableDto) {
        Game game = roundFetchableDto.getGame();
        List<GameRound> rounds = game.getRounds();

        roundFetchableDto.setGameRound(rounds.get(rounds.size() - 1));
    }
}
