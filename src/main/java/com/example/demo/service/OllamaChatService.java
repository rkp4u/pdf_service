package com.example.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OllamaChatService {

    private ChatClient chatClient;

    @Autowired
    public OllamaChatService(ChatClient.Builder chatClientBuilder) {
        // Build the ChatClient using the provided builder
        this.chatClient = chatClientBuilder.build();
    }

    public String promptTemplate(String documentText) {
        System.out.println("calling modal");
        PromptTemplate promptTemplate = new PromptTemplate("""
                Extract the following from the OCBC Payment Guarantee: date, beneficiary (name, address, country, postal code), guarantee number, currency, amount (words and figures), buyer, seller, goods/services, contract date, effective date, expiry date. Respond ONLY with valid JSON in this structure:

                {{
                  "date": "",
                  "beneficiary": {{"name": "", "address": "", "country": "", "postal_code": ""}},
                  "guarantee_number": "",
                  "currency": "",
                  "amount_words": "",
                  "amount_figures": "",
                  "buyer": "",
                  "seller": "",
                  "goods_services": "",
                  "contract_date": "",
                  "effective_date": "",
                  "expiry_date": ""
                }}
                """);

        Map<String, Object> variable = new HashMap<>();
        variable.put("document", documentText);
        Prompt prompt = promptTemplate.create(variable);
        String response = this.chatClient.prompt(prompt).call().content();

        System.out.println("modal response " + response);
        return response;
    }
}
