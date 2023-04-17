package codes.pmh.school.spring.guessaiword.game.dto;

import codes.pmh.school.spring.guessaiword.common.entity.Game;
import codes.pmh.school.spring.guessaiword.dictionary.enums.DictionaryCategory;
import codes.pmh.school.spring.guessaiword.game.enums.GameType;

public class GameCreationDto {
    private Game game;

    private String gameToken;

    private GameType gameType;

    private DictionaryCategory dictionaryCategory;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getGameToken() {
        return gameToken;
    }

    public void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public DictionaryCategory getDictionaryCategory() {
        return dictionaryCategory;
    }

    public void setDictionaryCategory(DictionaryCategory dictionaryCategory) {
        this.dictionaryCategory = dictionaryCategory;
    }
}
