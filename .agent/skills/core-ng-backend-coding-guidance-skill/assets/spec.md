# Core-NG Framework Specification

A comprehensive specification for Core-NG framework. This document is organized in two parts:
- **Part 1: Core Rules & Patterns** - Essential rules you MUST follow (always read)
- **Part 2: API Reference** - Detailed method signatures and configurations (read when needed)

---

# Part 1: Core Rules & Patterns

This section contains the essential rules and patterns. **Always read this section before writing Core-NG code.**

---

## 1. Framework Constraints (MUST Follow)

| Constraint | Description |
|------------|-------------|
| Single constructor | Only ONE public constructor per class |
| Field injection only | No constructor injection; use `@Inject` on fields |
| Explicit binding | All services MUST be registered via `bind()` in Module |
| @Id type | MongoDB ID must be `ObjectId` or `String` |
| Named handlers | Message/Job handlers must be named classes (NOT lambda/anonymous) |
| Static paths | WebSocket/SSE paths cannot have dynamic parameters (`:id`) |
| Annotation separation | `@Field` for MongoDB, `@Property` for API/JSON, `@Column` for SQL |
| Inner class registration | ALL static inner classes need `view()` registration for MongoDB |
| Impl package location | WebService impl classes MUST be placed in `web` package |
| Root build.gradle only | All build configuration MUST be in root `build.gradle`, subprojects have NONE |

---

## 2. Essential Patterns

### 2.1 Module Setup
```java
public class ItemModule extends Module {
    @Override
    protected void initialize() {
        // 1. MongoDB collections (register inner classes!)
        mongo().collection(Item.class);
        mongo().view(Item.Ingredient.class);  // REQUIRED for inner classes

        // 2. Bind services (dependencies first)
        bind(ItemService.class);

        // 3. Register API
        api().service(ItemWebService.class, bind(ItemWebServiceImpl.class));
    }
}
```

### 2.2 MongoDB Entity
```java
@Collection(name = "items")
public class Item {
    @Id
    public String id;

    @Field(name = "name")
    public String name;

    @Field(name = "status")
    public ItemStatus status;

    @Field(name = "ingredients")
    public List<Ingredient> ingredients;

    public static class Ingredient {
        @Field(name = "item_id")
        public String itemId;

        @Field(name = "qty")
        public BigDecimal quantity;
    }
}

public enum ItemStatus {
    @MongoEnumValue("ACTIVE")
    ACTIVE,
    @MongoEnumValue("INACTIVE")
    INACTIVE
}
```

### 2.3 SQL Entity
```java
@Table(name = "items")
public class ItemEntity {
    @PrimaryKey(autoIncrement = true)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "config", json = true)  // JSON serialized column
    public ItemConfig config;
}

public enum ItemStatus {
    @DBEnumValue("ACTIVE")
    ACTIVE,
    @DBEnumValue("INACTIVE")
    INACTIVE
}
```

### 2.4 WebService Interface
```java
public interface ItemWebService {
    @GET
    @Path("/api/items/:id")
    GetItemResponse get(@PathParam("id") String id);

    @POST
    @Path("/api/items")
    CreateItemResponse create(CreateItemRequest request);

    @PUT
    @Path("/api/items/:id")
    void update(@PathParam("id") String id, UpdateItemRequest request);

    @DELETE
    @Path("/api/items/:id")
    @ResponseStatus(HTTPStatus.NO_CONTENT)
    void delete(@PathParam("id") String id);
}
```

**Note on Collection Response Types:**
WebService interface methods cannot return `List<T>` directly. For collection responses, use a response bean class with `@Property` annotations:
```java
// WRONG - will cause runtime error
List<RoomView> listRooms(@PathParam("companyId") Long companyId);

// CORRECT - use a response bean with @Property annotations
RoomListResponse listRooms(@PathParam("companyId") Long companyId);

// Response bean example - must only have public default constructor
public class RoomListResponse {
    @Property(name = "items")
    public List<RoomView> items;

    public RoomListResponse() { }
}
```

### 2.5 Request/Response Beans
```java
public class CreateItemRequest {
    @NotNull
    @NotBlank
    @Property(name = "name")
    public String name;

    @NotNull
    @Property(name = "status")
    public ItemStatus status;

    @Size(max = 500)
    @Property(name = "description")
    public String description;
}

// Query parameters
public class SearchRequest {
    @QueryParam(name = "keyword")
    public String keyword;

    @QueryParam(name = "page_size")
    public Integer pageSize;
}
//WRONG - directly in interface is unsupported
List<RoomView> listRooms(@QueryParam("company_id") Long companyId);
```

### 2.6 Service with Dependency Injection
```java
public class ItemService {
    @Inject
    MongoCollection<Item> itemCollection;

    @Inject
    MessagePublisher<ItemChangedEvent> publisher;

    public Item get(String id) {
        return itemCollection.get(id)
            .orElseThrow(() -> new NotFoundException("item not found, id=" + id));
    }

    public void create(Item item) {
        item.id = UUID.randomUUID().toString();
        itemCollection.insert(item);
        publisher.publish(item.id, new ItemChangedEvent(item.id, "CREATED"));
    }
}
```

### 2.7 Kafka Handler (MUST be named class)
```java
// CORRECT - Named class
public class ItemChangedHandler implements MessageHandler<ItemChangedEvent> {
    @Inject
    ItemService itemService;

    @Override
    public void handle(@Nullable String key, ItemChangedEvent message) {
        itemService.processEvent(message);
    }
}

// WRONG - Lambda not supported
kafka.subscribe("topic", Event.class, (key, msg) -> process(msg));  // ERROR!
```

