<p align="center">
  <a href="README.md">English</a> · <a href="README.ru.md">Русский</a> · <a href="README.de.md">Deutsch</a> · <a href="README.es.md">Español</a>
</p>

<p align="center">
  <img src="docs/assets/banner.svg" alt="12609 — Offline Service CRM" width="100%">
</p>

<h1 align="center">12609 · Offline Service CRM</h1>
<p align="center"><strong>Kunden, Termine und Finanzen. Gemeinsam auf deinem Android-Gerät.</strong></p>

<p align="center">
  <a href="https://github.com/IgorNadein/12609/releases/latest"><img src="https://img.shields.io/github/v/release/IgorNadein/12609?style=flat-square&amp;color=c21159" alt="GitHub release"></a>
  <a href="https://github.com/IgorNadein/12609/actions/workflows/android-release.yml"><img src="https://github.com/IgorNadein/12609/actions/workflows/android-release.yml/badge.svg" alt="Android Release APK"></a>
  <img src="https://img.shields.io/badge/Android-8.0%2B-3d8061?style=flat-square" alt="Android 8.0+">
  <img src="https://img.shields.io/badge/Kotlin-2.0.21-7f52ff?style=flat-square" alt="Kotlin 2.0.21">
</p>

<p align="center">
  <a href="https://github.com/IgorNadein/12609/releases/latest"><strong>APK herunterladen</strong></a> ·
  <a href="https://github.com/IgorNadein/12609/releases">Versionen</a> ·
  <a href="docs/DEVELOPMENT.md">Build & Entwicklung</a> ·
  <a href="https://github.com/IgorNadein/12609/issues">Fehler melden</a>
</p>

Eine native Android-CRM-App für selbstständige Dienstleister und kleine Unternehmen mit Terminbetrieb. Verwalte den Arbeitsalltag lokal und verbinde bei Bedarf Android-Kontakte und Kalender.

## Ein Blick in die App

<table>
  <tr><th>Kunden</th><th>Termine</th><th>Finanzen</th></tr>
  <tr>
    <td width="33%"><img src="docs/screenshots/clients.png" alt="Kunden" width="100%"></td>
    <td width="33%"><img src="docs/screenshots/appointments.png" alt="Termine" width="100%"></td>
    <td width="33%"><img src="docs/screenshots/finances.png" alt="Finanzen" width="100%"></td>
  </tr>
</table>

Echte Screenshots der Version 0.4.0 aus einem Android-Emulator mit fiktiven Beispieldaten. Die App-Oberfläche ist derzeit auf Russisch; README-Übersetzungen sind oben verlinkt.

## Für den Arbeitsalltag

| | |
| :--- | :--- |
| **Kunden** | Kundenprofile, Kontaktdaten und Notizen verwalten; optional mit Android-Kontakten verknüpfen. |
| **Termine** | Tages-, 3-Tage-, Wochen- und Monatsansicht. Leistungen, Termindauer und freie Tage verwalten. |
| **Leistungen & Finanzen** | Preise und Dauer pflegen, Zahlungen erfassen und offene Beträge, Einnahmen und Ausgaben verfolgen. |
| **Automatisierung** | SMS-Abläufe mit Aufgabenwarteschlange und optionaler Bestätigung einrichten. |
| **Sicherung & Updates** | JSON-Sicherungen exportieren und importieren, automatische Sicherungen konfigurieren und auf GitHub Releases nach APK-Updates suchen. |

### Standardmäßig lokal

Die CRM-Daten liegen in einer Room/SQLite-Datenbank auf dem Gerät. Die tägliche Datenverwaltung funktioniert offline. Kontakte und Kalender werden über Android-Systemanbieter eingebunden; verknüpfte Konten können über eigene Dienste synchronisieren. SMS benötigt Geräteunterstützung und Berechtigung. Versionsprüfung und APK-Download benötigen Internet.

## App ausprobieren

1. Die [aktuelle Version](https://github.com/IgorNadein/12609/releases/latest) öffnen und die `.apk` unter **Assets** herunterladen.
2. Auf Android 8.0 oder neuer installieren. Bei Bedarf die Installation für die App erlauben, mit der die APK geöffnet wird.
3. Einen Kunden und eine Leistung anlegen und einen Termin erstellen. Optionale Integrationen und Sicherungen in den Einstellungen konfigurieren.

> Dieses Repository ist ein Portfolio-Projekt. Veröffentlichte APKs verwenden einen früher eingecheckten Signaturschlüssel, der als kompromittiert gilt. Für den produktiven Vertrieb ist ein neuer Schlüssel erforderlich. Siehe [Signierung](docs/DEVELOPMENT.md#release-signing).

## Technologien

**Kotlin** · **Jetpack Compose** · **Material 3** · **Room / SQLite** · **Coroutines / Flow** · **WorkManager** · **GitHub Actions**

## Quellcode erkunden

Der größte Teil der Oberfläche, Datenbank- und Integrationslogik befindet sich derzeit in `MainActivity.kt`. Kalenderindizes und zugehörige Performance-Hilfsfunktionen liegen in `PerformanceState.kt`.

- [MainActivity.kt](offline-beauty-crm/app/src/main/java/com/offlinebeautycrm/MainActivity.kt)
- [PerformanceState.kt](offline-beauty-crm/app/src/main/java/com/offlinebeautycrm/PerformanceState.kt)
- [PerformanceStateTest.kt](offline-beauty-crm/app/src/test/java/com/offlinebeautycrm/PerformanceStateTest.kt)
- [Android Release APK](.github/workflows/android-release.yml)

## Lokal bauen

Benötigt werden **JDK 17**, **Gradle 8.14.3** und **Android SDK Platform 36**. Das Repository enthält noch keinen Gradle Wrapper.

```bash
git clone https://github.com/IgorNadein/12609.git
cd 12609/offline-beauty-crm
gradle :app:assembleDebug
```

SDK-Konfiguration, Unit-Tests, Projektstruktur und signierte Releases sind im [Entwicklerhandbuch](docs/DEVELOPMENT.md) auf Englisch beschrieben.

## Feedback

Ein Problem gefunden oder eine Idee? [Ein Issue erstellen](https://github.com/IgorNadein/12609/issues). App- und Android-Version sowie Schritte zur Reproduktion angeben und fiktive Beispieldaten verwenden.
