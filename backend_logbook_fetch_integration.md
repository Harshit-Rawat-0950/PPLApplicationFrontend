# Fetching Logbooks from Spring Boot Backend

This guide outlines how to fetch the list of past shift logbooks from your Spring Boot backend and display them in the Jetpack Compose `PastLogbooksScreen`.

## 1. Define the Data Model

Create a Kotlin data class that matches the JSON structure returned by your Spring Boot API.

```kotlin
// app/src/main/java/com/ferhatozcelik/jetpackcomposetemplate/data/model/LogbookResponse.kt
data class LogbookResponse(
    val id: String,
    val date: String,
    val shiftName: String,
    val submitterId: String,
    val assetTag: String
)
```

## 2. Create the Retrofit API Interface

Add an endpoint to fetch the logbooks.

```kotlin
// app/src/main/java/com/ferhatozcelik/jetpackcomposetemplate/data/remote/LogbookApiService.kt
import retrofit2.http.GET
import retrofit2.http.Query

interface LogbookApiService {
    @GET("/api/v1/logbooks")
    suspend fun getPastLogbooks(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): List<LogbookResponse>
}
```

*Note: You can implement pagination here using Jetpack Paging 3, which is highly recommended for lists that can grow infinitely.*

## 3. Configure Hilt Module

Provide the `LogbookApiService` in your Hilt network module.

```kotlin
// app/src/main/java/com/ferhatozcelik/jetpackcomposetemplate/di/NetworkModule.kt
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // ... existing Retrofit setup ...

    @Provides
    @Singleton
    fun provideLogbookApiService(retrofit: Retrofit): LogbookApiService {
        return retrofit.create(LogbookApiService::class.java)
    }
}
```

## 4. Implement the Repository

The repository will coordinate between the API and your ViewModel. For offline-first architecture, you would also inject a Room DAO here.

```kotlin
// app/src/main/java/com/ferhatozcelik/jetpackcomposetemplate/data/repository/LogbookRepository.kt
class LogbookRepository @Inject constructor(
    private val apiService: LogbookApiService
    // private val logbookDao: LogbookDao // For offline caching
) {
    suspend fun fetchPastLogbooks(): Result<List<LogbookResponse>> {
        return try {
            val response = apiService.getPastLogbooks()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

## 5. Create the ViewModel

The ViewModel exposes the UI state to your Compose screen.

```kotlin
// app/src/main/java/com/ferhatozcelik/jetpackcomposetemplate/ui/viewmodel/PastLogbooksViewModel.kt
@HiltViewModel
class PastLogbooksViewModel @Inject constructor(
    private val repository: LogbookRepository
) : ViewModel() {

    private val _logbooks = MutableStateFlow<List<LogbookResponse>>(emptyList())
    val logbooks: StateFlow<List<LogbookResponse>> = _logbooks

    init {
        loadLogbooks()
    }

    private fun loadLogbooks() {
        viewModelScope.launch {
            repository.fetchPastLogbooks().onSuccess { logbookList ->
                _logbooks.value = logbookList
            }.onFailure { error ->
                // Handle error state (e.g., show a Toast or Error UI)
            }
        }
    }
}
```

## 6. Connect ViewModel to UI

Finally, inject the ViewModel into your `PastLogbooksScreen` and observe the state.

```kotlin
@Composable
fun PastLogbooksScreen(
    navController: NavController,
    viewModel: PastLogbooksViewModel = hiltViewModel()
) {
    val logbooks by viewModel.logbooks.collectAsState()
    
    // ... use the `logbooks` list in your LazyColumn instead of the dummy data ...
}
```

## Next Steps for Offline Support
If field operators need to view logbooks while disconnected from the factory Wi-Fi, you should implement **Room Database** caching. 
1. Create a `@Entity` for the logbook.
2. Create a `@Dao` to `insertAll` and `getAll`.
3. In your Repository, fetch from the API, save to Room, and then emit the Room data to the ViewModel as a `Flow`.
