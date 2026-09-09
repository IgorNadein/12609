# Development guide

[← Project overview](../README.md) · [Русский](../README.ru.md) · [Deutsch](../README.de.md) · [Español](../README.es.md)

## Requirements

Use the same toolchain as the checked-in release workflow:

| Tool | Version |
| :--- | :--- |
| JDK | 17 |
| Gradle | 8.14.3 |
| Android Gradle Plugin | 8.11.1 |
| Kotlin | 2.0.21 |
| Android SDK Platform | 36 |
| Minimum device version | Android 8.0 / API 26 |

Install Android SDK Platform 36 through Android Studio's SDK Manager and configure the SDK path. Android Studio can create `offline-beauty-crm/local.properties`, or you can create it yourself:

```properties
sdk.dir=/absolute/path/to/Android/Sdk
```

`local.properties` is ignored by Git. The repository does not currently include a Gradle wrapper, so the commands below require an installed Gradle 8.14.3 distribution on `PATH`.

## Build and test

Run from `offline-beauty-crm/`:

```bash
gradle :app:assembleDebug
gradle :app:testDebugUnitTest
```

The debug APK is written to `app/build/outputs/apk/debug/app-debug.apk`. To install it on a connected emulator or development device:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

A debug APK cannot replace a release APK with a different signing certificate. Use an emulator or a separate test device to keep existing installation data intact.

The existing unit tests cover calendar indexing and performance-related state. They do not replace device checks for contacts, calendar permissions, SMS, backup/import or APK installation.

## Source map

```text
.github/workflows/android-release.yml   Signed APK build and GitHub Release
docs/assets/                           README cover
docs/screenshots/                      App captures with fictional data
offline-beauty-crm/
  app/build.gradle                     SDK levels, dependencies, signing
  app/src/main/
    AndroidManifest.xml                Permissions and Android components
    java/com/offlinebeautycrm/
      MainActivity.kt                  Compose UI, Room, integrations, workers
      PerformanceState.kt              Calendar indexes and state helpers
    res/                               Icons, themes and Android resources
  app/src/test/java/com/offlinebeautycrm/
    PerformanceStateTest.kt             Calendar/state unit tests
```

The app is currently a single Android module. Most implementation lives in `MainActivity.kt`: Room entities and DAO, the view model, Compose screens, system integrations, automation, backups and release checks. `PerformanceState.kt` separates calendar lookup/indexing helpers from that implementation.

Local databases, test artifacts, APKs, logs, signing credentials and build outputs are excluded from Git. The screenshots in this repository were captured from version 0.4.0 on an Android emulator using fictional clients, appointments and finance records. They show the actual Russian-language interface; README translations do not imply app localization.

## Repository cover

[`assets/social-preview.png`](assets/social-preview.png) is the 1280 × 640 repository preview image. Its editable vector source is [`assets/social-preview.svg`](assets/social-preview.svg), based on the README banner and the existing project logo.

Set the PNG in **Settings → General → Social preview → Edit → Upload an image**. Committing the file alone does not update this GitHub setting. The image follows [GitHub's social preview requirements](https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/customizing-your-repositorys-social-media-preview).

## Release workflow

[Android Release APK](../.github/workflows/android-release.yml) runs on changes under `offline-beauty-crm/**` or to the workflow itself on `master`, and can also be started manually. Documentation-only commits do not trigger an APK release.

The workflow sets up JDK 17 and Gradle 8.14.3, restores the signing keystore, runs `gradle :app:assembleRelease`, and publishes an APK through GitHub Releases. It derives the release name from `versionName` plus the Actions run number. `VERSION_NAME` and `VERSION_CODE` override the local build defaults.

The app checks `IgorNadein/12609` on GitHub for the latest release. A fork needs to update `GITHUB_LATEST_RELEASE_URL` in `MainActivity.kt` to use its own release feed.

## Release signing

The published portfolio APKs retain a legacy signing key for compatibility with earlier test installations. That key was previously committed and **must be treated as compromised**. Keeping it in Actions Secrets does not make it safe for production; use a new key for production distribution.

For a local signed build, provide these environment variables through your local secret-management setup:

| Environment variable | Purpose |
| :--- | :--- |
| `SIGNING_STORE_FILE` | Path to the keystore |
| `SIGNING_STORE_PASSWORD` | Keystore password |
| `SIGNING_KEY_ALIAS` | Signing alias |
| `SIGNING_KEY_PASSWORD` | Key password |

All four are required to configure release signing. Without them, a local release build is unsigned.

For GitHub Actions, configure repository secrets:

| Actions secret | Used for |
| :--- | :--- |
| `KEYSTORE_BASE64` | Base64-encoded keystore restored to the runner's temporary directory |
| `KEYSTORE_PASSWORD` | `SIGNING_STORE_PASSWORD` |
| `KEY_ALIAS` | `SIGNING_KEY_ALIAS` |
| `KEY_PASSWORD` | `SIGNING_KEY_PASSWORD` |

The workflow sets `SIGNING_STORE_FILE` to the restored temporary keystore. Never add keystores or secret values to documentation, source files or issues.
