package azure.storage.logging.entity;

import azure.storage.System;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.UUID;

@Getter
@ToString
public class ActionLog  {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final String id = UUID.randomUUID().toString();
    private final ActionCategory category;
    private final Set<ActionObject> actionObjects;
    private final LocalDateTime actionDateTime;
    private final System system;
    private final String user;

    public ActionLog(ActionCategory category, Set<ActionObject> actionObjects, LocalDateTime actionDateTime, System system, String user) {
        this.category = category;
        this.actionObjects = actionObjects;
        this.actionDateTime = actionDateTime;
        this.system = system;
        this.user = user;
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ActionObject {
        private Long id;
        private Set<String> fields;
    }

    public ActionLog(ActionLogWrapper actionLogWrapper) {
        this.category = ActionCategory.valueOf(actionLogWrapper.getCategory());
        this.actionObjects = deSerializeActionObjects(actionLogWrapper.getActionObjects());
        this.actionDateTime = toLocalDateTime(actionLogWrapper);
        this.system = System.valueOf(actionLogWrapper.getSystem());
        this.user = actionLogWrapper.getUser();
    }

    private static LocalDateTime toLocalDateTime(ActionLogWrapper actionLogWrapper) {
        Instant instant = Instant.ofEpochMilli(actionLogWrapper.getActionDateTime().getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    private Set<ActionObject> deSerializeActionObjects(String actionObjects) {
        try {
            return OBJECT_MAPPER.readValue(
                    actionObjects, new TypeReference<Set<ActionObject>>() {
                    }
            );
        } catch (IOException e) {
            throw new MessageJsonProcessingException("Message JSON cannot be processed", e);
        }
    }
}