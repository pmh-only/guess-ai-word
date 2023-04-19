package codes.pmh.school.spring.guessaiword.common.entity;

import jakarta.persistence.*;

@Entity
public class GameAskRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String askPrompt;

    private String response;

    @ManyToOne
    private GameRound round;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAskPrompt() {
        return askPrompt;
    }

    public void setAskPrompt(String askPrompt) {
        this.askPrompt = askPrompt;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setRound(GameRound round) {
        this.round = round;
    }
}