### 2.8 Scheduled Job (MUST be named class)
```java
// CORRECT - Named class
public class CleanupJob implements Job {
    @Inject
    CleanupService cleanupService;

    @Override
    public void execute(JobContext context) {
        cleanupService.cleanup();
    }
}

// Registration
schedule().dailyAt("cleanup", new CleanupJob(), LocalTime.of(2, 0));

// WRONG - Lambda not supported
schedule().dailyAt("job", ctx -> doWork(), LocalTime.NOON);  // ERROR!
```

---

## 3. Common Mistakes & Fixes

| Mistake | Error | Fix |
|---------|-------|-----|
| Missing `view()` for inner class | "No codec found for class X" | Add `mongo().view(Entity.InnerClass.class)` |
| Missing `bind()` for service | "No binding found for class X" | Add `bind(ServiceClass.class)` in Module |
| Lambda for handler | Compile/runtime error | Use named class implementing `MessageHandler<T>` |
| Constructor injection | Dependencies are null | Use field injection with `@Inject` |
| Multiple public constructors | Framework error at startup | Keep only ONE public constructor |
| Missing `@Field`/`@Property` | Field not serialized | Add annotation - required for all fields |
| `@Field` on API bean | Wrong annotation | Use `@Property` for API, `@Field` for MongoDB |
| Using `*` in SQL select | Framework rejects query | List columns explicitly |
| Cache loader returns null | Exception | Use wrapper class for nullable values |
| Not calling `bind()` before `api().service()` | Dependencies not injected | `api().service(Interface.class, bind(Impl.class))` |

---

## 4. Exception Handling

| Exception | HTTP Status | Use Case |
|-----------|-------------|----------|
| `BadRequestException` | 400 | Invalid user input |
| `UnauthorizedException` | 401 | Authentication required |
| `ForbiddenException` | 403 | Permission denied |
| `NotFoundException` | 404 | Resource not found |
| `ConflictException` | 409 | Business rule violation |
| `TooManyRequestsException` | 429 | Rate limit exceeded |

```java
// Input validation
if (invalid) throw new BadRequestException("reason", "ERROR_CODE");

// Resource lookup
Item item = collection.get(id)
    .orElseThrow(() -> new NotFoundException("item not found, id=" + id));

// Business rules
if (violatesRule) throw new ConflictException("cannot delete active item", "ITEM_ACTIVE");
```

---

## 5. Annotation Summary

### MongoDB Annotations
| Annotation | Target | Required | Purpose |
|------------|--------|----------|---------|
| `@Collection(name)` | Class | Yes | MongoDB collection name |
| `@Id` | Field | Yes (one) | Document ID field |
| `@Field(name)` | Field | Yes (all non-ID) | MongoDB field name |
| `@MongoEnumValue(value)` | Enum constant | Yes (all) | Stored string value |

### SQL Annotations
| Annotation | Target | Required | Purpose |
|------------|--------|----------|---------|
| `@Table(name)` | Class | Yes | SQL table name |
| `@Column(name, json)` | Field | Yes | Column name, optional JSON |
| `@PrimaryKey(autoIncrement)` | Field | Yes (1+) | Primary key |
| `@DBEnumValue(value)` | Enum constant | Yes | Stored string value |

### API/JSON Annotations
| Annotation | Target | Purpose |
|------------|--------|---------|
| `@Property(name)` | Field | JSON field name |
| `@QueryParam(name)` | Field | Query parameter |
| `@PathParam(value)` | Parameter | Path variable |
| `@GET/@POST/@PUT/@DELETE/@PATCH` | Method | HTTP method |
| `@Path(value)` | Method | URL path pattern |
| `@ResponseStatus(HTTPStatus)` | Method | Response status code |

### Validation Annotations
| Annotation | Purpose | Applicable Types |
|------------|---------|------------------|
| `@NotNull` | Must not be null | Any |
| `@NotBlank` | Must not be blank | String |
| `@Size(min, max)` | Size constraints | String, List, Map |
| `@Min(value)` | Minimum value | Integer, Long, Double, BigDecimal |
| `@Max(value)` | Maximum value | Integer, Long, Double, BigDecimal |
| `@Pattern(value)` | Regex pattern | String |
| `@Digits(integer, fraction)` | Digit constraints | Integer, Double, BigDecimal |

---

## 6. Design Principles

### 6.1 Convention Over Configuration
- Field injection only (no constructor injection)
- Explicit `bind()` for all services
- Annotation-based mapping for MongoDB, SQL, and API
- Interface-based WebService definition

### 6.2 Type Safety
- `MongoCollection<T>` ensures type-safe MongoDB operations
- `Repository<T>` for type-safe SQL operations
- `MessagePublisher<T>` prevents wrong message types
- `Cache<T>` for typed caching

### 6.3 Fail Fast
- Module initialization validates all configs
- Startup validates all property usage
- Route conflicts detected before server starts
- Entity class validation at registration time

### 6.4 Separate Annotation Systems
Three distinct systems for different layers:
- `@Field` for MongoDB persistence
- `@Column` for SQL persistence
- `@Property` for JSON/API serialization

**Rationale**: Different serialization rules, prevents annotation conflicts, allows different field names per layer.

---

# Part 2: API Reference

This section contains detailed API signatures and configurations. **Read the relevant section when working on that specific module.**

---

## 7. Module System Reference

### 7.1 Module Base Methods
| Method | Purpose |
|--------|---------|
| `load(Module)` | Load sub-module |
| `loadProperties(String)` | Load properties file from classpath |
| `property(String)` | Get optional property (returns `Optional<String>`) |
| `requiredProperty(String)` | Get required property (throws if missing) |
| `bind(Class<T>)` | Create and bind service instance |
| `bind(T instance)` | Bind existing instance |
| `bind(Type, String, T)` | Bind with generic type and name |
| `bean(Class<T>)` | Retrieve bound bean |
| `onStartup(Task)` | Register startup hook |
| `onShutdown(Task)` | Register shutdown hook |

