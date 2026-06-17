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
- CI: GitHub Actions debug APK build.

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

## CI

GitHub Actions выполняет безопасную debug-сборку:

```bash
gradle :app:assembleDebug
```

Собранный debug APK загружается как workflow artifact. Release signing keys не хранятся в репозитории.

## Release Signing

Release signing настраивается только через environment variables:

- `SIGNING_STORE_FILE`
- `SIGNING_STORE_PASSWORD`
- `SIGNING_KEY_ALIAS`
- `SIGNING_KEY_PASSWORD`

Signing keys, keystores, локальные APK, generated build folders, test artifacts и local logs исключены из Git.

## Security Notes

Публичная версия подготовлена как portfolio snapshot. Старые публичные release APK и release tags удалены перед чисткой публикации. Любой signing key, который ранее попадал в Git, нужно считать скомпрометированным и не использовать для реального production distribution.
