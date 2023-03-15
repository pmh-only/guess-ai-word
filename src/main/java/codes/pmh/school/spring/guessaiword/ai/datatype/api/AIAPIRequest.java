package codes.pmh.school.spring.guessaiword.ai.datatype.api;

import java.util.ArrayList;
import java.util.List;

public class AIAPIRequest {
    private String model;

    private final List<AIAPIMessage> messages = new ArrayList<>();

    public String getModel() {
        return model;
    }

    public List<AIAPIMessage> getMessages() {
        return messages;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void appendMessage(AIAPIMessage message) {
        this.messages.add(message);
    }
}