### 7.2 Config Accessor Methods
| Method | Returns | Purpose |
|--------|---------|---------|
| `http()` | `HTTPConfig` | HTTP server configuration |
| `api()` | `APIConfig` | API/WebService configuration |
| `mongo()` / `mongo(name)` | `MongoConfig` | MongoDB configuration |
| `db()` / `db(name)` | `DBConfig` | Database configuration |
| `redis()` / `redis(name)` | `RedisConfig` | Redis configuration |
| `kafka()` / `kafka(name)` | `KafkaConfig` | Kafka configuration |
| `cache()` | `CacheConfig` | Cache configuration |
| `schedule()` | `SchedulerConfig` | Scheduler configuration |
| `log()` | `LogConfig` | Logging configuration |
| `site()` | `SiteConfig` | Site/template configuration |
| `ws()` | `WebSocketConfig` | WebSocket configuration |
| `sse()` | `ServerSentEventConfig` | SSE configuration |

### 7.3 Module Loading Order
```java
public class AppModule extends Module {
    @Override
    protected void initialize() {
        // 1. Properties first
        loadProperties("sys.properties");

        // 2. Infrastructure
        load(new MongoModule());
        load(new KafkaModule());
        http().listenHTTP("8080");

        // 3. Business modules
        load(new ItemModule());
    }
}
```

### 7.4 Lifecycle Hooks
```java
onStartup(() -> logger.info("Application started"));
onShutdown(() -> cleanup());
```

**Shutdown Stages (sequential):**
- Stage 0: Stop accepting external requests
- Stage 1: Await ongoing requests
- Stage 2-3: Shutdown executors
- Stage 4: Kafka producer flush
- Stage 5: Application hooks (`onShutdown()`)
- Stage 6: Resource cleanup (DB, Redis, Mongo)
- Stage 7-8: Log appender and HTTP server

---

## 8. MongoDB Reference

### 8.1 MongoConfig Methods
| Method | Purpose |
|--------|---------|
| `uri(String)` | Set MongoDB connection URI (required) |
| `poolSize(int min, int max)` | Configure connection pool |
| `timeout(Duration)` | Operation timeout (default 15s) |
| `collection(Class<T>)` | Register entity, returns `MongoCollection<T>` |
| `view(Class<T>)` | Register inner class or projection view |

### 8.2 MongoCollection<T> Operations
```java
// Insert
void insert(T entity);
void bulkInsert(List<T> entities);

// Read
Optional<T> get(Object id);
Optional<T> findOne(Bson filter);
List<T> find(Query query);
void forEach(Query query, Consumer<T> consumer);
long count(Bson filter);

// Aggregation
<V> List<V> aggregate(Aggregate<V> aggregate);

// Update
void replace(T entity);  // Upsert by ID
void bulkReplace(List<T> entities);
long update(Bson filter, Bson update);
long partialUpdate(Bson filter, T entity);  // Non-null fields only

// Delete
boolean delete(Object id);
long delete(Bson filter);
long bulkDelete(List<?> ids);
```

### 8.3 Query Object
```java
Query query = new Query();
query.filter = Filters.eq("status", "ACTIVE");
query.projection = Projections.include("itemNumber", "name");
query.sort = Sorts.descending("createdTime");
query.skip = 10;
query.limit = 20;
query.readPreference = ReadPreference.secondaryPreferred();

List<Item> items = itemCollection.find(query);
```

### 8.4 Aggregation Example
```java
List<Result> results = itemCollection.aggregate(
    Match.match(Filters.eq("status", "ACTIVE")),
    Group.group("$type").count("count"),
    Sort.sort(descending("count"))
);
```

### 8.5 Supported Field Types
- `ObjectId`, `String`, `Boolean`
- `Integer`, `Long`, `Double`, `BigDecimal`
- `LocalDateTime`, `ZonedDateTime`, `LocalDate`
- `List<T>`, `Map<String, V>`, `Map<Enum, V>`
- Nested entities, Enums with `@MongoEnumValue`

---

## 9. Relational Database Reference

### 9.1 DBConfig Methods
| Method | Purpose |
|--------|---------|
| `url(String)` | JDBC URL (required) |
| `user(String)` | Username (supports `iam/gcloud`, `iam/azure/{user}`) |
| `password(String)` | Password |
| `poolSize(int min, int max)` | Connection pool (default 5-50) |
| `isolationLevel(IsolationLevel)` | READ_COMMITTED or READ_UNCOMMITTED |
| `timeout(Duration)` | Query timeout |
| `longTransactionThreshold(Duration)` | Warning threshold (default 5s) |
| `view(Class<?>)` | Register read-only view class |
| `repository(Class<T>)` | Register entity, returns `Repository<T>` |

### 9.2 Database Interface
```java
<T> List<T> select(String sql, Class<T> viewClass, Object... params);
<T> Optional<T> selectOne(String sql, Class<T> viewClass, Object... params);
int execute(String sql, Object... params);
int[] batchExecute(String sql, List<Object[]> params);
Transaction beginTransaction();
```

### 9.3 Repository<T> Methods
```java
// Query Builder
Query<T> select();
default List<T> select(String where, Object... params);
default Optional<T> selectOne(String where, Object... params);
default long count(String where, Object... params);
Optional<T> get(Object... primaryKeys);

// Insert
OptionalLong insert(T entity);
boolean insertIgnore(T entity);
boolean upsert(T entity);
Optional<long[]> batchInsert(List<T> entities);

// Update
boolean update(T entity);              // Full update (null → NULL)
boolean partialUpdate(T entity);       // Non-null fields only
boolean partialUpdate(T entity, String where, Object... params);

// Delete
boolean delete(Object... primaryKeys);
boolean batchDelete(List<?> primaryKeys);
```

