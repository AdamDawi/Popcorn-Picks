# Contributing to Popcorn Picks

First of all, thank you for considering contributing to Popcorn Picks! We're excited to have your help in making this app better for everyone. Whether you're improving the codebase, fixing bugs, or adding new features, all contributions are welcome!

## How to Contribute

### 1. Fork the Repository
To contribute, first, **fork** the repository to your own GitHub account. This allows you to freely make changes without affecting the original project.

### 2. Clone Your Fork
Clone your fork to your local machine:
```bash
git clone https://github.com/your-username/Popcorn-Picks
```

### 3. Set Up Environment
Before running the project locally, make sure you have set up the following:
- **TMDB API Key:**
  To fetch movie data from the TMDB API, you'll need an API key. Create an account on [TMDB](https://developer.themoviedb.org/reference/intro/getting-started)
  Then, add the API key to your local gradle.properties file as follows:
```properties
API_KEY="your_api_key_here"
```
- **google-services.json:**
  Download your `google-services.json` file from Firebase and place it in the `app/` directory. This file is only required to run the project locally. Google Services are used only for Firebase Test Lab, which runs automatically during push and pull requests using my Firebase project.

### 4. Create a New Branch
Before you start working on a new feature or bug fix, create a new branch for your changes. This helps keep the repository organized.
```bash
git checkout -b feature/your-feature-name
```
or
```bash
git checkout -b bugfix/issue-name
```

### 5. Make Changes
Make your changes to the codebase. Try to keep your commits focused on a single task and write meaningful commit messages.

### 6. Run Tests
Before pushing your changes, ensure that your code is working as expected and that all tests pass. You can run unit tests locally using:
```bash
./gradlew test
```
You can run instrumentation tests locally using:
```bash
./gradlew connectedAndroidTest
```

### 7. Commit Your Changes
Once you're happy with your changes, commit them to your branch:
```bash
git add .
git commit -m "NEW: feature"
```

### 8. Push Your Changes
Push your changes to your fork:
```bash
git push origin feature/your-feature-name
```

### 9. Create a Pull Request
Go to your repository on GitHub and open a pull request (PR) to the Popcorn Picks repository. Describe your changes clearly in the PR description.

## Pull Request Guidelines
Please ensure your pull request follows these guidelines:
- **Description**: Provide a clear description of what your PR does and why it's needed.
- **Tests:** Ensure that your changes are covered by tests. If you are adding a new feature, include tests for it if you can.
- **Commit Messages:** Use clear and concise commit messages. Preferably use the following structure:
    - `NEW: Add feature name`
    - `FIX: Bug fix in feature name`
    - `UPDATE: Code refactoring`
    - `DELETE: Remove feature or code`

## Testing Guidelines
Popcorn Picks includes unit tests, instrumentation tests to ensure everything works smoothly. To maintain consistency and readability in our tests, please follow these guidelines:

### Unit Tests
- **Naming Convention**:
  The naming structure for unit tests should be:
  `unitOfWork_stateUnderTest_expectedBehaviour`

  For example:
    - `getGenres_success_genresListStateUpdatedWithCorrectData`
    - `fetchRecommendedMoviesFromApiByMovie_successAndResultListIsEmpty_updatePageForLikedMovieInvokedOnceWithCorrectPage`
    - `errorScreen_messageNotNull_displaysCorrectMessage`
- **Test Structure (AAA Pattern):**
  Follow the AAA (Arrange, Act, Assert) pattern in each unit test to keep your code organized and readable:
1. **Arrange:** Set up the necessary conditions for the test (e.g., initializing objects, setting mock data, etc.).
2. **Act:** Perform the action being tested (e.g., call a method or trigger an event).
3. **Assert:** Check the results of the action to ensure the expected behavior (e.g., validate the output, verify state changes, etc.).
   Example:
```kotlin
@Test
fun loginUser_validCredentials_successfulLogin() {
    // Arrange
    val username = "testUser"
    val password = "password123"
    val expectedResult = "Login successful"

    // Act
    val result = loginUser(username, password)

    // Assert
    assertEquals(expectedResult, result)
}
```
### Instrumentation tests
Just like in unit tests except the AAA pattern is not required in instrumentation tests.

## Continuous Integration (CI)
The repository uses GitHub Actions for continuous integration. The following workflows are triggered:
- **Unit Tests:** Automatically run on each push and pull request.
- **Instrumentation Tests:** Run on Firebase Test Lab for each pull request to ensure compatibility with real devices.

## Getting Help
If you have any questions or run into issues, feel free to open an issue in the repository or reach out directly.

## Thank You!
We appreciate your contributions to Popcorn Picks. Together, we can make it even better!