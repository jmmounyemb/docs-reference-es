package com.geo.docsReferenceEs.service.impl;

import com.geo.docsReferenceEs.service.IFaqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FaqServiceImpl implements IFaqService {

    private static final Logger log = LoggerFactory.getLogger(FaqServiceImpl.class);

    @Value("classpath:/prompts/spring-boot-reference.st")
    private Resource sbPromptTemplate;

    private final ChatClient chatClient;

    private final VectorStore vectorStore;


    public FaqServiceImpl(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String faqLLM(String question) {
        PromptTemplate promptTemplate = new PromptTemplate(sbPromptTemplate);
        var lineSeparator = System.getProperty("line.separator");
        Prompt sbPrompt = promptTemplate.create(Map.of("input", question, "documents", String.join(lineSeparator, findSimilarDocuments(question))));
        return chatClient.prompt(sbPrompt).call().content();
    }

    private List<String> findSimilarDocuments(String question) {
        List<Document> relevantDocuments = vectorStore.similaritySearch(SearchRequest.query(question).withTopK(3));
        List<String> contentList = relevantDocuments.stream().map(Document::getContent).toList();
        log.info("contentList is : {}", contentList);
        return contentList;
    }
}