### 9.4 Query Builder
```java
Query<User> query = repository.select();
query.where("status = ?", "ACTIVE");
query.where("created_date > ?", startDate);  // Chained with AND
query.orderBy("name ASC");
query.skip(10);
query.limit(20);
List<User> users = query.fetch();

// Projection
List<UserSummary> summaries = query.project("id, name", UserSummary.class);
long count = query.count();
```

### 9.5 Transaction
```java
Transaction tx = database.beginTransaction();
try {
    // Operations
    tx.commit();
} catch (Exception e) {
    tx.rollback();
    throw e;
} finally {
    tx.close();
}
```

---

## 10. Kafka Reference

### 10.1 KafkaConfig Methods
| Method | Purpose |
|--------|---------|
| `uri(String)` | Bootstrap servers (required) |
| `publish(String topic, Class<T>)` | Create publisher, returns `MessagePublisher<T>` |
| `subscribe(String, Class<T>, MessageHandler<T>)` | Single message handler |
| `subscribe(String, Class<T>, BulkMessageHandler<T>)` | Batch handler |
| `groupId(String)` | Consumer group (default: app name) |
| `concurrency(int)` | Processing threads (default: CPU * 16) |
| `maxRequestSize(int)` | Max message size (default 1MB) |
| `maxPoll(int records, int bytes)` | Poll limits |
| `minPoll(int bytes, Duration wait)` | Poll minimums |
| `longConsumerDelayThreshold(Duration)` | Warn threshold (default 30s) |
| `longConsumerDelayErrorThreshold(Duration)` | Error threshold (default 15m) |

### 10.2 MessagePublisher<T>
```java
void publish(String key, T value);
void publish(String topic, String key, T value);
```

### 10.3 Message Ordering
- **With key:** Messages with same key processed sequentially
- **Without key (null):** Processed concurrently (sticky partitioning)
- **Bulk handlers:** All messages in batch, no ordering guarantee

### 10.4 Header Propagation
Automatically propagates: `correlationId`, `trace`, `client`, `refId`, DataDog APM headers

---

## 11. HTTP/WebService Reference

### 11.1 HTTPConfig Methods
| Method | Purpose |
|--------|---------|
| `listenHTTP(String)` | Bind HTTP listener (host:port or port) |
| `listenHTTPS(String)` | Bind HTTPS listener |
| `route(HTTPMethod, String, Controller)` | Register custom controller |
| `bean(Class<?>)` | Register bean for documentation |
| `intercept(Interceptor)` | Add interceptor |
| `errorHandler(ErrorHandler)` | Custom error handler |
| `limitRate()` | Rate limiting configuration |
| `access()` | IP access control |
| `gzip()` | Enable compression |
| `maxProcessTime(Duration)` | Request timeout |
| `maxEntitySize(long)` | Max request body size |

### 11.2 Controller Pattern
```java
public class MigrationController implements Controller {
    @Override
    public Response execute(Request request) {
        String id = request.pathParam("id");
        Boolean dryRun = request.queryParam("dry_run")
            .map(Boolean::parseBoolean)
            .orElse(true);
        return Response.bean(result);
    }
}

http().route(HTTPMethod.POST, "/api/migration/:id", new MigrationController());
```

### 11.3 Request Object
```java
String requestURL();
String path();
HTTPMethod method();
Optional<String> header(String name);
String pathParam(String name);
Map<String, String> queryParams();
Map<String, String> formParams();
Map<String, MultipartFile> files();
Optional<byte[]> body();
<T> T bean(Class<T> beanClass);
String clientIP();
Session session();  // HTTPS only
```

### 11.4 Response Object
```java
Response.text(String text)
Response.bean(Object bean)                   // JSON
Response.html(String template, Object model)
Response.empty()                             // 204
Response.bytes(byte[] bytes)
Response.file(Path path)
Response.redirect(String url)

// Chainable
response.status(HTTPStatus.CREATED)
response.header("X-Custom", "value")
response.contentType(ContentType.APPLICATION_JSON)
```

### 11.5 Interceptor
```java
public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Invocation invocation) throws Exception {
        Request request = invocation.context().request();
        // Validate auth
        invocation.context().put("userId", userId);
        return invocation.proceed();
    }
}

http().intercept(new AuthInterceptor());
```

### 11.6 Rate Limiting
```java
LimitRateConfig limitRate = http().limitRate();
limitRate.maxEntries(5000);
limitRate.add("api", 100, 50, Duration.ofSeconds(1));  // 100 max, 50/sec fill
```

---

## 12. Redis and Caching Reference

### 12.1 RedisConfig Methods
| Method | Purpose |
|--------|---------|
| `host(String)` | Redis host (required) |
| `password(String)` | Authentication |
| `poolSize(int min, int max)` | Connection pool |
| `timeout(Duration)` | Operation timeout |
| `client()` | Get Redis client |

### 12.2 Redis Operations
```java
String get(String key);
boolean set(String key, String value, @Nullable Duration expiration, boolean onlyIfAbsent);
void expire(String key, Duration duration);
long del(String... keys);
long increaseBy(String key, long increment);
Map<String, String> multiGet(String... keys);
void multiSet(Map<String, String> values);
void forEach(String pattern, Consumer<String> consumer);

// Data structures
RedisHash hash();
RedisList list();
RedisSet set();
RedisSortedSet sortedSet();
RedisHyperLogLog hyperLogLog();
```

