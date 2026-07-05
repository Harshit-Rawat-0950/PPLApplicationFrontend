# Live Demo & Backend Implementation Guide

This document serves as the definitive guide to prototyping the PPL Application Frontend for a live demo and acts as a blueprint for the backend team to pick up where the frontend leaves off. 

## Part 1: Preparing for the Live Demo

The current frontend codebase is built using **Android Jetpack Compose**, utilizing **MVVM architecture**, **Dagger-Hilt for dependency injection**, and **Jetpack Room for local SQLite caching**. It is currently designed as an "Offline-First" prototype, meaning you can demo the core functionalities entirely without a backend.

### How to Demo the App
1. **Build the APK**: 
   - Open the project in Android Studio.
   - Wait for Gradle to sync.
   - Go to `Build -> Build Bundle(s) / APK(s) -> Build APK(s)`.
   - Transfer the generated `.apk` file (located in `app/build/outputs/apk/debug/`) to your industrial tablet or Android device.
2. **Demo Flow**:
   - **Login**: The app opens to a basic login screen. For the demo, typing anything will navigate to the Dashboard. *(Authentication is currently mocked)*.
   - **Dashboard**: Shows a clean, minimalist 2-button UI: **Shift Logbook** and **Near Miss Reporting**.
   - **Near Miss Reporting**: Tap this to go to the Near Miss dashboard. Tap **Report Nearmiss**. Fill in the Area dropdown, type a title/description, use the interactive **Risk Assessment Matrix (RAM)** chips to calculate a risk score, and tap the camera button to snap a photo. Hit Submit.
   - **Past Near Misses**: Go to Current Nearmisses to see the locally cached report appear instantly.
   - **Shift Logbook**: Go back to the Dashboard -> Shift Logbook -> Report Log Book. Demo the dynamic form by clicking "Add Another Asset". Fill in maintenance statuses. Hit Submit.
   - **Past Logbooks**: Go to Past Logbooks to see the dynamic filtering in action. Tap on the logbook you just submitted to open the detailed view showing all the nested assets.

### What is Missing from `appDoc.md`?
While the Near Miss and Logbook modules are fully fleshed out, the original `appDoc.md` mentioned several other features that are currently **ignored/omitted** for this specific prototype:
1. **Global Authentication & Role Based Access Control (RBAC)**: The login screen exists, but it does not map to a real User entity. The Employee ID is currently mocked as `"EID-1042"` in Room insertions.
2. **Asset Management Module**: The standalone asset registry (QR code scanning, maintenance history graphs) is not built.
3. **Alarms Module**: The standalone module to view and acknowledge live SCADA/PLC alarms is not built.
4. **Analytics Dashboard**: The charts and graphs summarizing plant health are not built.
5. **Image Persistence**: The Near Miss module opens the camera and captures a `Bitmap`, but it currently only caches it in RAM during the form session. It is *not* saved to the local Room DB or the file system due to the complexity of local scoped storage.

---

## Part 2: Backend Implementation Walkthrough

To turn this prototype into a production app, the backend team must implement a **Spring Boot** server (likely using PostgreSQL). The app uses an "Offline-First" sync architecture. 

### Core Concepts
Because field tablets lose Wi-Fi connectivity, operators save data to the local SQLite (Room) DB first. When Wi-Fi is restored, Android `WorkManager` must be configured to push this local data to your Spring Boot APIs.

### 1. Authentication & Users
- **Backend**: Implement Spring Security with JWT. Create a `/api/v1/auth/login` endpoint that returns a JWT and the user's `EmployeeID` and `Role`.
- **Frontend**: Update `LoginScreen.kt` to make a Retrofit call to this endpoint. Save the JWT and `EmployeeID` using Android `DataStore` (Preferences).
- **Integration**: Remove the mocked `"EID-1042"` string in `NearMissScreen` and `ShiftLogbookScreen` and read the real `EmployeeID` from `DataStore`.

### 2. Near Miss Reporting
- **Read the Guide**: Please review [backend_nearmiss_integration.md](backend_nearmiss_integration.md) for specific details.
- **Backend**: 
  - Create a Postgres table `near_misses` with columns: `id`, `title`, `description`, `plant_area`, `criticality`, `probability`, `risk_score`, `timestamp`, `photo_url`.
  - Create `/api/v1/nearmiss/submit` (POST). Since it includes photos, this endpoint must accept `multipart/form-data` to handle the JSON DTO and the image file simultaneously. Upload the image to an S3 bucket and save the URL to Postgres.
- **Frontend**:
  - Update `NearMissEntity` to store the local URI of the snapped photo.
  - Build a `WorkManager` class that checks for unsynced Room entries, reads the local image file, and POSTs to Spring Boot.

### 3. Shift Logbook (One-To-Many Relational Data)
- **Read the Guide**: Please review [backend_logbook_update_integration.md](backend_logbook_update_integration.md) for specific details.
- **Backend**:
  - The frontend currently stores the list of Assets as a flat JSON string using Gson. Your backend should **normalize** this.
  - Create `logbooks` table (id, date, shift, area, submitter_id).
  - Create `logbook_assets` table (id, logbook_id [Foreign Key], asset_tag, standing_alarms, maintenance_status, maintenance_done).
  - Create `/api/v1/logbooks/submit` (POST) that accepts the nested JSON payload and performs transactional inserts into both tables.

### 4. Fetching Data for the App
To populate the "Past Logbooks" and "Past Near Misses" screens with reports submitted by *other* users:
- **Backend**: Create `GET` endpoints for both modules.
- **Frontend**: On app launch (or manual pull-to-refresh), use Retrofit to fetch all data from Spring Boot. Insert/Update this data into the local Room database using `dao.insertLogbook()`. The UI will automatically react and update because it is observing Room via Kotlin `Flow`s.
