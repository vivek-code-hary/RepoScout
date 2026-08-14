# RepoScout

RepoScout is an Android application for discovering and exploring GitHub repositories. Users can search repositories, view detailed information, save repositories locally, and access saved repositories even when offline.

## ✨ Features

- 🔍 Search GitHub repositories
- ⏱️ Debounced search
- 📄 Pagination / Load more
- 📱 Repository details
- ⭐ Stars, forks, watchers and issues
- 🔖 Save / Unsave repositories
- 💾 Local storage with Room Database
- 📡 Offline access to saved repositories
- 🔄 Pull-to-refresh
- 🌐 Open repository on GitHub
- 🧭 Explore and Saved navigation
- ⚠️ API and network error handling


## 🛠️ Tech Stack

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **MVVM**
- **Clean Architecture**
- **Retrofit**
- **OkHttp**
- **Gson**
- **Room Database**
- **Kotlin Coroutines**
- **Kotlin Flow**
- **Navigation Compose**
- **GitHub REST API**


  ## 🏗️ Architecture

RepoScout follows a layered Clean Architecture approach.

text
Presentation
     ↓
Domain
     ↓
Data



  Project Structure

Iske immediately neeche:

markdown
## 📂 Project Structure

text
com.example.ashrut.reposcout
│
├── data
│   ├── local
│   │   ├── dao
│   │   ├── entity
│   │   └── RepoScoutDatabase
│   │
│   ├── remote
│   │   ├── api
│   │   ├── dto
│   │   └── RetrofitClient
│   │
│   └── repository
│
├── domain
│   ├── model
│   └── repository
│
├── presentation
│   ├── common
│   ├── details
│   ├── explore
│   ├── saved
│   └── screen
│
└── utils



## 🌐 GitHub API

RepoScout uses the GitHub REST API for repository search and repository details.

Base URL:

text
https://api.github.com/




## Search & Pagination

Iske immediately neeche paste karo:

markdown
## 🔎 Search & Pagination

Repository search uses a debounced search flow to avoid unnecessary API requests while typing.

Pagination loads additional repositories when the user approaches the bottom of the list.

The current search query is preserved while loading additional pages.




## 💾 Offline Support

Saved repositories are stored locally using Room Database.

When a saved repository cannot be fetched from the GitHub API, RepoScout can load its stored information from the local database.

text
GitHub API
    ↓
Success → Show fresh data
    ↓
Failure
    ↓
Room Database
    ↓
Show saved repository





## Section 9 — Saved Repositories

Iske immediately neeche:

markdown
## 🔖 Saved Repositories

Users can save repositories from the details screen.

Saved repositories can be:

- Viewed from the Saved screen
- Opened again
- Removed from saved
- Accessed offline


## 🧭 Navigation

The application contains three main destinations:

- **Explore** — Search and discover GitHub repositories
- **Details** — View detailed repository information
- **Saved** — View locally saved repositories

Bottom navigation is available on the Explore and Saved screens.





## 📸 Screenshots

### Explore

Add a screenshot of the Explore screen here.

### Repository Details

Add a screenshot of the Repository Details screen here.

### Saved Repositories

Add a screenshot of the Saved screen here.




## ⚠️ Error Handling

The application handles common API and network errors including:

- No internet connection
- GitHub API rate limits
- Invalid requests
- Resource not found
- GitHub server errors
- Pagination errors



## 🚀 Getting Started

### Requirements

- Android Studio
- Android SDK
- Kotlin
- Internet connection for GitHub API requests

### Installation

1. Clone this repository.
2. Open the project in Android Studio.
3. Sync the Gradle project.
4. Build the project.
5. Run the application on an emulator or physical Android device.



## 🔄 Git Workflow

The project uses feature-based Git commits.

Example:

text
feat: implement GitHub repository data flow
feat: add repository pagination
feat: improve explore repository cards
feat: add repository details screen
feat: complete repository search and saved features
docs: add project documentation



## License

markdown
## 📄 License

This project was created for learning and demonstration purposes.
