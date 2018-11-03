package azure.storage;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class StorageProps {
    private static final String CONNECTION_PATTERN = "DefaultEndpointsProtocol=%s;AccountName=%s;AccountKey=%s;";
    private static final String EXTENDED_CONNECTION_PATTERN = CONNECTION_PATTERN +
            "BlobEndpoint=%s://%s;" +
            "TableEndpoint=%s://%s;";
    private final String endpointsProtocol;
    private final String storageAccount;
    private final String storageKey;
    private final String blobStorageUrl;
    private final String tableStorageUrl;

    public StorageProps(
            @Value("${cloud.azure.storage.protocol}") String endpointsProtocol,
            @Value("${cloud.azure.storage.account}") String storageAccount,
            @Value("${cloud.azure.storage.key}") String storageKey,
            @Value("${cloud.azure.storage.blob.url}") String blobStorageUrl,
            @Value("${cloud.azure.storage.table.url}") String tableStorageUrl
    ) {
        this.endpointsProtocol = endpointsProtocol;
        this.storageAccount = storageAccount;
        this.storageKey = storageKey;
        this.blobStorageUrl = blobStorageUrl;
        this.tableStorageUrl = tableStorageUrl;
    }

    public String buildConnectionString() {
        return String.format(EXTENDED_CONNECTION_PATTERN,
                endpointsProtocol, storageAccount, storageKey,
                endpointsProtocol, blobStorageUrl,
                endpointsProtocol, tableStorageUrl);
    }
}
