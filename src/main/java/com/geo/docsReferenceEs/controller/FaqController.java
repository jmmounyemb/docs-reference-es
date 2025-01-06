package com.geo.docsReferenceEs.controller;

import com.geo.docsReferenceEs.model.Document;
import com.geo.docsReferenceEs.service.IFaqService;
import com.geo.docsReferenceEs.service.IIngestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/faq")
public class FaqController {

    private IFaqService faqService;
    private IIngestService ingestService;

    public FaqController(IFaqService faqService, IIngestService ingestService) {
        this.faqService = faqService;
        this.ingestService = ingestService;
    }

    @GetMapping("")
    public ResponseEntity<String> question(@RequestParam(defaultValue = "What is Spring Boot") String question) {
        return ResponseEntity.ok(faqService.faqLLM(question));
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingestDocument(@RequestBody Document document) {
        ingestService.ingestDocument(document.path());
        return ResponseEntity.ok().body("Success");
    }
}
