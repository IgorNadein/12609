**Idioma:** [English](README.md) | [Русский](README.ru.md) | [Deutsch](README.de.md) | [Español](README.es.md)

# 12609 - Offline Beauty CRM

12609 es una aplicacion Android nativa de CRM para profesionales de salones de belleza. La aplicacion esta disenada para trabajo local offline-first: clientes, citas, servicios, finanzas, sincronizacion con contactos y calendario de Android, automatizacion orientada a SMS, JSON backup/import y comprobacion de actualizaciones APK.

El proyecto demuestra desarrollo de producto Android con Kotlin, Jetpack Compose, Material 3, Room/SQLite, WorkManager, integraciones del sistema, almacenamiento local-first y GitHub Actions CI.

## Funciones

- Base de clientes con enlace opcional a contactos del sistema Android.
- Calendario de citas con vistas de dia, 3 dias, semana y mes.
- Modos de sincronizacion con Android Contacts y Calendar.
- Catalogo de servicios con precio y duracion.
- Finanzas locales: pagos, deudas, ingresos y gastos.
- Cola de automatizacion y SMS-oriented workflows.
- JSON backup e import para datos locales.
- Flujo de comprobacion de actualizaciones basado en GitHub Releases, configurable para signed APK distribution.

## Stack tecnico

- Language: Kotlin.
- UI: Jetpack Compose y Material 3.
- Database: Room sobre SQLite.
- Background work: WorkManager.
- Android integrations: contacts, calendar, SMS, system APK installer.
- CI: GitHub Actions signed release APK build.

## Estructura del proyecto

```text
offline-beauty-crm/   Android application module
build-logs/           Local build logs, ignored by Git
test-artifacts/       Local test artifacts, ignored by Git
salon-apks/           Local APK outputs, ignored by Git
```

## Build local

El proyecto requiere Android SDK y Gradle compatible con Android Gradle Plugin `8.11.1`.

```bash
cd offline-beauty-crm
gradle :app:assembleDebug
```

El repositorio todavia no incluye Gradle wrapper, por lo que el build local depende de una distribucion Gradle instalada o Android Studio.

## Release CI

GitHub Actions puede construir y publicar un signed release APK desde `master`:

```bash
gradle :app:assembleRelease
```

El workflow restaura el signing keystore desde GitHub Actions Secrets, construye un signed release APK, crea un GitHub Release y sube el APK asset. La aplicacion puede usar el ultimo GitHub Release para su flujo de comprobacion de actualizaciones.

## Release Signing

Release signing se configura solo mediante environment variables y GitHub Actions Secrets:

- `SIGNING_STORE_FILE`
- `SIGNING_STORE_PASSWORD`
- `SIGNING_KEY_ALIAS`
- `SIGNING_KEY_PASSWORD`
- `KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

Signing keys, keystores, APK locales, generated build folders, test artifacts y local logs estan excluidos de Git.

## Security Notes

Esta version publica esta preparada como portfolio snapshot. El update key se almacena solo en GitHub Actions Secrets, por lo que los dispositivos de prueba firmados con la clave APK anterior pueden seguir recibiendo actualizaciones mediante GitHub Releases. Como esta clave estuvo antes en Git, debe seguir considerandose comprometida y no debe reutilizarse para una distribucion real de produccion.
