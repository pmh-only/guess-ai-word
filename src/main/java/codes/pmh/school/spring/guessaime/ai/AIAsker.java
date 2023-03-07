package codes.pmh.school.spring.guessaime.ai;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class AIAsker {
    private static final String ENDPOINT_URL =
            "https://api.openai.com/v1/chat/completions";

    private static final String AI_MODEL =
            "gpt-3.5-turbo";

    private HttpURLConnection endpointConnection;

    @Autowired
    private Environment environment;

    public AIAskResult ask (String question) {
        try {
            return askImplementation(question);
        } catch (Exception exception) {
            logException(exception);
            return null;
        }
    }

    private AIAskResult askImplementation (String question) throws Exception {
        openEndpointConnection();
        setAuthorizations();
        setPOSTMethod();
        setRequestBody(question);
        return getResponseBody();
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
         return this.environment.getProperty("ai.authToken");
    }

    private void setRequestBody (String question) throws IOException {
        this.endpointConnection.setDoOutput(true);

        AIAskRequest aiAskRequest = getAskRequestFromQuestion(question);
        String aiAskRequestString = getStringFromAskRequest(aiAskRequest);

        setContentTypeJson();
        writeRequestBodyStream(aiAskRequestString);
    }

    private void setPOSTMethod () throws ProtocolException {
        this.endpointConnection.setRequestMethod("POST");
    }

    private AIAskRequest getAskRequestFromQuestion (String question) {
        AIAskRequest askRequest = new AIAskRequest();
        AIAskMessage askMessage = new AIAskMessage();

        askRequest.setModel(AIAsker.AI_MODEL);
        askMessage.setRole("user");
        askMessage.setContent(question);
        askRequest.appendMessage(askMessage);

        return askRequest;
    }

    private String getStringFromAskRequest (AIAskRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

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

    private AIAskResult getResponseBody () throws IOException {
        String body = readResponseBodyStream();
        return getAskResultFromString(body);
    }

    private String readResponseBodyStream () throws IOException {
        InputStream bodyStream = this.endpointConnection.getInputStream();

        return new String(bodyStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    private AIAskResult getAskResultFromString (String body) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper.readValue(body, AIAskResult.class);
    }

    private void logException (Exception exception) {
        System.err.println("Exception on AIAsker Class.");
        System.err.println(exception.toString());
    }
}
