# Data fetching and caching

### Data fetching

When you call the validation endpoint for some column, application does the next steps:
- loads data from left datasource (async);
- loads data from right datasource (async);
- caches the loaded data in column data storage for both datasources;
- validates fetched data and returns a validation result. 

Application fetches data from a datasource according to its concurrency factor (each datasource has its own max connections property).
That means when multiple users call the validation endpoint simultaneously for same datasource, their requests will be placed in a queue and 
processed one-by-one according to the max number of connections allowed for this datasource. In order to avoid delays when multiple users
works with application simultaneously (especially for datasources with low concurrent factor) or performing validation for multiple 
columns / tables, application allows you to preload the data from datasource asynchronously.


### Pre-fetching

Application allows you to preload the data from datasources. You can define tables / columns you want to preload and application will do this
in background.

Application loads column data one-by-one. In order not to block user requests in runtime while prefetching is running, application has a queue of requests
that performs data loading according to the priorities. Requests can be one of the next two types:
- validation requests called by users in runtime (have the highest priority);
- data preload requests (have the lowest priority).

That means when multiple users call validation endpoint for some columns their requests will be executed with the highest priority.
Preload requests will be executed with the lowest priority and only if there is available connection to a datasource.