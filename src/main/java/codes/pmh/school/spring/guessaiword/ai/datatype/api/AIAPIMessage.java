package codes.pmh.school.spring.guessaiword.ai.datatype.api;

public class AIAPIMessage {
    private String role;

    private String content;

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
