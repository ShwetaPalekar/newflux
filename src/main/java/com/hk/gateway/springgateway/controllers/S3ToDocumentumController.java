package com.hk.gateway.springgateway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class S3ToDocumentumController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private DocumentumService documentumService;

    @PostMapping("/transferMultiple")
    public Mono<String> transferMultipleDocuments(@RequestBody List<String> objectKeys) {
        String documentName = "your-document-name";
        String contentType = "pdf";

        Flux<String> objectKeysFlux = Flux.fromIterable(objectKeys);
        Flux<InputStream> inputStreams = s3Service.downloadFromS3(objectKeysFlux);

        return documentumService.uploadToDocumentum(inputStreams, documentName, contentType)
                .then(Mono.just("Documents transferred successfully"));
    }
}


