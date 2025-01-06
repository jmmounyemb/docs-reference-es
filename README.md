# Spring AI ChatBot with RAG capabilities

Welcome to the Spring AI Features for Retrieval Augmented Generation (RAG)! Retrieval Augmented Generation (RAG) is a 
technique useful to overcome the limitations of large language models that struggle with long-form content, factual accuracy, 
and context-awareness. This application demonstrates how to create a chatbot with RAG capabilities using Spring AI, Spring Boot 
and Elasticsearch as Vector database. 

## Overview

This project demonstrates the integration of Spring AI, Spring Boot, and Elasticsearch to build a chatbot 
capable of handling traditional request-response interactions, similar to a FAQ system, specifically for 
IT reference documentation.

## Project Requirements

- Java 17
- Maven 3.6.3 or newer
- Spring Boot 3.4.1
- Spring AI 1.0.0-M4
- Elasticsearch 8.15.3
- Docker-compose 3.4.1

## Getting Started

To get started with this project, ensure you have Java 17 or newer and Maven installed on your system. Then, follow these steps:

1. Set up your API keys:
    - Create a file `application.yml` in the `src/main/resources` directory
        - Add the following line, replacing `<ADD Your Weather API key here>` with your actual Weather API key:
          ```
          weather:
           api-key: <ADD Your Weather API key here>
           api-url: http://api.weatherapi.com/v1
          ```
        - Specify Open AI API Key:
          ```
          spring:
           ai:
            openai.api-key: <ADD Your Open API key here>
            openai.chat.options.model: gpt-4
           ```
        - Specify the vectorstore database: To connect to Elasticsearch and use the ElasticsearchVectorStore, we need to 
          provide access details for our instance.
          ```
          spring:
           elasticsearch:
            uris: <elasticsearch instance URIs>
            username: <elasticsearch username>
            password: <elasticsearch password>
           ai:
            vectorstore:
             elasticsearch:
             initialize-schema: true
             index-name: docs-reference
             dimensions: 1536
             similarity: cosine
             batching-strategy: TOKEN_COUNT
          ```  

2. Build the project:
   ```
   mvn clean install
   ```

3. Run the application:
   ```
   mvn spring-boot:run
   ```

The application will start, and you'll be able to access the chat endpoints.

## How to Run the Application

1. Ingest document (POST request):
   ```
   POST http://localhost:8080/faq/ingest
   Body:
    {
      "path": "<Path of the document>"
    }
   ```

2. Get answer (GET request):
   ```
   GET http://localhost:8080/faq?question=<Your question here>
   ```

You can use tools like cURL, Postman, or create a simple frontend to interact with these endpoints.

## Relevant Code Examples

### Ingestion Service

The `IngestServiceImpl` class create embeddings for the documentation reference and store them in Elasticsearch. 
Below is the method from that class which ingest documents.

```java
 @Override
public void ingestDocument(String path) {
    var config = PdfDocumentReaderConfig.builder()
            .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().withNumberOfBottomTextLinesToDelete(0)
                    .withNumberOfTopPagesToSkipBeforeDelete(0)
                    .build())
            .withPagesPerDocument(1)
            .build();

    var pdfReader = new PagePdfDocumentReader(path, config);
    var textSplitter = new TokenTextSplitter();
    vectorStore.accept(textSplitter.apply(pdfReader.get()));
}
```

### Application Configuration

The `DocsReferenceEsApplication` class is a standard Spring Boot application class:

```java
@SpringBootApplication
public class DocsReferenceEsApplication {
    public static void main(String[] args) {
        SpringApplication.run(DocsReferenceEsApplication.class, args);
    }
}
```

This class bootstraps the Spring application context and runs the embedded web server.

## Conclusion

This Spring AI Chatbot is designed to answer questions from various sources. Once a document is ingested 
(with its embeddings stored in Elasticsearch), users can query it on any subject covered in the document. 
This project highlights the flexibility of Spring AI, which significantly reduces the complexity of 
implementing Retrieval-Augmented Generation (RAG) solutions. 

Happy coding!
