<div align="center">

# 🏪 RepoStore

### A Modern GitHub-Powered Android App Store

[![Android](https://img.shields.io/badge/Android-8.0%2B-green.svg?style=flat&logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![GitHub release](https://img.shields.io/github/v/release/samyak2403/RepoStore?include_prereleases)](https://github.com/samyak2403/RepoStore/releases)

<img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp" width="120" alt="RepoStore Logo"/>

**Discover, Download & Install Android Apps from GitHub**

[<img src="https://img.shields.io/badge/Download-APK-brightgreen?style=for-the-badge&logo=android" />](https://github.com/samyak2403/RepoStore/releases/latest)

</div>

---

## � SRcreenshots

<div align="center">
<table>
  <tr>
    <td><img src="screenshots/home.png" width="200"/></td>
    <td><img src="screenshots/details.png" width="200"/></td>
    <td><img src="screenshots/search.png" width="200"/></td>
    <td><img src="screenshots/settings.png" width="200"/></td>
  </tr>
  <tr>
    <td align="center"><b>Home</b></td>
    <td align="center"><b>App Details</b></td>
    <td align="center"><b>Search</b></td>
    <td align="center"><b>Settings</b></td>
  </tr>
</table>
</div>

---

## ✨ Features

<table>
<tr>
<td>

### 🔍 Discover Apps
- Browse trending Android apps from GitHub
- Search repositories with powerful filters
- Category-based app discovery
- Featured apps carousel

</td>
<td>

### 📥 Easy Installation
- Direct APK download from releases
- Real-time download progress
- Auto-detect installed apps
- One-tap install/open

</td>
</tr>
<tr>
<td>

### � Richr Details
- Full README with Markdown support
- Screenshot gallery with zoom
- Release notes & changelogs
- Developer profiles

</td>
<td>

### 🔐 GitHub Integration
- OAuth sign-in support
- Increased API rate limits
- View developer repositories
- Star count & fork info

</td>
</tr>
</table>

### Additional Features

- 🌙 **Dark Mode** - Beautiful dark theme support
- 💳 **UPI Donations** - Support developers directly
- 📜 **Open Source Licenses** - View all library credits
- 🔄 **Pull to Refresh** - Always get latest data

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                        UI Layer                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │  Activities │  │  Fragments  │  │  Adapters   │     │
│  └──────┬──────┘  └──────┬──────┘  └─────────────┘     │
│         │                │                              │
│         └────────┬───────┘                              │
│                  ▼                                      │
│         ┌─────────────────┐                             │
│         │   ViewModels    │                             │
│         └────────┬────────┘                             │
└──────────────────┼──────────────────────────────────────┘
                   │
┌──────────────────┼──────────────────────────────────────┐
│                  ▼           Data Layer                 │
│         ┌─────────────────┐                             │
│         │   Repository    │                             │
│         └────────┬────────┘                             │
│                  │                                      │
│    ┌─────────────┼─────────────┐                        │
│    ▼             ▼             ▼                        │
│ ┌──────┐   ┌──────────┐   ┌────────┐                   │
│ │ Room │   │ Retrofit │   │  Auth  │                   │
│ │  DB  │   │   API    │   │ GitHub │                   │
│ └──────┘   └──────────┘   └────────┘                   │
└─────────────────────────────────────────────────────────┘
```



## 📦 Installation

### Download APK

[<img src="https://img.shields.io/badge/Download%20Latest-APK-success?style=for-the-badge" />](https://github.com/samyak2403/RepoStore/releases/latest)

### Build from Source

```bash
# Clone the repository
git clone https://github.com/samyak2403/RepoStore.git

# Navigate to project directory
cd RepoStore

# Build debug APK
./gradlew assembleDebug

# Or build release APK
./gradlew assembleRelease
```

---

## ⚙️ Configuration

### GitHub OAuth (Optional)

To enable GitHub sign-in for increased API limits (60 → 5,000 requests/hour):

1. Go to **[GitHub Developer Settings](https://github.com/settings/developers)**
2. Click **"New OAuth App"**
3. Fill in:
   - **Application name:** `RepoStore`
   - **Homepage URL:** `https://github.com`
   - **Callback URL:** `https://github.com`
4. Copy the **Client ID**
5. Update in `GitHubAuth.kt`:

```kotlin
private const val CLIENT_ID = "your_client_id_here"
```

---

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. 🍴 **Fork** the repository
2. 🌿 Create a **branch** (`git checkout -b feature/amazing-feature`)
3. 💾 **Commit** changes (`git commit -m 'Add amazing feature'`)
4. 📤 **Push** to branch (`git push origin feature/amazing-feature`)
5. 🔃 Open a **Pull Request**

---

## 📜 License

```
MIT License

Copyright (c) 2024 Samyak Kamble

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

---

## 📚 Libraries Used

| Library | Purpose |
|:--------|:--------|
| [Retrofit](https://github.com/square/retrofit) | HTTP Client |
| [OkHttp](https://github.com/square/okhttp) | Networking |
| [Glide](https://github.com/bumptech/glide) | Image Loading |
| [Room](https://developer.android.com/training/data-storage/room) | Local Database |
| [Markwon](https://github.com/noties/Markwon) | Markdown Rendering |
| [PhotoView](https://github.com/GetStream/photoview-android) | Zoomable Images |
| [Material Components](https://github.com/material-components/material-components-android) | UI Components |

---

## 👨‍💻 Author

<div align="center">

**Samyak Kamble**

[![GitHub](https://img.shields.io/badge/GitHub-samyak2403-181717?style=for-the-badge&logo=github)](https://github.com/samyak2403)

</div>

---

## 💖 Support

If you find this project helpful:

- ⭐ **Star** this repository
- 🐛 **Report** bugs and issues
- 💡 **Suggest** new features
- 💳 **Donate** via UPI (in-app)

---

<div align="center">

**Made with ❤️ in India**

![Visitors](https://visitor-badge.laobi.icu/badge?page_id=samyak2403.RepoStore)

</div>