### 12.3 CacheConfig Methods
| Method | Purpose |
|--------|---------|
| `local()` | Use local (in-memory) cache |
| `redis(String host)` | Use Redis cache |
| `redis(String host, String password)` | Redis with auth |
| `maxLocalSize(int)` | Max local cache entries (default 10,000) |
| `add(Class<T>, Duration)` | Register cache, returns `CacheStoreConfig` |

### 12.4 Cache<T> Interface
```java
T get(String key, Function<String, T> loader);
Map<String, T> getAll(Collection<String> keys, Function<String, T> loader);
void put(String key, T value);
void putAll(Map<String, T> values);
void evict(String key);
void evictAll(Collection<String> keys);
```

**Note:** Loader must NOT return null. Use wrapper class for nullable values.

---

## 13. Scheduler Reference

### 13.1 SchedulerConfig Methods
| Method | Purpose |
|--------|---------|
| `timeZone(ZoneId)` | Set timezone (before adding triggers) |
| `fixedRate(String name, Job job, Duration rate)` | Run at fixed intervals |
| `hourlyAt(String, Job, int minute, int second)` | Run hourly |
| `dailyAt(String, Job, LocalTime)` | Run daily |
| `weeklyAt(String, Job, DayOfWeek, LocalTime)` | Run weekly |
| `monthlyAt(String, Job, int dayOfMonth, LocalTime)` | Run monthly (day 1-28) |
| `trigger(String, Job, Trigger)` | Custom trigger |

### 13.2 JobContext
```java
String name;                    // Job identifier
ZonedDateTime scheduledTime;    // Scheduled execution time
```

### 13.3 Admin Endpoints
- `GET /_sys/job` - List all jobs
- `POST /_sys/job/:job` - Manually trigger job

---

## 14. WebSocket and SSE Reference

### 14.1 WebSocket Configuration
```java
ws().<ClientMsg, ServerMsg>listen(
    "/api/notifications",
    ClientMsg.class,
    ServerMsg.class,
    new NotificationListener()
);
```

### 14.2 ChannelListener
```java
public class NotificationListener implements ChannelListener<ClientMsg, ServerMsg> {
    @Override
    public void onConnect(Request request, Channel<ServerMsg> channel) {
        channel.context().put("userId", extractUserId(request));
        channel.join("all-users");
    }

    @Override
    public void onMessage(Channel<ServerMsg> channel, ClientMsg message) {
        channel.send(new ServerMsg("received"));
    }

    @Override
    public void onClose(Channel<ServerMsg> channel, int code, String reason) {}
}
```

### 14.3 Channel Interface
```java
void send(T message);
Context context();
void close();
void join(String group);
void leave(String group);
```

### 14.4 SSE Configuration
```java
sse().<EventMsg>listen("/api/events", EventMsg.class, new EventListener());
```

### 14.5 Constraints
- Path must be static (no `/:` parameters)
- Listener must be named class
- Default rate limit: 10 connections per 30 seconds
- WebSocket max message: 10MB
- SSE keep-alive: 15 seconds

---

## 15. Logging and Monitoring Reference

### 15.1 ActionLogContext API
```java
ActionLogContext.put("userId", userId);
List<String> values = ActionLogContext.get("userId");
ActionLogContext.stat("items_processed", count);
ActionLogContext.track("db", elapsed, readRows, writeRows);
ActionLogContext.triggerTrace(true);
```

### 15.2 LogConfig Methods
| Method | Purpose |
|--------|---------|
| `appendToConsole()` | Output to console |
| `appendToKafka(String uri)` | Send to Kafka |
| `maskFields(String...)` | Mask sensitive fields |
| `appender(LogAppender)` | Custom appender |

### 15.3 Performance Warning Thresholds
| Operation | Max Ops | Max Elapsed | Max Reads |
|-----------|---------|-------------|-----------|
| db | 2000 | 5s | 2000 |
| redis | 2000 | 500ms | 1000 |
| mongo | 2000 | 5s | 2000 |
| elasticsearch | 2000 | 5s | 2000 |

### 15.4 System Endpoints
- `/_sys/vm` - JVM info
- `/_sys/thread` - Thread dump
- `/_sys/heap` - Heap info
- `/_sys/property` - System properties
- `/_sys/api` - API documentation
- `/_sys/cache` - Cache inspection
- `/_sys/job` - Scheduled jobs

---

## 16. Testing Reference

### 16.1 Test Module Setup
```java
@Context(module = MyServiceTestModule.class)
class MyServiceTest {
    @Inject
    MyService myService;

    @Test
    void testMethod() {
        // Test
    }
}

public class MyServiceTestModule extends AbstractTestModule {
    @Override
    protected void initialize() {
        db().repository(User.class);
        redis();  // Uses MockRedis
        overrideBinding(ExternalService.class, new MockExternalService());
    }
}
```

### 16.2 Auto-Configured Test Classes
- `TestDBConfig` → In-memory HSQLDB
- `TestRedisConfig` → MockRedis
- `TestKafkaConfig` → MockMessagePublisher
- `TestAPIConfig` → Mocked web service clients
- `TestCacheConfig` → Local cache

### 16.3 InitDBConfig
```java
@Inject
InitDBConfig initDB;

@BeforeEach
void setUp() {
    initDB.createSchema();  // Auto-generates tables from entities
}
```

---

## 17. Project Structure

### 17.1 Service Directory Layout

```
{service-name-service}/
└── src/
    └── main/
        ├── java/
        │   ├── Main.java
        │   └── app.{service-name-package}/
        │       ├── {service-name-prefix}App.java       # Application, extends App
        │       └── {module-name}Module.java    # Module
        └── resources/
            ├── sys.properties          # System configuration
            └── app.properties          # Application configuration
```

### 17.2 Main Class

Each service MUST have a main class to start the application:

