# Gurukul – Offline-First School Management App (Android)

Gurukul is a modern **offline-first Android application** designed to manage school data such as **Classes and Students** efficiently.  
It follows **Clean Architecture + MVVM**, uses **Jetpack Compose** for UI, **Room** for local caching, and **Firebase Firestore** as the remote backend.

---

## ✨ Features

### 📚 Class Management
- Create new classes
- View class list (offline-first)
- Search classes (debounced)
- Filter by:
  - Gender (ALL / Boys / Girls)
  - Status (All / Active / Inactive)
- Pull-to-refresh sync
- Soft delete support (via Firestore)
- Background sync using WorkManager

### 📶 Offline-First Behavior
- Classes are **read from Room**
- Firestore acts as the **source of truth**
- Data is synced:
  - On screen open
  - On pull-to-refresh
  - Periodically via WorkManager

---

## 🧱 Tech Stack

### UI
- **Jetpack Compose**
- Material 3
- Reusable UI components (`core-ui`)
  - Search bar
  - Empty state
  - Loading state
  - Segmented toggle
  - Filter dropdown

### Architecture
- **MVVM**
- **Clean Architecture**
- Multi-module architecture

### Data
- **Room** – Local database
- **Firebase Firestore** – Remote backend
- Offline-first sync strategy

### Background Work
- **WorkManager**
- Periodic sync with network constraints

### Dependency Injection
- **Hilt**

---

## 🔮 Future Roadmap & Planned Enhancements

Gurukul is designed with **scalability and extensibility** in mind.  
The current Clean Architecture + MVVM setup allows us to introduce new features, data sources, and technologies **without major refactoring**.

---

### 🌐 Networking Enhancements
- Introduce **Retrofit + OkHttp** for REST-based APIs when backend expands beyond Firebase
- Support **multiple remote data sources** (Firebase + REST)
---

### 🧑‍🎓 New Feature Modules

#### Students Module
- Student profiles
- Class-wise student mapping
- Attendance tracking per student

#### Fees Module
- Fee structure management
- Payment status tracking
- Offline fee entry with background sync

#### Attendance Module
- Daily attendance marking
- Offline-first attendance sync
- Class-wise attendance reports

#### Dashboard / Analytics
- Class strength overview
- Attendance trends
- Performance & growth insights

---


### 🎨 UI & UX Enhancements
- Dark mode support
- Smooth animations & transitions
- Tablet & large-screen layouts
- Accessibility improvements (TalkBack, font scaling)


## 🧩 Module Structure

```text
app
│
├── core
│   ├── core-ui          # Reusable Compose components
│   ├── core-model       # Domain models
│   ├── core-database    # Room DB, entities, DAOs
│   ├── core-firebase    # Firestore data sources
│   ├── core-common      # UiState, utilities
│
├── feature
│   ├── feature-class
│   │   ├── presentation # Screens + ViewModels
│   │   ├── domain       # UseCases + Repository interfaces
│   │   └── data         # Repository impl, mappers, workers
│   ├── feature-auth
│
└── app                  # Navigation, Application class




