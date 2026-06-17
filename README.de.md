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
- CI: GitHub Actions debug APK build.

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

## CI

GitHub Actions fuehrt einen sicheren Debug-Build aus:

```bash
gradle :app:assembleDebug
```

Das erzeugte debug APK wird als workflow artifact hochgeladen. Release signing keys werden nicht im Repository gespeichert.

## Release Signing

Release signing wird nur ueber Environment Variables konfiguriert:

- `SIGNING_STORE_FILE`
- `SIGNING_STORE_PASSWORD`
- `SIGNING_KEY_ALIAS`
- `SIGNING_KEY_PASSWORD`

Signing keys, keystores, lokale APKs, generated build folders, test artifacts und local logs sind aus Git ausgeschlossen.

## Security Notes

Diese oeffentliche Version ist als Portfolio-Snapshot vorbereitet. Alte public release APKs und release tags wurden vor der Bereinigung entfernt. Jeder signing key, der frueher in Git enthalten war, muss als kompromittiert gelten und darf nicht fuer echte Production Distribution wiederverwendet werden.
