package codes.pmh.school.spring.guessaime.ai.api;

import java.util.ArrayList;
import java.util.List;

public class AIAPIRequest {
    private String model;
    private final List<AIAPIMessage> messages = new ArrayList<>();

    public void setModel(String model) {
        this.model = model;
    }

    public void appendMessage(AIAPIMessage message) {
        this.messages.add(message);
    }
}
