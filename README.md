# <img src="https://github.com/user-attachments/assets/934d01cd-4b34-4bf4-baab-ef2bba1dc73f" width="60" height="60" align="center" /> Popcorn Picks

**Popcorn Picks** is a movie recommendation app that turns discovery into a game. Pick your favorite genres, scratch to reveal tailored film suggestions, and save the ones you love. Built with **Kotlin**, **Jetpack Compose**, and the **TMDB API**, it features smooth animations, interactive UI effects, and a personalized recommendation loop.

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

- **Ktor Client Mock** - Mock API responses.

- **Compose UI testing** - Used to UI testing and end-to-end testing.


## Here are some overview pictures:
🚧 Work in Progress...

## Animations:
🚧 Work in Progress...

## 📈 Recommendation System Diagram
Below is a visual representation of how the recommendation system works. The diagram is divided into three main sections: Onboarding, Recommendations, and User Interactions. To make it easier to follow, the full diagram is shown first, followed by a closer look at each section.
### 1. **Full Diagram**
![Image](https://github.com/user-attachments/assets/af3c5268-14be-4924-aa30-994b52be9f0b)
_An overview of the entire recommendation system, highlighting the interactions between different components._

### 2. **Onboarding Section**
![Image](https://github.com/user-attachments/assets/4f25c5f5-3e0d-4e09-9f74-ed5696a984d3)
_This section covers the onboarding process, where users are introduced to the system and initial preferences are set._

### 3. **Recommendations Section**
![Image](https://github.com/user-attachments/assets/55ab471f-7f95-44a2-832d-dbfc89f31490)
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

## Requirements
Minimum version: Android 7.0 (API level 24) or later📱

Target version: Android 14 (API level 34) or later📱

## Author

Adam Dawidziuk🧑‍💻
