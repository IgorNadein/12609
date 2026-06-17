**Язык:** [English](README.md) | [Русский](README.ru.md) | [Deutsch](README.de.md) | [Español](README.es.md)

# 12609 - Offline Beauty CRM

12609 - нативное Android-приложение CRM для мастера салона красоты. Приложение рассчитано на offline-first работу: клиенты, записи, услуги, финансы, синхронизация с системными контактами и календарем, SMS-oriented автоматизация задач, JSON backup/import и проверка обновлений APK.

Проект демонстрирует Android product development: Kotlin, Jetpack Compose, Material 3, Room/SQLite, WorkManager, системные Android-интеграции, local-first хранение данных и GitHub Actions CI.

## Возможности

- Клиентская база с опциональной привязкой к системным контактам Android.
- Календарь записей: день, 3 дня, неделя и месяц.
- Режимы синхронизации с Android Contacts и Calendar.
- Каталог услуг с ценой и длительностью.
- Локальные финансы: оплаты, долги, доходы и расходы.
- Очередь автоматизации и SMS-oriented workflows.
- JSON backup и import локальных данных.
- Проверка обновлений через GitHub Releases, которую можно настроить для signed APK distribution.

## Стек

- Language: Kotlin.
- UI: Jetpack Compose и Material 3.
- Database: Room поверх SQLite.
- Background work: WorkManager.
- Android integrations: contacts, calendar, SMS, system APK installer.
- CI: GitHub Actions signed release APK build.

## Структура проекта

```text
offline-beauty-crm/   Android application module
build-logs/           Local build logs, ignored by Git
test-artifacts/       Local test artifacts, ignored by Git
salon-apks/           Local APK outputs, ignored by Git
```

## Локальная сборка

Нужны Android SDK и Gradle, совместимый с Android Gradle Plugin `8.11.1`.

```bash
cd offline-beauty-crm
gradle :app:assembleDebug
```

Gradle wrapper пока не добавлен, поэтому локальная сборка зависит от установленного Gradle или Android Studio.

## Release CI

GitHub Actions может собирать и публиковать signed release APK из `master`:

```bash
gradle :app:assembleRelease
```

Workflow восстанавливает signing keystore из GitHub Actions Secrets, собирает signed release APK, создает GitHub Release и загружает APK asset. Приложение может использовать latest GitHub Release для update-checking flow.

## Release Signing

Release signing настраивается только через environment variables и GitHub Actions Secrets:

- `SIGNING_STORE_FILE`
- `SIGNING_STORE_PASSWORD`
- `SIGNING_KEY_ALIAS`
- `SIGNING_KEY_PASSWORD`
- `KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

Signing keys, keystores, локальные APK, generated build folders, test artifacts и local logs исключены из Git.

## Security Notes

Публичная версия подготовлена как portfolio snapshot. Update key хранится только в GitHub Actions Secrets, поэтому тестовые устройства со старой подписью APK смогут получать обновления через GitHub Releases. Так как этот ключ раньше попадал в Git, его все равно нужно считать скомпрометированным и не использовать для реального production distribution.
