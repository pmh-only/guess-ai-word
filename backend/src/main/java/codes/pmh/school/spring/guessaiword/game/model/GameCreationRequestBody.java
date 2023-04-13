package codes.pmh.school.spring.guessaiword.game.model;

import codes.pmh.school.spring.guessaiword.dictionary.enums.DictionaryCategory;
import codes.pmh.school.spring.guessaiword.game.enums.GameType;
import jakarta.validation.constraints.NotNull;

public class GameCreationRequestBody {
    @NotNull
    private GameType gameType;

    @NotNull
    private DictionaryCategory dictionaryCategory;

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
