package io.github.EfficiencAI;

import io.github.EfficiencAI.pojo.Entites.node.ConversationNode;

public class ConversationNodeTests {
    public static void main(String[] args) {
        // Create a new UserNode
        ConversationNode conversationNode = new ConversationNode();
        conversationNode.setConversationNodeID("01");
        conversationNode.setContextStartIdx("13");
        conversationNode.setContextEndIdx("47");
        conversationNode.saveSelfToFile("./Saves/ConversationNode/");
        conversationNode = ConversationNode.loadFromFile("./Saves/ConversationNode/01.json", ConversationNode.class);
        if (conversationNode == null) {
            System.out.println("Failed to load conversation node");
            return;
        }
        System.out.println(conversationNode.getConversationNodeID());
        System.out.println(conversationNode.getContextStartIdx());
        System.out.println(conversationNode.getContextEndIdx());
    }

}
