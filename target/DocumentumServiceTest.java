import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ConcurrentLinkedQueue;

public Result streamFilesToDocumentum(List<String> keys, String bucketName) {
    Result result = new Result();
    result.setData("Some data");

    ExecutorService executor = Executors.newFixedThreadPool(5); // Define pool size based on your needs
    List<AnotherClass> anotherClassList = new ArrayList<>();
    ConcurrentLinkedQueue<IDfSysObject> successfullyCommittedDocs = new ConcurrentLinkedQueue<>();
    List<Future<Boolean>> futures = new ArrayList<>();
    boolean allSuccessful = true;

    // Process each key in a separate thread
    for (String key : keys) {
        futures.add(executor.submit(() -> {
            try (IDfSession session = sessionManager.getSession("repo")) {
                session.beginTransaction();

                S3Object s3Object = amazonS3.getObject(bucketName, key);
                InputStream inputStream = s3Object.getObjectContent();

                IDfSysObject newDoc = prepareAndCommitDocument(session, inputStream, key);
                
                session.commitTransaction();
                successfullyCommittedDocs.add(newDoc);

                AnotherClass anotherClass = new AnotherClass();
                anotherClass.setMoreData("Successfully processed and committed file: " + key);
                synchronized (anotherClassList) {
                    anotherClassList.add(anotherClass);
                }
                return true; // Success
            } catch (Exception e) {
                System.err.println("Error processing file: " + key + " - " + e.getMessage());
                AnotherClass failedClass = new AnotherClass();
                failedClass.setMoreData("Failed to process file: " + key + " due to error: " + e.getMessage());
                synchronized (anotherClassList) {
                    anotherClassList.add(failedClass);
                }
                return false; // Failure
            }
        }));
    }

    // Check if all threads were successful
    for (Future<Boolean> future : futures) {
        try {
            if (!future.get()) {
                allSuccessful = false;
                break;
            }
        } catch (Exception e) {
            allSuccessful = false;
            e.printStackTrace();
        }
    }

    // Rollback if any failure occurred
    if (!allSuccessful) {
        try (IDfSession session = sessionManager.getSession("repo")) {
            for (IDfSysObject doc : successfullyCommittedDocs) {
                try {
                    doc.destroy();
                } catch (Exception e) {
                    System.err.println("Error rolling back document: " + doc.getObjectName() + " - " + e.getMessage());
                }
            }
            System.err.println("Rollback completed due to errors during processing.");
        }
    }

    executor.shutdown(); // Ensure the executor shuts down

    result.setAnotherClass(anotherClassList);
    return result;
}

private IDfSysObject prepareAndCommitDocument(IDfSession session, InputStream input, String key) throws Exception {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    byte[] data = new byte[65536]; // 64 KB buffer
    while ((nRead = input.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
    }

    IDfSysObject newDoc = (IDfSysObject) session.newObject("dm_document");
    newDoc.setObjectName(key);
    newDoc.setContentType("application/octet-stream");
    newDoc.setContent(buffer);

    // Save and commit immediately
    newDoc.save();

    return newDoc;
}
