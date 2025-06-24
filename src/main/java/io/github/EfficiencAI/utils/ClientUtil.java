package io.github.EfficiencAI.utils;

import io.github.EfficiencAI.pojo.DTO.ChatRequestDTO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;

public class ClientUtil {

    public static ChatClient chatClient(ChatRequestDTO chatRequestDTO){
       return ChatClient
               .builder(openAiChatModel(chatRequestDTO))
               .build();
    }


    private static OpenAiChatModel openAiChatModel(ChatRequestDTO chatRequestDTO) {
        return OpenAiChatModel
                .builder()
                .openAiApi(OpenAiApi.builder()
                        .apiKey(chatRequestDTO.getApikey())
                        .baseUrl(chatRequestDTO.getBaseurl())
                        .build())
                .build();
    }


}
