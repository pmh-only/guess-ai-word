package codes.pmh.school.spring.guessaiword.game.entity;

import codes.pmh.school.spring.guessaiword.game.datatype.enums.GameType;
import codes.pmh.school.spring.guessaiword.game.datatype.enums.GameWordCategory;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.*;

@Entity

public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private GameType type;

    private GameWordCategory wordCategory;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameRound> rounds = new ArrayList<>();

    private int finalScore = 0;

    private String playerName;

    @CreationTimestamp()
    private Date createdAt;

    private Date endedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GameType getType() {
        return type;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public GameWordCategory getWordCategory() {
        return wordCategory;
    }

    public void setWordCategory(GameWordCategory wordCategory) {
        this.wordCategory = wordCategory;
    }

    public List<GameRound> getRounds() {
        return rounds;
    }

    public void setRounds(List<GameRound> rounds) {
        this.rounds = rounds;
    }

    public void appendRound(GameRound round) {
        round.setGame(this);
        this.rounds.add(round);
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Date endedAt) {
        this.endedAt = endedAt;
    }
}
