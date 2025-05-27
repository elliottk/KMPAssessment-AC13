# 🗞️News Reader - 🚀Andrew Carmichael 🚀

A cross-platform news reader application built with Kotlin Multiplatform and Compose Multiplatform targeting Android and iOS.

## Build instructions

The application can be built from Android Studio under the following recommended configuration.

- OSX 15.5
- Android Studio Meerkat Feature Drop | 2024.3.2
- Kotlin Multiplatform Plugin 0.8.5(243)-7
- XCode 16.2
- Android emulator 16.0
- iOS Simulator 18.3

To run:
1. Open the project in Android Studio.
2. Select either the Android or iOS run configuration.
3. Build and run on emulator or simulator.

## Features

- ✅ Multiplatform UI and business logic
- ✅ Paginated headline list with headline, image, and published date
- ✅ Error and loading states
- ✅ Supports pagination with configurable page size
- ✅ Unit tested core logic (ViewModel)
- 🟡 Offline caching: not persisted to disk but survives configuration changes and connection loss 

## Tech Stack

- **UI Tech**: JetBrains Compose Multiplatform (Android & iOS)
- **Network**: Ktor with kotlinx.serialization
- **DI**: Dependency injection partially implemented using Koin
- **Testing**: kotlinx.coroutines + Turbine + assertk

## Architecture

The application is built using Clean Architecture following Google's recommended best practices.
Data moves through the application's architecture in a unidirectional pattern through each layer
in the architecture. These layers are easily navigated in the code base by following the package names.
- `data`: Data layer deals with loading news headlines from the network
- `domain`: Domain layer depends on the data layer, but adds business logic like paging and sorting
- `presentation`: Presentation layer depends on Domain Layer, converting the data into displayable format.
- `ui`: Ui layer consumes presentation layer data and displays it on the screen.