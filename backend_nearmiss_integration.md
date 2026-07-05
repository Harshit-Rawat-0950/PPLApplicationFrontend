# Near Miss Backend and Local Caching Integration

This guide details how the local SQLite Room database caching works and how to link the application's Near Miss module to a Spring Boot backend in an "Offline-First" configuration.

## Current Setup (Offline Caching)

The application currently has a fully functional **Local SQLite Cache** using Android Jetpack Room. This ensures operators can submit Near Miss reports even when disconnected from the plant Wi-Fi.

### Key Components Added:
1. `NearMissEntity`: Defines the SQLite table structure mapping.
2. `NearMissDao`: Contains `@Insert` (saves the report) and `@Query` (retrieves all past reports).
3. `AppDatabase`: The main Room database configured in Dagger-Hilt for dependency injection.
4. `NearMissViewModel`: The architectural middleman that the UI calls to insert or fetch items locally.

## Linking to Spring Boot Backend

To push the locally saved SQLite records to your remote Spring Boot server, follow these steps:

### 1. Retrofit API Definition
Create a new API service in your `data/remote/` package.

```kotlin
interface NearMissApiService {
    @POST("/api/v1/nearmiss")
    suspend fun submitNearMiss(@Body nearMiss: NearMissDto): Response<Unit>
    
    @GET("/api/v1/nearmiss")
    suspend fun getNearMisses(): List<NearMissDto>
}
```

### 2. Synchronization Strategy (WorkManager)
Because operators might be offline when submitting a report, you should not rely on immediate API calls. Instead, use **Android WorkManager** to synchronize the local Room database with the remote Spring Boot API.

1. Create a `SyncNearMissWorker` extending `CoroutineWorker`.
2. Inside `doWork()`, fetch all un-synced entries from your Room `NearMissDao`.
3. Loop through them and call `apiService.submitNearMiss(...)`.
4. If successful, mark the item as 'synced' in Room or delete it if you prefer.

### 3. Fetching Remote Data
If you want to view Near Misses submitted by *other* operators across the plant, you will fetch from the Spring Boot API:
1. On app launch or manual refresh, call `apiService.getNearMisses()`.
2. Insert those records into the local Room database using `nearMissDao.insertNearMiss(...)`.
3. The UI (`PastNearMissesScreen`) is already observing the local Room DB as a `Flow`. When the new data is inserted, the UI will automatically update.

This ensures a robust, offline-capable architecture for industrial field use!
