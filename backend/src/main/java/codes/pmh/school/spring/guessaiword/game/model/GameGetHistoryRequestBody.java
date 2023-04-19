package codes.pmh.school.spring.guessaiword.game.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class GameGetHistoryRequestBody {
    @NotNull
    @PositiveOrZero
    private int lastId;

    @NotNull
    @Positive
    @Max(10)
    private int count;

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
