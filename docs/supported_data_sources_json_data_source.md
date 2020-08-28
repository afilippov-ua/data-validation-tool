# JSON data source

JSON data source allows you to load data from JSON file.

#### Configuration

Test in-memory datasource has next configuration properties:
- metadataFilePath: path to JSON file with metadata;
- dataFilePath: path to JSON file with data;
- maxConnections: concurrency factor.

#### Metadata JSON file example:

```
{
  "tables": [
    {
      "name": "users",
      "primaryKey": "id",
      "columns": [
        "id",
        "username",
        "password"
      ]
    },
    {
      "name": "departments",
      "primaryKey": "id",
      "columns": [
        "id",
        "name",
        "numberOfEmployees"
      ]
    }
  ],
  "columns": [
    {
      "tableName": "users",
      "name": "id",
      "dataType": "INTEGER"
    },
    {
      "tableName": "users",
      "name": "username",
      "dataType": "STRING"
    },
    {
      "tableName": "users",
      "name": "password",
      "dataType": "STRING"
    },
    {
      "tableName": "departments",
      "name": "id",
      "dataType": "INTEGER"
    },
    {
      "tableName": "departments",
      "name": "name",
      "dataType": "STRING"
    },
    {
      "tableName": "departments",
      "name": "numberOfEmployees",
      "dataType": "INTEGER"
    }
  ]
}
```

#### Data JSON file example:

```
{
  "users": {
    "id": {
      "keyColumn": {
        "tableName": "users",
        "name": "id",
        "dataType": "INTEGER"
      },
      "dataColumn": {
        "tableName": "users",
        "name": "id",
        "dataType": "INTEGER"
      },
      "keys": [
        1,
        2,
        3,
        4,
        5,
        7
      ],
      "values": [
        1,
        2,
        3,
        4,
        5,
        7
      ]
    },
    "username": {
      "keyColumn": {
        "tableName": "users",
        "name": "id",
        "dataType": "INTEGER"
      },
      "dataColumn": {
        "tableName": "users",
        "name": "username",
        "dataType": "STRING"
      },
      "keys": [
        1,
        2,
        3,
        4,
        5,
        7
      ],
      "values": [
        "user1",
        "user2",
        "user3",
        "user4",
        "user5",
        "user7"
      ]
    },
    "password": {
      "keyColumn": {
        "tableName": "users",
        "name": "id",
        "dataType": "INTEGER"
      },
      "dataColumn": {
        "tableName": "users",
        "name": "password",
        "dataType": "STRING"
      },
      "keys": [
        1,
        2,
        3,
        4,
        5,
        7
      ],
      "values": [
        "pass1",
        "pass2",
        "pass3",
        "pass4",
        "pass5",
        "pass7"
      ]
    }
  },
  "departments": {
    "id": {
      "primaryKey": {
        "tableName": "departments",
        "name": "id",
        "dataType": "INTEGER"
      },
      "column": {
        "tableName": "departments",
        "name": "id",
        "dataType": "INTEGER"
      },
      "keys": [
        10,
        20,
        30,
        40,
        50,
        70
      ],
      "values": [
        10,
        20,
        30,
        40,
        50,
        70
      ]
    },
    "name": {
      "primaryKey": {
        "tableName": "departments",
        "name": "id",
        "dataType": "INTEGER"
      },
      "column": {
        "tableName": "departments",
        "name": "name",
        "dataType": "STRING"
      },
      "keys": [
        10,
        20,
        30,
        40,
        50,
        70
      ],
      "values": [
        "dep1",
        "dep2",
        "dep3",
        "dep4",
        "dep5",
        "dep7"
      ]
    },
    "numberOfEmployees": {
      "primaryKey": {
        "tableName": "departments",
        "name": "id",
        "dataType": "INTEGER"
      },
      "column": {
        "tableName": "departments",
        "name": "numberOfEmployees",
        "dataType": "INTEGER"
      },
      "keys": [
        10,
        20,
        30,
        40,
        50,
        70
      ],
      "values": [
        25,
        50,
        75,
        100,
        125,
        175
      ]
    }
  }
}
```