```java
import app.meetingroom.MeetingRoomApp;

public class Main {
    public static void main(String[] args) {
        new MeetingRoomApp().start();
    }
}
```

### 17.3 sys.properties (System Configuration)

Contains system-level configuration:

```properties
# HTTP Server
sys.http.listen=0.0.0.0:8080

# HTTPS Server
sys.https.listen=0.0.0.0:8443

# Database (JDBC)
sys.jdbc.url=jdbc:mysql://localhost:3306/service_db
sys.jdbc.user=service_user
sys.jdbc.password=secret

# Kafka
sys.kafka.uri=localhost:9092

# Logging
sys.log.appender=console
```

### 17.4 Database Migration Modules

Each service that uses a database MUST have corresponding migration modules:

**Rules:**
- If a service uses **MySQL/SQL database**, it MUST have a `{service-name}-db-migration` module
- If a service uses **MongoDB**, it MUST have a `{service-name}-mongo-migration` module
- Only create migration modules if the service actually uses the corresponding database; if a database is not used, do not create the migration module
- Both migration modules follow the pattern defined in the architecture

**Migration Module Structure:**

**db-migration** (Flyway):
```
{service-name}-db-migration/
└── src/main/resources/db/migration/
    ├── V1__create_table.sql
    └── R__initial_data.sql
```

**mongo-migration** (Custom Gradle plugin):
```
{service-name}-mongo-migration/
├── src/main/java/{package}/
│   ├── Main.java                     # Migration entry point
│   └── script/
│       └── {Entity}Script.java      # Data migration scripts
└── src/main/resources/
    └── sys.properties               # MongoDB URI config (sys.mongo.uri)
```

### 17.5 MongoDB Migration Details

**Main.java** - Entry point that executes migration:
```java
public class Main {
    public static void main(String[] args) {
        var migration = new MongoMigration("sys.properties");
        migration.migrate(mongo -> {
            createIndex(mongo);
        });
        executeMigrationScript();
    }

    private static void executeMigrationScript() {
        Properties properties = new Properties();
        properties.load("sys.properties");
        var uri = properties.get("sys.mongo.uri").orElseThrow();
        var migration = new core.ext.mongo.migration.MongoMigration(uri);
        migration.scanPackagePath("app.{package}.script").migration();
    }
}
```

**sys.properties** - MongoDB configuration:
```properties
sys.mongo.uri=mongodb://localhost:27017/service_db
```

**Script Classes** - Annotated migration scripts:
```java
@Flyway(collection = "collection_name")
public class EntityScript {

    @Script(ticket = "XXX", description = "description", testMethod = "none", runAlways = false)
    public void initData(MongoCollection<Document> collection) {
        // Insert/update operations
    }

    @Script(ticket = "XXX", description = "description", testMethod = "none")
    public void deleteIndex(MongoCollection<Document> collection) {
        collection.dropIndex("index_name");
    }
}
```

**Annotations:**
- `@Flyway(collection)` - Specifies the MongoDB collection to migrate
- `@Script(ticket, description, testMethod, runAlways)` - Marks a migration method
  - `ticket`: Jira ticket number
  - `description`: Migration description
  - `testMethod`: Test method name, or `"none"` if not needed
  - `runAlways`: Whether to run on every startup (default `true`)

---

## 18. Application Startup

### 18.1 Startup Order

1. Load `sys.properties` first
2. Initialize infrastructure (DB, MongoDB, Kafka, Redis)
3. Load `app.properties`
4. Initialize business modules
5. Start HTTP/HTTPS servers

### 18.2 Application Setup Example

```java
package app.meetingroom;

import core.framework.module.App;
import core.framework.module.SystemModule;

public class MeetingRoomApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));
        load(new MeetingRoomModule());
    }
}
```

### 18.3 Module Setup Example

```java
public class AppModule extends Module {
    @Override
    protected void initialize() {
        loadProperties("sys.properties");
        loadProperties("app.properties");

        db().url(requiredProperty("sys.jdbc.url"))
           .user(requiredProperty("sys.jdbc.user"))
           .password(requiredProperty("sys.jdbc.password"));

        kafka().uri(requiredProperty("sys.kafka.uri"));

        http().listenHTTP(property("sys.http.listen").orElse("0.0.0.0:8080"));

        load(new ItemModule());
    }
}
```

---
## 19. Frontend Gateways Specification

### 19.1. Architecture and Module Division
In this system, the Gateway layer is divided into two independent service modules, unified under the `frontend` directory:
- **`website`**: The frontend gateway for regular users.
- **`backoffice`**: The backend management system gateway for administrators.

**Important Principle: Separation of Service and Interface**
To ensure clear modules and decoupled dependencies, all microservices and Gateway layers must separate `service` (concrete implementation and business logic) and `interface` (API definitions, Request/Response DTOs, View models, etc.) into different modules. In this way, if other services need to make a call, they only need to depend on the `interface` module. This applies equally to gateways, meaning `website` should be split into `website-interface` and `website`, and `backoffice` into `backoffice-interface` and `backoffice`.

**Important Principle: API Definition using Web Service Interfaces**
Unless there is a specific need to handle file uploads which requires direct `Controller` usage, gateway APIs MUST be defined using Web Service interfaces (with annotations like `@Path`, `@POST`, `@GET`), exactly like internal microservices. Do not use custom `AJAXController` implementations with manual `Request`/`Response` bean parsing.

**Important Principle: Separate Folder for Requests and Responses in Interface Module**
In the interface module, request and response files MUST be placed in a separate package/folder based on their domain, instead of being directly under the `api` package. For instance, `LoginAJAXRequest` and `LoginAJAXResponse` should be placed in the `user` folder (e.g., `app.website.user.api.user`). However, the API interface class itself (e.g., `UserAJAXWebService`) MUST remain directly under the top-level `api` package (e.g., `app.website.user.api`). This keeps the `api` package clean and makes the code structure more organized, just like in internal microservices.

