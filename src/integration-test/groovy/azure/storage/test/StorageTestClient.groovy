package azure.storage.test

import azure.storage.StorageProps
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.table.CloudTable
import com.microsoft.azure.storage.table.CloudTableClient
import org.springframework.stereotype.Component

@Component
class StorageTestClient {

    final CloudTableClient tableClient

    StorageTestClient(StorageProps storageProps) {
        def connectionString = storageProps.buildConnectionString()
        CloudStorageAccount account = CloudStorageAccount.parse(connectionString)
        tableClient = account.createCloudTableClient()
    }

    def getTableByName(tableName) {
        CloudTable table = tableClient.getTableReference(tableName)
        table.createIfNotExists()
        table
    }
}
