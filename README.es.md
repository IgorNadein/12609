<p align="center">
  <a href="README.md">English</a> · <a href="README.ru.md">Русский</a> · <a href="README.de.md">Deutsch</a> · <a href="README.es.md">Español</a>
</p>

<p align="center">
  <img src="docs/assets/banner.svg" alt="12609 — Offline Service CRM" width="100%">
</p>

<h1 align="center">12609 · Offline Service CRM</h1>
<p align="center"><strong>Clientes, citas y finanzas. Juntos en tu dispositivo Android.</strong></p>

<p align="center">
  <a href="https://github.com/IgorNadein/12609/releases/latest"><img src="https://img.shields.io/github/v/release/IgorNadein/12609?style=flat-square&amp;color=c21159" alt="GitHub release"></a>
  <a href="https://github.com/IgorNadein/12609/actions/workflows/android-release.yml"><img src="https://github.com/IgorNadein/12609/actions/workflows/android-release.yml/badge.svg" alt="Android Release APK"></a>
  <img src="https://img.shields.io/badge/Android-8.0%2B-3d8061?style=flat-square" alt="Android 8.0+">
  <img src="https://img.shields.io/badge/Kotlin-2.0.21-7f52ff?style=flat-square" alt="Kotlin 2.0.21">
</p>

<p align="center">
  <a href="https://github.com/IgorNadein/12609/releases/latest"><strong>Descargar APK</strong></a> ·
  <a href="https://github.com/IgorNadein/12609/releases">Versiones</a> ·
  <a href="docs/DEVELOPMENT.md">Compilación y desarrollo</a> ·
  <a href="https://github.com/IgorNadein/12609/issues">Informar de un error</a>
</p>

Una aplicación CRM nativa para Android, pensada para profesionales independientes y pequeños negocios que trabajan con citas. Gestiona el día a día de forma local y conecta los contactos y el calendario de Android si lo necesitas.

## Así es la aplicación

<table>
  <tr><th>Clientes</th><th>Citas</th><th>Finanzas</th></tr>
  <tr>
    <td width="33%"><img src="docs/screenshots/clients.png" alt="Clientes" width="100%"></td>
    <td width="33%"><img src="docs/screenshots/appointments.png" alt="Citas" width="100%"></td>
    <td width="33%"><img src="docs/screenshots/finances.png" alt="Finanzas" width="100%"></td>
  </tr>
</table>

Capturas reales de la versión 0.4.0 en un emulador Android con datos ficticios. La interfaz está actualmente en ruso; las traducciones del README están enlazadas arriba.

## Para el trabajo diario

| | |
| :--- | :--- |
| **Clientes** | Fichas de clientes, datos de contacto y notas, con vinculación opcional a los contactos de Android. |
| **Citas** | Vistas de día, tres días, semana y mes. Gestión de servicios, duración de las citas y días libres. |
| **Servicios y finanzas** | Catálogo de precios y duración, registro de pagos, deudas, ingresos y gastos. |
| **Automatización** | Flujos de SMS con cola de tareas y opciones de confirmación. |
| **Copias y actualizaciones** | Exportación e importación de JSON, copias automáticas configurables y búsqueda de actualizaciones APK en GitHub Releases. |

### Almacenamiento local por defecto

Los datos del CRM se guardan en una base Room/SQLite del dispositivo. La gestión diaria funciona sin conexión. Las integraciones usan los proveedores de contactos y calendario de Android; las cuentas vinculadas pueden sincronizarse mediante sus propios servicios. Los SMS requieren compatibilidad del dispositivo y permiso. La consulta de versiones y la descarga del APK necesitan internet.

## Probar la aplicación

1. Abre la [última versión](https://github.com/IgorNadein/12609/releases/latest) y descarga el archivo `.apk` de **Assets**.
2. Instálalo en Android 8.0 o posterior. Si se solicita, permite la instalación desde la aplicación con la que abres el APK.
3. Añade un cliente, crea un servicio y programa una cita. Configura las integraciones opcionales y las copias de seguridad en Ajustes.

> Este repositorio es un proyecto de portafolio. Los APK publicados usan una clave de firma que se incluyó anteriormente en Git y debe considerarse comprometida. Usa una clave nueva para distribución en producción. Consulta la [guía de firma](docs/DEVELOPMENT.md#release-signing).

## Tecnologías

**Kotlin** · **Jetpack Compose** · **Material 3** · **Room / SQLite** · **Coroutines / Flow** · **WorkManager** · **GitHub Actions**

## Explorar el código

La mayor parte de la interfaz, la base de datos y las integraciones se encuentra actualmente en `MainActivity.kt`. Los índices del calendario y las funciones de rendimiento están en `PerformanceState.kt`.

- [MainActivity.kt](offline-beauty-crm/app/src/main/java/com/offlinebeautycrm/MainActivity.kt)
- [PerformanceState.kt](offline-beauty-crm/app/src/main/java/com/offlinebeautycrm/PerformanceState.kt)
- [PerformanceStateTest.kt](offline-beauty-crm/app/src/test/java/com/offlinebeautycrm/PerformanceStateTest.kt)
- [Android Release APK](.github/workflows/android-release.yml)

## Compilación local

Necesitas **JDK 17**, **Gradle 8.14.3** y **Android SDK Platform 36**. El repositorio todavía no incluye Gradle wrapper.

```bash
git clone https://github.com/IgorNadein/12609.git
cd 12609/offline-beauty-crm
gradle :app:assembleDebug
```

Consulta la [guía de desarrollo](docs/DEVELOPMENT.md), en inglés, para configurar el SDK, ejecutar pruebas unitarias y compilar versiones firmadas.

## Comentarios

¿Encontraste un problema o tienes una idea? [Abre un issue](https://github.com/IgorNadein/12609/issues). Incluye las versiones de la aplicación y de Android y los pasos para reproducirlo. Usa datos ficticios en los ejemplos.