**Important Principle: Clean Repository**
The repository should be kept clean. All `.classpath`, `.project`, `.settings/`, `*.bin`, `*.class`, `*.lock`, `.gradle/`, `.idea/`, and `build/` files/directories should be ignored in `.gitignore` and must not be committed to the repository.

**Important Principle: Validation Annotations**
All Entities (SQL `@Table` / Mongo `@Collection`), API Requests, and API Responses MUST include appropriate validation annotations (such as `@NotNull`, `@NotBlank`, `@Size`, `@Min`, `@Max`). This is essential to ensure fail-fast data validation at the framework level before the data reaches the business logic.

**Important Principle: Database Migration Modules**
If a service uses a relational database (MySQL/SQL), it MUST have a corresponding `{service-name}-db-migration` module (e.g., `user-service-db-migration`). If a service uses MongoDB, it MUST have a `{service-name}-mongo-migration` module. The migration module ensures schema versioning and initial data population are tied to the service's lifecycle.

**Service Naming Convention for Backoffice:**
Service classes that implement business logic specifically used by Backoffice (BO) interfaces MUST be named with a `BO` prefix (e.g., `BOUserService`). This ensures a clear separation between regular business logic and admin/backend logic.

**Interface Naming Convention:**
The interface module MUST be named with a `-interface` suffix relative to the service module name. For example, the interface module for `user-service` MUST be named `user-service-interface` (do NOT use names like `user-interface`).

### 19.2. API and Naming Conventions
To clarify the caller and usage context of APIs, the system establishes the following conventions:
- **Backoffice Internal Service APIs**: Internal APIs provided by microservices for the backoffice gateway to call. The path must include `/bo/`, and the class name must use the `BO` prefix.
  - *Example*: `@Path("/bo/company")`, `BOCreateCompanyRequest`
- **Gateway APIs Provided to Frontend Pages**: APIs exposed by `website` and `backoffice` gateways to frontend pages. The path must include `/ajax/`, and class names and view models must use `AJAX` as a suffix.
  - *Example*: `@Path("/ajax/company/list")`, `ListCompanyAJAXRequest`, `CompanyAJAXView`

---

### 19.3. Gateway API Design (API List)

### 19.3.1 Website Gateway APIs (`frontend/website`)
Operations for regular users.

**User & Auth**
- `POST /ajax/user/register`
  - Request: `RegisterUserAJAXRequest`
  - Response: `RegisterUserAJAXResponse`
- `POST /ajax/user/login`
  - Request: `LoginAJAXRequest`
  - Response: `LoginAJAXResponse` (contains Token and user info)

**Resource**
- `GET /ajax/company/list`
  - Request: `ListCompanyAJAXRequest`
  - Response: `ListCompanyAJAXResponse` (contains `CompanyAJAXView` list)
- `GET /ajax/room/list`
  - Request: `ListRoomAJAXRequest` (filters by company)
  - Response: `ListRoomAJAXResponse` (contains `RoomAJAXView` list)

**Reservation**
- `GET /ajax/reservation/calendar`
  - Request: `GetCalendarAJAXRequest` (params: room ID, date; restricted to 7 days in the future)
  - Response: `GetCalendarAJAXResponse` (contains 30-minute interval occupancy status view)
- `POST /ajax/reservation/reserve`
  - Request: `ReserveRoomAJAXRequest`
  - Response: `ReserveRoomAJAXResponse`
- `POST /ajax/reservation/cancel`
  - Request: `CancelReservationAJAXRequest`
  - Response: `CancelReservationAJAXResponse`

---

### 19.3.2 Backoffice Gateway APIs (`frontend/backoffice`)
Operations for administrators in the backend.

**Admin Auth**
- `POST /ajax/admin/login`
  - Request: `AdminLoginAJAXRequest`
  - Response: `AdminLoginAJAXResponse`

**Company Management**
- `POST /ajax/company/create`
  - Request: `CreateCompanyAJAXRequest`
  - Response: `CreateCompanyAJAXResponse`
- `DELETE /ajax/company/{id}`
  - Request: `DeleteCompanyAJAXRequest`
  - Response: `DeleteCompanyAJAXResponse`
- `GET /ajax/company/list`
  - Request: `ListCompanyAJAXRequest`
  - Response: `ListCompanyAJAXResponse` (contains `CompanyAJAXView` list)

**Room Management**
- `POST /ajax/room/create`
  - Request: `CreateRoomAJAXRequest`
  - Response: `CreateRoomAJAXResponse`
- `DELETE /ajax/room/{id}`
  - Request: `DeleteRoomAJAXRequest`
  - Response: `DeleteRoomAJAXResponse`
- `GET /ajax/room/list`
  - Request: `ListRoomAJAXRequest`
  - Response: `ListRoomAJAXResponse` (contains `RoomAJAXView` list)

**Reservation Management**
- `GET /ajax/reservation/list`
  - Request: `SearchReservationAJAXRequest` (supports searching by company, room)
  - Response: `SearchReservationAJAXResponse` (contains `ReservationAJAXView` list)

**User Management**
- `PUT /ajax/user/status`
  - Request: `UpdateUserStatusAJAXRequest` (activate/deactivate)
  - Response: `UpdateUserStatusAJAXResponse`

---

### 19.4. Backend Microservices BO API Design Example (Backend Microservices)
*Example of underlying microservice APIs for the `backoffice` gateway to aggregate and call.*

