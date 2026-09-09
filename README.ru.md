<p align="center">
  <a href="README.md">English</a> · <a href="README.ru.md">Русский</a> · <a href="README.de.md">Deutsch</a> · <a href="README.es.md">Español</a>
</p>

<p align="center">
  <img src="docs/assets/banner.svg" alt="12609 — Offline Service CRM" width="100%">
</p>

<h1 align="center">12609 · Offline Service CRM</h1>
<p align="center"><strong>Клиенты, записи и финансы — вместе на вашем Android-устройстве.</strong></p>

<p align="center">
  <a href="https://github.com/IgorNadein/12609/releases/latest"><img src="https://img.shields.io/github/v/release/IgorNadein/12609?style=flat-square&amp;color=c21159" alt="GitHub release"></a>
  <a href="https://github.com/IgorNadein/12609/actions/workflows/android-release.yml"><img src="https://github.com/IgorNadein/12609/actions/workflows/android-release.yml/badge.svg" alt="Android Release APK"></a>
  <img src="https://img.shields.io/badge/Android-8.0%2B-3d8061?style=flat-square" alt="Android 8.0+">
  <img src="https://img.shields.io/badge/Kotlin-2.0.21-7f52ff?style=flat-square" alt="Kotlin 2.0.21">
</p>

<p align="center">
  <a href="https://github.com/IgorNadein/12609/releases/latest"><strong>Скачать APK</strong></a> ·
  <a href="https://github.com/IgorNadein/12609/releases">Релизы</a> ·
  <a href="docs/DEVELOPMENT.md">Сборка и разработка</a> ·
  <a href="https://github.com/IgorNadein/12609/issues">Сообщить о проблеме</a>
</p>

Нативная Android CRM для частных специалистов и небольших компаний, работающих по записи. Повседневная работа хранится локально; контакты и календарь Android можно подключить по желанию.

## Как выглядит приложение

<table>
  <tr><th>Клиенты</th><th>Записи</th><th>Финансы</th></tr>
  <tr>
    <td width="33%"><img src="docs/screenshots/clients.png" alt="Клиенты" width="100%"></td>
    <td width="33%"><img src="docs/screenshots/appointments.png" alt="Записи" width="100%"></td>
    <td width="33%"><img src="docs/screenshots/finances.png" alt="Финансы" width="100%"></td>
  </tr>
</table>

Реальные скриншоты версии 0.4.0 из Android-эмулятора с вымышленными данными. Интерфейс приложения сейчас на русском; переводы README доступны выше.

## Для повседневной работы

| | |
| :--- | :--- |
| **Клиенты** | Карточки клиентов, контактные данные и заметки. При необходимости — связь с контактами Android. |
| **Записи** | Календарь на день, три дня, неделю и месяц. Услуги, длительность визитов и выходные. |
| **Услуги и финансы** | Каталог цен и длительности услуг, оплаты, задолженности, доходы и расходы. |
| **Автоматизация** | SMS-сценарии, очередь задач и возможность подтверждения перед выполнением. |
| **Копии и обновления** | Экспорт и импорт JSON, настройка автоматических резервных копий и проверка обновлений APK через GitHub Releases. |

### Локальное хранение по умолчанию

Основные данные CRM находятся в базе Room/SQLite на устройстве. Для ведения клиентов и записей интернет не нужен. Интеграции используют системные контакты и календарь Android; подключённые аккаунты могут синхронизироваться через свои сервисы. Для SMS нужны поддержка устройства и разрешение. Проверка релизов и загрузка APK требуют интернета.

## Попробовать приложение

1. Откройте [последний релиз](https://github.com/IgorNadein/12609/releases/latest) и скачайте `.apk` из раздела **Assets**.
2. Установите APK на Android 8.0 или новее. Если система запросит, разрешите установку для приложения, через которое открываете файл.
3. Добавьте клиента, создайте услугу и запись. Дополнительные интеграции и резервные копии настраиваются в разделе «Настройки».

> Репозиторий опубликован как портфолио. APK используют прежний ключ подписи, который ранее попадал в Git; его следует считать скомпрометированным. Для промышленного распространения нужен новый ключ. Подробнее — в [инструкции по подписи](docs/DEVELOPMENT.md#release-signing).

## Технологии

**Kotlin** · **Jetpack Compose** · **Material 3** · **Room / SQLite** · **Coroutines / Flow** · **WorkManager** · **GitHub Actions**

## Устройство проекта

Большая часть интерфейса, работы с базой и интеграций пока находится в `MainActivity.kt`. Индексы календаря и вспомогательная логика производительности вынесены в `PerformanceState.kt`.

- [MainActivity.kt](offline-beauty-crm/app/src/main/java/com/offlinebeautycrm/MainActivity.kt)
- [PerformanceState.kt](offline-beauty-crm/app/src/main/java/com/offlinebeautycrm/PerformanceState.kt)
- [PerformanceStateTest.kt](offline-beauty-crm/app/src/test/java/com/offlinebeautycrm/PerformanceStateTest.kt)
- [Android Release APK](.github/workflows/android-release.yml)

## Локальная сборка

Нужны **JDK 17**, **Gradle 8.14.3** и **Android SDK Platform 36**. Gradle wrapper в репозитории пока отсутствует.

```bash
git clone https://github.com/IgorNadein/12609.git
cd 12609/offline-beauty-crm
gradle :app:assembleDebug
```

Настройка SDK, запуск модульных тестов, структура проекта и выпуск подписанного APK описаны в [руководстве разработчика](docs/DEVELOPMENT.md) (на английском).

## Обратная связь

Нашли ошибку или есть идея? [Создайте issue](https://github.com/IgorNadein/12609/issues). Укажите версию приложения и Android, опишите шаги воспроизведения. Для примеров используйте вымышленные данные.
