# 12609

Нативное Android-приложение для локальной CRM мастера салона красоты.

## Проект

- Android-модуль: `offline-beauty-crm`
- Package ID: `com.offlinebeautycrm`
- UI: Kotlin + Jetpack Compose + Material 3
- Локальная база: Room / SQLite
- Название приложения: `12609`
- Исходный SVG логотипа: `340851.svg`

## Возможности

- Клиентская база с привязкой к системным контактам.
- Записи в календарном виде: день, 3 дня, неделя, месяц.
- Синхронизация с системными контактами и календарем через выбранные режимы.
- Каталог услуг с ценой и длительностью.
- Локальные финансы: оплаты, долги, доходы и расходы.
- Автоматизация через очередь задач и SMS.
- JSON backup/import локальных данных.
- Проверка обновлений через стабильные GitHub Releases с установкой APK через системный установщик Android.

## Сборка

Требуется Android SDK и Gradle, совместимый с Android Gradle Plugin `8.11.1`.

```bash
cd offline-beauty-crm
gradle :app:assembleDebug
```

В этом репозитории Gradle wrapper пока не добавлен, поэтому сборка зависит от установленного локально `gradle` или Android Studio.

## Релизы и обновления

Приложение проверяет обновления через GitHub Releases. Для публикации APK на GitHub используется workflow `.github/workflows/android-release.yml`.

Один раз добавьте в `Settings -> Secrets and variables -> Actions` такие repository secrets:

- `KEYSTORE_BASE64` - release keystore, закодированный в base64.
- `KEYSTORE_PASSWORD` - пароль keystore.
- `KEY_ALIAS` - alias ключа.
- `KEY_PASSWORD` - пароль ключа.

Перед релизом увеличьте `versionCode` и `versionName` в `offline-beauty-crm/app/build.gradle`, затем создайте и отправьте тег:

```bash
git tag v0.3.1
git push origin v0.3.1
```

GitHub Actions соберет подписанный APK и прикрепит его к GitHub Release. Тег должен быть выше установленной версии, иначе приложение покажет, что обновлений нет.

## Локальные артефакты

Папки `test-artifacts`, `build-logs`, `salon-apks`, `tooling`, а также Gradle build-кеши не коммитятся. Они используются только для локального тестирования, скриншотов и временных APK.
