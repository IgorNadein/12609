**Sprache:** [English](README.md) | [Русский](README.ru.md) | [Deutsch](README.de.md) | [Español](README.es.md)

# 12609 - Offline Beauty CRM

12609 ist eine native Android-CRM-App fuer Beauty-Salon-Master. Die Anwendung ist fuer offline-first lokale Arbeit gebaut: Kunden, Termine, Dienstleistungen, Finanzen, Synchronisierung mit Android-Kontakten und Kalender, SMS-orientierte Aufgabenautomatisierung, JSON backup/import und APK update checks.

Das Projekt zeigt Android-Produktentwicklung mit Kotlin, Jetpack Compose, Material 3, Room/SQLite, WorkManager, Systemintegrationen, local-first Datenspeicherung und GitHub Actions CI.

## Funktionen

- Kundendatenbank mit optionaler Verknuepfung zu Android-Systemkontakten.
- Terminkalender mit Tages-, 3-Tage-, Wochen- und Monatsansicht.
- Synchronisierungsmodi fuer Android Contacts und Calendar.
- Dienstleistungskatalog mit Preisen und Dauer.
- Lokale Finanzverwaltung: Zahlungen, Schulden, Einnahmen und Ausgaben.
- Automatisierungswarteschlange und SMS-orientierte Workflows.
- JSON backup und import fuer lokale Daten.
- Update-Pruefung ueber GitHub Releases, konfigurierbar fuer signed APK distribution.

## Tech Stack

- Language: Kotlin.
- UI: Jetpack Compose und Material 3.
- Database: Room ueber SQLite.
- Background work: WorkManager.
- Android integrations: contacts, calendar, SMS, system APK installer.
- CI: GitHub Actions signed release APK build.

## Projektstruktur

```text
offline-beauty-crm/   Android application module
build-logs/           Local build logs, ignored by Git
test-artifacts/       Local test artifacts, ignored by Git
salon-apks/           Local APK outputs, ignored by Git
```

## Lokaler Build

Das Projekt benoetigt Android SDK und Gradle, kompatibel mit Android Gradle Plugin `8.11.1`.

```bash
cd offline-beauty-crm
gradle :app:assembleDebug
```

Ein Gradle Wrapper ist noch nicht enthalten, daher haengt der lokale Build von einer installierten Gradle-Distribution oder Android Studio ab.

## Release CI

GitHub Actions kann aus `master` ein signed release APK bauen und veroeffentlichen:

```bash
gradle :app:assembleRelease
```

Der Workflow stellt den signing keystore aus GitHub Actions Secrets wieder her, baut ein signed release APK, erstellt ein GitHub Release und laedt das APK asset hoch. Die App kann das neueste GitHub Release fuer ihren Update-Check verwenden.

## Release Signing

Release signing wird nur ueber Environment Variables und GitHub Actions Secrets konfiguriert:

- `SIGNING_STORE_FILE`
- `SIGNING_STORE_PASSWORD`
- `SIGNING_KEY_ALIAS`
- `SIGNING_KEY_PASSWORD`
- `KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

Signing keys, keystores, lokale APKs, generated build folders, test artifacts und local logs sind aus Git ausgeschlossen.

## Security Notes

Diese oeffentliche Version ist als Portfolio-Snapshot vorbereitet. Der update key wird nur in GitHub Actions Secrets gespeichert, sodass Testgeraete mit der alten APK-Signatur weiterhin Updates ueber GitHub Releases erhalten koennen. Da dieser key frueher in Git enthalten war, muss er trotzdem als kompromittiert gelten und darf nicht fuer echte Production Distribution wiederverwendet werden.
