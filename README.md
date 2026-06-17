**Language:** [English](README.md) | [Русский](README.ru.md) | [Deutsch](README.de.md) | [Español](README.es.md)

# 12609 - Offline Beauty CRM

12609 is a native Android CRM for beauty salon masters. The application is built for offline-first local work: clients, appointments, services, finance records, system contact/calendar synchronization, SMS task automation, JSON backup/import, and APK update checks.

The project demonstrates Android product development with Kotlin, Jetpack Compose, Material 3, Room/SQLite, WorkManager, system integrations, local-first data storage, and GitHub Actions CI.

## Features

- Client database with optional links to Android system contacts.
- Appointment calendar with day, 3-day, week, and month views.
- Synchronization modes for Android contacts and calendar events.
- Service catalog with prices and duration.
- Local finance tracking: payments, debts, income, and expenses.
- Automation task queue and SMS-oriented workflows.
- JSON backup and import for local data.
- Update-checking flow based on GitHub Releases, configurable for signed APK distribution.

## Tech Stack

- Language: Kotlin.
- UI: Jetpack Compose and Material 3.
- Database: Room over SQLite.
- Background work: WorkManager.
- Android integrations: contacts, calendar, SMS, system APK installer.
- CI: GitHub Actions signed release APK build.

## Project Structure

```text
offline-beauty-crm/   Android application module
build-logs/           Local build logs, ignored by Git
test-artifacts/       Local test artifacts, ignored by Git
salon-apks/           Local APK outputs, ignored by Git
```

## Local Build

The project requires Android SDK and Gradle compatible with Android Gradle Plugin `8.11.1`.

```bash
cd offline-beauty-crm
gradle :app:assembleDebug
```

The repository does not include a Gradle wrapper yet, so the local build depends on an installed Gradle distribution or Android Studio.

## Release CI

GitHub Actions can build and publish a signed release APK from `master`:

```bash
gradle :app:assembleRelease
```

The workflow restores the signing keystore from GitHub Actions Secrets, builds a signed release APK, creates a GitHub Release, and uploads the APK asset. The app can use the latest GitHub Release for its update-checking flow.

## Release Signing

Release signing is configured through environment variables and GitHub Actions Secrets only:

- `SIGNING_STORE_FILE`
- `SIGNING_STORE_PASSWORD`
- `SIGNING_KEY_ALIAS`
- `SIGNING_KEY_PASSWORD`
- `KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

Signing keys, keystores, local APKs, generated build folders, test artifacts, and local logs are excluded from Git.

## Security Notes

This public version is prepared as a portfolio snapshot. The update key is stored in GitHub Actions Secrets only, so test devices signed with the previous APK key can still receive updates through GitHub Releases. Because this key was previously committed, it must still be treated as compromised and should not be reused for real production distribution.
