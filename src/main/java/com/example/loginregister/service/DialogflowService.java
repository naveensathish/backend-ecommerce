package com.example.loginregister.service;

import com.google.cloud.dialogflow.v2.*;
import org.springframework.stereotype.Service;

@Service
public class DialogflowService {

    public String detectIntent(String projectId, String sessionId, String text) throws Exception {
        try (SessionsClient sessionsClient = SessionsClient.create()) {
            SessionName session = SessionName.of(projectId, sessionId);
            TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode("en");
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
            QueryResult queryResult = response.getQueryResult();
            return queryResult.getFulfillmentText();
        }
    }
}
