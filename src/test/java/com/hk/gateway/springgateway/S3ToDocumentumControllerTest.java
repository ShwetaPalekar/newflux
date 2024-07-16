package com.hk.gateway.springgateway;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class S3ToDocumentumControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private S3Service s3Service;

    @MockBean
    private DocumentumService documentumService;

    @Test
    public void testTransferMultipleDocuments() {
        List<String> objectKeys = List.of("test-key1", "test-key2");
        InputStream inputStream1 = Mockito.mock(InputStream.class);
        InputStream inputStream2 = Mockito.mock(InputStream.class);

        Mockito.when(s3Service.downloadFromS3(Flux.fromIterable(objectKeys))).thenReturn(Flux.just(inputStream1, inputStream2));
        Mockito.when(documentumService.uploadToDocumentum(Flux.just(inputStream1, inputStream2), "your-document-name", "pdf")).thenReturn(Flux.empty());

        webTestClient.post().uri("/transferMultiple")
                .bodyValue(objectKeys)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Documents transferred successfully");
    }
}
