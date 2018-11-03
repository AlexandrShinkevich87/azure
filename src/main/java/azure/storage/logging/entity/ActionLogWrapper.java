package azure.storage.logging.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.storage.table.TableServiceEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

public class ActionLogWrapper extends TableServiceEntity {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Getter
    @Setter
    private String category;
    @Getter
    @Setter
    private String actionObjects;
    @Getter
    @Setter
    private Date actionDateTime;
    @Getter
    @Setter
    private String system;
    @Getter
    @Setter
    private String user;

    public ActionLogWrapper() {
    }

    public ActionLogWrapper(ActionLog log) {
        this.category = log.getCategory().name();
        this.actionObjects = serializeActionObjects(log.getActionObjects());
        this.actionDateTime = toDate(log);
        this.system = log.getSystem().name();
        this.user = log.getUser();

        this.partitionKey = buildPartitionKey();
        this.rowKey = log.getId();
    }

    private Date toDate(ActionLog log) {
        val instant = log.getActionDateTime().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    private static String serializeActionObjects(Set<ActionLog.ActionObject> actionObjects) {
        try {
            return OBJECT_MAPPER.writeValueAsString(actionObjects);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Action objects cannot be serialized", e);
        }
    }

    private String buildPartitionKey() {
        String key;
        if (StringUtils.isNotBlank(this.user)) {
            key = this.system + "_" + this.user;
        } else {
            key = this.system;
        }
        return key;
    }

}
