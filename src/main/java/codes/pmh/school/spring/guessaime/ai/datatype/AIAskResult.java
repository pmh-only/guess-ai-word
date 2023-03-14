package codes.pmh.school.spring.guessaime.ai.datatype;

import java.util.Date;
import java.util.List;

public class AIAskResult {
    private Date createdAt = new Date();

    private String word;

    private List<AIAskQnAResult> qna;

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<AIAskQnAResult> getQna() {
        return qna;
    }

    public void setQna(List<AIAskQnAResult> qna) {
        this.qna = qna;
    }
}
