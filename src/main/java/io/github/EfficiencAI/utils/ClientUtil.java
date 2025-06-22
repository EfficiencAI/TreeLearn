package site.arookieofc.aichat.utils;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import site.arookieofc.aichat.pojo.DTO.ChatRequestDTO;

public class ClientUtil {

    public static ChatClient openAiChatClient(ChatRequestDTO chatRequestDTO) {
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
