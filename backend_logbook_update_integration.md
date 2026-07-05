# Logbook Backend Integration Guide

This guide explains how to connect the new Shift Logbook (with dynamic asset lists) to your Spring Boot backend.

## Local Caching Approach (Current State)

We are using an "Offline-First" architecture using Jetpack Room. Because a single Logbook can have *many* Assets (One-to-Many), creating standard SQL relational tables in Room is quite complex to setup initially and query for simple offline caches.

Instead, we used **Gson TypeConverters** (`Converters.kt`). The `ShiftLogbookEntity` has a field `assets: List<AssetData>`. Room automatically converts this list into a JSON string to store it in a single column, and converts it back to a List when retrieved.

## Connecting to Spring Boot Backend

### 1. Spring Boot DTOs

On your backend, create a matching hierarchy to catch the JSON payload:

```java
public class ShiftLogbookDto {
    private String date;
    private String shift;
    private String area;
    private String submitterId; // This is the user's ID
    private List<AssetDataDto> assets;
    
    // Getters and Setters...
}

public class AssetDataDto {
    private String assetTag;
    private String standingAlarms;
    private int maintenanceStatus;
    private String maintenanceDone;

    // Getters and Setters...
}
```

### 2. Spring Boot Controller
```java
@RestController
@RequestMapping("/api/v1/logbooks")
public class LogbookController {

    @PostMapping("/submit")
    public ResponseEntity<Void> submitLogbook(@RequestBody ShiftLogbookDto logbookDto) {
        // Save to Database (e.g. Postgres) via JPA.
        // You can choose to split the DTO into actual Relational SQL tables (Logbook table, Asset table) on the backend here.
        return ResponseEntity.ok().build();
    }
}
```

### 3. Android Retrofit Integration
Create your API Service on the Android side:

```kotlin
interface LogbookApiService {
    @POST("/api/v1/logbooks/submit")
    suspend fun submitLogbook(@Body logbook: ShiftLogbookEntity): Response<Unit>
}
```

### 4. Synchronization (WorkManager)
Since we want offline capability:
1. When the user taps "Submit", it saves to Room immediately (this is already implemented).
2. Schedule a one-time `WorkManager` job.
3. The `Worker` queries Room for un-synced logbooks, calls `LogbookApiService.submitLogbook(logbook)`, and if successful, marks them as synced.

### Note on Authentication
The user's identity is currently mocked as `"EID-1042"`. Once your login flow is active, you will save the username to `SharedPreferences` or `DataStore` on login, and simply read that cache to populate the `submitterId` field when calling `viewModel.insertLogbook()`.
