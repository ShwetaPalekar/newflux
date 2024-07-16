package com.hk.gateway.springgateway;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.InputStream;

public class DocumentumServiceTest {

    private final DocumentumService documentumService = Mockito.mock(DocumentumService.class);

    @Test
    public void testUploadToDocumentum() {
        InputStream inputStream = Mockito.mock(InputStream.class);
        String documentName = "test-document";
        String contentType = "pdf";

        Mockito.when(documentumService.uploadToDocumentum(Flux.just(inputStream), documentName, contentType)).thenReturn(Flux.empty());

        StepVerifier.create(documentumService.uploadToDocumentum(Flux.just(inputStream), documentName, contentType))
                .verifyComplete();
    }
}
