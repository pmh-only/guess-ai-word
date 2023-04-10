package codes.pmh.school.spring.guessaiword.ai;

import codes.pmh.school.spring.guessaiword.ai.dto.AIAskDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@Service("aiService")
public class AIService {
    @Value("${guessaiword.ai.apikey}")
    private String API_KEY;

    @Value("${guessaiword.ai.model:gpt-3.5-turbo}")
    private String API_MODEL;

    private ObjectMapper objectMapper = new ObjectMapper();

    private URL API_URL;

    public AIService () throws MalformedURLException {
        this.API_URL = new URL("https://api.openai.com/v1/chat/completions");
    }

    private void askToAi (AIAskDto askDto) throws Exception {
        openAIConnection(askDto);
        prepareRequestHeader(askDto);
        prepareRequestBody(askDto);
        sendRequestBody(askDto);
        readResponseBody(askDto);
        parseResponseBody(askDto);
    }

    private void openAIConnection (AIAskDto askDto) throws IOException {
        askDto.setHttpURLConnection(
                (HttpURLConnection) this.API_URL.openConnection());
    }

    private void prepareRequestHeader (AIAskDto askDto) throws ProtocolException {
        HttpURLConnection urlConnection = askDto.getHttpURLConnection();

        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Authorization", "Bearer " + this.API_KEY);

        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
    }

    private void prepareRequestBody (AIAskDto askDto) {
        ObjectNode requestBodyNode = objectMapper.createObjectNode();

        ArrayNode messagesNode = objectMapper.createArrayNode();
        ObjectNode messageNode = objectMapper.createObjectNode();

        messageNode.put("role", "user");
        messageNode.put("content", askDto.getAskPrompt());
        messagesNode.add(messageNode);

        requestBodyNode.put("model", this.API_MODEL);
        requestBodyNode.put("messages", messagesNode);

        askDto.setRequestBodyNode(requestBodyNode);
    }

    private void sendRequestBody (AIAskDto askDto) throws IOException {
        HttpURLConnection urlConnection = askDto.getHttpURLConnection();
        OutputStream requestBodyStream = urlConnection.getOutputStream();

        objectMapper.writeValue(requestBodyStream, askDto.getRequestBodyNode());
    }

    private void readResponseBody (AIAskDto askDto) throws IOException {
        HttpURLConnection urlConnection = askDto.getHttpURLConnection();
        InputStream responseBodyStream = urlConnection.getInputStream();

        ObjectNode responseBodyNode =
                (ObjectNode) objectMapper.readTree(responseBodyStream);

        askDto.setResponseBodyNode(responseBodyNode);
    }

    private void parseResponseBody (AIAskDto askDto) {
        ObjectNode responseBodyNode = askDto.getResponseBodyNode();
        String response = responseBodyNode
                .get("choices")
                .get(0)
                .get("message")
                .get("content")
                .textValue();

        askDto.setResponse(response);
    }
}
