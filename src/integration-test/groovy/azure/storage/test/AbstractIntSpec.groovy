package azure.storage.test

import azure.storage.Application
import azure.storage.test.Storage
import azure.storage.test.StorageTestClient
import azure.storage.test.TestContextInitializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest(classes = [Application.class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContextInitializer.class)
abstract class AbstractIntSpec extends Specification {

    def setupSpec() {
        Storage.init()
    }

    @Autowired
    StorageTestClient tableStorageClient
}
