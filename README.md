# VolumeTracker 🚀

A production-ready Android application built with Kotlin and Jetpack Compose that tracks Solana memecoin volume spikes using GMGN data and sends push notifications.

## Features

- **Real-time Volume Spike Detection**: Monitors Solana memecoins for 2x volume spikes with wash-trading filters
- **Push Notifications**: Instant alerts when volume spikes are detected
- **Dashboard**: View all triggered alerts with potential gain tracking
- **Search**: Find coins by name, ticker, or contract address
- **Watchlist**: Track your favorite coins
- **Background Monitoring**: WorkManager runs checks every 2 minutes even when app is closed

## Architecture

This app follows **Clean Architecture** principles with clear separation of concerns:

```
├── data/              # Data sources (API, Database, Repositories)
│   ├── api/           # Retrofit API services and DTOs
│   ├── db/            # Room database entities and DAOs
│   ├── repository/    # Repository implementations
│   └── worker/        # WorkManager background jobs
├── domain/            # Business logic layer
│   ├── model/         # Domain models
│   ├── usecase/       # Use cases
│   └── util/          # Utilities (wash trading detection)
├── presentation/      # UI layer (Jetpack Compose)
│   ├── dashboard/     # Dashboard screen
│   ├── search/        # Search screen
│   ├── detail/        # Coin detail screen
│   ├── watchlist/     # Watchlist screen
│   ├── settings/      # Settings screen
│   ├── navigation/    # Navigation graph
│   └── theme/         # Material 3 theme
├── di/                # Hilt dependency injection modules
└── security/          # Security utilities and encrypted preferences
```

## Tech Stack

- **Language**: Kotlin 2.0
- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt (Dagger)
- **Networking**: Retrofit + OkHttp + Moshi
- **Database**: Room
- **Background Work**: WorkManager
- **Image Loading**: Coil
- **Charts**: Vico
- **Logging**: Timber

## Security Features

- ✅ HTTPS-only with certificate pinning
- ✅ Encrypted SharedPreferences (Android Keystore)
- ✅ ProGuard/R8 obfuscation
- ✅ Input validation and sanitization
- ✅ No cleartext traffic
- ✅ Secure backup rules

## Setup Instructions

### Prerequisites

- Android Studio Hedgehog or later
- JDK 17
- Android SDK with API 26-35

### Building the App

1. Clone the repository:
```bash
git clone https://github.com/Sakam77/VolumeTracker.git
cd VolumeTracker
```

2. Configure Firebase (Optional, for FCM):
   - Add your `google-services.json` to the `app/` directory
   - Or use the placeholder file for local development

3. Build the project:
```bash
./gradlew build
```

4. Run on device/emulator:
```bash
./gradlew installDebug
```

### Configuration

The app uses the GMGN API at `https://gmgn.ai`. No API key is required for basic functionality.

For production deployment:
1. Update certificate pins in `network_security_config.xml`
2. Configure release signing in `app/build.gradle.kts`
3. Replace Firebase placeholder config with real credentials

## Volume Spike Detection Algorithm

The app detects volume spikes using:

1. **Historical Volume Tracking**: Stores volume snapshots every 2 minutes
2. **Rolling Average Calculation**: Computes average volume over the past hour
3. **2x Threshold Detection**: Triggers alert when current volume ≥ 2x average
4. **Wash Trading Filter**: Filters out suspicious volume using:
   - Unique wallet count vs. trade count ratio
   - Buy/sell balance analysis
   - Volume concentration over time

## Disclaimer

⚠️ **IMPORTANT**: This app is for informational purposes only and is NOT financial advice. Memecoin trading is extremely high-risk. Past volume spikes do not guarantee future gains. Always do your own research and never invest more than you can afford to lose.

## License

This project is open source and available under the MIT License.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Support

For issues and questions, please open an issue on GitHub.
