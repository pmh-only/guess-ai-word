package codes.pmh.school.spring.guessaiword.game;

import codes.pmh.school.spring.guessaiword.ai.AIService;
import codes.pmh.school.spring.guessaiword.ai.dto.AIAskDto;
import codes.pmh.school.spring.guessaiword.dictionary.DictionaryServiceAskPromptImpl;
import codes.pmh.school.spring.guessaiword.dictionary.DictionaryServiceWordImpl;
import codes.pmh.school.spring.guessaiword.dictionary.dto.DictionaryFileContentDto;
import codes.pmh.school.spring.guessaiword.dictionary.enums.DictionaryCategory;
import codes.pmh.school.spring.guessaiword.game.dto.*;
import codes.pmh.school.spring.guessaiword.common.entity.Game;
import codes.pmh.school.spring.guessaiword.common.entity.GameAskCandidate;
import codes.pmh.school.spring.guessaiword.common.entity.GameAskRecord;
import codes.pmh.school.spring.guessaiword.common.entity.GameRound;
import codes.pmh.school.spring.guessaiword.game.enums.GameType;
import codes.pmh.school.spring.guessaiword.common.repository.GameAskCandidateRepository;
import codes.pmh.school.spring.guessaiword.common.repository.GameAskRecordRepository;
import codes.pmh.school.spring.guessaiword.common.repository.GameRepository;
import codes.pmh.school.spring.guessaiword.common.repository.GameRoundRepository;
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
    private GameAskRecordRepository askRecordRepository;

    @Autowired
    private AIService aiService;

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

        if (!isGameRoundCreatable(roundCreationDto))
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
        DictionaryFileContentDto answerWord = wordDictionaryService.getRandoms(category, 1).get(0);

        roundCreationDto.setAnswerWord(answerWord);
    }

    private void createGameRoundEntity (GameRoundCreationDto roundCreationDto) {
        Game game = roundCreationDto.getGame();
        GameRound gameRound = new GameRound();
        DictionaryFileContentDto answerWord = roundCreationDto.getAnswerWord();

        gameRound.setGame(game);
        gameRound.setAnswer(answerWord.getContent());
        gameRound.setAnswerCategory(answerWord.getCategory());

        gameRoundRepository.save(gameRound);
    }

    public void createAskCandidate (GameAskCandidateCreationDto candidateCreationDto) throws Exception {
        getGameIdByToken(candidateCreationDto);
        getGameById(candidateCreationDto);
        getGameRoundByGame(candidateCreationDto);

        if (!isAskCandidateCreatable(candidateCreationDto))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        createAskCandidateList(candidateCreationDto);
        saveCandidateList(candidateCreationDto);
        saveAskedAt(candidateCreationDto);
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
        DictionaryCategory dictionaryCategory = candidateCreationDto.getGameRound().getAnswerCategory();
        GameType gameType = candidateCreationDto.getGame().getGameType();

        List<GameAskCandidateDto> candidateList = candidateCreationDto.getCandidates();
        List<DictionaryFileContentDto> askPrompts = this.askPromptDictionaryService.getRandoms(dictionaryCategory, gameType.getCandidateCount());

        for (DictionaryFileContentDto askPrompt : askPrompts) {
            GameAskCandidateDto candidateDto = new GameAskCandidateDto();

            candidateDto.setAskPrompt(askPrompt.getContent());
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

            candidateEntity.setSecret(candidateSecret);
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

    public void askToAI (GameAskToAIDto gameAskToAIDto) throws Exception {
        getGameIdByToken(gameAskToAIDto);
        getGameById(gameAskToAIDto);
        getGameRoundByGame(gameAskToAIDto);
        getCandidateBySecretAndId(gameAskToAIDto);

        if (!isAskerable(gameAskToAIDto))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        askToAIAsker(gameAskToAIDto);
        maskAIResponse(gameAskToAIDto);
        saveCandidateToRecord(gameAskToAIDto);
        removeAllCandidate(gameAskToAIDto);
    }

    private boolean isAskerable (GameAskToAIDto gameAskToAIDto) {
        GameRound gameRound = gameAskToAIDto.getGameRound();

        long candidateRoundId = gameAskToAIDto.getCandidate().getRound().getId();
        long gameTokenRoundId = gameRound.getId();

        return candidateRoundId == gameTokenRoundId && !gameRound.isCorrectAnswerShowed();
    }

    private void askToAIAsker (GameAskToAIDto gameAskToAIDto) throws Exception {
        AIAskDto aiAskDto = new AIAskDto();
        GameAskCandidate candidate = gameAskToAIDto.getCandidate();

        String prompt = String.format(candidate.getAskPrompt(), candidate.getRound().getAnswer());

        aiAskDto.setAskPrompt(prompt);

        aiService.askToAi(aiAskDto);

        gameAskToAIDto.setAiDto(aiAskDto);
    }

    private void maskAIResponse (GameAskToAIDto gameAskToAIDto) {
        AIAskDto aiAskDto = gameAskToAIDto.getAiDto();
        String response = aiAskDto.getResponse();
        String answer = gameAskToAIDto.getGameRound().getAnswer();

        aiAskDto.setResponse(response.replaceAll(answer, "%s"));
    }

    private void saveCandidateToRecord (GameAskToAIDto gameAskToAIDto) {
        GameAskCandidate candidate = gameAskToAIDto.getCandidate();
        GameRound round = candidate.getRound();

        GameAskRecord askRecord = new GameAskRecord();

        askRecord.setAskPrompt(candidate.getAskPrompt());
        askRecord.setRound(round);
        askRecord.setResponse(gameAskToAIDto.getAiDto().getResponse());

        askRecordRepository.save(askRecord);
    }

    private void removeAllCandidate (GameAskToAIDto gameAskToAIDto) {
        String candidateSecret = gameAskToAIDto.getCandidateSecret();

        askCandidateRepository.deleteBySecret(candidateSecret);
    }

    public void submitGameAnswer (GameSubmitAnswerDto submitAnswerDto) throws Exception {
        getGameIdByToken(submitAnswerDto);
        getGameById(submitAnswerDto);
        getGameRoundByGame(submitAnswerDto);

        if (!isAnswerSubmittable(submitAnswerDto))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        getIsCorrectAnswer(submitAnswerDto);
        saveSubmitAnswer(submitAnswerDto);
    }

    private boolean isAnswerSubmittable (GameSubmitAnswerDto submitAnswerDto) {
        Game game = submitAnswerDto.getGame();
        GameRound gameRound = submitAnswerDto.getGameRound();
        Date lastSubmittedAt = gameRound.getLastSubmittedAt();

        if (lastSubmittedAt == null)
            return true;

        if (gameRound.isCorrectAnswer())
            return false;

        if (gameRound.isCorrectAnswerShowed())
            return false;

        Duration throttleSecond = Duration.ofSeconds(game.getGameType().getAnswerSubmitThrottleSecond());
        Instant throttleExpireAt = lastSubmittedAt.toInstant().plus(throttleSecond);

        return throttleExpireAt.isBefore(new Date().toInstant());
    }

    private void saveSubmitAnswer (GameSubmitAnswerDto submitAnswerDto) {
        GameRound gameRound = submitAnswerDto.getGameRound();

        gameRound.setLastSubmittedAt(new Date());
        gameRound.setCorrectAnswer(submitAnswerDto.isCorrect());

        gameRoundRepository.save(gameRound);
    }

    private void getIsCorrectAnswer (GameSubmitAnswerDto submitAnswerDto) {
        GameRound gameRound = submitAnswerDto.getGameRound();
        boolean isCorrectAnswer = gameRound.getAnswer().equals(submitAnswerDto.getAnswer());

        submitAnswerDto.setCorrect(isCorrectAnswer);
    }

    public void getCorrectAnswer (GameGetCorrectAnswerDto getCorrectAnswerDto) throws Exception {
        getGameIdByToken(getCorrectAnswerDto);
        getGameById(getCorrectAnswerDto);
        getGameRoundByGame(getCorrectAnswerDto);

        getCorrectAnswerString(getCorrectAnswerDto);
        saveCorrectAnswerShowed(getCorrectAnswerDto);
    }

    private void getCorrectAnswerString (GameGetCorrectAnswerDto getCorrectAnswerDto) {
        GameRound gameRound = getCorrectAnswerDto.getGameRound();
        getCorrectAnswerDto.setCorrectAnswer(gameRound.getAnswer());
    }

    private void saveCorrectAnswerShowed (GameGetCorrectAnswerDto getCorrectAnswerDto) {
        GameRound gameRound = getCorrectAnswerDto.getGameRound();
        gameRound.setCorrectAnswerShowed(true);

        gameRoundRepository.save(gameRound);
    }

    public void calculateScore (GameCalculateScoreDto calculateScoreDto) throws Exception {
        getGameIdByToken(calculateScoreDto);
        getGameById(calculateScoreDto);

        calculateRoundsScore(calculateScoreDto);
        saveGameScore(calculateScoreDto);
    }

    public void calculateRoundsScore (GameCalculateScoreDto calculateScoreDto) {
        List<GameRound> rounds = calculateScoreDto.getGame().getRounds();
        int roundsScore = 0;

        for (GameRound round : rounds)
            roundsScore += calculateRoundScore(round);

        calculateScoreDto.setGameScore(roundsScore);
    }

    public int calculateRoundScore (GameRound round) {
        if (!round.isCorrectAnswer()) // 오답시 0점
            return 0;

        int roundScore = 1000; // 정답시 기본점수 1000점

        long startsAt = round.getStartedAt().getTime();
        long endsAt = round.getLastSubmittedAt().getTime();

        long diffSeconds = (endsAt - startsAt) / 1000;

        roundScore -= diffSeconds * 5; // 푸는데 걸린 초 당 5점 감점
        roundScore -= round.getAsks().size() * 30; // 힌트 사용시 30점 감점

        if (roundScore < 0) // 음수 스코어는 0점 처리
            roundScore = 0;

        return  roundScore;
    }

    public void saveGameScore (GameCalculateScoreDto calculateScoreDto) {
        Game game = calculateScoreDto.getGame();

        game.setFinished(true);
        game.setFinalScore(calculateScoreDto.getGameScore());

        gameRepository.save(game);
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
        Game game = gameRepository.findById(gameFetchableDto.getGameId()).orElseThrow();

        gameFetchableDto.setGame(game);
    }

    private void getGameRoundByGame (GameRoundFetchableDto roundFetchableDto) {
        Game game = roundFetchableDto.getGame();
        List<GameRound> rounds = game.getRounds();

        roundFetchableDto.setGameRound(rounds.get(rounds.size() - 1));
    }

    private void getCandidateBySecretAndId (GameAskCandidateFetchableDto candidateFetchableDto) {
        int candidateId = candidateFetchableDto.getCandidateId();
        String candidateSecret = candidateFetchableDto.getCandidateSecret();

        GameAskCandidate candidate = askCandidateRepository
                .findByIdAndSecret(candidateId, candidateSecret)
                .orElseThrow();

        candidateFetchableDto.setCandidate(candidate);
    }
}
