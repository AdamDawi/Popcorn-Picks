# <img src="https://github.com/user-attachments/assets/934d01cd-4b34-4bf4-baab-ef2bba1dc73f" width="60" height="60" align="center" /> Popcorn Picks

**Popcorn Picks** is a movie recommendation app that turns discovery into a game. Pick your favorite genres, scratch to reveal tailored film suggestions, and save the ones you love. Built with **Kotlin**, **Jetpack Compose**, and the [TMBD API](https://developer.themoviedb.org/reference/intro/getting-started), it features smooth animations, interactive UI effects, and a personalized recommendation loop.

[![Android Unit Tests](https://github.com/AdamDawi/Popcorn-Picks/actions/workflows/android-unit-tests.yml/badge.svg)](https://github.com/AdamDawi/Popcorn-Picks/actions/workflows/android-unit-tests.yml) [![Android Instrumentation Tests](https://github.com/AdamDawi/Popcorn-Picks/actions/workflows/android_instrumentation_tests.yml/badge.svg)](https://github.com/AdamDawi/Popcorn-Picks/actions/workflows/android_instrumentation_tests.yml)

## ⭐️Features
### 🎬 Movie Discovery & Recommendations
- **Scratch Card System:** Reveal movie recommendations one at a time by scratching off the interactive card.
  
- **Personalized Suggestions:** Movies are recommended based on your chosen genres and previously liked films.

- **Reroll Option:** Not interested in the current recommendation? Use reroll to get a new one instantly.

### 📝 Onboarding & Genre Selection
- **First-Time Setup:** Users must select at least two genres to start.

- **Data Fetching:** For each selected genre, up to 60 movies are fetched to choose from.

- **Dynamic Borders Animation:** A red border animation highlights selected genres during setup.

### 📅 Movie Details
- **Smooth Scrolling Effect:** The header image smoothly transitions as you scroll through movie details.

- **Additional Information:** View detailed movie info, including rating, released date, description and more.

- **Shimmer Effect:** A loading animation enhances the user experience while fetching images.

### 👤 User Profile & Liked Movies
🚧 Work in Progress...
- **This feature is not yet implemented.** Development is in progress, and the User Profile screen will include a list of saved movies and more features in future updates.

## ⚙️Technologies
### 📱App:
- **Kotlin & Jetpack Compose** - Modern UI toolkit for declarative UI design.

- **MVI Architecture** - Organized with **package structure by feature** for maintainability.

- **Ktor** - Network client for fetching movie data from TMDB API.

- **Room Database** - Stores user-selected genres, liked movies and cached movie recommendations.

- **Koin** - Dependency injection

- **Coil** - Efficient image loading and loading states.

- **Timber** - Logging utility.

- **Gson Serialization** - Data parsing.

### ✅Testing:
- **JUnit4** - Utilized to write and execute unit tests.

- **Kotlin Coroutines Test** - Used to test asynchronous code that utilizes coroutines.

- **Hamcrest & Truth** - Assertion libraries for test validations.

- **MockK** - Mocking framework for unit tests.

- **Turbine** - Used for testing Kotlin Flow emissions.

- **Ktor Client Mock** - Mock API responses.

- **Compose UI testing** - Used to UI testing and end-to-end testing.

### ✅CI and Testing:
- **Continuous integration (CI)** ensures unit tests are automatically executed on each push and pull request.
- **Unit Tests:**
Unit tests are executed on GitHub-hosted Ubuntu containers, ensuring that each code change is automatically tested in a clean, consistent environment during the CI process.

- **Instrumental Tests on Firebase Test Lab:**
Before running the instrumentation tests on Firebase Test Lab, the project must first be built. This includes checking out the repository, setting up the necessary files (like `google-services.json`), and configuring the environment, including JDK 17. Once the project is built, the Android Test APK is generated and deployed. After that, the tests are executed on Firebase Test Lab using real production devices, ensuring that the app performs smoothly across a wide range of devices. Test results and logs are readily accessible via the Firebase Console for detailed analysis and debugging.

## Here are some overview pictures:

![Image](https://github.com/user-attachments/assets/1676f730-e62d-4e93-9aee-60265e04f7ed)
![Image](https://github.com/user-attachments/assets/2486c92b-f68a-4efa-b51b-59301c730805)
![Image](https://github.com/user-attachments/assets/c9afd63e-6b73-4b3d-9429-61e5a0d8ca4f)
![Image](https://github.com/user-attachments/assets/e736c823-5d32-41a1-93de-cc54fa15a4e2)
![Image](https://github.com/user-attachments/assets/9a44335a-48d9-49e4-a62e-27a178e737ca)
![Image](https://github.com/user-attachments/assets/5eb8df03-a464-4d7c-b239-20194cf79463)
![Image](https://github.com/user-attachments/assets/04dbdb49-2a5b-4eff-aebc-dbd43d4cf784)
![Image](https://github.com/user-attachments/assets/2cc9b381-1538-45f0-8a08-f1c3e8d2ef65)

## Animations:
🚧 Work in Progress...

![Image](https://github.com/user-attachments/assets/6fce77d2-aa49-468e-9e38-f8fc0cdba9b4)
![Image](https://github.com/user-attachments/assets/00bdc138-5788-4269-8a4d-646e365f02b6)
![Image](https://github.com/user-attachments/assets/371136cb-db7e-4363-b293-69936e5ebda2)
![Image](https://github.com/user-attachments/assets/544948c1-1072-4d5f-831d-06751336f8d4)
![Image](https://github.com/user-attachments/assets/51ce65ab-76bc-485f-9aba-bf3f46abdda8)

## 📈 Recommendation System Diagram
Below is a visual representation of how the recommendation system works. The diagram is divided into three main sections: Onboarding, Recommendations, and User Interactions. To make it easier to follow, the full diagram is shown first, followed by a closer look at each section.
### 1. **Full Diagram**
![Image](https://github.com/user-attachments/assets/af3c5268-14be-4924-aa30-994b52be9f0b)
_An overview of the entire recommendation system, highlighting the interactions between different components._

### 2. **Onboarding Section**
![Image](https://github.com/user-attachments/assets/4f25c5f5-3e0d-4e09-9f74-ed5696a984d3)
_This section covers the onboarding process, where users are introduced to the system and initial preferences are set._

### 3. **Recommendations Section**
![Image](https://github.com/user-attachments/assets/4dcd911b-1013-415c-9c7b-d6765b6f265b)
_This part explains how the recommendation system generates personalized suggestions for users based on their preferences._

### 4. **User Interactions Section**
![Image](https://github.com/user-attachments/assets/5f9839ba-2efc-443a-b7f8-750adc41e122)

_This section illustrates how users interact with the system and how their feedback influences future recommendations._

## Installation

1. Clone the repository:
```bash
git clone https://github.com/AdamDawi/Popcorn-Picks
```
2. Open the project in Android Studio.
3. Be sure the versions in gradle are same as on github
4. Get the [TMBD API Key](https://developer.themoviedb.org/reference/intro/getting-started)
5. Add the API key to your local `gradle.properties` file:
```bash
API_KEY="your_api_key_here"
```
6. Download your `google-services.json` file from Firebase and place it in the `app/` directory.
- This file is only required to run the project locally.
- Google Services are used only for Firebase Test Lab, which runs automatically during push and pull requests using my Firebase project.

## 🤝 Contributing
Any kind of positive contribution is welcome!
> Please take a moment to read my [`CONTRIBUTING`](CONTRIBUTING.md) guidelines for details on how to contribute, including how to submit pull requests. Also, be sure to review my [`CODE OF CONDUCT`](CODE_OF_CONDUCT.md) to ensure a respectful and collaborative environment.

## Requirements
Minimum version: Android 7.0 (API level 24) or later📱

Target version: Android 14 (API level 34) or later📱

## Author

Adam Dawidziuk🧑‍💻
