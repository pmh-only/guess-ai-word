package codes.pmh.school.spring.guessaime.ai;

import codes.pmh.school.spring.guessaime.ai.datatype.api.AIAPIMessage;
import codes.pmh.school.spring.guessaime.ai.datatype.api.AIAPIRequest;
import codes.pmh.school.spring.guessaime.ai.datatype.api.AIAPIResult;
import codes.pmh.school.spring.guessaime.ai.datatype.AIAskResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AIAsker {
    private static final String ENDPOINT_URL =
            "https://api.openai.com/v1/chat/completions";

    private static final String AI_MODEL =
            "gpt-3.5-turbo";

    private HttpURLConnection endpointConnection;

    private String prompt;

    private final Logger logger = LoggerFactory.getLogger(AIAsker.class);

    public AIAsker () {
        if (getAuthorizeToken() == null)
            logger.warn("Environment variable OPENAI_API_AUTH_TOKEN not found. Calling AIAsker:ask() will be thrown.");
    }

    public List<AIAskResult> ask (String prompt) {
        setPrompt(prompt);

        try {
            return askImplementation();
        } catch (Exception exception) {
            logException(exception);
            return null;
        }
    }

    private void setPrompt (String prompt) {
        this.prompt = prompt;
    }

    private List<AIAskResult> askImplementation () throws Exception {
        openEndpointConnection();
        setAuthorizations();
        setPOSTMethod();
        setRequestBody();
        return parseResult(getResponseBody());
    }

    private void openEndpointConnection () throws IOException {
        URL url = new URL(AIAsker.ENDPOINT_URL);

        this.endpointConnection = (HttpURLConnection) url.openConnection();
    }

    private void setAuthorizations () {
        String token = getAuthorizeToken();
        this.endpointConnection.setRequestProperty("Authorization", "Bearer " + token);
    }

    private String getAuthorizeToken () {
        return System.getenv("OPENAI_API_AUTH_TOKEN");
    }

    private void setRequestBody () throws IOException {
        this.endpointConnection.setDoOutput(true);

        AIAPIRequest AIAPIRequest = getAskRequestFromQuestion();
        String aiAskRequestString = getStringFromAskRequest(AIAPIRequest);

        setContentTypeJson();
        writeRequestBodyStream(aiAskRequestString);
    }

    private void setPOSTMethod () throws ProtocolException {
        this.endpointConnection.setRequestMethod("POST");
    }

    private AIAPIRequest getAskRequestFromQuestion () {
        AIAPIRequest askRequest = new AIAPIRequest();
        AIAPIMessage askMessage = new AIAPIMessage();

        askRequest.setModel(AIAsker.AI_MODEL);
        askMessage.setRole("user");
        askMessage.setContent(this.prompt);
        askRequest.appendMessage(askMessage);

        return askRequest;
    }

    private String getStringFromAskRequest (AIAPIRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(request);
    }

    private void setContentTypeJson () {
        this.endpointConnection.setRequestProperty("Content-Type", "application/json");
    }

    private void writeRequestBodyStream (String body) throws IOException {
        OutputStream bodyStream = this.endpointConnection.getOutputStream();
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        bodyStream.write(bodyBytes, 0, bodyBytes.length);
        bodyStream.flush();
        bodyStream.close();
    }

    private AIAPIResult getResponseBody () throws IOException {
        String body = readResponseBodyStream();
        return getAskResultFromString(body);
    }

    private String readResponseBodyStream () throws IOException {
        InputStream bodyStream = this.endpointConnection.getInputStream();

        return new String(bodyStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    private AIAPIResult getAskResultFromString (String body) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper.readValue(body, AIAPIResult.class);
    }

    private List<AIAskResult> parseResult (AIAPIResult apiResult) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper.readValue(apiResult.getContent(), new TypeReference<List<AIAskResult>>() {});
    }

    private void logException (Exception exception) {
        logger.error("Exception on AIAsker Class.", exception);
    }
}
