package azure.storage.logging.entity

import azure.storage.test.AbstractIntSpec
import com.microsoft.azure.storage.table.CloudTable
import com.microsoft.azure.storage.table.TableOperation
import com.microsoft.azure.storage.table.TableQuery

import java.time.LocalDateTime

import static azure.storage.System.*

class ActionLogSystemSpec extends AbstractIntSpec {

    def "should save ActionLog entity to table storage"() {
        given: "save entity"
        def actionLog = new ActionLog(
                ActionCategory.CREATE,
                [new ActionLog.ActionObject(20L, ["id"].toSet())].toSet(),
                LocalDateTime.now(),
                SYSTEM01,
                "user"
        )

        ActionLogWrapper actionLogWrapper = new ActionLogWrapper(actionLog)
        def tableName = "ActionLog"
        saveActionLogToTable(tableName, actionLogWrapper)
        when: "return entity from table storage"
        def logWrapper = getActionLogFromTableByPartitionAndRowKey(tableName, SYSTEM01.name(), "user", actionLogWrapper.getRowKey())
        ActionLog log = new ActionLog(logWrapper[0])
        then:
        with(log) {
            it.category == ActionCategory.CREATE
            it.system == SYSTEM01
            it.user == "user"
        }
    }

    def saveActionLogToTable(String tableName, ActionLogWrapper actionLogWrapper) {
        CloudTable table = tableStorageClient.getTableByName(tableName)

        TableOperation insertActionLog = TableOperation.insert(actionLogWrapper)
        table.execute(insertActionLog)
    }

    def getActionLogFromTableByPartitionAndRowKey(String tableName, String system, String user, String uuid) {
        CloudTable table = tableStorageClient.getTableByName(tableName)

        String filter =
                TableQuery.combineFilters(
                        TableQuery.generateFilterCondition("PartitionKey", TableQuery.QueryComparisons.EQUAL, String.format("%s_%s", system, user)),
                        TableQuery.Operators.AND,
                        TableQuery.generateFilterCondition("RowKey", TableQuery.QueryComparisons.EQUAL, uuid)
                )

        TableQuery<ActionLogWrapper> query = TableQuery.from(ActionLogWrapper.class).where(filter)
        table.execute(query)
    }
}
