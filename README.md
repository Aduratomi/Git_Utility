# Smart Toolkit (Git Utility)

Smart Toolkit is a versatile, all-in-one Android utility application designed to provide essential everyday tools in a single, modern interface. Built with **Kotlin** and **Jetpack Compose**, the app features a responsive, adaptive UI that works seamlessly across different screen orientations.

## 🚀 Features

### 1. Currency Converter
- **Live Rates**: Fetches real-time exchange rates using a REST API.
- **Searchable Selection**: Easily find any currency using an immersive search interface.
- **Offline Support**: Uses cached rates when the internet is unavailable.
- **Swap Functionality**: Quickly switch between "From" and "To" currencies.

### 2. Unit Converter
- **Multiple Categories**: Convert units for **Length**, **Weight**, and **Volume**.
- **Precision**: High-precision calculations for technical use cases.
- **Dynamic UI**: UI updates instantly as you type or change units.

### 3. Temperature Converter
- Supports **Celsius**, **Fahrenheit**, and **Kelvin**.
- Instant bi-directional conversion.

### 4. Calculator
- **Expression Support**: Build and calculate complex mathematical formulas.
- **Error Handling**: Gracefully handles invalid mathematical operations.
- **Modern Interface**: Clean, grid-based layout with distinct operator styling.

### 5. Notes & Tasks
- **Notes**: Create, edit, and delete personal notes with local persistence.
- **Tasks**: Manage a to-do list with completion checkboxes.
- **Reminders**: Set specific **Date and Time reminders** for tasks using integrated pickers.
- **Local Storage**: All data is saved securely on your device using the **Room Database**.

### 6. Adaptive UI
- **Portrait**: Standard bottom navigation for easy thumb access.
- **Landscape**: Automatically switches to a **Navigation Rail** and grid layouts to maximize vertical space.
- **State Persistence**: Remembers your current screen and tab even after tilting or rotating the phone.

## 🛠 Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room (SQLite abstraction)
- **Networking**: Retrofit & OkHttp (JSON parsing with Gson)
- **Concurrency**: Kotlin Coroutines & Flow
- **Dependency Management**: Gradle (Version Catalog / `libs.versions.toml`)

## 🏃 How to Run

### Prerequisites
- **Android Studio** (Latest stable version recommended)
- **JDK 17** or higher
- An Android device or emulator (API 24+)

### Steps to Build and Run
1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   ```
2. **Open in Android Studio**:
   - Launch Android Studio and select "Open".
   - Navigate to the `GitUtility` folder and click "OK".
3. **Sync Project**:
   - Wait for the Gradle sync to complete. Android Studio will automatically download the necessary dependencies.
4. **Run the App**:
   - Select your device/emulator from the dropdown at the top.
   - Click the green **Run** button (or press `Shift + F10`).

## 📁 Project Structure
- `data/`: Contains Room database entities, DAOs, and API interfaces.
- `viewmodel/`: The logic layer handling state and business operations.
- `screens/`: Jetpack Compose UI for each feature.
- `components/`: Reusable UI elements like buttons, cards, and selectors.
- `network/`: Retrofit client configuration.