**Resource Service**
- `POST /bo/company` -> `BOCreateCompanyRequest` / `BOCreateCompanyResponse`
- `DELETE /bo/company/{id}` -> `BODeleteCompanyResponse`
- `GET /bo/company/list` -> `BOListCompanyRequest` / `BOListCompanyResponse`

**Account Service**
- `PUT /bo/user/status` -> `BOUpdateUserStatusRequest` / `BOUpdateUserStatusResponse`

**Reservation Service**
- `GET /bo/reservation/list` -> `BOSearchReservationRequest` / `BOSearchReservationResponse`

### 19.5 Session Management
**Important Principle:** For gateway modules (e.g. `website`, `backoffice`), use Core-NG's session management (`site().session().local();` or redis based) to maintain user login state. Store session data via cookies or headers. After a user logs in, store their `userId` in the session. In subsequent requests, use an Interceptor or direct `Request` injection to validate the session and retrieve the user's data.

### 19.6 Gateway Package Structure
**Important Principle:** Gateway service modules do NOT need a `web` package suffix for their implementations (e.g. use `app.website.user` instead of `app.website.user.web`). Furthermore, in gateway interface modules, the `api` package should be located at the common parent node for all domains (e.g. `app.website.api.user` instead of `app.website.user.api.user`). This mirrors the structure of internal microservices which also place their interfaces under a common `api` package.

---

## 20. Architecture & Design Principles

### 20.1 Scheduler Jobs Separation
**Important Principle:** Scheduler jobs (like sending notifications or regular cleanups) MUST be placed in a dedicated `scheduler-service` module rather than inside business microservices (e.g. `booking-service`). This ensures that jobs run as a single logical node or a cleanly separated process, avoiding duplicate data fetching or concurrent executions across multiple instances of the business service.

### 20.2 Kafka Message Types and Scheduler Jobs
**Important Principle:** The `scheduler-service` MUST NOT perform direct business data modifications. Instead, it must trigger business modules via Kafka messages. Kafka messages should be explicitly categorized into two types:
1. **Request/Command style messages:** Typically emitted by Jobs to instruct a specific business service to perform an action. (e.g., `notify-upcoming-reservation-message`).
2. **Event/Result style messages:** Typically emitted by business services after an action has occurred, allowing other domains to react. (e.g., `reservation-upcoming-message`, `reservation-canceled-message`).
Naming conventions for Kafka topics and message objects MUST clearly reflect whether they are a command or an event. Furthermore, all Java class names for Kafka message objects MUST end with the `Message` suffix (e.g., `NotifyUpcomingReservationMessage`).

---

## 21. Coding Style & Best Practices

### 21.1 Inject Annotation Format
**Important Principle:** When using `@Inject` for field injection, the annotation MUST be placed on a separate line above the field declaration, not on the same line. For example, use:
```java
@Inject
BookingWebService bookingWebService;
```
instead of `@Inject BookingWebService bookingWebService;`.

**Important Principle:** When multiple fields are injected consecutively using `@Inject`, there MUST NOT be any blank lines between them. For example:
```java
@Inject
CompanyWebService companyWebService;
@Inject
RoomWebService roomWebService;
```
instead of having an empty line separating the injected fields.

### 21.2 Import and Class Spacing
**Important Principle:** There MUST be exactly one blank line between the last `import` statement and the `public class` or `public interface` declaration.

**Important Principle:** There MUST be a space between the `import` keyword and the imported package, and exactly one space between `public class` (or `public interface`) and the class name.

### 21.3 Interface Annotation Spacing
**Important Principle:** In an interface definition, the first annotation (e.g., `@GET`, `@POST`) MUST immediately follow the interface declaration without any blank lines in between. For example:
```java
public interface MyWebService {
    @GET
    ...
```
instead of having a blank line after the opening brace.

### 21.4 BO Sub-Package Flattening
**Important Principle:** When organizing interface modules, DO NOT nest request/response beans under `bo.<domain>`. Instead, merge them directly into the `<domain>` package alongside the non-BO beans. This simplifies the folder structure and reduces nesting depth.

### 21.5 Singular Nouns in Interfaces
**Important Principle:** In interface definitions (both URL paths and method names), ALWAYS use singular nouns instead of plurals. For example, use `/ajax/company/list` instead of `/ajax/companies/list`, and name the method `listCompany` instead of `listCompanies`.

### 21.6 No Separate DTO Package
**Important Principle:** In interface modules, do NOT place Request, Response, or View beans in a separate `dto` package (e.g., avoid `app.meetingroom.api.dto`). Instead, they MUST be placed in a package named after their specific domain or feature alongside the related services, or directly under the domain package if following the gateway structure (e.g., `app.meetingroom.api.room`). This follows the earlier rule: "request and response files MUST be placed in a separate package/folder based on their domain, instead of being directly under the `api` package."

### 21.7 Mandatory NotNull for Known Non-Null Fields
**Important Principle:** In all Request, Response, View DTOs, and **Kafka message objects** within interface modules, if a field is known or intended to be non-null (e.g., identifiers, status codes, timestamps, primitive wrappers that shouldn't be null), it MUST be annotated with `@NotNull` (from `core.framework.api.validate.NotNull`). This guarantees validation at the boundary.

### 21.8 No API Prefix in Interface Paths
**Important Principle:** In internal microservice interface path definitions, DO NOT include the `/api` or `/bo` prefix. The paths should reflect the resource directly, such as `/reservation/:id/cancel` instead of `/api/reservation/:id/cancel` or `/user` instead of `/bo/user`. Gateways may still use `/ajax` for frontend distinctions, but internal services MUST NOT use arbitrary routing prefixes.

---

## References

- Upstream project: https://github.com/neowu/core-ng-project
- Upstream Wiki: https://github.com/neowu/core-ng-project/wiki
