# Test in-memory datasource 

Test in-memory datasource was designed for application testing in a sandbox mode. It has very simple static data set and allows 
you to test the API and see how the validation results are look like. You can expand the data source if needed.  

#### Configuration

Test in-memory datasource has next configuration properties:
- relationType: LEFT or RIGHT (define what data set will be used for this datasource in runtime. You can find exact data sets below);
- maxConnections: concurrency factor.

#### Left datasource data:

###### Users table:
|ID|Username|Password|
|---|:---:|---:|
|1|user1|pass1|
|2|user2|pass2|
|3|user3|pass3|
|4|user4|pass4|
|5|user5|pass5|
|7|user7|pass7|

###### Departments table:
|ID|Name|Number of employees|
|---|:---:|---:|
|10|dep1|25|
|20|dep2|50|
|30|dep3|75|
|40|dep4|100|
|50|dep5|125|
|70|dep7|175|


#### Right datasource data:

###### Users table:
|ID|Username|Password|
|---|:---:|---:|
|1|user1|pass1|
|2|user2_changed|pass2_changed|
|3|user3|pass3|
|4|user4|pass4|
|5|user5|pass5|
|6|user6|pass6|

###### Departments table:
|ID|Name|Number of employees|
|---|:---:|---:|
|10|dep1|25|
|20|dep2_changed|-50|
|30|dep3|75|
|40|dep4|100|
|50|dep5|125|
|60|dep6|150|