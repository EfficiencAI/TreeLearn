package io.github.EfficiencAI.pojo.VO;

public class ConversationNodeRegisterResult {
    public ConversationNodeRegisterResult(String conversationNodeId, String registrationCertificate) {
        this.conversationNodeId = conversationNodeId;
        this.registrationCertificate = registrationCertificate;
    }
    public String conversationNodeId;
    public String registrationCertificate;
}
