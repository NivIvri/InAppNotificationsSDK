# InApp Notifications SDK

An Android SDK that lets you show in-app notification dialogs to users. The notifications are managed through a backend and admin portal, so you can create and control them without releasing a new app version.

This was built as a course project to learn Android SDK development, REST APIs, and Firebase.


---

## Main Features

- Show a notification dialog on any screen in an Android app
- Filter notifications by screen and user type (regular / premium)
- Date range support — notifications only show between start and end dates
- Show-once mode — a notification won't appear again after the user dismisses it
- Tracks how many times each notification was viewed and clicked
- Admin portal to create, edit, activate/deactivate, and delete notifications

---

## Technologies

- **Android SDK** — Kotlin, Retrofit, SharedPreferences
- **Backend** — Python, Flask, Flask-CORS
- **Database** — Google Firestore
- **Admin Portal** — Plain HTML, CSS, JavaScript
- **Deployment** — Vercel (configured but not deployed)

---

## Project Structure

```
InAppNotificationsSDK/
├── app/                        # Demo Android app
│   └── src/main/java/.../demo/
│       ├── MainActivity.kt
│       ├── HomeScreenActivity.kt
│       └── SettingsScreenActivity.kt
│
├── notification-sdk/           # The SDK module
│   └── src/main/java/.../notificationsdk/
│       ├── Notification.kt         # Data model
│       ├── NotificationApi.kt      # Retrofit interface
│       ├── NotificationSdk.kt      # Main SDK entry point
│       └── NotificationView.kt     # Dialog UI
│
├── backend/                    # Flask backend
│   ├── app.py
│   ├── routes.py
│   ├── firestore_service.py
│   ├── requirements.txt
│   └── vercel.json
│
├── admin-portal/               # Web admin interface
│   ├── index.html
│   ├── style.css
│   └── script.js
│
└── docs/                       # Documentation
```

---

## How to Run the Backend

1. Get your Firebase service account key from Firebase Console → Project Settings → Service Accounts → Generate new private key
2. Save it as `backend/serviceAccountKey.json`
3. Open a terminal in the `backend/` folder

```bash
python -m venv venv
venv\Scripts\activate.bat      # Windows
pip install -r requirements.txt
python app.py
```

Server runs at `http://localhost:5000`

---

## How to Run the Admin Portal

The admin portal is a static HTML page. Just open it in a browser:

```
admin-portal/index.html
```

Make sure the backend is running first. The portal connects to `http://localhost:5000`.

---

## How to Run the Android App

1. Open the project in Android Studio
2. In `app/src/main/.../demo/MainActivity.kt`, make sure `BASE_URL` points to your backend:
   - Emulator: `http://10.0.2.2:5000/`
   - Real device: use your computer's local IP, e.g. `http://192.168.1.x:5000/`
3. Run the app on an emulator or device

---

## SDK Usage Example

```kotlin
// 1. Initialize once (e.g. in MainActivity)
NotificationSdk.initialize("http://10.0.2.2:5000/")

// 2. Call on any screen to show a relevant notification
NotificationSdk.checkAndShow(this, "home", "premium")

// 3. Reset seen notifications (e.g. in Settings)
NotificationSdk.clearSeenNotifications(this)
```

**Parameters for `checkAndShow`:**
- `activity` — the current Activity
- `screen` — name of the current screen (must match `targetScreen` in the notification)
- `userType` — `"regular"` or `"premium"` (must match `audience` in the notification)

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/notifications?screen=&userType=` | Get active notifications for a screen |
| GET | `/api/notifications/<id>` | Get a single notification |
| POST | `/api/notifications` | Create a notification |
| PUT | `/api/notifications/<id>` | Update a notification |
| PATCH | `/api/notifications/<id>/activate` | Activate |
| PATCH | `/api/notifications/<id>/deactivate` | Deactivate |
| PATCH | `/api/notifications/<id>/view` | Increment view counter |
| PATCH | `/api/notifications/<id>/click` | Increment click counter |
| DELETE | `/api/notifications/<id>` | Delete |
| GET | `/api/admin/notifications` | Get all notifications (admin only) |

---

## Known Limitations

- The backend has no authentication — anyone with the URL can call the API
- `showOnce` is stored on the device (SharedPreferences), not tied to a user account. Reinstalling the app resets it
- Date filtering uses string comparison (`YYYY-MM-DD`), not proper datetime objects
- Only one notification is shown per screen visit (the first valid one)
- The backend is not deployed — it only runs locally
- No error messages shown to the user if the network request fails
- Not tested on older Android versions (minSdk is 28)
