package azure.storage.test

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer

class Storage {

    private static final Logger log = LoggerFactory.getLogger(Storage.class)

    private static final int BLOB_STORAGE_PORT = 10000
    private static final int TABLE_STORAGE_PORT = 10002
    private static final String BLOB_STORAGE_URL_KEY = "cloud.azure.storage.blob.url"
    private static final String TABLE_STORAGE_URL_KEY = "cloud.azure.storage.table.url"

    private static GenericContainer instance = new GenericContainer("arafato/azurite:latest")
            .withExposedPorts(BLOB_STORAGE_PORT, TABLE_STORAGE_PORT)

    static init() {
        startStorage()
        initEnvironmentProperties()

        log.info("Storage emulator initialized")
    }

    private static void startStorage() {
        if (!Boolean.TRUE.equals(instance.isRunning())) {
            log.info("Storage emulator starting...")
            instance.start()
            log.info("Storage emulator started")
        }
    }

    private static void initEnvironmentProperties() {
        def host = instance.getContainerIpAddress()
        TestContextInitializer.addEnvironmentProperty(BLOB_STORAGE_URL_KEY,
                host + ":" + instance.getMappedPort(BLOB_STORAGE_PORT).toString() + "/devstoreaccount1")
        TestContextInitializer.addEnvironmentProperty(TABLE_STORAGE_URL_KEY,
                host + ":" + instance.getMappedPort(TABLE_STORAGE_PORT).toString() + "/devstoreaccount1")
    }
}
