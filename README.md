# Colors

Colors is a modern Android application built with Jetpack Compose that likely features color-based
puzzles or level-based progression. The project follows Clean Architecture principles and utilizes a
robust stack of modern Android libraries.

## 🚀 Features

- **Level-based Gameplay**: Progressive difficulty through structured levels.
- **Clean Architecture**: Separation of concerns between Data, Domain, and UI layers.
- **Reactive UI**: Built entirely with Jetpack Compose.
- **Dependency Injection**: Powered by Hilt.
- **Modern Navigation**: Type-safe navigation using Kotlin Serialization.
- **Monetization**: Integrated with Google AdMob.
- **Analytics & Stability**: Integrated with Firebase Analytics and Crashlytics.
- **CI/CD**: Automated release pipeline via GitHub Actions.

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/)
- **Architecture**: Clean Architecture (MVI/MVVM)
- **Navigation**: [Compose Navigation](https://developer.android.com/jetpack/compose/navigation)
  with Type Safety
- **Local Storage
  **: [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore)
- **Networking/Serialization
  **: [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- **Monetization**: [Google AdMob](https://admob.google.com/)
- **Firebase**: Analytics & Crashlytics

## 🏗 Project Structure

```text
app/src/main/java/com/alavpa/colors/
├── data/           # Data source implementations (Local, Remote)
├── di/             # Hilt Modules
├── domain/         # Business logic: Models, Use Cases, Repository interfaces
├── ui/             # UI Components: Compose screens, ViewModels, Theme
│   ├── components/ # Reusable UI pieces
│   ├── level/      # Level-specific logic and screens
│   ├── navigation/ # Type-safe route definitions
│   └── theme/      # Material 3 color schemes and typography
└── ColorsApp.kt    # Application class with Hilt and Ads initialization
```

## ⚙️ Setup & Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/Colors.git
   ```
2. **Setup Local Properties**:
   Create a `local.properties` file in the root directory and add your AdMob IDs if testing locally:
   ```properties
   admob.app_id=ca-app-pub-xxxxxxxxxxxxxxxx~xxxxxxxxxx
   admob.banner_id=ca-app-pub-xxxxxxxxxxxxxxxx/xxxxxxxxxx
   admob.interstitial_id=ca-app-pub-xxxxxxxxxxxxxxxx/xxxxxxxxxx
   admob.rewarded_id=ca-app-pub-xxxxxxxxxxxxxxxx/xxxxxxxxxx
   ```
3. **Build the project**:
   Open the project in **Android Studio (Ladybug or newer)** and sync Gradle.

## 🤖 CI/CD with GitHub Actions

The project includes a `release.yml` workflow that:

- Decodes the release keystore and Google Services JSON.
- Builds the release Bundle (`.aab`) and APK.
- Authenticates via **Workload Identity Federation**.
- Automatically uploads to the **Google Play Internal Testing** track.

## 📄 License

This project is for educational/personal use. Please check the license file for more details.
