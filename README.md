# 🧩 Puzzle - Sliding Puzzle Android App

Aplikasi Android sliding puzzle native yang dibangun dengan Kotlin, Jetpack Compose, dan Material 3. Rasakan pengalaman menyelesaikan puzzle geser seperti puzzle fisik nyata dalam genggaman smartphone Anda.

## ✨ Fitur

- **Sliding Puzzle Gameplay** — Geser ubin untuk menyelesaikan puzzle
- **Ukuran Puzzle Fleksibel** — Pilihan ukuran dari 3×3 hingga 3×10
- **Upload Gambar Kustom** — Gunakan gambar sendiri atau gambar default
- **Timer & Penghitung Gerakan** — Lacak waktu dan jumlah gerakan
- **Acak Puzzle (Shuffle)** — Acak ulang puzzle kapan saja
- **Lihat Gambar** — Pratinjau gambar asli untuk referensi
- **Tampilkan/Angka Sembunyikan** — Toggle angka pada ubin
- **Statistik Permainan** — Data tersimpan secara lokal
- **Desain Board-Game** — UI/UX terinspirasi puzzle fisik
- **Mobile-First** — Dioptimalkan untuk layar smartphone

## 🎮 Mekanisme Puzzle

Puzzle terdiri dari ubin-ubin bergambar dengan satu slot kosong. Pemain harus menggeser ubin satu per satu hingga semua ubin tersusun dalam urutan yang benar dan gambar utuh terbentuk kembali.

Algoritma pembangkitan puzzle menjamin bahwa setiap puzzle yang dihasilkan selalu bisa diselesaikan (solvable). Puzzle tidak akan pernah berada dalam kondisi mustahil untuk diselesaikan.

## 🔢 Ukuran yang Didukung

| Ukuran | Total Ubin |
|--------|-----------|
| 3 × 3  | 9         |
| 3 × 4  | 12        |
| 3 × 5  | 15        |
| 3 × 6  | 18        |
| 3 × 7  | 21        |
| 3 × 8  | 24        |
| 3 × 9  | 27        |
| 3 × 10 | 30        |

## 🎨 Aset

- **Gambar Default Puzzle** — Disimpan sebagai resource lokal di `res/drawable/default_puzzle.jpg`
- **Logo Aplikasi** — Disimpan sebagai resource lokal di `res/drawable/puzzle_logo.jpg`

Kedua aset diunduh dan disimpan secara lokal, tidak bergantung pada URL remote saat runtime.

## 🏗️ Arsitektur

```
com.puzzle.app/
├── PuzzleApp.kt                 # Application class
├── MainActivity.kt              # Entry point Activity
├── data/
│   └── StatisticsRepository.kt  # DataStore persistence
├── engine/
│   └── PuzzleEngine.kt          # Core puzzle logic
└── ui/
    ├── PuzzleNavigation.kt      # Navigation Compose
    ├── screens/
    │   ├── HomeScreen.kt        # Layar awal
    │   ├── SetupScreen.kt       # Konfigurasi puzzle
    │   ├── PuzzleScreen.kt      # Gameplay
    │   └── StatisticsScreen.kt  # Statistik
    └── theme/
        └── Theme.kt             # Material 3 Theme
```

## 🛠️ Tech Stack

- **Bahasa:** Kotlin
- **UI Framework:** Jetpack Compose
- **Design System:** Material 3
- **Navigation:** Compose Navigation
- **Persistence:** DataStore Preferences
- **Image Loading:** Coil
- **Min SDK:** 26 (Android 8.0)
- **Target SDK:** 35 (Android 15)

## 📦 Instalasi

### Prasyarat
- Android Studio Hedgehog (2023.1.1) atau lebih baru
- JDK 17
- Android SDK 35

### Local Development

```bash
# Clone repository
git clone https://github.com/reyswumbo/puzzle.git

# Buka di Android Studio atau build dari terminal
./gradlew assembleDebug

# Install ke perangkat
./gradlew installDebug
```

## 🧪 Testing

```bash
# Jalankan unit tests
./gradlew test

# Jalankan semua tests
./gradlew connectedAndroidTest
```

Test meliputi:
- Solvability puzzle
- Shuffle generation
- Valid/invalid movement
- Solved-state detection
- Move counting
- Inversion counting

## 🔒 Release Build

Release build menggunakan R8 untuk optimasi:

```bash
# Build release APK
./gradlew assembleRelease

# Build release AAB (untuk Play Store)
./gradlew bundleRelease
```

Konfigurasi R8:
- `isMinifyEnabled = true` — Code shrinking & obfuscation
- `isShrinkResources = true` — Resource shrinking

## ⚙️ GitHub Actions

Workflow otomatis berjalan setiap push dan pull request ke branch `main`:

1. Checkout repository
2. Setup JDK 17
3. Setup Android SDK
4. Jalankan unit tests
5. Build release APK
6. Build release AAB
7. Upload artifacts

## 💾 Penyimpanan Statistik

Statistik disimpan menggunakan **DataStore Preferences** di penyimpanan lokal Android. Data yang disimpan:

- Total waktu bermain
- Total puzzle dimainkan
- Total puzzle terselesaikan
- Total gerakan

Data tetap tersimpan meskipun aplikasi ditutup dan dibuka kembali. Tidak memerlukan akun atau koneksi internet.

## 📊 Sistem Statistik

| Statistik | Deskripsi |
|-----------|-----------|
| ⏱ Total Waktu Bermain | Akumulasi waktu bermain puzzle |
| 🧩 Puzzle Dimainkan | Jumlah puzzle yang sudah dimulai |
| 🏆 Puzzle Terselesaikan | Jumlah puzzle yang berhasil diselesaikan |
| 🔢 Total Gerakan | Jumlah total gerakan semua puzzle |

## 🚀 Deployment

Aplikasi dapat di-build sebagai:
- **APK** — Untuk instalasi langsung
- **AAB** — Untuk distribusi via Google Play Store

## 📋 Alur Pengguna

```
Buka Aplikasi
     ↓
  Logo Puzzle
     ↓
 Start / Statistik
     ↓
    Start
     ↓
Upload Gambar / Gambar Default
     ↓
 Pilih Ukuran 3×3 — 3×10
     ↓
    Mulai
     ↓
 Puzzle Diacak
     ↓
Timer Mulai + Gerakan = 0
     ↓
   Bermain
     ↓
Acak / Lihat Gambar / Tampilkan Angka
     ↓
Puzzle Selesai
     ↓
Timer Berhenti
     ↓
Waktu + Gerakan Disimpan
     ↓
 Statistik Diperbarui
```

## 📄 Lisensi

Lihat [LICENSE](LICENSE) untuk informasi lisensi.
