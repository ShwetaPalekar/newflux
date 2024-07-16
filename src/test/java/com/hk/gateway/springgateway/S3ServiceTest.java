package com.hk.gateway.springgateway;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.InputStream;

public class S3ServiceTest {

    private final S3Service s3Service = Mockito.mock(S3Service.class);

    @Test
    public void testDownloadFromS3() {
        String objectKey = "test-key";
        InputStream inputStream = Mockito.mock(InputStream.class);

        Mockito.when(s3Service.downloadFromS3(Flux.just(objectKey))).thenReturn(Flux.just(inputStream));

        StepVerifier.create(s3Service.downloadFromS3(Flux.just(objectKey)))
                .expectNext(inputStream)
                .verifyComplete();
    }
}
