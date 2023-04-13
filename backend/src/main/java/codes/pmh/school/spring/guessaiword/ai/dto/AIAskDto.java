package codes.pmh.school.spring.guessaiword.ai.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.HttpURLConnection;

public class AIAskDto {
    private String askPrompt;

    private String response;

    private HttpURLConnection httpURLConnection;

    private ObjectNode requestBodyNode;

    private ObjectNode responseBodyNode;

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

    public HttpURLConnection getHttpURLConnection() {
        return httpURLConnection;
    }

    public void setHttpURLConnection(HttpURLConnection httpURLConnection) {
        this.httpURLConnection = httpURLConnection;
    }

    public ObjectNode getRequestBodyNode() {
        return requestBodyNode;
    }

    public void setRequestBodyNode(ObjectNode requestBodyNode) {
        this.requestBodyNode = requestBodyNode;
    }

    public ObjectNode getResponseBodyNode() {
        return responseBodyNode;
    }

    public void setResponseBodyNode(ObjectNode responseBodyNode) {
        this.responseBodyNode = responseBodyNode;
    }
}
