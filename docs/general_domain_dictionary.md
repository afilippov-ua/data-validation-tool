# Domain dictionary

**Datasource** - the main abstraction that represents the source of a columnar data. Datasource has its own metadata and concurrency 
factor (how many concurrent threads can be used for fetching data from the datasource).

**Datasource type** - possible predefined values: TEST_IN_MEMORY_DATASOURCE, JSON_DATASOURCE.

**Datasource metadata** - metadata consists of "Datasource tables" and "Datasource columns".

**Datasource table** - representation of a table in datasource. Each table has to consist at least one datasource column. 
Table must have table name and primary key column.

**Datasource column** - representation of a datasource column that belongs to a specific table. Datasource column must have column name and specific data type.

**Metadata** - metadata in terms of data validation tool is a pair of two datasource metadata. It consists of table pairs and column pairs. 

**Table pair** - abstraction that represents a pair of tables: table from left datasource and table from right datasource. 
Pair of tables and their column pairs are being validated by the data validation tool.

**Column pair** - abstraction that represents a pair of columns: column from left datasource table and column from right datasource table. 
Column pair also stores left and right datasource column transformers. Column pairs are being validated by the data validation tool. 

**Transformer** - simple function that accepts one argument and produces a result. Transformers used on validation stage for data type transformation or
value transformation in order to compare values in the same format for both datasources.

**Column data** - column data that represented as map of key -> value pairs (primary key -> data). After fetching data from a datasource,
application creates a new ColumnData from primary column and data column for the given table. 

**Column data cache** - representation of a storage for caching ColumnData fetched from a datasource. Current column data cache implementations 
is "InMemoryColumnDataCache" that stores all column data in memory of your machine. In future additional storages will be implemented. 

**Data storage** - abstraction that provides fetching API and caching API using Datasource and ColumnDataCache. 
This component has its own ThreadPool for fetching and caching data according to the datasource concurrency factor. 

**Data validator** - component which performs data validation using two ColumnData's and produces a ValidationResult.
Validator applies transformers defined for every column pair which we use for comparison and only after that compares two values.
If data is not the same for both datasources it marks the id of this row as failed. Validation result contains failed ids.

**Validation result** - abstraction that represents a list of failed ids provided by DataValidator. Failed ids are the ids of rows which data
is differ in both datasources.

**Data binder** - component that accepts an id of a specific entity and returns a row-pair representation which contains: 
id, left raw value, right raw value, left value after transformation, right value after transformation. Binder is used for building the
validation result for the end user, and also for building the final discrepancies report.

**Workspace** - a pair of datasources that represents pair of datasources for validation. It has unique id, name, used datasources and their concurrency factors.