# RecipeMate

RecipeMate is a sophisticated Android application designed to enrich your culinary journey. Developed by a team of skilled developers, RecipeMate empowers users to discover, save, and manage an extensive collection of recipes. Featuring a modern user interface and robust functionality, RecipeMate seamlessly integrates with external APIs to provide a comprehensive recipe management experience.

## Table of Contents

1. [Overview](#overview)
2. [Key Features](#key-features)
3. [Architecture](#architecture)
4. [Technology Stack](#technology-stack)
5. [Installation Guide](#installation-guide)
6. [Usage Instructions](#usage-instructions)
7. [Screenshots](#screenshots)
8. [Video Demonstration](#video-demonstration)
9. [Contact Information](#contact-information)

## Overview

RecipeMate offers an intuitive platform for exploring a diverse array of recipes, managing favorites, and enhancing your cooking experience. The app leverages a clean, modern design and advanced functionality to deliver an engaging and efficient user experience.

## Key Features

### Authentication

- Welcome users with a splash screen.
- Secure registration and login functionalities.

### Home Screen

- Curated list of recipes sourced from a reliable external API.
- Bookmark recipes with a single tap; bookmarks are visually highlighted.
- Efficient bottom navigation for easy access to various sections of the app.

### Search Functionality

- Comprehensive search feature to find recipes using keywords.
- Grid view display of search results with Lottie animations for no-match scenarios.

### Bookmark Management

- Manage saved recipes with a dedicated Bookmark Fragment.
- Remove recipes from bookmarks by clicking the bookmark icon. A Snackbar provides an undo option for accidental deletions.
- A confirmation dialog is presented for deletions to ensure intentional actions.

### Profile Management

- Customize your profile with a gallery-selected photo.
- View and update personal information such as email, phone number, gender, and password.
- Secure password updates through a custom dialog.
- Sign out with a confirmation dialog for user security.

### Recipe Details

- Detailed view of each recipe, including origin, ingredients, and preparation instructions.
- Options to bookmark, share, and watch related YouTube videos within the app.

### Responsive User Interface

- Shimmer effects enhance visual appeal during data loading.
- Consistent Material Design components for a modern and user-friendly experience.

## Architecture

RecipeMate adheres to the MVVM (Model-View-ViewModel) architectural pattern to ensure a clean separation between the user interface and business logic. This approach enhances maintainability and scalability, allowing for efficient management of app state and UI updates.

### Key Components

- **Repository Pattern**: Serves as the single source of truth for data operations, managing data access and manipulation.
  
- **LiveData & ViewModel**: 
  - **LiveData**: Provides lifecycle-aware data that updates the UI automatically in response to data changes.
  - **ViewModel**: Manages UI-related data in a lifecycle-conscious manner, ensuring that data survives configuration changes.
  
- **Kotlin Coroutines**: Handles asynchronous tasks efficiently, allowing for smooth and responsive app performance by managing background operations.

## Technology Stack

- **MVVM Architecture**: Promotes scalability and maintainability.
- **Kotlin Coroutines**: Manages background tasks and ensures smooth performance.
- **Room Database**: Local storage solution for user data and bookmarked recipes.
- **Retrofit**: Facilitates API interactions for fetching recipe data.
- **Shimmer**: Provides appealing loading animations for a polished user experience.
- **Lottie Animations**: Enhances interactions with engaging animations.
- **YouTube Player**: Integrates video content directly into the app.
- **Navigation Component**: Simplifies in-app navigation and user flow.

## Installation Guide

To set up RecipeMate in your local development environment:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/MahmoudDabour1/RecipeMate
2. **Open the project in Android Studio.**
3. **Build and run the application on an emulator or physical device.**
4. **Configure necessary permissions and API keys for accessing recipe data.**

## Usage Instructions

Explore the app’s functionality with the following instructions:

- **Home Fragment**: 
  - Browse through a curated list of recipes.
  - Bookmark your favorites with a single tap.
  - Access detailed information about each recipe, including ingredients and preparation steps.

- **Search Fragment**: 
  - Use the search feature to find recipes based on keywords.
  - View search results in a grid layout.
  - Experience Lottie animations for scenarios with no matching recipes.

- **Bookmark Fragment**: 
  - Manage your saved recipes.
  - Delete bookmarks with a confirmation dialog.
  - Undo accidental deletions using Snackbar.

- **Profile Fragment**: 
  - Update your profile details, including personal information and profile picture.
  - Manage account settings and securely change your password.

- **Details Fragment**: 
  - View comprehensive information about each recipe, including origin, ingredients, and preparation instructions.
  - Share recipes via other apps.
  - Watch related YouTube videos for more details and cooking demonstrations.

## Screenshots

Here are some screenshots showcasing the key features of RecipeMate:
### Home Screen ###

<img src="https://github.com/user-attachments/assets/f2862390-2cea-4a83-81f1-266029cfd1d9" alt="Home Screen" width="350">

### Search Screen ###

<img src="https://github.com/user-attachments/assets/64100b15-72df-4f24-bb3d-05c53f7e46c3" alt="Search Functionality" width="350">

### Bookmark Screen ###

<img src="https://github.com/user-attachments/assets/ffebb7d7-1632-418d-a8a1-3181e1aa6d02" alt="Bookmark Management" width="350">

### Recipe Details Screen ###

<img src="https://github.com/user-attachments/assets/24515a71-4c70-42ac-bba6-bbd44c501c31" alt="Recipe Details" width="350">

### Profile Screen ###

<img src="https://github.com/user-attachments/assets/c1e1637e-eab2-4263-82fb-b91374f43384" alt="Profile Management" width="350">




## Video Demonstration

Experience RecipeMate in action through the following video:

[video5942665760340972802.webm](https://github.com/user-attachments/assets/f401ebd8-e325-471b-b54f-122fdcc58433)

## Contact Information

**Project Maintainers:**

- [Mahmoud Dabour](https://github.com/MahmoudDabour1) (Team Leader)
- [Dina Fadel](https://github.com/1dina)
- [Mohamed Attia](https://github.com/MohammedAttia3104)
- [Mohamed Esam](https://github.com/MoEsam2)

---

We welcome your feedback and questions! Feel free to reach out to us through the provided contact details. We are always here to assist you and appreciate your interest in RecipeMate. Thank you for exploring our app!

