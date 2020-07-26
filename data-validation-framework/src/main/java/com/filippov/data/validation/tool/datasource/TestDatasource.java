package com.filippov.data.validation.tool.datasource;

import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceConfig;
import com.filippov.data.validation.tool.datasource.model.DatasourceMetadata;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.model.DataType;

import java.util.List;

import static java.util.Arrays.asList;

public class TestDatasource implements Datasource {

    private static final DatasourceConfig CONFIG = DatasourceConfig.builder()
            .datasourceType(DatasourceType.TEST_DATASOURCE)
            .config("connection string")
            .build();

    private static final List<DatasourceTable> TABLES = asList(
            DatasourceTable.builder()
                    .name("users")
                    .primaryKey("userId")
                    .columns(asList("userId", "username", "birthDate", "email", "password"))
                    .build(),
            DatasourceTable.builder()
                    .name("userGroups")
                    .primaryKey("userGroupId")
                    .columns(asList("userGroupId", "groupName", "permissions", "parent"))
                    .build(),
            DatasourceTable.builder()
                    .name("departments")
                    .primaryKey("departmentId")
                    .columns(asList("departmentId", "departmentName", "users", "manager", "employees", "phoneNumber"))
                    .build(),
            DatasourceTable.builder()
                    .name("messages")
                    .primaryKey("messageId")
                    .columns(asList("messageId", "text", "from", "to", "date", "read", "active", "expireDate"))
                    .build());

    private static final List<DatasourceColumn> COLUMNS = asList(
            DatasourceColumn.builder().tableName("users").name("userId").dataType(DataType.INTEGER).build(),
            DatasourceColumn.builder().tableName("users").name("username").dataType(DataType.STRING).build(),
            DatasourceColumn.builder().tableName("users").name("birthDate").dataType(DataType.STRING).build(),
            DatasourceColumn.builder().tableName("users").name("email").dataType(DataType.STRING).build(),
            DatasourceColumn.builder().tableName("users").name("password").dataType(DataType.STRING).build(),

            DatasourceColumn.builder().tableName("userGroups").name("userGroupId").dataType(DataType.INTEGER).build(),
            DatasourceColumn.builder().tableName("userGroups").name("groupName").dataType(DataType.STRING).build(),
            DatasourceColumn.builder().tableName("userGroups").name("permissions").dataType(DataType.STRING).build(),
            DatasourceColumn.builder().tableName("userGroups").name("parent").dataType(DataType.STRING).build(),

            DatasourceColumn.builder().tableName("departments").name("departmentId").dataType(DataType.INTEGER).build(),
            DatasourceColumn.builder().tableName("departments").name("departmentName").dataType(DataType.STRING).build(),
            DatasourceColumn.builder().tableName("departments").name("users").dataType(DataType.STRING).build(),
            DatasourceColumn.builder().tableName("departments").name("manager").dataType(DataType.STRING).build(),
            DatasourceColumn.builder().tableName("departments").name("employees").dataType(DataType.STRING).build(),
            DatasourceColumn.builder().tableName("departments").name("phoneNumber").dataType(DataType.STRING).build(),

            DatasourceColumn.builder().tableName("messages").name("messageId").dataType(DataType.INTEGER).build(),
            DatasourceColumn.builder().tableName("messages").name("text").dataType(DataType.STRING).build(),
            DatasourceColumn.builder().tableName("messages").name("from").dataType(DataType.STRING).build(),
            DatasourceColumn.builder().tableName("messages").name("to").dataType(DataType.STRING).build(),
            DatasourceColumn.builder().tableName("messages").name("date").dataType(DataType.STRING).build(),
            DatasourceColumn.builder().tableName("messages").name("read").dataType(DataType.STRING).build(),
            DatasourceColumn.builder().tableName("messages").name("active").dataType(DataType.STRING).build(),
            DatasourceColumn.builder().tableName("messages").name("expireDate").dataType(DataType.STRING).build());
    private static final DatasourceMetadata METADATA = DatasourceMetadata.builder()
            .tables(TABLES)
            .columns(COLUMNS)
            .build();

    @Override
    public DatasourceConfig getConfig() {
        return CONFIG;
    }

    @Override
    public DatasourceMetadata getMetadata() {
        return METADATA;
    }

    @Override
    public <K, V> ColumnData<K, V> getColumnData(DatasourceQuery query) {
        return null;
    }
}
