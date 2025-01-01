package com.geo.docsReferenceEs.service.impl;

import com.geo.docsReferenceEs.exception.IngestionDocumentException;
import com.geo.docsReferenceEs.service.IIngestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class IngestServiceImpl implements IIngestService {

    private static final Logger log = LoggerFactory.getLogger(IngestServiceImpl.class);

    private final VectorStore vectorStore;

    public IngestServiceImpl(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void ingestDocument(String path) {
        log.info("Loading Spring Boot Reference PDF into Vector Store");
        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().withNumberOfBottomTextLinesToDelete(0)
                        .withNumberOfTopPagesToSkipBeforeDelete(0)
                        .build())
                .withPagesPerDocument(1)
                .build();

        try {
            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(path, config);
            TokenTextSplitter textSplitter = new TokenTextSplitter();
            vectorStore.accept(textSplitter.apply(pdfReader.get()));
            log.info("Reference Document was successfully stored in the vector database");
        } catch (Exception ex) {
            log.error("An Error occured while ingesting document.", ex);
            throw new IngestionDocumentException("Could not ingest the document. Please contact the Administrator");
        }
    }
}
