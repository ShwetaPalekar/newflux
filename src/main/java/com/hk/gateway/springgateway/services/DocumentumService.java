package com.hk.gateway.springgateway.services;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionFactory;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.common.DfClient;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLoginInfo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;

@Service
public class DocumentumService {

    private static final String REPOSITORY_NAME = "your-repository-name";
    private static final String DOCUMENTUM_USER = "your-username";
    private static final String DOCUMENTUM_PASSWORD = "your-password";
    private static final String DOCUMENTUM_DOCUMENT_TYPE = "dm_document";

    private IDfSession getSession() throws DfException {
        IDfSessionFactory sessionFactory = DfClient.getLocalClient().newSessionFactory();
        DfLoginInfo loginInfo = new DfLoginInfo();
        loginInfo.setUser(DOCUMENTUM_USER);
        loginInfo.setPassword(DOCUMENTUM_PASSWORD);
        return sessionFactory.getSession(REPOSITORY_NAME, loginInfo);
    }

    public Flux<Void> uploadToDocumentum(Flux<InputStream> inputStreams, String documentName, String contentType) {
        return inputStreams.flatMap(inputStream -> Mono.fromRunnable(() -> {
            try (IDfSession session = getSession()) {
                IDfDocument document = (IDfDocument) session.newObject(DOCUMENTUM_DOCUMENT_TYPE);
                document.setObjectName(documentName);
                document.setContentType(contentType);
                document.setContent(inputStream);
                document.save();
            } catch (DfException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}

