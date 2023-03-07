package codes.pmh.school.spring.guessaime.ai;

import java.util.ArrayList;
import java.util.List;

public class AIAskRequest {
    private String model;
    private final List<AIAskMessage> messages = new ArrayList<>();

    public void setModel(String model) {
        this.model = model;
    }

    public void appendMessage(AIAskMessage message) {
        this.messages.add(message);
    }
}
