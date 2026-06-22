package com.offlinebeautycrm

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.ContentProviderOperation
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.provider.DocumentsContract
import android.provider.Settings
import android.telephony.SmsManager
import android.telecom.TelecomManager
import android.telecom.VideoProfile
import androidx.activity.compose.BackHandler
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

private const val CHANNEL_SMS = "SMS"
private const val STATUS_QUEUED = "queued"
private const val STATUS_NEEDS_CONFIG = "needs_config"
private const val STATUS_WAITING_PERMISSION = "waiting_permission"
private const val STATUS_SENT = "sent"
private const val STATUS_FAILED = "failed"
private const val STATUS_CANCELLED = "cancelled"
private const val OUTBOX_CONFIRMATION_REQUIRED = "Ждет подтверждения"
private const val AUTOMATION_NOTIFICATION_CHANNEL = "automation_confirmations"
private const val ACTION_AUTOMATION_SEND_NOW = "com.offlinebeautycrm.action.AUTOMATION_SEND_NOW"
private const val ACTION_AUTOMATION_SKIP = "com.offlinebeautycrm.action.AUTOMATION_SKIP"
private const val EXTRA_OUTBOX_ID = "outboxId"
private const val AUTO_BACKUP_WORK = "auto_backup_json"
private const val AUTO_BACKUP_PREFS = "auto_backup"
private const val AUTO_BACKUP_MODE_OFF = "off"
private const val AUTO_BACKUP_MODE_DAILY = "daily"
private const val AUTO_BACKUP_MODE_WEEKLY = "weekly"
private const val SYNC_RESOURCE_CONTACTS = "contacts"
private const val SYNC_RESOURCE_CALENDAR = "calendar"
private const val SYNC_MODE_OFF = "off"
private const val SYNC_MODE_MANUAL = "manual"
private const val SYNC_MODE_LOCAL_TO_SYSTEM = "local_to_system"
private const val SYNC_MODE_SYSTEM_TO_LOCAL = "system_to_local"
private const val SYNC_MODE_TWO_WAY = "two_way"
private const val APP_ICON_IMAGE_SIZE = 512
private const val PAYMENT_STATUS_UNPAID = "unpaid"
private const val PAYMENT_STATUS_PARTIAL = "partial"
private const val PAYMENT_STATUS_PAID = "paid"
private const val APPOINTMENT_STATUS_PLANNED = "Запланирована"
private const val APPOINTMENT_STATUS_IN_PROGRESS = "Идет"
private const val APPOINTMENT_STATUS_PAST = "Прошла"
private const val APPOINTMENT_STATUS_CANCELLED = "Отменена"
private const val FINANCE_TYPE_INCOME = "income"
private const val FINANCE_TYPE_EXPENSE = "expense"
private const val AUTOMATION_TRIGGER_NONE = "manual"
private const val AUTOMATION_TRIGGER_BEFORE_APPOINTMENT = "before_appointment"
private const val AUTOMATION_TRIGGER_AFTER_APPOINTMENT = "after_appointment"
private const val AUTOMATION_TRIGGER_APPOINTMENT_CREATED = "appointment_created"
private const val AUTOMATION_TRIGGER_APPOINTMENT_RESCHEDULED = "appointment_rescheduled"
private const val AUTOMATION_TRIGGER_APPOINTMENT_CANCELLED = "appointment_cancelled"
private const val AUTOMATION_TRIGGER_APPOINTMENT_REACTIVATED = "appointment_reactivated"
private const val AUTOMATION_TRIGGER_PAYMENT_RECEIVED = "payment_received"
private const val AUTOMATION_TRIGGER_DEBT_AFTER_APPOINTMENT = "debt_after_appointment"
private const val AUTOMATION_TRIGGER_RETENTION_BY_SERVICE = "retention_by_service"
private const val AUTOMATION_CONDITION_DEBT_REMAINING = "debt_remaining"
private const val AUTOMATION_RETENTION_WORK = "automation_retention_scan"
private const val GOOGLE_MEET_PACKAGE = "com.google.android.apps.tachyon"
private const val GOOGLE_MEET_PHONE_VIDEO_MIME = "vnd.android.cursor.item/com.google.android.apps.tachyon.phone.meet"
private const val GOOGLE_MEET_EMAIL_VIDEO_MIME = "vnd.android.cursor.item/com.google.android.apps.tachyon.email.meet"
private const val GITHUB_LATEST_RELEASE_URL = "https://api.github.com/repos/IgorNadein/12609/releases/latest"

private val triggerOptions = listOf("За 24 часа до записи", "За 2 часа до записи", "После визита", "Ручная задача")
private val automationTriggerTypes = listOf(
    AUTOMATION_TRIGGER_BEFORE_APPOINTMENT,
    AUTOMATION_TRIGGER_AFTER_APPOINTMENT,
    AUTOMATION_TRIGGER_APPOINTMENT_CREATED,
    AUTOMATION_TRIGGER_APPOINTMENT_RESCHEDULED,
    AUTOMATION_TRIGGER_APPOINTMENT_CANCELLED,
    AUTOMATION_TRIGGER_APPOINTMENT_REACTIVATED,
    AUTOMATION_TRIGGER_PAYMENT_RECEIVED,
    AUTOMATION_TRIGGER_DEBT_AFTER_APPOINTMENT,
    AUTOMATION_TRIGGER_RETENTION_BY_SERVICE,
    AUTOMATION_TRIGGER_NONE
)
private val automationTemplateFields = listOf(
    "{client.name}",
    "{client.phone}",
    "{client.email}",
    "{appointment.service}",
    "{appointment.date}",
    "{appointment.time}",
    "{appointment.end_time}",
    "{appointment.duration}",
    "{appointment.price}",
    "{appointment.paid}",
    "{appointment.debt}",
    "{appointment.status}",
    "{services}",
    "{last_appointment.date}",
    "{last_appointment.service}"
)
private val channelOptions = listOf(CHANNEL_SMS)
private val autoBackupModeOptions = listOf(AUTO_BACKUP_MODE_OFF, AUTO_BACKUP_MODE_DAILY, AUTO_BACKUP_MODE_WEEKLY)
private val ruLocale = Locale("ru")
private val appointmentStorageFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.US)
private val dayOfWeekFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE", ruLocale)
private val monthTitleFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("LLLL yyyy", ruLocale)
private val dayMonthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM", ruLocale)
private val appointmentDurationOptions = listOf(15, 30, 45, 60, 75, 90, 120, 150, 180, 240, 300, 360)
private val paymentMethodOptions = listOf("Наличные", "Карта", "Перевод", "Другое")
private val financeTypeOptions = listOf(FINANCE_TYPE_INCOME, FINANCE_TYPE_EXPENSE)
private val financeFilterOptions = listOf("Все", "Доходы", "Расходы", "Долги")
private val syncModeOptions = listOf(
    SYNC_MODE_OFF,
    SYNC_MODE_MANUAL,
    SYNC_MODE_LOCAL_TO_SYSTEM,
    SYNC_MODE_SYSTEM_TO_LOCAL,
    SYNC_MODE_TWO_WAY
)

private data class AppVersionInfo(
    val versionName: String,
    val versionCode: Long
)

private data class GitHubReleaseInfo(
    val tagName: String,
    val versionName: String,
    val publishedAt: String,
    val body: String,
    val htmlUrl: String,
    val apkName: String,
    val apkDownloadUrl: String
)

private sealed class UpdateCheckState {
    data object Idle : UpdateCheckState()
    data object Checking : UpdateCheckState()
    data class Current(val currentVersion: AppVersionInfo, val release: GitHubReleaseInfo) : UpdateCheckState()
    data class Available(val currentVersion: AppVersionInfo, val release: GitHubReleaseInfo) : UpdateCheckState()
    data class Error(val currentVersion: AppVersionInfo, val message: String) : UpdateCheckState()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OfflineBeautyApp()
        }
    }
}

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val givenName: String = "",
    val middleName: String = "",
    val familyName: String = "",
    val nickname: String = "",
    val phone: String = "",
    val phoneKey: String = "",
    val phoneHome: String = "",
    val phoneWork: String = "",
    val phoneOther: String = "",
    val email: String = "",
    val emailWork: String = "",
    val address: String = "",
    val addressWork: String = "",
    val company: String = "",
    val jobTitle: String = "",
    val website: String = "",
    val birthday: String = "",
    val contactId: Long = 0,
    val contactLookupKey: String = "",
    val contactSyncedAt: String = "",
    val contactPhotoUri: String = "",
    val social: String = "",
    val notes: String = "",
    val createdAt: String = now(),
    val updatedAt: String = now()
)

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientId: Long,
    val service: String,
    val startAt: String,
    val durationMinutes: Int = 60,
    val price: String = "",
    val priceCents: Long = 0,
    val paidAmountCents: Long = 0,
    val paymentStatus: String = PAYMENT_STATUS_UNPAID,
    val paymentMethod: String = "",
    val paidAt: String = "",
    val status: String = APPOINTMENT_STATUS_PLANNED,
    val notes: String = "",
    val calendarEventId: Long = 0,
    val calendarSyncedAt: String = "",
    val createdAt: String = now(),
    val updatedAt: String = now()
)

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val priceCents: Long = 0,
    val durationMinutes: Int = 60,
    val isActive: Boolean = true,
    val createdAt: String = now(),
    val updatedAt: String = now()
)

@Entity(tableName = "appointment_services")
data class AppointmentServiceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val appointmentId: Long,
    val serviceId: Long = 0,
    val serviceName: String,
    val priceCents: Long = 0,
    val durationMinutes: Int = 60,
    val sortOrder: Int = 0,
    val createdAt: String = now()
)

@Entity(tableName = "automation_rules")
data class AutomationRuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val channel: String,
    val triggerName: String,
    val message: String,
    val enabled: Boolean = true,
    val createdAt: String = now(),
    val triggerType: String = AUTOMATION_TRIGGER_NONE,
    val offsetMinutes: Int = 0,
    val retentionDays: Int = 0,
    val serviceFilterIds: String = "",
    val condition: String = "",
    val askBeforeRun: Boolean = false,
    val updatedAt: String = now()
)

@Entity(tableName = "outbox")
data class OutboxItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ruleId: Long? = null,
    val clientId: Long? = null,
    val appointmentId: Long? = null,
    val channel: String,
    val payload: String,
    val status: String = STATUS_QUEUED,
    val scheduledAt: String = "",
    val error: String = "",
    val createdAt: String = now(),
    val sentAt: String = "",
    val dedupeKey: String = ""
)

@Entity(tableName = "connector_settings")
data class ConnectorSettingsEntity(
    @PrimaryKey val channel: String,
    val telegramBotToken: String = "",
    val telegramChatId: String = "",
    val enabled: Boolean = false,
    val updatedAt: String = now()
)

@Entity(tableName = "sync_settings")
data class SyncSettingsEntity(
    @PrimaryKey val resource: String,
    val mode: String = SYNC_MODE_TWO_WAY,
    val updatedAt: String = now(),
    val lastRunAt: String = "",
    val selectedSystemId: String = ""
)

@Entity(tableName = "finance_transactions")
data class FinanceTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val amountCents: Long,
    val title: String,
    val category: String = "",
    val date: String,
    val paymentMethod: String = "",
    val notes: String = "",
    val clientId: Long? = null,
    val appointmentId: Long? = null,
    val createdAt: String = now(),
    val updatedAt: String = now()
)

data class AppointmentRow(
    val id: Long,
    val clientId: Long,
    val service: String,
    val clientName: String,
    val startAt: String,
    val durationMinutes: Int,
    val price: String,
    val priceCents: Long,
    val paidAmountCents: Long,
    val paymentStatus: String,
    val paymentMethod: String,
    val paidAt: String,
    val status: String,
    val notes: String,
    val calendarEventId: Long,
    val calendarSyncedAt: String,
    val updatedAt: String
)

data class OutboxRow(
    val id: Long,
    val ruleName: String,
    val clientName: String,
    val clientPhone: String,
    val channel: String,
    val scheduledAt: String,
    val status: String,
    val payload: String,
    val error: String,
    val createdAt: String,
    val sentAt: String
)

data class OutboxSendData(
    @Embedded val outbox: OutboxItemEntity,
    @ColumnInfo(name = "clientName") val clientName: String?,
    @ColumnInfo(name = "clientPhone") val clientPhone: String?
)

data class Counts(
    val clients: Int,
    val appointments: Int,
    val rules: Int,
    val outbox: Int
)

data class AutoBackupState(
    val mode: String = AUTO_BACKUP_MODE_OFF,
    val folderUri: String = "",
    val lastRunAt: String = ""
)

data class AppIconState(
    val path: String = "",
    val updatedAt: Long = 0L
) {
    val isSet: Boolean
        get() = path.isNotBlank()
}

data class ClientDraft(
    val name: String,
    val givenName: String = "",
    val middleName: String = "",
    val familyName: String = "",
    val nickname: String = "",
    val phone: String = "",
    val phoneHome: String = "",
    val phoneWork: String = "",
    val phoneOther: String = "",
    val email: String = "",
    val emailWork: String = "",
    val address: String = "",
    val addressWork: String = "",
    val company: String = "",
    val jobTitle: String = "",
    val website: String = "",
    val birthday: String = "",
    val social: String = "",
    val notes: String = "",
    val photoUri: String = ""
) {
    fun displayName(): String {
        val explicit = name.trim()
        if (explicit.isNotBlank()) return explicit
        return listOf(givenName, middleName, familyName)
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .joinToString(" ")
    }
}

data class ContactCandidate(
    val name: String,
    val givenName: String,
    val middleName: String,
    val familyName: String,
    val nickname: String,
    val phone: String,
    val phoneKey: String,
    val phoneHome: String,
    val phoneWork: String,
    val phoneOther: String,
    val email: String,
    val emailWork: String,
    val address: String,
    val addressWork: String,
    val company: String,
    val jobTitle: String,
    val website: String,
    val birthday: String,
    val social: String,
    val notes: String,
    val contactId: Long,
    val lookupKey: String,
    val photoUri: String
) {
    val key: String = "$contactId:$phoneKey:$name"
}

data class ContactRef(
    val id: Long,
    val lookupKey: String,
    val name: String,
    val givenName: String,
    val middleName: String,
    val familyName: String,
    val nickname: String,
    val phone: String,
    val phoneHome: String,
    val phoneWork: String,
    val phoneOther: String,
    val email: String,
    val emailWork: String,
    val address: String,
    val addressWork: String,
    val company: String,
    val jobTitle: String,
    val website: String,
    val birthday: String,
    val social: String,
    val notes: String,
    val photoUri: String,
    val updatedAtMillis: Long = 0
)

data class ContactSyncResult(
    val client: ClientEntity,
    val action: String
)

data class CalendarEventRef(
    val id: Long,
    val title: String,
    val startAt: String,
    val durationMinutes: Int = 60
)

data class CalendarSyncResult(
    val appointment: AppointmentEntity,
    val action: String
)

data class AppointmentServiceDraft(
    val localId: Int,
    val serviceId: Long = 0,
    val serviceName: String = "",
    val price: String = "",
    val durationMinutes: Int = 60
) {
    val priceCents: Long get() = parseMoneyToCents(price)
    val isSelected: Boolean get() = serviceName.isNotBlank()
}

data class SystemCalendar(
    val id: Long,
    val displayName: String,
    val accountName: String,
    val accountType: String,
    val ownerAccount: String,
    val isPrimary: Boolean,
    val isLocal: Boolean
)

private fun ClientEntity.withContact(contact: ContactRef, pullContactData: Boolean): ClientEntity {
    fun merged(local: String, remote: String): String =
        if (pullContactData && remote.isNotBlank()) remote else local
    val mergedName = merged(name, contact.name)
    val mergedPhone = merged(phone, contact.phone)
    val syncedAt = now()
    return copy(
        name = mergedName,
        givenName = merged(givenName, contact.givenName),
        middleName = merged(middleName, contact.middleName),
        familyName = merged(familyName, contact.familyName),
        nickname = merged(nickname, contact.nickname),
        phone = mergedPhone,
        phoneKey = normalizePhone(mergedPhone),
        phoneHome = merged(phoneHome, contact.phoneHome),
        phoneWork = merged(phoneWork, contact.phoneWork),
        phoneOther = merged(phoneOther, contact.phoneOther),
        email = merged(email, contact.email),
        emailWork = merged(emailWork, contact.emailWork),
        address = merged(address, contact.address),
        addressWork = merged(addressWork, contact.addressWork),
        company = merged(company, contact.company),
        jobTitle = merged(jobTitle, contact.jobTitle),
        website = merged(website, contact.website),
        birthday = merged(birthday, contact.birthday),
        contactId = contact.id,
        contactLookupKey = contact.lookupKey,
        contactSyncedAt = syncedAt,
        contactPhotoUri = contact.photoUri,
        social = merged(social, contact.social),
        notes = merged(notes, contact.notes),
        updatedAt = if (pullContactData) syncedAt else updatedAt
    )
}

private fun ContactRef.toCandidate(phoneKey: String = normalizePhone(phone)): ContactCandidate =
    ContactCandidate(
        name = name,
        givenName = givenName,
        middleName = middleName,
        familyName = familyName,
        nickname = nickname,
        phone = phone,
        phoneKey = phoneKey,
        phoneHome = phoneHome,
        phoneWork = phoneWork,
        phoneOther = phoneOther,
        email = email,
        emailWork = emailWork,
        address = address,
        addressWork = addressWork,
        company = company,
        jobTitle = jobTitle,
        website = website,
        birthday = birthday,
        social = social,
        notes = notes,
        contactId = id,
        lookupKey = lookupKey,
        photoUri = photoUri
    )

private fun ContactCandidate.toClientEntity(): ClientEntity =
    ClientEntity(
        name = name,
        givenName = givenName,
        middleName = middleName,
        familyName = familyName,
        nickname = nickname,
        phone = phone,
        phoneKey = phoneKey,
        phoneHome = phoneHome,
        phoneWork = phoneWork,
        phoneOther = phoneOther,
        email = email,
        emailWork = emailWork,
        address = address,
        addressWork = addressWork,
        company = company,
        jobTitle = jobTitle,
        website = website,
        birthday = birthday,
        contactId = contactId,
        contactLookupKey = lookupKey,
        contactSyncedAt = now(),
        contactPhotoUri = photoUri,
        social = social,
        notes = notes
    )

private fun ClientDraft.toEntity(): ClientEntity {
    val displayName = displayName()
    return ClientEntity(
        name = displayName,
        givenName = givenName.trim(),
        middleName = middleName.trim(),
        familyName = familyName.trim(),
        nickname = nickname.trim(),
        phone = phone.trim(),
        phoneKey = normalizePhone(phone),
        phoneHome = phoneHome.trim(),
        phoneWork = phoneWork.trim(),
        phoneOther = phoneOther.trim(),
        email = email.trim(),
        emailWork = emailWork.trim(),
        address = address.trim(),
        addressWork = addressWork.trim(),
        company = company.trim(),
        jobTitle = jobTitle.trim(),
        website = website.trim(),
        birthday = birthday.trim(),
        contactPhotoUri = photoUri.trim(),
        social = social.trim(),
        notes = notes.trim()
    )
}

object ContactsSync {
    private fun canRead(context: Context): Boolean =
        context.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

    private fun canWrite(context: Context): Boolean =
        context.checkSelfPermission(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED

    fun readCandidates(context: Context): List<ContactCandidate> {
        if (!canRead(context)) return emptyList()
        val result = mutableListOf<ContactCandidate>()
        val cursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
            ),
            null,
            null,
            "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} COLLATE LOCALIZED ASC"
        ) ?: return emptyList()
        cursor.use {
            val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
            val lookupIndex = it.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)
            val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val photoIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
            while (it.moveToNext()) {
                val name = it.getString(nameIndex)?.trim().orEmpty()
                val contactId = it.getLong(idIndex)
                val contact = contactFromId(
                    context = context,
                    contactId = contactId,
                    knownLookup = it.getString(lookupIndex).orEmpty(),
                    knownName = name
                ) ?: ContactRef(
                        id = it.getLong(idIndex),
                        lookupKey = it.getString(lookupIndex).orEmpty(),
                        name = name,
                        givenName = "",
                        middleName = "",
                        familyName = "",
                        nickname = "",
                        phone = "",
                        phoneHome = "",
                        phoneWork = "",
                        phoneOther = "",
                        email = "",
                        emailWork = "",
                        address = "",
                        addressWork = "",
                        company = "",
                        jobTitle = "",
                        website = "",
                        birthday = "",
                        social = "",
                        notes = "",
                        photoUri = it.getString(photoIndex).orEmpty()
                )
                if (contact.name.isBlank() && contact.phone.isBlank() && contact.email.isBlank()) continue
                result.add(contact.toCandidate())
                if (result.size >= 500) break
            }
        }
        return result
    }

    fun linkOrCreate(context: Context, client: ClientEntity): ContactRef? {
        if (canRead(context)) {
            findByPhone(context, client.phone)?.let { return it }
        }
        if (!canWrite(context) || client.name.isBlank()) {
            return null
        }
        return createContact(context, client)
    }

    fun pushClient(context: Context, client: ClientEntity): ContactSyncResult {
        val canRead = canRead(context)
        val canWrite = canWrite(context)
        if (canRead) {
            val linked = findByLookup(context, client.contactId, client.contactLookupKey)
            if (linked != null) {
                if (canWrite) fillMissingContactData(context, linked, client)
                val refreshed = findByLookup(context, linked.id, linked.lookupKey) ?: linked
                return ContactSyncResult(client.withContact(refreshed, pullContactData = false), "linked")
            }

            val byPhone = findByPhone(context, client.phone)
            if (byPhone != null) {
                if (canWrite) fillMissingContactData(context, byPhone, client)
                val refreshed = findByLookup(context, byPhone.id, byPhone.lookupKey) ?: byPhone
                return ContactSyncResult(client.withContact(refreshed, pullContactData = false), "linked")
            }
        }

        if (!canWrite || client.name.isBlank()) {
            return ContactSyncResult(client, "skipped")
        }

        val created = createContact(context, client)
        return if (created != null) {
            ContactSyncResult(client.withContact(created, pullContactData = false), "created")
        } else {
            ContactSyncResult(client, "skipped")
        }
    }

    fun pullClient(context: Context, client: ClientEntity): ContactSyncResult {
        if (!canRead(context)) return ContactSyncResult(client, "skipped")
        val linked = findByLookup(context, client.contactId, client.contactLookupKey)
        if (linked != null) {
            return ContactSyncResult(client.withContact(linked, pullContactData = true), "pulled")
        }
        val byPhone = findByPhone(context, client.phone)
        return if (byPhone != null) {
            ContactSyncResult(client.withContact(byPhone, pullContactData = true), "linked")
        } else {
            ContactSyncResult(client, "skipped")
        }
    }

    fun syncClient(context: Context, client: ClientEntity): ContactSyncResult {
        if (!canRead(context)) return pushClient(context, client)
        val linked = findByLookup(context, client.contactId, client.contactLookupKey)
            ?: findByPhone(context, client.phone)
        if (linked == null) return pushClient(context, client)

        val lastSync = timestampMillis(client.contactSyncedAt)
        val localChanged = timestampMillis(client.updatedAt) > lastSync
        val remoteChanged = linked.updatedAtMillis > lastSync
        return when {
            remoteChanged && !localChanged -> pullClient(context, client)
            localChanged && !remoteChanged -> pushClient(context, client)
            remoteChanged && localChanged && linked.updatedAtMillis > timestampMillis(client.updatedAt) -> pullClient(context, client)
            else -> pushClient(context, client)
        }
    }

    private fun fillMissingContactData(context: Context, contact: ContactRef, client: ClientEntity) {
        val rawContactId = rawContactIdForContact(context, contact.id) ?: return
        val operations = ArrayList<ContentProviderOperation>()
        addMissingStructuredName(operations, rawContactId, contact, client)
        if (contact.phone.isBlank()) addExistingPhoneInsert(operations, rawContactId, client.phone, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
        if (contact.phoneHome.isBlank()) addExistingPhoneInsert(operations, rawContactId, client.phoneHome, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
        if (contact.phoneWork.isBlank()) addExistingPhoneInsert(operations, rawContactId, client.phoneWork, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
        if (contact.phoneOther.isBlank()) addExistingPhoneInsert(operations, rawContactId, client.phoneOther, ContactsContract.CommonDataKinds.Phone.TYPE_OTHER)
        if (contact.email.isBlank()) addExistingEmailInsert(operations, rawContactId, client.email, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
        if (contact.emailWork.isBlank()) addExistingEmailInsert(operations, rawContactId, client.emailWork, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
        if (contact.address.isBlank()) addExistingPostalInsert(operations, rawContactId, client.address, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME)
        if (contact.addressWork.isBlank()) addExistingPostalInsert(operations, rawContactId, client.addressWork, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK)
        if (contact.company.isBlank() && contact.jobTitle.isBlank()) addExistingOrganizationInsert(operations, rawContactId, client.company, client.jobTitle)
        if (contact.website.isBlank()) addExistingWebsiteInsert(operations, rawContactId, client.website)
        if (contact.birthday.isBlank()) addExistingBirthdayInsert(operations, rawContactId, client.birthday)
        if (contact.nickname.isBlank()) addExistingNicknameInsert(operations, rawContactId, client.nickname)
        if (contact.social.isBlank()) addExistingImInsert(operations, rawContactId, client.social)
        if (contact.notes.isBlank()) addExistingNoteInsert(operations, rawContactId, client.notes)
        if (operations.isNotEmpty()) {
            runCatching { context.contentResolver.applyBatch(ContactsContract.AUTHORITY, operations) }
        }
    }

    fun refreshClientLink(context: Context, client: ClientEntity): ClientEntity? {
        if (!canRead(context)) return null
        val linked = findByLookup(context, client.contactId, client.contactLookupKey)
            ?: findByPhone(context, client.phone)
            ?: return null
        return client.withContact(linked, pullContactData = false)
    }

    private fun findByPhone(context: Context, phone: String): ContactRef? {
        if (phone.isBlank()) return null
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone))
        val cursor = context.contentResolver.query(
            uri,
            arrayOf(
                ContactsContract.PhoneLookup._ID,
                ContactsContract.PhoneLookup.LOOKUP_KEY,
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI
            ),
            null,
            null,
            null
        ) ?: return null
        cursor.use {
            if (!it.moveToFirst()) return null
            val id = it.getLong(0)
            val lookup = it.getString(1).orEmpty()
            val name = it.getString(2).orEmpty()
            return contactFromId(context, id, lookup, name)
                ?: ContactRef(
                    id = id,
                    lookupKey = lookup,
                    name = name,
                    givenName = "",
                    middleName = "",
                    familyName = "",
                    nickname = "",
                    phone = it.getString(3).orEmpty(),
                    phoneHome = "",
                    phoneWork = "",
                    phoneOther = "",
                    email = "",
                    emailWork = "",
                    address = "",
                    addressWork = "",
                    company = "",
                    jobTitle = "",
                    website = "",
                    birthday = "",
                    social = "",
                    notes = "",
                    photoUri = it.getString(4).orEmpty()
                )
        }
    }

    private fun findByLookup(context: Context, contactId: Long, lookupKey: String): ContactRef? {
        if (contactId == 0L || lookupKey.isBlank()) return null
        val lookupUri = ContactsContract.Contacts.getLookupUri(contactId, lookupKey) ?: return null
        val cursor = context.contentResolver.query(
            lookupUri,
            arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            ),
            null,
            null,
            null
        ) ?: return null
        cursor.use {
            if (!it.moveToFirst()) return null
            val id = it.getLong(0)
            val lookup = it.getString(1).orEmpty()
            val name = it.getString(2).orEmpty()
            return contactFromId(context, id, lookup, name)
        }
    }

    private fun createContact(context: Context, client: ClientEntity): ContactRef? {
        return try {
            val operations = ArrayList<ContentProviderOperation>()
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build()
            )
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, client.name)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, client.givenName.ifBlank { null })
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME, client.middleName.ifBlank { null })
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, client.familyName.ifBlank { null })
                    .build()
            )
            addPhoneInsert(operations, client.phone, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
            addPhoneInsert(operations, client.phoneHome, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
            addPhoneInsert(operations, client.phoneWork, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
            addPhoneInsert(operations, client.phoneOther, ContactsContract.CommonDataKinds.Phone.TYPE_OTHER)
            addEmailInsert(operations, client.email, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
            addEmailInsert(operations, client.emailWork, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
            addPostalInsert(operations, client.address, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME)
            addPostalInsert(operations, client.addressWork, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK)
            addOrganizationInsert(operations, client.company, client.jobTitle)
            addWebsiteInsert(operations, client.website)
            addBirthdayInsert(operations, client.birthday)
            addNicknameInsert(operations, client.nickname)
            addNoteInsert(operations, client.notes)
            addImInsert(operations, client.social)
            val results = context.contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
            val rawContactId = results.firstOrNull()?.uri?.lastPathSegment?.toLongOrNull()
                ?: return findByPhone(context, client.phone)
            contactFromRawContactId(context, rawContactId) ?: findByPhone(context, client.phone)
        } catch (e: Exception) {
            null
        }
    }

    private fun addPhoneInsert(operations: ArrayList<ContentProviderOperation>, value: String, type: Int) {
        if (value.isBlank()) return
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, value)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, type)
                .build()
        )
    }

    private fun addEmailInsert(operations: ArrayList<ContentProviderOperation>, value: String, type: Int) {
        if (value.isBlank()) return
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, value)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, type)
                .build()
        )
    }

    private fun addPostalInsert(operations: ArrayList<ContentProviderOperation>, value: String, type: Int) {
        if (value.isBlank()) return
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, value)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, type)
                .build()
        )
    }

    private fun addOrganizationInsert(operations: ArrayList<ContentProviderOperation>, company: String, title: String) {
        if (company.isBlank() && title.isBlank()) return
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company.ifBlank { null })
                .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, title.ifBlank { null })
                .build()
        )
    }

    private fun addWebsiteInsert(operations: ArrayList<ContentProviderOperation>, value: String) {
        if (value.isBlank()) return
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Website.URL, value)
                .build()
        )
    }

    private fun addBirthdayInsert(operations: ArrayList<ContentProviderOperation>, value: String) {
        if (value.isBlank()) return
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, value)
                .withValue(ContactsContract.CommonDataKinds.Event.TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY)
                .build()
        )
    }

    private fun addNicknameInsert(operations: ArrayList<ContentProviderOperation>, value: String) {
        if (value.isBlank()) return
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Nickname.NAME, value)
                .build()
        )
    }

    private fun addNoteInsert(operations: ArrayList<ContentProviderOperation>, value: String) {
        if (value.isBlank()) return
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Note.NOTE, value)
                .build()
        )
    }

    private fun addImInsert(operations: ArrayList<ContentProviderOperation>, value: String) {
        if (value.isBlank()) return
        operations.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Im.DATA, value)
                .build()
        )
    }

    private fun addMissingStructuredName(
        operations: ArrayList<ContentProviderOperation>,
        rawContactId: Long,
        contact: ContactRef,
        client: ClientEntity
    ) {
        if (
            (contact.givenName.isNotBlank() || contact.middleName.isNotBlank() || contact.familyName.isNotBlank()) ||
            (client.givenName.isBlank() && client.middleName.isBlank() && client.familyName.isBlank())
        ) return
        addExistingDataInsert(
            operations,
            rawContactId,
            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME to client.name,
            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME to client.givenName.ifBlank { null },
            ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME to client.middleName.ifBlank { null },
            ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME to client.familyName.ifBlank { null }
        )
    }

    private fun addExistingPhoneInsert(operations: ArrayList<ContentProviderOperation>, rawContactId: Long, value: String, type: Int) {
        if (value.isBlank()) return
        addExistingDataInsert(
            operations,
            rawContactId,
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Phone.NUMBER to value,
            ContactsContract.CommonDataKinds.Phone.TYPE to type
        )
    }

    private fun addExistingEmailInsert(operations: ArrayList<ContentProviderOperation>, rawContactId: Long, value: String, type: Int) {
        if (value.isBlank()) return
        addExistingDataInsert(
            operations,
            rawContactId,
            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Email.ADDRESS to value,
            ContactsContract.CommonDataKinds.Email.TYPE to type
        )
    }

    private fun addExistingPostalInsert(operations: ArrayList<ContentProviderOperation>, rawContactId: Long, value: String, type: Int) {
        if (value.isBlank()) return
        addExistingDataInsert(
            operations,
            rawContactId,
            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS to value,
            ContactsContract.CommonDataKinds.StructuredPostal.TYPE to type
        )
    }

    private fun addExistingOrganizationInsert(operations: ArrayList<ContentProviderOperation>, rawContactId: Long, company: String, title: String) {
        if (company.isBlank() && title.isBlank()) return
        addExistingDataInsert(
            operations,
            rawContactId,
            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Organization.COMPANY to company.ifBlank { null },
            ContactsContract.CommonDataKinds.Organization.TITLE to title.ifBlank { null }
        )
    }

    private fun addExistingWebsiteInsert(operations: ArrayList<ContentProviderOperation>, rawContactId: Long, value: String) {
        if (value.isBlank()) return
        addExistingDataInsert(
            operations,
            rawContactId,
            ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Website.URL to value
        )
    }

    private fun addExistingBirthdayInsert(operations: ArrayList<ContentProviderOperation>, rawContactId: Long, value: String) {
        if (value.isBlank()) return
        addExistingDataInsert(
            operations,
            rawContactId,
            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Event.START_DATE to value,
            ContactsContract.CommonDataKinds.Event.TYPE to ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY
        )
    }

    private fun addExistingNicknameInsert(operations: ArrayList<ContentProviderOperation>, rawContactId: Long, value: String) {
        if (value.isBlank()) return
        addExistingDataInsert(
            operations,
            rawContactId,
            ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Nickname.NAME to value
        )
    }

    private fun addExistingImInsert(operations: ArrayList<ContentProviderOperation>, rawContactId: Long, value: String) {
        if (value.isBlank()) return
        addExistingDataInsert(
            operations,
            rawContactId,
            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Im.DATA to value
        )
    }

    private fun addExistingNoteInsert(operations: ArrayList<ContentProviderOperation>, rawContactId: Long, value: String) {
        if (value.isBlank()) return
        addExistingDataInsert(
            operations,
            rawContactId,
            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Note.NOTE to value
        )
    }

    private fun addExistingDataInsert(
        operations: ArrayList<ContentProviderOperation>,
        rawContactId: Long,
        mimeType: String,
        vararg values: Pair<String, Any?>
    ) {
        val builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            .withValue(ContactsContract.Data.MIMETYPE, mimeType)
        values.forEach { (key, value) -> builder.withValue(key, value) }
        operations.add(builder.build())
    }

    private fun rawContactIdForContact(context: Context, contactId: Long): Long? {
        val cursor = context.contentResolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            arrayOf(ContactsContract.RawContacts._ID),
            "${ContactsContract.RawContacts.CONTACT_ID} = ? AND ${ContactsContract.RawContacts.DELETED} = 0",
            arrayOf(contactId.toString()),
            null
        ) ?: return null
        cursor.use {
            return if (it.moveToFirst()) it.getLong(0) else null
        }
    }

    private fun contactFromRawContactId(context: Context, rawContactId: Long): ContactRef? {
        val cursor = context.contentResolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            arrayOf(ContactsContract.RawContacts.CONTACT_ID),
            "${ContactsContract.RawContacts._ID} = ?",
            arrayOf(rawContactId.toString()),
            null
        ) ?: return null
        cursor.use {
            if (!it.moveToFirst()) return null
            val contactId = it.getLong(0)
            return contactFromId(context, contactId)
        }
    }

    private fun contactFromId(context: Context, contactId: Long, knownLookup: String = "", knownName: String = ""): ContactRef? {
        var lookup = knownLookup
        var name = knownName
        var photoUri = ""
        if (lookup.isBlank() || name.isBlank()) {
            val contactCursor = context.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                arrayOf(
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                    ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
                ),
                "${ContactsContract.Contacts._ID} = ?",
                arrayOf(contactId.toString()),
                null
            ) ?: return null
            contactCursor.use {
                if (!it.moveToFirst()) return null
                lookup = it.getString(0).orEmpty()
                name = it.getString(1).orEmpty()
                photoUri = it.getString(2).orEmpty()
            }
        }
        if (photoUri.isBlank()) {
            photoUri = photoUriForContact(context, contactId)
        }
        val updatedAtMillis = contactUpdatedAtMillis(context, contactId)

        var displayName = name
        var givenName = ""
        var middleName = ""
        var familyName = ""
        var nickname = ""
        var phone = ""
        var phoneHome = ""
        var phoneWork = ""
        var phoneOther = ""
        var email = ""
        var emailWork = ""
        var address = ""
        var addressWork = ""
        var company = ""
        var jobTitle = ""
        var website = ""
        var birthday = ""
        var social = ""
        var notes = ""

        val cursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.DATA1,
                ContactsContract.Data.DATA2,
                ContactsContract.Data.DATA3,
                ContactsContract.Data.DATA4,
                ContactsContract.Data.DATA5
            ),
            "${ContactsContract.Data.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        ) ?: return ContactRef(
            id = contactId,
            lookupKey = lookup,
            name = displayName,
            givenName = "",
            middleName = "",
            familyName = "",
            nickname = "",
            phone = firstPhoneForContact(context, contactId),
            phoneHome = "",
            phoneWork = "",
            phoneOther = "",
            email = "",
            emailWork = "",
            address = "",
            addressWork = "",
            company = "",
            jobTitle = "",
            website = "",
            birthday = "",
            social = "",
            notes = "",
            photoUri = photoUri,
            updatedAtMillis = updatedAtMillis
        )
        cursor.use {
            while (it.moveToNext()) {
                val mime = it.getString(0).orEmpty()
                val data1 = it.getString(1).orEmpty()
                val data2 = it.getString(2).orEmpty()
                val type = data2.toIntOrNull() ?: 0
                val data3 = it.getString(3).orEmpty()
                val data4 = it.getString(4).orEmpty()
                val data5 = it.getString(5).orEmpty()
                when (mime) {
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE -> {
                        if (displayName.isBlank()) displayName = data1
                        if (givenName.isBlank()) givenName = data2
                        if (familyName.isBlank()) familyName = data3
                        if (middleName.isBlank()) middleName = data5
                    }
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE -> {
                        if (phone.isBlank()) phone = data1
                        when (type) {
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> if (phone.isBlank() || phone == data1) phone = data1
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> if (phoneHome.isBlank()) phoneHome = data1
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> if (phoneWork.isBlank()) phoneWork = data1
                            else -> if (phoneOther.isBlank()) phoneOther = data1
                        }
                    }
                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE -> {
                        when (type) {
                            ContactsContract.CommonDataKinds.Email.TYPE_WORK -> if (emailWork.isBlank()) emailWork = data1
                            else -> if (email.isBlank()) email = data1
                        }
                    }
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE -> {
                        when (type) {
                            ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK -> if (addressWork.isBlank()) addressWork = data1
                            else -> if (address.isBlank()) address = data1
                        }
                    }
                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE -> {
                        if (company.isBlank()) company = data1
                        if (jobTitle.isBlank()) jobTitle = data4
                    }
                    ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE -> if (website.isBlank()) website = data1
                    ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE -> {
                        if (type == ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY && birthday.isBlank()) birthday = data1
                    }
                    ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE -> if (nickname.isBlank()) nickname = data1
                    ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE -> if (notes.isBlank()) notes = data1
                    ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE -> if (social.isBlank()) social = data1
                }
            }
        }
        return ContactRef(
            id = contactId,
            lookupKey = lookup,
            name = displayName,
            givenName = givenName,
            middleName = middleName,
            familyName = familyName,
            nickname = nickname,
            phone = phone,
            phoneHome = phoneHome,
            phoneWork = phoneWork,
            phoneOther = phoneOther,
            email = email,
            emailWork = emailWork,
            address = address,
            addressWork = addressWork,
            company = company,
            jobTitle = jobTitle,
            website = website,
            birthday = birthday,
            social = social,
            notes = notes,
            photoUri = photoUri,
            updatedAtMillis = updatedAtMillis
        )
    }

    private fun firstPhoneForContact(context: Context, contactId: Long): String {
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        ) ?: return ""
        cursor.use {
            return if (it.moveToFirst()) it.getString(0).orEmpty() else ""
        }
    }

    private fun photoUriForContact(context: Context, contactId: Long): String {
        val cursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            arrayOf(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI),
            "${ContactsContract.Contacts._ID} = ?",
            arrayOf(contactId.toString()),
            null
        ) ?: return ""
        cursor.use {
            return if (it.moveToFirst()) it.getString(0).orEmpty() else ""
        }
    }

    private fun contactUpdatedAtMillis(context: Context, contactId: Long): Long {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) return 0L
        val cursor = runCatching {
            context.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                arrayOf(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP),
                "${ContactsContract.Contacts._ID} = ?",
                arrayOf(contactId.toString()),
                null
            )
        }.getOrNull() ?: return 0L
        cursor.use {
            return if (it.moveToFirst()) it.getLong(0) else 0L
        }
    }
}

private fun AppointmentEntity.withCalendarEvent(
    event: CalendarEventRef,
    client: ClientEntity,
    pullCalendarData: Boolean
): AppointmentEntity {
    val titleService = event.title
        .removeSuffix(" - ${client.name}")
        .trim()
        .ifBlank { event.title.trim() }
    val syncedAt = now()
    return copy(
        service = if (pullCalendarData && titleService.isNotBlank()) titleService else service,
        startAt = if (pullCalendarData && event.startAt.isNotBlank()) event.startAt else startAt,
        durationMinutes = if (pullCalendarData) event.durationMinutes.coerceAtLeast(15) else durationMinutes,
        calendarEventId = event.id,
        calendarSyncedAt = syncedAt,
        updatedAt = if (pullCalendarData) syncedAt else updatedAt
    )
}

object CalendarSync {
    private const val DEFAULT_DURATION_MILLIS = 60L * 60L * 1000L

    private fun canRead(context: Context): Boolean =
        context.checkSelfPermission(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED

    private fun canWrite(context: Context): Boolean =
        context.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED

    private fun eventStatus(appointment: AppointmentEntity): Int =
        if (appointment.status == APPOINTMENT_STATUS_CANCELLED) {
            CalendarContract.Events.STATUS_CANCELED
        } else {
            CalendarContract.Events.STATUS_CONFIRMED
        }

    fun createForAppointment(
        context: Context,
        appointment: AppointmentEntity,
        client: ClientEntity,
        preferredCalendarId: Long? = null
    ): CalendarEventRef? {
        if (!canRead(context) || !canWrite(context)) return null
        return createEvent(context, appointment, client, preferredCalendarId)
    }

    fun pushAppointment(
        context: Context,
        appointment: AppointmentEntity,
        client: ClientEntity,
        preferredCalendarId: Long? = null
    ): CalendarSyncResult {
        if (!canRead(context) || !canWrite(context)) {
            return CalendarSyncResult(appointment, "skipped")
        }
        if (appointment.calendarEventId > 0 && findEvent(context, appointment.calendarEventId) != null) {
            val updated = updateEvent(context, appointment, client)
            if (updated) {
                val refreshed = findEvent(context, appointment.calendarEventId)
                return CalendarSyncResult(
                    appointment.withCalendarEvent(refreshed ?: CalendarEventRef(appointment.calendarEventId, calendarTitle(appointment, client), appointment.startAt, appointment.durationMinutes), client, pullCalendarData = false),
                    "updated"
                )
            }
        }
        val created = createEvent(context, appointment, client, preferredCalendarId)
        return if (created != null) {
            CalendarSyncResult(
                appointment.withCalendarEvent(created, client, pullCalendarData = false),
                "created"
            )
        } else {
            CalendarSyncResult(appointment, "skipped")
        }
    }

    fun pullAppointment(
        context: Context,
        appointment: AppointmentEntity,
        client: ClientEntity
    ): CalendarSyncResult {
        if (!canRead(context) || appointment.calendarEventId <= 0) {
            return CalendarSyncResult(appointment, "skipped")
        }
        val linked = findEvent(context, appointment.calendarEventId)
            ?: return CalendarSyncResult(appointment, "skipped")
        return CalendarSyncResult(
            appointment.withCalendarEvent(linked, client, pullCalendarData = true),
            "pulled"
        )
    }

    fun deleteAppointmentEvent(context: Context, appointment: AppointmentEntity): Boolean {
        if (!canWrite(context) || appointment.calendarEventId <= 0) return false
        val uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, appointment.calendarEventId)
        return runCatching {
            context.contentResolver.delete(uri, null, null) > 0
        }.getOrDefault(false)
    }

    fun syncAppointment(
        context: Context,
        appointment: AppointmentEntity,
        client: ClientEntity,
        preferredCalendarId: Long? = null
    ): CalendarSyncResult {
        return if (appointment.calendarEventId > 0 && timestampMillis(appointment.updatedAt) <= timestampMillis(appointment.calendarSyncedAt)) {
            pullAppointment(context, appointment, client)
        } else {
            pushAppointment(context, appointment, client, preferredCalendarId)
        }
    }

    private fun createEvent(
        context: Context,
        appointment: AppointmentEntity,
        client: ClientEntity,
        preferredCalendarId: Long?
    ): CalendarEventRef? {
        val calendarId = writableCalendarId(context, preferredCalendarId) ?: return null
        val startMillis = parseAppointmentMillis(appointment.startAt) ?: return null
        val title = calendarTitle(appointment, client)
        val durationMillis = appointment.durationMinutes.coerceAtLeast(15).toLong() * 60L * 1000L
        val values = ContentValues().apply {
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, calendarDescription(appointment, client))
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, startMillis + durationMillis)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
            put(CalendarContract.Events.STATUS, eventStatus(appointment))
        }
        return try {
            val uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
            val id = uri?.lastPathSegment?.toLongOrNull() ?: return null
            CalendarEventRef(id = id, title = title, startAt = appointment.startAt, durationMinutes = appointment.durationMinutes)
        } catch (e: Exception) {
            null
        }
    }

    private fun updateEvent(context: Context, appointment: AppointmentEntity, client: ClientEntity): Boolean {
        val startMillis = parseAppointmentMillis(appointment.startAt) ?: return false
        val durationMillis = appointment.durationMinutes.coerceAtLeast(15).toLong() * 60L * 1000L
        val uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, appointment.calendarEventId)
        val values = ContentValues().apply {
            put(CalendarContract.Events.TITLE, calendarTitle(appointment, client))
            put(CalendarContract.Events.DESCRIPTION, calendarDescription(appointment, client))
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, startMillis + durationMillis)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
            put(CalendarContract.Events.STATUS, eventStatus(appointment))
        }
        return runCatching {
            context.contentResolver.update(uri, values, null, null) > 0
        }.getOrDefault(false)
    }

    private fun findEvent(context: Context, eventId: Long): CalendarEventRef? {
        val cursor = context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            arrayOf(
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND
            ),
            "${CalendarContract.Events._ID} = ? AND ${CalendarContract.Events.DELETED} = 0",
            arrayOf(eventId.toString()),
            null
        ) ?: return null
        cursor.use {
            if (!it.moveToFirst()) return null
            val startMillis = it.getLong(2)
            val endMillis = it.getLong(3).takeIf { value -> value > startMillis } ?: (startMillis + DEFAULT_DURATION_MILLIS)
            val durationMinutes = ((endMillis - startMillis) / 60000L).toInt().coerceAtLeast(15)
            return CalendarEventRef(
                id = it.getLong(0),
                title = it.getString(1).orEmpty(),
                startAt = formatAppointmentMillis(startMillis),
                durationMinutes = durationMinutes
            )
        }
    }

    fun writableCalendars(context: Context): List<SystemCalendar> {
        if (!canRead(context)) return emptyList()
        return try {
            val cursor = context.contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                arrayOf(
                    CalendarContract.Calendars._ID,
                    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                    CalendarContract.Calendars.ACCOUNT_NAME,
                    CalendarContract.Calendars.ACCOUNT_TYPE,
                    CalendarContract.Calendars.OWNER_ACCOUNT,
                    CalendarContract.Calendars.IS_PRIMARY
                ),
                "${CalendarContract.Calendars.VISIBLE} = 1 AND ${CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL} >= ?",
                arrayOf(CalendarContract.Calendars.CAL_ACCESS_CONTRIBUTOR.toString()),
                "${CalendarContract.Calendars.ACCOUNT_TYPE} COLLATE LOCALIZED ASC, ${CalendarContract.Calendars.IS_PRIMARY} DESC, ${CalendarContract.Calendars.CALENDAR_DISPLAY_NAME} COLLATE LOCALIZED ASC"
            ) ?: return emptyList()
            cursor.use {
                buildList {
                    while (it.moveToNext()) {
                        val accountType = it.getString(3).orEmpty()
                        add(
                            SystemCalendar(
                                id = it.getLong(0),
                                displayName = it.getString(1).orEmpty(),
                                accountName = it.getString(2).orEmpty(),
                                accountType = accountType,
                                ownerAccount = it.getString(4).orEmpty(),
                                isPrimary = it.getInt(5) == 1,
                                isLocal = accountType == CalendarContract.ACCOUNT_TYPE_LOCAL
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun defaultWritableCalendar(calendars: List<SystemCalendar>): SystemCalendar? =
        calendars.firstOrNull { it.isLocal }
            ?: calendars.firstOrNull { it.isPrimary }
            ?: calendars.firstOrNull()

    private fun writableCalendarId(context: Context, preferredCalendarId: Long?): Long? {
        val calendars = writableCalendars(context)
        if (preferredCalendarId != null && calendars.any { it.id == preferredCalendarId }) {
            return preferredCalendarId
        }
        return defaultWritableCalendar(calendars)?.id
    }

    private fun calendarTitle(appointment: AppointmentEntity, client: ClientEntity): String =
        listOf(appointment.service, client.name).filter { it.isNotBlank() }.joinToString(" - ")

    private fun calendarDescription(appointment: AppointmentEntity, client: ClientEntity): String =
        buildString {
            append("Клиент: ").append(client.name)
            if (client.phone.isNotBlank()) append("\nТелефон: ").append(client.phone)
            val priceLabel = appointment.price.takeIf { it.isNotBlank() } ?: formatMoney(appointment.priceCents)
            if (appointment.priceCents > 0 || appointment.price.isNotBlank()) append("\nЦена: ").append(priceLabel)
            if (appointment.status == APPOINTMENT_STATUS_CANCELLED) append("\nСтатус: ").append(appointment.status)
            if (appointment.notes.isNotBlank()) append("\n").append(appointment.notes)
        }
}

@Dao
interface AppDao {
    @Query("SELECT * FROM clients ORDER BY name COLLATE NOCASE")
    fun observeClients(): Flow<List<ClientEntity>>

    @Query("SELECT * FROM automation_rules ORDER BY id")
    fun observeRules(): Flow<List<AutomationRuleEntity>>

    @Query(
        """
        SELECT a.id, a.clientId, a.service, c.name AS clientName, a.startAt, a.durationMinutes, a.price,
               a.priceCents, a.paidAmountCents, a.paymentStatus, a.paymentMethod, a.paidAt,
               a.status, a.notes, a.calendarEventId, a.calendarSyncedAt, a.updatedAt
        FROM appointments a
        JOIN clients c ON c.id = a.clientId
        ORDER BY a.startAt
        """
    )
    fun observeAppointments(): Flow<List<AppointmentRow>>

    @Query(
        """
        SELECT o.id, COALESCE(r.name, 'Ручная задача') AS ruleName, COALESCE(c.name, '-') AS clientName,
               COALESCE(c.phone, '') AS clientPhone, o.channel, o.scheduledAt, o.status, o.payload,
               o.error, o.createdAt, o.sentAt
        FROM outbox o
        LEFT JOIN automation_rules r ON r.id = o.ruleId
        LEFT JOIN clients c ON c.id = o.clientId
        ORDER BY o.id DESC
        """
    )
    fun observeOutbox(): Flow<List<OutboxRow>>

    @Query("SELECT * FROM connector_settings ORDER BY channel")
    fun observeSettings(): Flow<List<ConnectorSettingsEntity>>

    @Query("SELECT * FROM sync_settings ORDER BY resource")
    fun observeSyncSettings(): Flow<List<SyncSettingsEntity>>

    @Query("SELECT * FROM finance_transactions ORDER BY date DESC, id DESC")
    fun observeFinanceTransactions(): Flow<List<FinanceTransactionEntity>>

    @Query("SELECT * FROM services WHERE isActive = 1 ORDER BY name COLLATE NOCASE")
    fun observeServices(): Flow<List<ServiceEntity>>

    @Query("SELECT COUNT(*) FROM clients")
    suspend fun countClients(): Int

    @Query("SELECT COUNT(*) FROM appointments")
    suspend fun countAppointments(): Int

    @Query("SELECT COUNT(*) FROM automation_rules")
    suspend fun countRules(): Int

    @Query("SELECT COUNT(*) FROM outbox")
    suspend fun countOutbox(): Int

    @Query("SELECT * FROM clients")
    suspend fun clientsOnce(): List<ClientEntity>

    @Query("SELECT * FROM appointments")
    suspend fun appointmentsOnce(): List<AppointmentEntity>

    @Query("SELECT * FROM services ORDER BY name COLLATE NOCASE")
    suspend fun servicesOnce(): List<ServiceEntity>

    @Query("SELECT * FROM appointment_services ORDER BY appointmentId, sortOrder, id")
    suspend fun appointmentServicesOnce(): List<AppointmentServiceEntity>

    @Query("SELECT * FROM automation_rules")
    suspend fun rulesOnce(): List<AutomationRuleEntity>

    @Query("SELECT * FROM outbox")
    suspend fun outboxOnce(): List<OutboxItemEntity>

    @Query("SELECT * FROM connector_settings")
    suspend fun settingsOnce(): List<ConnectorSettingsEntity>

    @Query("SELECT * FROM sync_settings")
    suspend fun syncSettingsOnce(): List<SyncSettingsEntity>

    @Query("SELECT * FROM finance_transactions")
    suspend fun financeTransactionsOnce(): List<FinanceTransactionEntity>

    @Query("SELECT * FROM automation_rules WHERE enabled = 1")
    suspend fun enabledRules(): List<AutomationRuleEntity>

    @Query("SELECT * FROM automation_rules WHERE id = :id LIMIT 1")
    suspend fun ruleById(id: Long): AutomationRuleEntity?

    @Query("SELECT * FROM clients WHERE id = :id LIMIT 1")
    suspend fun clientById(id: Long): ClientEntity?

    @Query("SELECT * FROM appointments WHERE id = :id LIMIT 1")
    suspend fun appointmentById(id: Long): AppointmentEntity?

    @Query("SELECT * FROM services WHERE id = :id LIMIT 1")
    suspend fun serviceById(id: Long): ServiceEntity?

    @Query("SELECT * FROM appointment_services WHERE appointmentId = :appointmentId ORDER BY sortOrder, id")
    suspend fun appointmentServicesFor(appointmentId: Long): List<AppointmentServiceEntity>

    @Query("SELECT * FROM connector_settings WHERE channel = :channel LIMIT 1")
    suspend fun settingsFor(channel: String): ConnectorSettingsEntity?

    @Query("SELECT * FROM sync_settings WHERE resource = :resource LIMIT 1")
    suspend fun syncSettingsFor(resource: String): SyncSettingsEntity?

    @Query(
        """
        SELECT o.*, c.name AS clientName, c.phone AS clientPhone
        FROM outbox o
        LEFT JOIN clients c ON c.id = o.clientId
        WHERE o.id = :id
        LIMIT 1
        """
    )
    suspend fun sendData(id: Long): OutboxSendData?

    @Query("SELECT id FROM outbox WHERE status IN (:statuses) ORDER BY id")
    suspend fun outboxIdsByStatus(statuses: List<String>): List<Long>

    @Query("SELECT id FROM outbox WHERE status IN (:statuses) AND (scheduledAt = '' OR scheduledAt <= :now) ORDER BY scheduledAt, id")
    suspend fun dueOutboxIdsByStatus(statuses: List<String>, now: String): List<Long>

    @Query("SELECT COUNT(*) FROM outbox WHERE dedupeKey = :dedupeKey")
    suspend fun countOutboxByDedupeKey(dedupeKey: String): Int

    @Query("DELETE FROM outbox WHERE appointmentId = :appointmentId AND status IN (:statuses)")
    suspend fun clearOutboxForAppointmentByStatus(appointmentId: Long, statuses: List<String>)

    @Query("DELETE FROM outbox WHERE ruleId = :ruleId AND status IN (:statuses)")
    suspend fun clearOutboxForRuleByStatus(ruleId: Long, statuses: List<String>)

    @Query("DELETE FROM outbox WHERE channel != :channel")
    suspend fun clearOutboxExceptChannel(channel: String)

    @Query("DELETE FROM outbox WHERE appointmentId = :appointmentId")
    suspend fun clearOutboxForAppointment(appointmentId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: ClientEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClients(clients: List<ClientEntity>)

    @Update
    suspend fun updateClient(client: ClientEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointments(appointments: List<AppointmentEntity>)

    @Update
    suspend fun updateAppointment(appointment: AppointmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceEntity>)

    @Update
    suspend fun updateService(service: ServiceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointmentService(service: AppointmentServiceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointmentServices(services: List<AppointmentServiceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(rule: AutomationRuleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRules(rules: List<AutomationRuleEntity>)

    @Update
    suspend fun updateRule(rule: AutomationRuleEntity)

    @Query("UPDATE automation_rules SET enabled = :enabled, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setRuleEnabled(id: Long, enabled: Boolean, updatedAt: String)

    @Query("DELETE FROM automation_rules WHERE id = :id")
    suspend fun deleteRuleById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOutbox(item: OutboxItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOutbox(items: List<OutboxItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: ConnectorSettingsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: List<ConnectorSettingsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSyncSettings(settings: SyncSettingsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSyncSettings(settings: List<SyncSettingsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinanceTransaction(transaction: FinanceTransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinanceTransactions(transactions: List<FinanceTransactionEntity>)

    @Query("UPDATE outbox SET status = :status, error = :error, sentAt = :sentAt WHERE id = :id")
    suspend fun updateOutboxStatus(id: Long, status: String, error: String = "", sentAt: String = "")

    @Query("DELETE FROM outbox")
    suspend fun clearOutbox()

    @Query("DELETE FROM automation_rules")
    suspend fun clearRules()

    @Query("DELETE FROM appointments")
    suspend fun clearAppointments()

    @Query("DELETE FROM appointment_services")
    suspend fun clearAppointmentServices()

    @Query("DELETE FROM appointment_services WHERE appointmentId = :appointmentId")
    suspend fun clearAppointmentServicesFor(appointmentId: Long)

    @Query("DELETE FROM appointments WHERE id = :appointmentId")
    suspend fun deleteAppointmentById(appointmentId: Long)

    @Query("DELETE FROM services")
    suspend fun clearServices()

    @Query("DELETE FROM clients")
    suspend fun clearClients()

    @Query("DELETE FROM connector_settings")
    suspend fun clearSettings()

    @Query("DELETE FROM connector_settings WHERE channel != :channel")
    suspend fun clearSettingsExceptChannel(channel: String)

    @Query("DELETE FROM sync_settings")
    suspend fun clearSyncSettings()

    @Query("DELETE FROM finance_transactions")
    suspend fun clearFinanceTransactions()
}

@Database(
    entities = [
        ClientEntity::class,
        AppointmentEntity::class,
        ServiceEntity::class,
        AppointmentServiceEntity::class,
        AutomationRuleEntity::class,
        OutboxItemEntity::class,
        ConnectorSettingsEntity::class,
        SyncSettingsEntity::class,
        FinanceTransactionEntity::class
    ],
    version = 14,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): AppDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "offline_beauty_crm.db"
                )
                    .addMigrations(
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7,
                        MIGRATION_7_8,
                        MIGRATION_8_9,
                        MIGRATION_9_10,
                        MIGRATION_10_11,
                        MIGRATION_11_12,
                        MIGRATION_12_13,
                        MIGRATION_13_14
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE clients ADD COLUMN contactId INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE clients ADD COLUMN contactLookupKey TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE clients ADD COLUMN contactSyncedAt TEXT NOT NULL DEFAULT ''")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE appointments ADD COLUMN calendarEventId INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE appointments ADD COLUMN calendarSyncedAt TEXT NOT NULL DEFAULT ''")
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    UPDATE clients
                    SET notes = ''
                    WHERE TRIM(notes) IN ('Импортировано из контактов', 'Импортировано из телефонной книги')
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE clients ADD COLUMN contactPhotoUri TEXT NOT NULL DEFAULT ''")
            }
        }

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                listOf(
                    "givenName",
                    "middleName",
                    "familyName",
                    "nickname",
                    "phoneHome",
                    "phoneWork",
                    "phoneOther",
                    "email",
                    "emailWork",
                    "address",
                    "addressWork",
                    "company",
                    "jobTitle",
                    "website",
                    "birthday"
                ).forEach { column ->
                    db.execSQL("ALTER TABLE clients ADD COLUMN $column TEXT NOT NULL DEFAULT ''")
                }
            }
        }

        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE appointments ADD COLUMN durationMinutes INTEGER NOT NULL DEFAULT 60")
            }
        }

        private val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE clients ADD COLUMN updatedAt TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE appointments ADD COLUMN updatedAt TEXT NOT NULL DEFAULT ''")
                db.execSQL("UPDATE clients SET updatedAt = createdAt WHERE updatedAt = ''")
                db.execSQL("UPDATE appointments SET updatedAt = createdAt WHERE updatedAt = ''")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS sync_settings (
                        resource TEXT NOT NULL PRIMARY KEY,
                        mode TEXT NOT NULL,
                        updatedAt TEXT NOT NULL,
                        lastRunAt TEXT NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    "INSERT OR IGNORE INTO sync_settings(resource, mode, updatedAt, lastRunAt) VALUES('$SYNC_RESOURCE_CONTACTS', '$SYNC_MODE_TWO_WAY', '', '')"
                )
                db.execSQL(
                    "INSERT OR IGNORE INTO sync_settings(resource, mode, updatedAt, lastRunAt) VALUES('$SYNC_RESOURCE_CALENDAR', '$SYNC_MODE_TWO_WAY', '', '')"
                )
            }
        }

        private val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE appointments ADD COLUMN priceCents INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE appointments ADD COLUMN paidAmountCents INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE appointments ADD COLUMN paymentStatus TEXT NOT NULL DEFAULT '$PAYMENT_STATUS_UNPAID'")
                db.execSQL("ALTER TABLE appointments ADD COLUMN paymentMethod TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE appointments ADD COLUMN paidAt TEXT NOT NULL DEFAULT ''")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS finance_transactions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        type TEXT NOT NULL,
                        amountCents INTEGER NOT NULL,
                        title TEXT NOT NULL,
                        category TEXT NOT NULL,
                        date TEXT NOT NULL,
                        paymentMethod TEXT NOT NULL,
                        notes TEXT NOT NULL,
                        clientId INTEGER,
                        appointmentId INTEGER,
                        createdAt TEXT NOT NULL,
                        updatedAt TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE sync_settings ADD COLUMN selectedSystemId TEXT NOT NULL DEFAULT ''")
            }
        }

        private val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS services (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        priceCents INTEGER NOT NULL,
                        durationMinutes INTEGER NOT NULL,
                        isActive INTEGER NOT NULL,
                        createdAt TEXT NOT NULL,
                        updatedAt TEXT NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS appointment_services (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        appointmentId INTEGER NOT NULL,
                        serviceId INTEGER NOT NULL,
                        serviceName TEXT NOT NULL,
                        priceCents INTEGER NOT NULL,
                        durationMinutes INTEGER NOT NULL,
                        sortOrder INTEGER NOT NULL,
                        createdAt TEXT NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO appointment_services(
                        appointmentId, serviceId, serviceName, priceCents, durationMinutes, sortOrder, createdAt
                    )
                    SELECT id, 0, service, priceCents, durationMinutes, 0, createdAt
                    FROM appointments
                    WHERE TRIM(service) != ''
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_12_13 = object : Migration(12, 13) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE automation_rules ADD COLUMN triggerType TEXT NOT NULL DEFAULT '$AUTOMATION_TRIGGER_NONE'")
                db.execSQL("ALTER TABLE automation_rules ADD COLUMN offsetMinutes INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE automation_rules ADD COLUMN retentionDays INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE automation_rules ADD COLUMN serviceFilterIds TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE automation_rules ADD COLUMN condition TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE automation_rules ADD COLUMN updatedAt TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE outbox ADD COLUMN dedupeKey TEXT NOT NULL DEFAULT ''")
                db.execSQL(
                    """
                    UPDATE automation_rules
                    SET triggerType = CASE triggerName
                        WHEN 'За 24 часа до записи' THEN '$AUTOMATION_TRIGGER_BEFORE_APPOINTMENT'
                        WHEN 'За 2 часа до записи' THEN '$AUTOMATION_TRIGGER_BEFORE_APPOINTMENT'
                        WHEN 'После визита' THEN '$AUTOMATION_TRIGGER_AFTER_APPOINTMENT'
                        ELSE '$AUTOMATION_TRIGGER_NONE'
                    END,
                    offsetMinutes = CASE triggerName
                        WHEN 'За 24 часа до записи' THEN 1440
                        WHEN 'За 2 часа до записи' THEN 120
                        ELSE 0
                    END,
                    updatedAt = CASE WHEN createdAt IS NULL OR createdAt = '' THEN '' ELSE createdAt END
                    """.trimIndent()
                )
                db.execSQL("UPDATE outbox SET scheduledAt = '' WHERE scheduledAt NOT LIKE '____-__-__ __:__%'")
            }
        }

        private val MIGRATION_13_14 = object : Migration(13, 14) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE automation_rules ADD COLUMN askBeforeRun INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}

private object AutomationEngine {
    private val pendingStatuses = listOf(STATUS_QUEUED, STATUS_NEEDS_CONFIG, STATUS_WAITING_PERMISSION, STATUS_FAILED)

    suspend fun enqueueForAppointmentEvent(
        context: Context,
        dao: AppDao,
        appointment: AppointmentEntity,
        client: ClientEntity,
        eventType: String,
        includeScheduledRules: Boolean
    ): Int {
        val services = dao.appointmentServicesFor(appointment.id)
        var created = 0
        dao.enabledRules().forEach { rule ->
            val triggerType = normalizedTriggerType(rule)
            val isScheduledRule = triggerType in listOf(
                AUTOMATION_TRIGGER_BEFORE_APPOINTMENT,
                AUTOMATION_TRIGGER_AFTER_APPOINTMENT,
                AUTOMATION_TRIGGER_DEBT_AFTER_APPOINTMENT
            )
            if (isScheduledRule && !includeScheduledRules) return@forEach
            if (!isScheduledRule && triggerType != eventType) return@forEach
            if (triggerType == AUTOMATION_TRIGGER_NONE || triggerType == AUTOMATION_TRIGGER_RETENTION_BY_SERVICE) return@forEach
            if (appointment.status == APPOINTMENT_STATUS_CANCELLED && triggerType != AUTOMATION_TRIGGER_APPOINTMENT_CANCELLED) return@forEach
            if (!ruleMatchesServices(rule, services)) return@forEach
            if (triggerType == AUTOMATION_TRIGGER_DEBT_AFTER_APPOINTMENT && appointmentRemainingCents(appointment) <= 0L) return@forEach
            val scheduledAt = scheduledAtFor(rule, triggerType, appointment) ?: return@forEach
            val dedupeKey = appointmentDedupeKey(rule, appointment, triggerType, eventType, scheduledAt)
            if (dao.countOutboxByDedupeKey(dedupeKey) > 0) return@forEach
            val outboxId = dao.insertOutbox(
                OutboxItemEntity(
                    ruleId = rule.id,
                    clientId = client.id,
                    appointmentId = appointment.id,
                    channel = rule.channel,
                    payload = renderTemplate(rule.message, client, appointment, services, appointment),
                    status = if (rule.askBeforeRun) STATUS_NEEDS_CONFIG else STATUS_QUEUED,
                    scheduledAt = scheduledAt,
                    error = if (rule.askBeforeRun) OUTBOX_CONFIRMATION_REQUIRED else "",
                    dedupeKey = dedupeKey
                )
            )
            if (rule.askBeforeRun) {
                scheduleOutboxConfirmation(context, outboxId, scheduledAt)
            } else {
                scheduleOutboxWork(context, outboxId, scheduledAt)
            }
            created++
        }
        return created
    }

    suspend fun enqueueRetentionCandidates(context: Context, dao: AppDao): Int {
        val rules = dao.enabledRules()
            .filter { normalizedTriggerType(it) == AUTOMATION_TRIGGER_RETENTION_BY_SERVICE }
        if (rules.isEmpty()) return 0
        val clients = dao.clientsOnce().associateBy { it.id }
        val appointments = dao.appointmentsOnce()
            .filter { it.status != APPOINTMENT_STATUS_CANCELLED }
        val servicesByAppointment = dao.appointmentServicesOnce().groupBy { it.appointmentId }
        val now = LocalDateTime.now()
        var created = 0
        rules.forEach { rule ->
            clients.values.forEach { client ->
                val clientAppointments = appointments
                    .filter { it.clientId == client.id && ruleMatchesServices(rule, servicesByAppointment[it.id].orEmpty()) }
                val hasFuture = clientAppointments.any { appointment ->
                    val start = parseAppointmentDateTime(appointment.startAt)
                    start != null && start.isAfter(now)
                }
                if (hasFuture) return@forEach
                val last = clientAppointments
                    .filter { appointment ->
                        appointmentEndDateTime(appointment)?.isBefore(now) == true
                    }
                    .maxByOrNull { parseAppointmentMillis(it.startAt) ?: Long.MIN_VALUE }
                    ?: return@forEach
                val lastEnd = appointmentEndDateTime(last) ?: return@forEach
                val dueAt = lastEnd.plusDays(rule.retentionDays.coerceAtLeast(1).toLong())
                if (dueAt.isAfter(now)) return@forEach
                val dedupeKey = "rule:${rule.id}:retention:client:${client.id}:last:${last.id}"
                if (dao.countOutboxByDedupeKey(dedupeKey) > 0) return@forEach
                val lastServices = servicesByAppointment[last.id].orEmpty()
                val outboxId = dao.insertOutbox(
                    OutboxItemEntity(
                        ruleId = rule.id,
                        clientId = client.id,
                        appointmentId = last.id,
                        channel = rule.channel,
                        payload = renderTemplate(rule.message, client, last, lastServices, last),
                        status = if (rule.askBeforeRun) STATUS_NEEDS_CONFIG else STATUS_QUEUED,
                        scheduledAt = "",
                        error = if (rule.askBeforeRun) OUTBOX_CONFIRMATION_REQUIRED else "",
                        dedupeKey = dedupeKey
                    )
                )
                if (rule.askBeforeRun) {
                    scheduleOutboxConfirmation(context, outboxId, "")
                } else {
                    scheduleOutboxWork(context, outboxId, "")
                }
                created++
            }
        }
        return created
    }

    suspend fun shouldSkipBeforeSend(dao: AppDao, outbox: OutboxItemEntity): String {
        val rule = outbox.ruleId?.let { dao.ruleById(it) } ?: return ""
        val appointment = outbox.appointmentId?.let { dao.appointmentById(it) } ?: return ""
        val triggerType = normalizedTriggerType(rule)
        if (
            appointment.status == APPOINTMENT_STATUS_CANCELLED &&
            triggerType in listOf(
                AUTOMATION_TRIGGER_BEFORE_APPOINTMENT,
                AUTOMATION_TRIGGER_AFTER_APPOINTMENT,
                AUTOMATION_TRIGGER_DEBT_AFTER_APPOINTMENT,
                AUTOMATION_TRIGGER_RETENTION_BY_SERVICE
            )
        ) {
            return "Запись отменена"
        }
        if (triggerType == AUTOMATION_TRIGGER_DEBT_AFTER_APPOINTMENT && appointmentRemainingCents(appointment) <= 0L) {
            return "Долга уже нет"
        }
        return ""
    }

    private fun normalizedTriggerType(rule: AutomationRuleEntity): String =
        rule.triggerType.ifBlank { legacyTriggerType(rule.triggerName) }

    private fun legacyTriggerType(triggerName: String): String = when (triggerName) {
        "За 24 часа до записи", "За 2 часа до записи" -> AUTOMATION_TRIGGER_BEFORE_APPOINTMENT
        "После визита" -> AUTOMATION_TRIGGER_AFTER_APPOINTMENT
        else -> AUTOMATION_TRIGGER_NONE
    }

    private fun scheduledAtFor(rule: AutomationRuleEntity, triggerType: String, appointment: AppointmentEntity): String? {
        val start = parseAppointmentDateTime(appointment.startAt) ?: return null
        val target = when (triggerType) {
            AUTOMATION_TRIGGER_BEFORE_APPOINTMENT -> start.minusMinutes(rule.offsetMinutes.coerceAtLeast(0).toLong())
            AUTOMATION_TRIGGER_AFTER_APPOINTMENT -> appointmentEndDateTime(appointment)
                ?.plusMinutes(rule.offsetMinutes.coerceAtLeast(0).toLong())
            AUTOMATION_TRIGGER_DEBT_AFTER_APPOINTMENT -> appointmentEndDateTime(appointment)
                ?.plusDays(rule.retentionDays.coerceAtLeast(1).toLong())
            else -> LocalDateTime.now()
        } ?: return null
        return target.format(appointmentStorageFormatter)
    }

    private fun ruleMatchesServices(rule: AutomationRuleEntity, services: List<AppointmentServiceEntity>): Boolean {
        val filterIds = parseServiceFilterIds(rule.serviceFilterIds)
        if (filterIds.isEmpty()) return true
        return services.any { it.serviceId in filterIds }
    }

    private fun parseServiceFilterIds(value: String): Set<Long> =
        value.split(",")
            .mapNotNull { it.trim().toLongOrNull() }
            .filter { it > 0L }
            .toSet()

    private fun appointmentDedupeKey(
        rule: AutomationRuleEntity,
        appointment: AppointmentEntity,
        triggerType: String,
        eventType: String,
        scheduledAt: String
    ): String {
        val eventVersion = when (triggerType) {
            AUTOMATION_TRIGGER_APPOINTMENT_RESCHEDULED,
            AUTOMATION_TRIGGER_APPOINTMENT_CANCELLED,
            AUTOMATION_TRIGGER_APPOINTMENT_REACTIVATED,
            AUTOMATION_TRIGGER_PAYMENT_RECEIVED -> appointment.updatedAt
            else -> appointment.startAt
        }
        return listOf(
            "rule:${rule.id}",
            "appointment:${appointment.id}",
            "trigger:$triggerType",
            "event:$eventType",
            "schedule:$scheduledAt",
            "version:$eventVersion"
        ).joinToString(":")
    }

    private fun renderTemplate(
        template: String,
        client: ClientEntity,
        appointment: AppointmentEntity,
        services: List<AppointmentServiceEntity>,
        lastAppointment: AppointmentEntity
    ): String {
        val start = parseAppointmentDateTime(appointment.startAt)
        val end = appointmentEndDateTime(appointment)
        val lastStart = parseAppointmentDateTime(lastAppointment.startAt)
        val serviceNames = services
            .map { it.serviceName }
            .filter { it.isNotBlank() }
            .ifEmpty { listOf(appointment.service) }
            .joinToString(" + ")
        val replacements = mapOf(
            "{client.name}" to client.name,
            "{client.phone}" to clientPrimaryPhone(client),
            "{client.email}" to firstFilled(client.email, client.emailWork),
            "{appointment.service}" to appointment.service,
            "{appointment.date}" to (start?.toLocalDate()?.format(DateTimeFormatter.ofPattern("d MMMM yyyy", ruLocale)) ?: appointment.startAt),
            "{appointment.time}" to (start?.toLocalTime()?.toString() ?: appointment.startAt),
            "{appointment.end_time}" to (end?.toLocalTime()?.toString() ?: ""),
            "{appointment.duration}" to formatDurationMinutes(appointment.durationMinutes),
            "{appointment.price}" to formatMoney(appointment.priceCents),
            "{appointment.paid}" to formatMoney(appointment.paidAmountCents),
            "{appointment.debt}" to formatMoney(appointmentRemainingCents(appointment)),
            "{appointment.status}" to appointmentEntityUiStatusLabel(appointment),
            "{services}" to serviceNames,
            "{last_appointment.date}" to (lastStart?.toLocalDate()?.format(DateTimeFormatter.ofPattern("d MMMM yyyy", ruLocale)) ?: lastAppointment.startAt),
            "{last_appointment.service}" to lastAppointment.service,
            "{client}" to client.name,
            "{service}" to appointment.service,
            "{time}" to appointment.startAt
        )
        return replacements.entries.fold(template) { acc, (key, value) -> acc.replace(key, value) }
    }
}

private fun scheduleOutboxWork(context: Context, outboxId: Long, scheduledAt: String) {
    val scheduledMillis = parseAppointmentMillis(scheduledAt) ?: System.currentTimeMillis()
    val delayMillis = (scheduledMillis - System.currentTimeMillis()).coerceAtLeast(0L)
    val request = OneTimeWorkRequestBuilder<AutomationWorker>()
        .setInputData(workDataOf("outboxId" to outboxId))
        .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
        .build()
    WorkManager.getInstance(context.applicationContext).enqueue(request)
}

private fun scheduleRetentionWorker(context: Context) {
    val request = PeriodicWorkRequestBuilder<AutomationWorker>(1, TimeUnit.DAYS)
        .setInputData(workDataOf("retentionScan" to true))
        .build()
    WorkManager.getInstance(context.applicationContext)
        .enqueueUniquePeriodicWork(AUTOMATION_RETENTION_WORK, ExistingPeriodicWorkPolicy.KEEP, request)
}

private object AutoBackupStore {
    private const val KEY_MODE = "mode"
    private const val KEY_FOLDER_URI = "folder_uri"
    private const val KEY_LAST_RUN_AT = "last_run_at"

    fun load(context: Context): AutoBackupState {
        val prefs = context.getSharedPreferences(AUTO_BACKUP_PREFS, Context.MODE_PRIVATE)
        return AutoBackupState(
            mode = prefs.getString(KEY_MODE, AUTO_BACKUP_MODE_OFF)
                ?.takeIf { it in autoBackupModeOptions }
                ?: AUTO_BACKUP_MODE_OFF,
            folderUri = prefs.getString(KEY_FOLDER_URI, "").orEmpty(),
            lastRunAt = prefs.getString(KEY_LAST_RUN_AT, "").orEmpty()
        )
    }

    fun saveMode(context: Context, mode: String): AutoBackupState {
        val cleanMode = mode.takeIf { it in autoBackupModeOptions } ?: AUTO_BACKUP_MODE_OFF
        context.getSharedPreferences(AUTO_BACKUP_PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_MODE, cleanMode)
            .apply()
        return load(context)
    }

    fun saveFolder(context: Context, uri: Uri): AutoBackupState {
        context.getSharedPreferences(AUTO_BACKUP_PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_FOLDER_URI, uri.toString())
            .apply()
        return load(context)
    }

    fun saveLastRun(context: Context, value: String): AutoBackupState {
        context.getSharedPreferences(AUTO_BACKUP_PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LAST_RUN_AT, value)
            .apply()
        return load(context)
    }
}

private object AppIconStore {
    private const val ICON_DIR = "branding"
    private const val ICON_FILE = "app_icon.png"
    private const val LEGACY_LOGO_FILE = "app_logo.png"

    fun load(context: Context): AppIconState {
        val file = iconFile(context)
        val legacyFile = legacyLogoFile(context)
        if (!file.exists() && legacyFile.exists()) {
            runCatching { legacyFile.renameTo(file) }
        }
        return if (file.exists() && file.length() > 0L) {
            AppIconState(path = file.absolutePath, updatedAt = file.lastModified())
        } else {
            AppIconState()
        }
    }

    fun save(context: Context, uri: Uri): AppIconState {
        val source = decodeIconBitmap(context, uri) ?: error("не удалось прочитать изображение")
        val icon = source.centerSquare(APP_ICON_IMAGE_SIZE)
        val file = iconFile(context)
        file.parentFile?.mkdirs()
        val tmp = File(file.parentFile, "$ICON_FILE.tmp")
        FileOutputStream(tmp).use { stream ->
            if (!icon.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                error("не удалось сохранить иконку")
            }
        }
        if (file.exists()) file.delete()
        if (!tmp.renameTo(file)) {
            error("не удалось заменить иконку")
        }
        return load(context)
    }

    fun reset(context: Context): AppIconState {
        iconFile(context).delete()
        return AppIconState()
    }

    fun bitmap(context: Context): Bitmap? =
        BitmapFactory.decodeFile(iconFile(context).absolutePath)

    private fun iconFile(context: Context): File =
        File(File(context.filesDir, ICON_DIR), ICON_FILE)

    private fun legacyLogoFile(context: Context): File =
        File(File(context.filesDir, ICON_DIR), LEGACY_LOGO_FILE)

    private fun decodeIconBitmap(context: Context, uri: Uri): Bitmap? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri)) { decoder, info, _ ->
                val largestSide = maxOf(info.size.width, info.size.height)
                if (largestSide > APP_ICON_IMAGE_SIZE) {
                    decoder.setTargetSampleSize((largestSide / APP_ICON_IMAGE_SIZE).coerceAtLeast(1))
                }
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            }
        } else {
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, bounds)
            }
            val largestSide = maxOf(bounds.outWidth, bounds.outHeight).coerceAtLeast(1)
            val sampleSize = (largestSide / APP_ICON_IMAGE_SIZE).coerceAtLeast(1)
            val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
            context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, options)
            }
        }

    private fun Bitmap.centerSquare(targetSize: Int): Bitmap {
        val side = minOf(width, height)
        val left = (width - side) / 2
        val top = (height - side) / 2
        val square = Bitmap.createBitmap(this, left, top, side, side)
        return if (side == targetSize) {
            square
        } else {
            Bitmap.createScaledBitmap(square, targetSize, targetSize, true)
        }
    }
}

private object AutoBackupJson {
    suspend fun create(context: Context, db: AppDatabase, requireEnabled: Boolean = true): String? {
        val state = AutoBackupStore.load(context)
        if ((requireEnabled && state.mode == AUTO_BACKUP_MODE_OFF) || state.folderUri.isBlank()) return null
        val treeUri = Uri.parse(state.folderUri)
        val parentUri = DocumentsContract.buildDocumentUriUsingTree(
            treeUri,
            DocumentsContract.getTreeDocumentId(treeUri)
        )
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
        val fileName = "offline-beauty-crm-$timestamp.json"
        val fileUri = DocumentsContract.createDocument(
            context.contentResolver,
            parentUri,
            "application/json",
            fileName
        ) ?: return null
        val json = BackupJson.export(db).toString(2)
        context.contentResolver.openOutputStream(fileUri)?.use { stream ->
            OutputStreamWriter(stream).use { writer -> writer.write(json) }
        } ?: return null
        AutoBackupStore.saveLastRun(context, now())
        return fileName
    }
}

private fun scheduleAutoBackupWorker(context: Context) {
    val state = AutoBackupStore.load(context)
    val workManager = WorkManager.getInstance(context.applicationContext)
    if (state.mode == AUTO_BACKUP_MODE_OFF || state.folderUri.isBlank()) {
        workManager.cancelUniqueWork(AUTO_BACKUP_WORK)
        return
    }
    val repeatDays = if (state.mode == AUTO_BACKUP_MODE_WEEKLY) 7L else 1L
    val request = PeriodicWorkRequestBuilder<AutoBackupWorker>(repeatDays, TimeUnit.DAYS).build()
    workManager.enqueueUniquePeriodicWork(AUTO_BACKUP_WORK, ExistingPeriodicWorkPolicy.REPLACE, request)
}

class AutoBackupWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return try {
            AutoBackupJson.create(applicationContext, AppDatabase.get(applicationContext))
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

private fun scheduleOutboxConfirmation(context: Context, outboxId: Long, scheduledAt: String) {
    val scheduledMillis = parseAppointmentMillis(scheduledAt) ?: System.currentTimeMillis()
    val delayMillis = (scheduledMillis - System.currentTimeMillis()).coerceAtLeast(0L)
    val request = OneTimeWorkRequestBuilder<AutomationWorker>()
        .setInputData(workDataOf("confirmOutboxId" to outboxId))
        .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
        .build()
    WorkManager.getInstance(context.applicationContext).enqueue(request)
}

class AutomationNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val outboxId = intent.getLongExtra(EXTRA_OUTBOX_ID, 0L)
        if (outboxId <= 0L) return
        AutomationNotifications.cancel(context, outboxId)
        when (intent.action) {
            ACTION_AUTOMATION_SEND_NOW -> {
                val request = OneTimeWorkRequestBuilder<AutomationWorker>()
                    .setInputData(workDataOf("outboxId" to outboxId))
                    .build()
                WorkManager.getInstance(context.applicationContext).enqueue(request)
            }
            ACTION_AUTOMATION_SKIP -> {
                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        AppDatabase.get(context.applicationContext)
                            .dao()
                            .updateOutboxStatus(outboxId, STATUS_CANCELLED, "Отправка отменена пользователем")
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
        }
    }
}

private object GitHubUpdates {
    fun currentVersion(context: Context): AppVersionInfo {
        val info = context.packageManager.getPackageInfo(context.packageName, 0)
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            info.versionCode.toLong()
        }
        return AppVersionInfo(info.versionName.orEmpty().ifBlank { "0" }, versionCode)
    }

    suspend fun check(context: Context): UpdateCheckState = withContext(Dispatchers.IO) {
        val current = currentVersion(context)
        try {
            val release = fetchLatestStableRelease()
            val currentParts = parseVersionParts(current.versionName)
                ?: return@withContext UpdateCheckState.Error(current, "Не удалось распознать текущую версию ${current.versionName}")
            val releaseParts = parseVersionParts(release.versionName)
                ?: return@withContext UpdateCheckState.Error(current, "Не удалось распознать версию релиза ${release.tagName}")
            if (compareVersionParts(releaseParts, currentParts) > 0) {
                UpdateCheckState.Available(current, release)
            } else {
                UpdateCheckState.Current(current, release)
            }
        } catch (e: Exception) {
            UpdateCheckState.Error(current, e.message ?: "Не удалось проверить обновления")
        }
    }

    suspend fun downloadApk(context: Context, release: GitHubReleaseInfo): File = withContext(Dispatchers.IO) {
        val updatesDir = File(context.cacheDir, "updates").apply { mkdirs() }
        updatesDir.listFiles { file -> file.extension.equals("apk", ignoreCase = true) }
            ?.forEach { file -> runCatching { file.delete() } }
        val cleanName = release.apkName.replace(Regex("[^A-Za-z0-9._-]"), "_").ifBlank { "12609-update.apk" }
        val target = File(updatesDir, cleanName)
        val connection = (URL(release.apkDownloadUrl).openConnection() as HttpURLConnection).apply {
            connectTimeout = 15000
            readTimeout = 30000
            requestMethod = "GET"
            setRequestProperty("Accept", "application/octet-stream")
            setRequestProperty("User-Agent", "12609-android-updater")
        }
        try {
            val code = connection.responseCode
            if (code !in 200..299) error("GitHub вернул HTTP $code при скачивании APK")
            connection.inputStream.use { input ->
                FileOutputStream(target).use { output -> input.copyTo(output) }
            }
        } finally {
            connection.disconnect()
        }
        if (!target.exists() || target.length() <= 0L) error("APK скачан пустым файлом")
        target
    }

    fun installApk(context: Context, apkFile: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            !context.packageManager.canRequestPackageInstalls()
        ) {
            val settingsIntent = Intent(
                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                Uri.parse("package:${context.packageName}")
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(settingsIntent)
            return
        }
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            apkFile
        )
        val intent = Intent(Intent.ACTION_VIEW)
            .setDataAndType(uri, "application/vnd.android.package-archive")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }

    fun openRelease(context: Context, release: GitHubReleaseInfo) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(release.htmlUrl))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun fetchLatestStableRelease(): GitHubReleaseInfo {
        val json = JSONObject(fetchText(GITHUB_LATEST_RELEASE_URL))
        if (json.optBoolean("draft") || json.optBoolean("prerelease")) {
            error("Последний релиз не является стабильным")
        }
        val tagName = json.optString("tag_name").trim()
        if (tagName.isBlank()) error("GitHub Release без тега версии")
        val assets = json.optJSONArray("assets") ?: JSONArray()
        var apkName = ""
        var apkDownloadUrl = ""
        for (index in 0 until assets.length()) {
            val asset = assets.getJSONObject(index)
            val name = asset.optString("name").trim()
            val downloadUrl = asset.optString("browser_download_url").trim()
            if (name.endsWith(".apk", ignoreCase = true) && downloadUrl.isNotBlank()) {
                apkName = name
                apkDownloadUrl = downloadUrl
                break
            }
        }
        if (apkDownloadUrl.isBlank()) error("В последнем релизе нет APK-файла")
        return GitHubReleaseInfo(
            tagName = tagName,
            versionName = tagName.removePrefix("v").removePrefix("V"),
            publishedAt = json.optString("published_at").trim(),
            body = json.optString("body").trim(),
            htmlUrl = json.optString("html_url").trim(),
            apkName = apkName,
            apkDownloadUrl = apkDownloadUrl
        )
    }

    private fun fetchText(url: String): String {
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            connectTimeout = 15000
            readTimeout = 15000
            requestMethod = "GET"
            setRequestProperty("Accept", "application/vnd.github+json")
            setRequestProperty("User-Agent", "12609-android-updater")
        }
        return try {
            val code = connection.responseCode
            if (code == 404) error("GitHub Releases пока не опубликованы")
            if (code !in 200..299) error("GitHub вернул HTTP $code")
            connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }
}

private object AutomationNotifications {
    suspend fun showConfirmation(context: Context, db: AppDatabase, outboxId: Long): Boolean {
        if (!hasAutomationNotificationPermission(context)) return false
        val data = db.dao().sendData(outboxId) ?: return false
        if (data.outbox.status != STATUS_NEEDS_CONFIG || data.outbox.error != OUTBOX_CONFIRMATION_REQUIRED) {
            return false
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(
            NotificationChannel(
                AUTOMATION_NOTIFICATION_CHANNEL,
                "Подтверждение автоматизации",
                NotificationManager.IMPORTANCE_HIGH
            )
        )
        val sendIntent = PendingIntent.getBroadcast(
            context,
            outboxNotificationId(outboxId) + 1,
            Intent(context, AutomationNotificationReceiver::class.java)
                .setAction(ACTION_AUTOMATION_SEND_NOW)
                .putExtra(EXTRA_OUTBOX_ID, outboxId),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val skipIntent = PendingIntent.getBroadcast(
            context,
            outboxNotificationId(outboxId) + 2,
            Intent(context, AutomationNotificationReceiver::class.java)
                .setAction(ACTION_AUTOMATION_SKIP)
                .putExtra(EXTRA_OUTBOX_ID, outboxId),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val openIntent = PendingIntent.getActivity(
            context,
            outboxNotificationId(outboxId) + 3,
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val title = "Подтвердить отправку"
        val text = "${data.clientName ?: "Клиент"}: ${data.outbox.payload}"
        val largeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_logo_notification_large)
        val notification = Notification.Builder(context, AUTOMATION_NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(largeIcon)
            .setColor(context.getColor(R.color.launcher_background))
            .setContentTitle(title)
            .setContentText(text.take(90))
            .setStyle(Notification.BigTextStyle().bigText(text))
            .setContentIntent(openIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_notification, "Отправить", sendIntent)
            .addAction(R.drawable.ic_notification, "Пропустить", skipIntent)
            .build()
        manager.notify(outboxNotificationId(outboxId), notification)
        return true
    }

    fun cancel(context: Context, outboxId: Long) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(outboxNotificationId(outboxId))
    }
}

private fun outboxNotificationId(outboxId: Long): Int =
    (outboxId % 1_000_000L).toInt() + 20_000

private fun hasAutomationNotificationPermission(context: Context): Boolean =
    Build.VERSION.SDK_INT < 33 ||
        context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.get(application)
    private val dao = db.dao()

    val clients: Flow<List<ClientEntity>> = dao.observeClients()
    val appointments: Flow<List<AppointmentRow>> = dao.observeAppointments()
    val services: Flow<List<ServiceEntity>> = dao.observeServices()
    val rules: Flow<List<AutomationRuleEntity>> = dao.observeRules()
    val outbox: Flow<List<OutboxRow>> = dao.observeOutbox()
    val settings: Flow<List<ConnectorSettingsEntity>> = dao.observeSettings()
    val syncSettings: Flow<List<SyncSettingsEntity>> = dao.observeSyncSettings()
    val financeTransactions: Flow<List<FinanceTransactionEntity>> = dao.observeFinanceTransactions()

    var message by mutableStateOf("")
        private set
    var autoBackupState by mutableStateOf(AutoBackupStore.load(application))
        private set
    var appIconState by mutableStateOf(AppIconStore.load(application))
        private set
    var contacts by mutableStateOf<List<ContactCandidate>>(emptyList())
        private set
    var counts by mutableStateOf(Counts(0, 0, 0, 0))
        private set

    init {
        scheduleRetentionWorker(application)
        scheduleAutoBackupWorker(application)
        viewModelScope.launch {
            seedDefaults()
            normalizeSmsOnlyAutomation()
            backfillAppointmentPriceCents()
            refreshContactLinksFromPhonebook()
            refreshCounts()
        }
    }

    fun clearMessage(expectedMessage: String = message) {
        if (message == expectedMessage) {
            message = ""
        }
    }

    fun saveAppIcon(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                appIconState = withContext(Dispatchers.IO) {
                    AppIconStore.save(context.applicationContext, uri)
                }
                message = "Изображение иконки сохранено"
            } catch (e: Exception) {
                message = "Не удалось сохранить иконку: ${e.message ?: "ошибка изображения"}"
            }
        }
    }

    fun resetAppIcon() {
        appIconState = AppIconStore.reset(getApplication())
        message = "Изображение иконки сброшено"
    }

    fun createAppIconShortcut(context: Context) {
        val appContext = context.applicationContext
        val bitmap = AppIconStore.bitmap(appContext)
        if (bitmap == null || !appIconState.isSet) {
            message = "Сначала выбери изображение иконки"
            return
        }
        val shortcutManager = appContext.getSystemService(android.content.pm.ShortcutManager::class.java)
        if (!shortcutManager.isRequestPinShortcutSupported) {
            message = "Лаунчер не поддерживает создание ярлыков"
            return
        }
        val appName = appContext.getString(R.string.app_name)
        val launchIntent = Intent(appContext, MainActivity::class.java).apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        }
        val shortcut = android.content.pm.ShortcutInfo.Builder(appContext, "custom_app_icon")
            .setShortLabel(appName)
            .setLongLabel(appName)
            .setIcon(android.graphics.drawable.Icon.createWithAdaptiveBitmap(bitmap))
            .setIntent(launchIntent)
            .build()
        val updated = shortcutManager.pinnedShortcuts.any { it.id == shortcut.id } &&
            shortcutManager.updateShortcuts(listOf(shortcut))
        if (updated) {
            message = "Иконка ярлыка обновлена"
            return
        }
        message = if (shortcutManager.requestPinShortcut(shortcut, null)) {
            "Подтверди создание ярлыка на главном экране"
        } else {
            "Лаунчер не принял запрос на ярлык"
        }
    }

    fun addClient(context: Context, draft: ClientDraft, syncContact: Boolean) {
        val clientDraft = draft.toEntity()
        if (clientDraft.name.isBlank()) {
            message = "Укажи имя клиента"
            return
        }
        viewModelScope.launch {
            if (clientExists(clientDraft.name, clientDraft.phone)) {
                message = "Клиент с таким номером уже есть"
                return@launch
            }
            var manualSyncAction = "skipped"
            val manuallySyncedClient = if (syncContact) {
                val result = withContext(Dispatchers.IO) {
                    ContactsSync.pushClient(context.applicationContext, clientDraft)
                }
                manualSyncAction = result.action
                result.client
            } else {
                null
            }
            val savedClient = manuallySyncedClient ?: clientDraft
            val clientId = dao.insertClient(savedClient)
            val storedClient = savedClient.copy(id = clientId)
            val contactsMode = syncModeFor(SYNC_RESOURCE_CONTACTS)
            val shouldAutoSync = shouldAutoSyncToSystem(contactsMode)
            val hasPermissions = hasContactsPermissions(context.applicationContext)
            val autoSynced = if (!syncContact && shouldAutoSync && hasPermissions) {
                val result = withContext(Dispatchers.IO) {
                    ContactsSync.pushClient(context.applicationContext, storedClient)
                }
                if (result.client != storedClient) dao.updateClient(result.client)
                result.action != "skipped"
            } else {
                false
            }
            refreshCounts()
            message = when {
                syncContact && manualSyncAction != "skipped" -> "Клиент сохранен и связан с контактом телефона"
                autoSynced -> "Клиент сохранен и синхронизирован с контактами"
                shouldAutoSync && !hasPermissions -> "Клиент сохранен локально. Разрешения для контактов включаются в настройках"
                else -> "Клиент сохранен локально"
            }
        }
    }

    fun importContacts(selected: List<ContactCandidate>) {
        if (selected.isEmpty()) {
            message = "Выбери хотя бы один контакт"
            return
        }
        viewModelScope.launch {
            var added = 0
            var skipped = 0
            selected.forEach { contact ->
                if (clientExists(contact.name, contact.phone)) {
                    skipped++
                } else {
                    dao.insertClient(
                        contact.toClientEntity()
                    )
                    added++
                }
            }
            refreshCounts()
            message = "Добавлено: $added" + if (skipped > 0) ", пропущено дублей: $skipped" else ""
        }
    }

    fun loadContacts(context: Context) {
        viewModelScope.launch {
            contacts = withContext(Dispatchers.IO) { ContactsSync.readCandidates(context.applicationContext) }
            message = if (contacts.isEmpty()) "Контакты не найдены" else "Контактов: ${contacts.size}"
        }
    }

    fun syncContacts(context: Context) {
        val appContext = context.applicationContext
        viewModelScope.launch {
            val mode = syncModeFor(SYNC_RESOURCE_CONTACTS)
            if (mode == SYNC_MODE_OFF) {
                message = "Синхронизация контактов отключена"
                return@launch
            }
            if (!hasContactsPermissions(appContext)) {
                message = "Нужны разрешения контактов в настройках синхронизации"
                return@launch
            }
            val effectiveMode = if (mode == SYNC_MODE_MANUAL) SYNC_MODE_TWO_WAY else mode
            var pulled = 0
            var linked = 0
            var created = 0
            var skipped = 0
            dao.clientsOnce().forEach { client ->
                val result = withContext(Dispatchers.IO) {
                    when (effectiveMode) {
                        SYNC_MODE_LOCAL_TO_SYSTEM -> ContactsSync.pushClient(appContext, client)
                        SYNC_MODE_SYSTEM_TO_LOCAL -> ContactsSync.pullClient(appContext, client)
                        else -> ContactsSync.syncClient(appContext, client)
                    }
                }
                if (result.client != client) {
                    dao.updateClient(result.client)
                }
                when (result.action) {
                    "pulled" -> pulled++
                    "linked" -> linked++
                    "created" -> created++
                    else -> skipped++
                }
            }
            markSyncRun(SYNC_RESOURCE_CONTACTS)
            refreshCounts()
            message = "Синхронизация: обновлено $pulled, связано $linked, создано $created, пропущено $skipped"
        }
    }

    fun addAppointment(
        context: Context,
        clientId: Long,
        services: List<AppointmentServiceDraft>,
        startAt: String,
        notes: String,
        syncCalendar: Boolean
    ) {
        val selectedServices = services.filter { it.isSelected }
        val catalogServiceIds = selectedServices
            .map { it.serviceId }
            .filter { it > 0 }
        val hasDuplicateServices = catalogServiceIds
            .distinct()
            .size != catalogServiceIds.size
        if (clientId == 0L || selectedServices.isEmpty() || startAt.trim().isEmpty()) {
            message = "Укажи клиента, услугу и дату"
            return
        }
        if (hasDuplicateServices) {
            message = "Одна услуга не может повторяться в одной записи"
            return
        }
        viewModelScope.launch {
            val client = dao.clientById(clientId)
            val totalPriceCents = selectedServices.sumOf { it.priceCents }
            val totalDurationMinutes = selectedServices.sumOf { it.durationMinutes.coerceAtLeast(15) }
            val serviceTitle = selectedServices.joinToString(" + ") { it.serviceName.trim() }
            var appointment = AppointmentEntity(
                clientId = clientId,
                service = serviceTitle,
                startAt = startAt.trim(),
                durationMinutes = totalDurationMinutes.coerceAtLeast(15),
                price = if (totalPriceCents > 0) formatMoneyPlain(totalPriceCents) else "",
                priceCents = totalPriceCents,
                notes = notes.trim()
            )
            val calendarMode = syncModeFor(SYNC_RESOURCE_CALENDAR)
            val shouldAutoSync = shouldAutoSyncToSystem(calendarMode)
            val hasPermissions = hasCalendarPermissions(context.applicationContext)
            val preferredCalendarId = selectedCalendarId()
            val calendarEvent = if ((syncCalendar || shouldAutoSync) && hasPermissions && client != null) {
                withContext(Dispatchers.IO) {
                    CalendarSync.createForAppointment(context.applicationContext, appointment, client, preferredCalendarId)
                }
            } else {
                null
            }
            if (calendarEvent != null && client != null) {
                appointment = appointment.withCalendarEvent(calendarEvent, client, pullCalendarData = false)
            }
            val appointmentId = dao.insertAppointment(appointment)
            val storedAppointment = appointment.copy(id = appointmentId)
            appointment = storedAppointment
            dao.insertAppointmentServices(
                selectedServices.mapIndexed { index, serviceDraft ->
                    AppointmentServiceEntity(
                        appointmentId = appointmentId,
                        serviceId = serviceDraft.serviceId,
                        serviceName = serviceDraft.serviceName.trim(),
                        priceCents = serviceDraft.priceCents,
                        durationMinutes = serviceDraft.durationMinutes.coerceAtLeast(15),
                        sortOrder = index
                    )
                }
            )
            if (calendarEvent == null && !syncCalendar && shouldAutoSync && hasPermissions && client != null) {
                val result = withContext(Dispatchers.IO) {
                    CalendarSync.pushAppointment(context.applicationContext, storedAppointment, client, preferredCalendarId)
                }
                if (result.appointment != storedAppointment) {
                    dao.updateAppointment(result.appointment)
                }
                appointment = result.appointment
            }
            if (client != null) {
                AutomationEngine.enqueueForAppointmentEvent(
                    context = context.applicationContext,
                    dao = dao,
                    appointment = appointment,
                    client = client,
                    eventType = AUTOMATION_TRIGGER_APPOINTMENT_CREATED,
                    includeScheduledRules = true
                )
            }
            refreshCounts()
            message = when {
                calendarEvent != null || appointment.calendarEventId > 0 -> "Запись сохранена и добавлена в календарь, задачи добавлены в очередь"
                syncCalendar -> "Запись сохранена локально, календарь не обновлен"
                shouldAutoSync && !hasPermissions -> "Запись сохранена локально. Разрешения календаря включаются в настройках"
                else -> "Запись сохранена, задачи добавлены в очередь"
            }
        }
    }

    fun loadAppointmentServicesForEdit(
        appointment: AppointmentRow,
        onLoaded: (List<AppointmentServiceDraft>) -> Unit
    ) {
        viewModelScope.launch {
            val storedServices = dao.appointmentServicesFor(appointment.id)
            val drafts = if (storedServices.isNotEmpty()) {
                storedServices.mapIndexed { index, service ->
                    AppointmentServiceDraft(
                        localId = index,
                        serviceId = service.serviceId,
                        serviceName = service.serviceName,
                        price = formatMoneyPlain(service.priceCents),
                        durationMinutes = service.durationMinutes.coerceAtLeast(15)
                    )
                }
            } else {
                listOf(
                    AppointmentServiceDraft(
                        localId = 0,
                        serviceId = 0,
                        serviceName = appointment.service,
                        price = formatMoneyPlain(appointment.priceCents),
                        durationMinutes = appointment.durationMinutes.coerceAtLeast(15)
                    )
                )
            }.filter { it.serviceName.isNotBlank() }
                .ifEmpty { listOf(AppointmentServiceDraft(localId = 0)) }
            onLoaded(drafts)
        }
    }

    fun updateAppointment(
        context: Context,
        appointmentId: Long,
        clientId: Long,
        services: List<AppointmentServiceDraft>,
        startAt: String,
        notes: String
    ) {
        val selectedServices = services.filter { it.isSelected }
        val catalogServiceIds = selectedServices
            .map { it.serviceId }
            .filter { it > 0 }
        val hasDuplicateServices = catalogServiceIds.distinct().size != catalogServiceIds.size
        if (appointmentId == 0L || clientId == 0L || selectedServices.isEmpty() || startAt.trim().isEmpty()) {
            message = "Укажи клиента, услугу и дату"
            return
        }
        if (hasDuplicateServices) {
            message = "Одна услуга не может повторяться в одной записи"
            return
        }
        viewModelScope.launch {
            val current = dao.appointmentById(appointmentId)
            if (current == null) {
                message = "Запись не найдена"
                return@launch
            }
            val client = dao.clientById(clientId)
            val totalPriceCents = selectedServices.sumOf { it.priceCents }
            val totalDurationMinutes = selectedServices.sumOf { it.durationMinutes.coerceAtLeast(15) }
            val serviceTitle = selectedServices.joinToString(" + ") { it.serviceName.trim() }
            val paymentStatus = when {
                totalPriceCents <= 0L && current.paidAmountCents > 0L -> PAYMENT_STATUS_PAID
                current.paidAmountCents <= 0L -> PAYMENT_STATUS_UNPAID
                current.paidAmountCents >= totalPriceCents -> PAYMENT_STATUS_PAID
                else -> PAYMENT_STATUS_PARTIAL
            }
            var updatedAppointment = current.copy(
                clientId = clientId,
                service = serviceTitle,
                startAt = startAt.trim(),
                durationMinutes = totalDurationMinutes.coerceAtLeast(15),
                price = if (totalPriceCents > 0) formatMoneyPlain(totalPriceCents) else "",
                priceCents = totalPriceCents,
                paymentStatus = paymentStatus,
                notes = notes.trim(),
                updatedAt = now()
            )
            val calendarMode = syncModeFor(SYNC_RESOURCE_CALENDAR)
            val shouldAutoSync = shouldAutoSyncToSystem(calendarMode)
            val hasPermissions = hasCalendarPermissions(context.applicationContext)
            val preferredCalendarId = selectedCalendarId()
            var calendarMessage = ""
            if (shouldAutoSync && client != null) {
                if (hasPermissions) {
                    val result = withContext(Dispatchers.IO) {
                        CalendarSync.pushAppointment(context.applicationContext, updatedAppointment, client, preferredCalendarId)
                    }
                    updatedAppointment = result.appointment
                    calendarMessage = if (updatedAppointment.calendarEventId > 0) " Календарь обновлен." else ""
                } else {
                    calendarMessage = " Календарь не обновлен: нет разрешений."
                }
            }
            dao.updateAppointment(updatedAppointment)
            dao.clearAppointmentServicesFor(appointmentId)
            dao.insertAppointmentServices(
                selectedServices.mapIndexed { index, serviceDraft ->
                    AppointmentServiceEntity(
                        appointmentId = appointmentId,
                        serviceId = serviceDraft.serviceId,
                        serviceName = serviceDraft.serviceName.trim(),
                        priceCents = serviceDraft.priceCents,
                        durationMinutes = serviceDraft.durationMinutes.coerceAtLeast(15),
                        sortOrder = index
                    )
                }
            )
            dao.clearOutboxForAppointmentByStatus(
                appointmentId = appointmentId,
                statuses = listOf(STATUS_QUEUED, STATUS_NEEDS_CONFIG, STATUS_WAITING_PERMISSION, STATUS_FAILED)
            )
            if (updatedAppointment.status != APPOINTMENT_STATUS_CANCELLED && client != null) {
                AutomationEngine.enqueueForAppointmentEvent(
                    context = context.applicationContext,
                    dao = dao,
                    appointment = updatedAppointment,
                    client = client,
                    eventType = if (current.startAt != updatedAppointment.startAt) {
                        AUTOMATION_TRIGGER_APPOINTMENT_RESCHEDULED
                    } else {
                        AUTOMATION_TRIGGER_NONE
                    },
                    includeScheduledRules = true
                )
            }
            refreshCounts()
            message = "Запись обновлена.$calendarMessage"
        }
    }

    fun cancelAppointment(context: Context, appointmentId: Long) {
        viewModelScope.launch {
            val current = dao.appointmentById(appointmentId)
            if (current == null) {
                message = "Запись не найдена"
                return@launch
            }
            if (current.status == APPOINTMENT_STATUS_CANCELLED) {
                message = "Запись уже отменена"
                return@launch
            }
            val client = dao.clientById(current.clientId)
            var canceled = current.copy(
                status = APPOINTMENT_STATUS_CANCELLED,
                updatedAt = now()
            )
            val calendarMode = syncModeFor(SYNC_RESOURCE_CALENDAR)
            val shouldAutoSync = shouldAutoSyncToSystem(calendarMode)
            val hasPermissions = hasCalendarPermissions(context.applicationContext)
            var calendarMessage = ""
            if (shouldAutoSync && client != null && canceled.calendarEventId > 0) {
                if (hasPermissions) {
                    val result = withContext(Dispatchers.IO) {
                        CalendarSync.pushAppointment(
                            context.applicationContext,
                            canceled,
                            client,
                            selectedCalendarId()
                        )
                    }
                    canceled = result.appointment
                    calendarMessage = " Календарь обновлен."
                } else {
                    calendarMessage = " Календарь не обновлен: нет разрешений."
                }
            }
            dao.updateAppointment(canceled)
            dao.clearOutboxForAppointmentByStatus(
                appointmentId = appointmentId,
                statuses = listOf(STATUS_QUEUED, STATUS_NEEDS_CONFIG, STATUS_WAITING_PERMISSION, STATUS_FAILED)
            )
            if (client != null) {
                AutomationEngine.enqueueForAppointmentEvent(
                    context = context.applicationContext,
                    dao = dao,
                    appointment = canceled,
                    client = client,
                    eventType = AUTOMATION_TRIGGER_APPOINTMENT_CANCELLED,
                    includeScheduledRules = false
                )
            }
            refreshCounts()
            message = "Запись отменена.$calendarMessage"
        }
    }

    fun updateAppointmentStatus(context: Context, appointmentId: Long, selectedStatus: String) {
        val targetStatus = if (selectedStatus == APPOINTMENT_STATUS_CANCELLED) {
            APPOINTMENT_STATUS_CANCELLED
        } else {
            APPOINTMENT_STATUS_PLANNED
        }
        viewModelScope.launch {
            val current = dao.appointmentById(appointmentId)
            if (current == null) {
                message = "Запись не найдена"
                return@launch
            }
            if (current.status == targetStatus) {
                message = "Статус уже выбран"
                return@launch
            }
            val client = dao.clientById(current.clientId)
            var updated = current.copy(status = targetStatus, updatedAt = now())
            val calendarMode = syncModeFor(SYNC_RESOURCE_CALENDAR)
            val shouldAutoSync = shouldAutoSyncToSystem(calendarMode)
            val hasPermissions = hasCalendarPermissions(context.applicationContext)
            var calendarMessage = ""
            val shouldPushCalendar = shouldAutoSync &&
                client != null &&
                (targetStatus != APPOINTMENT_STATUS_CANCELLED || updated.calendarEventId > 0)
            if (shouldPushCalendar) {
                if (hasPermissions) {
                    val result = withContext(Dispatchers.IO) {
                        CalendarSync.pushAppointment(
                            context.applicationContext,
                            updated,
                            client,
                            selectedCalendarId()
                        )
                    }
                    updated = result.appointment
                    calendarMessage = if (updated.calendarEventId > 0) " Календарь обновлен." else ""
                } else {
                    calendarMessage = " Календарь не обновлен: нет разрешений."
                }
            }
            dao.updateAppointment(updated)
            dao.clearOutboxForAppointmentByStatus(
                appointmentId = appointmentId,
                statuses = listOf(STATUS_QUEUED, STATUS_NEEDS_CONFIG, STATUS_WAITING_PERMISSION, STATUS_FAILED)
            )
            if (client != null) {
                AutomationEngine.enqueueForAppointmentEvent(
                    context = context.applicationContext,
                    dao = dao,
                    appointment = updated,
                    client = client,
                    eventType = if (targetStatus == APPOINTMENT_STATUS_CANCELLED) {
                        AUTOMATION_TRIGGER_APPOINTMENT_CANCELLED
                    } else {
                        AUTOMATION_TRIGGER_APPOINTMENT_REACTIVATED
                    },
                    includeScheduledRules = targetStatus != APPOINTMENT_STATUS_CANCELLED &&
                        appointmentEntityCanHavePendingTasks(updated)
                )
            }
            refreshCounts()
            message = "Статус: ${appointmentEntityUiStatusLabel(updated)}.$calendarMessage"
        }
    }

    fun rescheduleAppointment(context: Context, appointmentId: Long, startAt: String) {
        if (startAt.trim().isEmpty()) {
            message = "Укажи дату и время"
            return
        }
        viewModelScope.launch {
            val current = dao.appointmentById(appointmentId)
            if (current == null) {
                message = "Запись не найдена"
                return@launch
            }
            if (current.status == APPOINTMENT_STATUS_CANCELLED) {
                message = "Отмененную запись нельзя переносить"
                return@launch
            }
            val client = dao.clientById(current.clientId)
            var updated = current.copy(
                startAt = startAt.trim(),
                updatedAt = now()
            )
            val calendarMode = syncModeFor(SYNC_RESOURCE_CALENDAR)
            val shouldAutoSync = shouldAutoSyncToSystem(calendarMode)
            val hasPermissions = hasCalendarPermissions(context.applicationContext)
            var calendarMessage = ""
            if (shouldAutoSync && client != null) {
                if (hasPermissions) {
                    val result = withContext(Dispatchers.IO) {
                        CalendarSync.pushAppointment(
                            context.applicationContext,
                            updated,
                            client,
                            selectedCalendarId()
                        )
                    }
                    updated = result.appointment
                    calendarMessage = if (updated.calendarEventId > 0) " Календарь обновлен." else ""
                } else {
                    calendarMessage = " Календарь не обновлен: нет разрешений."
                }
            }
            dao.updateAppointment(updated)
            dao.clearOutboxForAppointmentByStatus(
                appointmentId = appointmentId,
                statuses = listOf(STATUS_QUEUED, STATUS_NEEDS_CONFIG, STATUS_WAITING_PERMISSION, STATUS_FAILED)
            )
            if (client != null) {
                AutomationEngine.enqueueForAppointmentEvent(
                    context = context.applicationContext,
                    dao = dao,
                    appointment = updated,
                    client = client,
                    eventType = AUTOMATION_TRIGGER_APPOINTMENT_RESCHEDULED,
                    includeScheduledRules = true
                )
            }
            refreshCounts()
            message = "Запись перенесена.$calendarMessage"
        }
    }

    fun rescheduleAndReactivateAppointment(context: Context, appointmentId: Long, startAt: String) {
        if (startAt.trim().isEmpty()) {
            message = "Укажи дату и время"
            return
        }
        viewModelScope.launch {
            val current = dao.appointmentById(appointmentId)
            if (current == null) {
                message = "Запись не найдена"
                return@launch
            }
            val client = dao.clientById(current.clientId)
            var updated = current.copy(
                startAt = startAt.trim(),
                status = APPOINTMENT_STATUS_PLANNED,
                updatedAt = now()
            )
            val calendarMode = syncModeFor(SYNC_RESOURCE_CALENDAR)
            val shouldAutoSync = shouldAutoSyncToSystem(calendarMode)
            val hasPermissions = hasCalendarPermissions(context.applicationContext)
            var calendarMessage = ""
            if (shouldAutoSync && client != null) {
                if (hasPermissions) {
                    val result = withContext(Dispatchers.IO) {
                        CalendarSync.pushAppointment(
                            context.applicationContext,
                            updated,
                            client,
                            selectedCalendarId()
                        )
                    }
                    updated = result.appointment
                    calendarMessage = if (updated.calendarEventId > 0) " Календарь обновлен." else ""
                } else {
                    calendarMessage = " Календарь не обновлен: нет разрешений."
                }
            }
            dao.updateAppointment(updated)
            dao.clearOutboxForAppointmentByStatus(
                appointmentId = appointmentId,
                statuses = listOf(STATUS_QUEUED, STATUS_NEEDS_CONFIG, STATUS_WAITING_PERMISSION, STATUS_FAILED)
            )
            if (client != null) {
                AutomationEngine.enqueueForAppointmentEvent(
                    context = context.applicationContext,
                    dao = dao,
                    appointment = updated,
                    client = client,
                    eventType = AUTOMATION_TRIGGER_APPOINTMENT_REACTIVATED,
                    includeScheduledRules = appointmentEntityCanHavePendingTasks(updated)
                )
            }
            refreshCounts()
            message = "Запись возобновлена и перенесена.$calendarMessage"
        }
    }

    fun deleteAppointment(context: Context, appointmentId: Long) {
        viewModelScope.launch {
            val current = dao.appointmentById(appointmentId)
            if (current == null) {
                message = "Запись не найдена"
                return@launch
            }
            val calendarDeleted = if (current.calendarEventId > 0) {
                withContext(Dispatchers.IO) {
                    CalendarSync.deleteAppointmentEvent(context.applicationContext, current)
                }
            } else {
                false
            }
            dao.clearAppointmentServicesFor(appointmentId)
            dao.clearOutboxForAppointment(appointmentId)
            dao.deleteAppointmentById(appointmentId)
            refreshCounts()
            message = when {
                current.calendarEventId > 0 && calendarDeleted -> "Запись удалена, событие календаря удалено"
                current.calendarEventId > 0 -> "Запись удалена локально. Событие календаря не удалено"
                else -> "Запись удалена"
            }
        }
    }

    fun saveService(
        name: String,
        price: String,
        durationMinutes: Int,
        serviceId: Long? = null,
        onSaved: (ServiceEntity) -> Unit = {}
    ) {
        val cleanName = name.trim()
        if (cleanName.isEmpty()) {
            message = "Укажи название услуги"
            return
        }
        viewModelScope.launch {
            val current = serviceId?.let { dao.serviceById(it) }
            val service = (current ?: ServiceEntity(name = cleanName)).copy(
                name = cleanName,
                priceCents = parseMoneyToCents(price),
                durationMinutes = durationMinutes.coerceAtLeast(15),
                isActive = true,
                updatedAt = now()
            )
            val savedId = if (service.id == 0L) dao.insertService(service) else {
                dao.updateService(service)
                service.id
            }
            val saved = service.copy(id = savedId)
            message = "Услуга сохранена"
            onSaved(saved)
        }
    }

    fun archiveService(service: ServiceEntity) {
        viewModelScope.launch {
            dao.updateService(service.copy(isActive = false, updatedAt = now()))
            message = "Услуга скрыта из каталога"
        }
    }

    fun addFinanceTransaction(
        type: String,
        amount: String,
        title: String,
        category: String,
        date: String,
        paymentMethod: String,
        notes: String
    ) {
        val normalizedType = if (type == FINANCE_TYPE_EXPENSE) FINANCE_TYPE_EXPENSE else FINANCE_TYPE_INCOME
        val amountCents = parseMoneyToCents(amount)
        if (amountCents <= 0L) {
            message = "Укажи сумму"
            return
        }
        if (title.trim().isBlank()) {
            message = "Укажи название операции"
            return
        }
        viewModelScope.launch {
            dao.insertFinanceTransaction(
                FinanceTransactionEntity(
                    type = normalizedType,
                    amountCents = amountCents,
                    title = title.trim(),
                    category = category.trim(),
                    date = date.ifBlank { financeDateString(LocalDate.now()) },
                    paymentMethod = paymentMethod.trim(),
                    notes = notes.trim()
                )
            )
            message = if (normalizedType == FINANCE_TYPE_EXPENSE) "Расход добавлен" else "Доход добавлен"
        }
    }

    fun recordAppointmentPayment(
        appointmentId: Long,
        amount: String,
        paymentMethod: String,
        paidAt: String,
        payInFull: Boolean
    ) {
        viewModelScope.launch {
            val appointment = dao.appointmentById(appointmentId)
            if (appointment == null) {
                message = "Запись не найдена"
                return@launch
            }
            if (appointment.priceCents <= 0L) {
                message = "У записи нет суммы"
                return@launch
            }
            val requestedAmount = if (payInFull) {
                appointment.priceCents - appointment.paidAmountCents
            } else {
                parseMoneyToCents(amount)
            }
            if (requestedAmount <= 0L) {
                message = "Укажи сумму оплаты"
                return@launch
            }
            val newPaid = (appointment.paidAmountCents + requestedAmount).coerceAtMost(appointment.priceCents)
            val status = when {
                newPaid <= 0L -> PAYMENT_STATUS_UNPAID
                newPaid >= appointment.priceCents -> PAYMENT_STATUS_PAID
                else -> PAYMENT_STATUS_PARTIAL
            }
            val updatedAppointment = appointment.copy(
                paidAmountCents = newPaid,
                paymentStatus = status,
                paymentMethod = paymentMethod.trim(),
                paidAt = paidAt.ifBlank { now() },
                updatedAt = now()
            )
            dao.updateAppointment(updatedAppointment)
            dao.clientById(updatedAppointment.clientId)?.let { client ->
                AutomationEngine.enqueueForAppointmentEvent(
                    context = getApplication<Application>().applicationContext,
                    dao = dao,
                    appointment = updatedAppointment,
                    client = client,
                    eventType = AUTOMATION_TRIGGER_PAYMENT_RECEIVED,
                    includeScheduledRules = false
                )
            }
            message = if (status == PAYMENT_STATUS_PAID) "Запись оплачена" else "Частичная оплата сохранена"
        }
    }

    fun syncCalendar(context: Context) {
        val appContext = context.applicationContext
        viewModelScope.launch {
            val mode = syncModeFor(SYNC_RESOURCE_CALENDAR)
            if (mode == SYNC_MODE_OFF) {
                message = "Синхронизация календаря отключена"
                return@launch
            }
            if (!hasCalendarPermissions(appContext)) {
                message = "Нужны разрешения календаря в настройках синхронизации"
                return@launch
            }
            val effectiveMode = if (mode == SYNC_MODE_MANUAL) SYNC_MODE_TWO_WAY else mode
            val preferredCalendarId = selectedCalendarId()
            var pulled = 0
            var created = 0
            var updated = 0
            var skipped = 0
            dao.appointmentsOnce().forEach { appointment ->
                val client = dao.clientById(appointment.clientId)
                if (client == null) {
                    skipped++
                    return@forEach
                }
                val result = withContext(Dispatchers.IO) {
                    when (effectiveMode) {
                        SYNC_MODE_LOCAL_TO_SYSTEM -> CalendarSync.pushAppointment(appContext, appointment, client, preferredCalendarId)
                        SYNC_MODE_SYSTEM_TO_LOCAL -> CalendarSync.pullAppointment(appContext, appointment, client)
                        else -> CalendarSync.syncAppointment(appContext, appointment, client, preferredCalendarId)
                    }
                }
                if (result.appointment != appointment) {
                    dao.updateAppointment(result.appointment)
                }
                when (result.action) {
                    "pulled" -> pulled++
                    "created" -> created++
                    "updated" -> updated++
                    else -> skipped++
                }
            }
            markSyncRun(SYNC_RESOURCE_CALENDAR)
            refreshCounts()
            message = "Календарь: подтянуто $pulled, обновлено $updated, создано $created, пропущено $skipped"
        }
    }

    fun saveRule(
        name: String,
        channel: String,
        triggerType: String,
        offsetMinutes: Int,
        retentionDays: Int,
        serviceFilterIds: Set<Long>,
        condition: String,
        askBeforeRun: Boolean,
        messageTemplate: String,
        ruleId: Long? = null
    ) {
        if (name.trim().isEmpty() || messageTemplate.trim().isEmpty()) {
            message = "Укажи название и текст сценария"
            return
        }
        val normalizedTrigger = triggerType.takeIf { it in automationTriggerTypes } ?: AUTOMATION_TRIGGER_NONE
        val cleanChannel = channel.takeIf { it in channelOptions } ?: CHANNEL_SMS
        val triggerName = automationTriggerLabel(normalizedTrigger)
        viewModelScope.launch {
            val current = ruleId?.let { dao.ruleById(it) }
            val rule = (current ?: AutomationRuleEntity(
                name = name.trim(),
                channel = cleanChannel,
                triggerName = triggerName,
                message = messageTemplate.trim()
            )).copy(
                    name = name.trim(),
                    channel = cleanChannel,
                    triggerName = triggerName,
                    message = messageTemplate.trim(),
                    triggerType = normalizedTrigger,
                    offsetMinutes = offsetMinutes.coerceAtLeast(0),
                    retentionDays = retentionDays.coerceAtLeast(0),
                    serviceFilterIds = serviceFilterIds.filter { it > 0L }.sorted().joinToString(","),
                    condition = condition.trim(),
                    askBeforeRun = askBeforeRun,
                    updatedAt = now()
            )
            if (rule.id == 0L) {
                dao.insertRule(rule)
            } else {
                dao.updateRule(rule)
                dao.clearOutboxForRuleByStatus(rule.id, listOf(STATUS_QUEUED, STATUS_NEEDS_CONFIG, STATUS_WAITING_PERMISSION, STATUS_FAILED))
            }
            refreshCounts()
            message = "Сценарий сохранен"
        }
    }

    fun toggleRule(rule: AutomationRuleEntity, enabled: Boolean) {
        viewModelScope.launch {
            dao.setRuleEnabled(rule.id, enabled, now())
            if (!enabled) {
                dao.clearOutboxForRuleByStatus(rule.id, listOf(STATUS_QUEUED, STATUS_NEEDS_CONFIG, STATUS_WAITING_PERMISSION, STATUS_FAILED))
            }
            refreshCounts()
            message = if (enabled) "Сценарий включен" else "Сценарий выключен"
        }
    }

    fun deleteRule(rule: AutomationRuleEntity) {
        viewModelScope.launch {
            dao.clearOutboxForRuleByStatus(rule.id, listOf(STATUS_QUEUED, STATUS_NEEDS_CONFIG, STATUS_WAITING_PERMISSION, STATUS_FAILED))
            dao.deleteRuleById(rule.id)
            refreshCounts()
            message = "Сценарий удален"
        }
    }

    fun runRetentionAutomationNow(context: Context) {
        viewModelScope.launch {
            val created = AutomationEngine.enqueueRetentionCandidates(context.applicationContext, dao)
            refreshCounts()
            message = "Возврат клиентов: задач добавлено $created"
        }
    }

    fun sendNow(id: Long) {
        val request = OneTimeWorkRequestBuilder<AutomationWorker>()
            .setInputData(workDataOf("outboxId" to id))
            .build()
        WorkManager.getInstance(getApplication()).enqueue(request)
        message = "Отправка поставлена в WorkManager"
    }

    fun skipOutbox(id: Long) {
        viewModelScope.launch {
            dao.updateOutboxStatus(id, STATUS_CANCELLED, "Отправка отменена пользователем")
            AutomationNotifications.cancel(getApplication(), id)
            refreshCounts()
            message = "Отправка пропущена"
        }
    }

    fun sendAllQueued() {
        val request = OneTimeWorkRequestBuilder<AutomationWorker>().build()
        WorkManager.getInstance(getApplication()).enqueue(request)
        message = "Очередь отправки запущена"
    }

    fun exportBackup(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val json = withContext(Dispatchers.IO) { BackupJson.export(db) }
                withContext(Dispatchers.IO) {
                    context.contentResolver.openOutputStream(uri)?.use { stream ->
                        OutputStreamWriter(stream, Charsets.UTF_8).use { it.write(json.toString(2)) }
                    } ?: error("Не удалось открыть файл")
                }
                message = "Backup сохранен"
            } catch (e: Exception) {
                message = "Ошибка backup: ${e.message}"
            }
        }
    }

    fun importBackup(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val json = withContext(Dispatchers.IO) {
                    context.contentResolver.openInputStream(uri)?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }
                        ?: error("Не удалось открыть файл")
                }
                withContext(Dispatchers.IO) { BackupJson.import(db, JSONObject(json)) }
                refreshCounts()
                message = "Backup импортирован"
            } catch (e: Exception) {
                message = "Ошибка импорта: ${e.message}"
            }
        }
    }

    fun saveAutoBackupFolder(context: Context, uri: Uri) {
        val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        runCatching { context.contentResolver.takePersistableUriPermission(uri, flags) }
        autoBackupState = AutoBackupStore.saveFolder(getApplication(), uri)
        scheduleAutoBackupWorker(getApplication())
        message = "Папка автобэкапов сохранена"
    }

    fun saveAutoBackupMode(mode: String) {
        autoBackupState = AutoBackupStore.saveMode(getApplication(), mode)
        scheduleAutoBackupWorker(getApplication())
        message = when (autoBackupState.mode) {
            AUTO_BACKUP_MODE_DAILY -> "Автобэкап: ежедневно"
            AUTO_BACKUP_MODE_WEEKLY -> "Автобэкап: еженедельно"
            else -> "Автобэкап отключен"
        }
    }

    fun runAutoBackupNow(context: Context) {
        viewModelScope.launch {
            try {
                val fileName = withContext(Dispatchers.IO) {
                    AutoBackupJson.create(context.applicationContext, db, requireEnabled = false)
                }
                autoBackupState = AutoBackupStore.load(getApplication())
                message = if (fileName == null) {
                    "Выбери папку и включи автобэкап"
                } else {
                    "Бэкап создан: $fileName"
                }
            } catch (e: Exception) {
                message = "Не удалось создать автобэкап: ${e.message ?: "ошибка доступа"}"
            }
        }
    }

    fun saveSyncMode(resource: String, mode: String) {
        if (resource !in listOf(SYNC_RESOURCE_CONTACTS, SYNC_RESOURCE_CALENDAR) || mode !in syncModeOptions) return
        viewModelScope.launch {
            val current = dao.syncSettingsFor(resource)
            dao.saveSyncSettings(
                (current ?: defaultSyncSetting(resource)).copy(
                    mode = mode,
                    updatedAt = now()
                )
            )
            message = "${syncResourceLabel(resource)}: ${syncModeLabel(resource, mode)}"
        }
    }

    fun saveCalendarTarget(calendarId: Long?) {
        viewModelScope.launch {
            val current = dao.syncSettingsFor(SYNC_RESOURCE_CALENDAR) ?: defaultSyncSetting(SYNC_RESOURCE_CALENDAR)
            dao.saveSyncSettings(
                current.copy(
                    selectedSystemId = calendarId?.toString().orEmpty(),
                    updatedAt = now()
                )
            )
            message = if (calendarId == null) {
                "Календарь: авто-выбор локального календаря"
            } else {
                "Календарь для записей сохранен"
            }
        }
    }

    fun refreshCounts() {
        viewModelScope.launch {
            counts = Counts(
                clients = dao.countClients(),
                appointments = dao.countAppointments(),
                rules = dao.countRules(),
                outbox = dao.countOutbox()
            )
        }
    }

    private suspend fun seedDefaults() {
        ensureSyncDefaults()
        if (dao.countRules() == 0) {
            dao.insertRule(
                AutomationRuleEntity(
                    name = "SMS напоминание за день",
                    channel = CHANNEL_SMS,
                    triggerName = "За 24 часа до записи",
                    message = "Здравствуйте, {client.name}! Напоминаем о записи: {appointment.service}, {appointment.date} в {appointment.time}.",
                    triggerType = AUTOMATION_TRIGGER_BEFORE_APPOINTMENT,
                    offsetMinutes = 1440
                )
            )
            dao.insertRule(
                AutomationRuleEntity(
                    name = "SMS напоминание за 2 часа",
                    channel = CHANNEL_SMS,
                    triggerName = "За 2 часа до записи",
                    message = "{client.name}, напоминаем: {appointment.service}, {appointment.date} в {appointment.time}.",
                    triggerType = AUTOMATION_TRIGGER_BEFORE_APPOINTMENT,
                    offsetMinutes = 120
                )
            )
            dao.insertRule(
                AutomationRuleEntity(
                    name = "SMS сообщение после визита",
                    channel = CHANNEL_SMS,
                    triggerName = "После визита",
                    message = "{client.name}, спасибо за визит! Буду рада отзыву и новой записи.",
                    triggerType = AUTOMATION_TRIGGER_AFTER_APPOINTMENT,
                    offsetMinutes = 0
                )
            )
        }
        if (dao.settingsFor(CHANNEL_SMS) == null) {
            dao.saveSettings(ConnectorSettingsEntity(channel = CHANNEL_SMS, enabled = true))
        }
    }

    private suspend fun normalizeSmsOnlyAutomation() {
        dao.rulesOnce().forEach { rule ->
            if (rule.channel != CHANNEL_SMS) {
                dao.updateRule(rule.copy(channel = CHANNEL_SMS, updatedAt = now()))
            }
        }
        dao.clearOutboxExceptChannel(CHANNEL_SMS)
        dao.clearSettingsExceptChannel(CHANNEL_SMS)
    }

    private suspend fun backfillAppointmentPriceCents() {
        dao.appointmentsOnce().forEach { appointment ->
            if (appointment.priceCents == 0L && appointment.price.isNotBlank()) {
                val parsed = parseMoneyToCents(appointment.price)
                if (parsed > 0L) {
                    dao.updateAppointment(
                        appointment.copy(
                            priceCents = parsed,
                            price = formatMoneyPlain(parsed)
                        )
                    )
                }
            }
        }
    }

    private suspend fun ensureSyncDefaults() {
        if (dao.syncSettingsFor(SYNC_RESOURCE_CONTACTS) == null) {
            dao.saveSyncSettings(defaultSyncSetting(SYNC_RESOURCE_CONTACTS))
        }
        if (dao.syncSettingsFor(SYNC_RESOURCE_CALENDAR) == null) {
            dao.saveSyncSettings(defaultSyncSetting(SYNC_RESOURCE_CALENDAR))
        }
    }

    private suspend fun refreshContactLinksFromPhonebook() {
        val appContext = getApplication<Application>().applicationContext
        val mode = syncModeFor(SYNC_RESOURCE_CONTACTS)
        if (mode == SYNC_MODE_OFF || mode == SYNC_MODE_MANUAL) return
        if (appContext.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) return
        dao.clientsOnce().forEach { client ->
            val refreshed = withContext(Dispatchers.IO) {
                ContactsSync.refreshClientLink(appContext, client)
            }
            if (refreshed != null && refreshed != client) {
                dao.updateClient(refreshed)
            }
        }
    }

    private suspend fun clientExists(name: String, phone: String): Boolean {
        val key = normalizePhone(phone)
        return dao.clientsOnce().any { client ->
            key.isNotEmpty() && key == client.phoneKey || key.isEmpty() && client.name.equals(name, ignoreCase = true)
        }
    }

    private suspend fun syncModeFor(resource: String): String =
        dao.syncSettingsFor(resource)?.mode ?: SYNC_MODE_TWO_WAY

    private suspend fun selectedCalendarId(): Long? =
        dao.syncSettingsFor(SYNC_RESOURCE_CALENDAR)?.selectedSystemId?.toLongOrNull()

    private suspend fun markSyncRun(resource: String) {
        val current = dao.syncSettingsFor(resource) ?: defaultSyncSetting(resource)
        dao.saveSyncSettings(current.copy(lastRunAt = now()))
    }

    private fun shouldAutoSyncToSystem(mode: String): Boolean =
        mode == SYNC_MODE_LOCAL_TO_SYSTEM || mode == SYNC_MODE_TWO_WAY

    private fun hasContactsPermissions(context: Context): Boolean =
        context.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
            context.checkSelfPermission(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED

    private fun hasCalendarPermissions(context: Context): Boolean =
        context.checkSelfPermission(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED &&
            context.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
}

class AutomationWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val db = AppDatabase.get(applicationContext)
        val id = inputData.getLong("outboxId", 0L)
        val confirmId = inputData.getLong("confirmOutboxId", 0L)
        val retentionScan = inputData.getBoolean("retentionScan", false)
        return try {
            if (retentionScan) {
                AutomationEngine.enqueueRetentionCandidates(applicationContext, db.dao())
            } else if (confirmId > 0L) {
                AutomationNotifications.showConfirmation(applicationContext, db, confirmId)
            } else if (id > 0L) {
                AutomationSender.sendOne(applicationContext, db, id)
            } else {
                db.dao().dueOutboxIdsByStatus(
                    statuses = listOf(STATUS_QUEUED, STATUS_FAILED),
                    now = LocalDateTime.now().format(appointmentStorageFormatter)
                ).forEach {
                    AutomationSender.sendOne(applicationContext, db, it)
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

object AutomationSender {
    suspend fun sendOne(context: Context, db: AppDatabase, outboxId: Long) {
        val dao = db.dao()
        val data = dao.sendData(outboxId) ?: return
        val skipReason = AutomationEngine.shouldSkipBeforeSend(dao, data.outbox)
        if (skipReason.isNotBlank()) {
            dao.updateOutboxStatus(outboxId, STATUS_FAILED, skipReason)
            return
        }
        when (data.outbox.channel) {
            CHANNEL_SMS -> sendSms(context, dao, data)
            else -> dao.updateOutboxStatus(outboxId, STATUS_FAILED, "В этой версии оставлен только SMS")
        }
    }

    private suspend fun sendSms(context: Context, dao: AppDao, data: OutboxSendData) {
        if (context.checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            dao.updateOutboxStatus(data.outbox.id, STATUS_WAITING_PERMISSION, "Нужно разрешение SEND_SMS")
            return
        }
        val phone = data.clientPhone.orEmpty()
        if (phone.isBlank()) {
            dao.updateOutboxStatus(data.outbox.id, STATUS_FAILED, "У клиента нет телефона")
            return
        }
        withContext(Dispatchers.IO) {
            SmsManager.getDefault().sendTextMessage(phone, null, data.outbox.payload, null, null)
        }
        dao.updateOutboxStatus(data.outbox.id, STATUS_SENT, sentAt = now())
    }
}

object BackupJson {
    suspend fun export(db: AppDatabase): JSONObject {
        val dao = db.dao()
        return JSONObject()
            .put("app", "Offline Beauty CRM")
            .put("schema_version", 14)
            .put("generated_at", now())
            .put("clients", JSONArray().also { array ->
                dao.clientsOnce().forEach { array.put(JSONObject(it.toMap())) }
            })
            .put("appointments", JSONArray().also { array ->
                dao.appointmentsOnce().forEach { array.put(JSONObject(it.toMap())) }
            })
            .put("services", JSONArray().also { array ->
                dao.servicesOnce().forEach { array.put(JSONObject(it.toMap())) }
            })
            .put("appointment_services", JSONArray().also { array ->
                dao.appointmentServicesOnce().forEach { array.put(JSONObject(it.toMap())) }
            })
            .put("automation_rules", JSONArray().also { array ->
                dao.rulesOnce().forEach { array.put(JSONObject(it.toMap())) }
            })
            .put("outbox", JSONArray().also { array ->
                dao.outboxOnce().forEach { array.put(JSONObject(it.toMap())) }
            })
            .put("connector_settings", JSONArray().also { array ->
                dao.settingsOnce().forEach { array.put(JSONObject(it.toMap())) }
            })
            .put("sync_settings", JSONArray().also { array ->
                dao.syncSettingsOnce().forEach { array.put(JSONObject(it.toMap())) }
            })
            .put("finance_transactions", JSONArray().also { array ->
                dao.financeTransactionsOnce().forEach { array.put(JSONObject(it.toMap())) }
            })
    }

    suspend fun import(db: AppDatabase, json: JSONObject) {
        db.withTransaction {
            val dao = db.dao()
            dao.clearOutbox()
            dao.clearFinanceTransactions()
            dao.clearRules()
            dao.clearAppointmentServices()
            dao.clearAppointments()
            dao.clearServices()
            dao.clearClients()
            dao.clearSettings()
            dao.clearSyncSettings()
            val importedAppointments = json.optJSONArray("appointments").toAppointments()
            dao.insertClients(json.optJSONArray("clients").toClients())
            dao.insertAppointments(importedAppointments)
            dao.insertServices(json.optJSONArray("services").toServices())
            dao.insertAppointmentServices(json.optJSONArray("appointment_services").toAppointmentServices(importedAppointments))
            dao.insertRules(json.optJSONArray("automation_rules").toRules())
            dao.insertOutbox(json.optJSONArray("outbox").toOutbox())
            dao.saveSettings(json.optJSONArray("connector_settings").toSettings())
            dao.saveSyncSettings(json.optJSONArray("sync_settings").toSyncSettings())
            dao.insertFinanceTransactions(json.optJSONArray("finance_transactions").toFinanceTransactions())
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun OfflineBeautyApp(viewModel: AppViewModel = viewModel()) {
    val context = LocalContext.current
    val view = LocalView.current
    val darkTheme = isSystemInDarkTheme()
    var dynamicColorRefreshKey by remember { mutableStateOf(0) }
    val colorScheme = remember(context, darkTheme, dynamicColorRefreshKey) {
        appColorScheme(context = context, darkTheme = darkTheme)
    }
    val clients by viewModel.clients.collectAsState(initial = emptyList())
    val appointments by viewModel.appointments.collectAsState(initial = emptyList())
    val services by viewModel.services.collectAsState(initial = emptyList())
    val rules by viewModel.rules.collectAsState(initial = emptyList())
    val outbox by viewModel.outbox.collectAsState(initial = emptyList())
    val syncSettings by viewModel.syncSettings.collectAsState(initial = emptyList())
    val financeTransactions by viewModel.financeTransactions.collectAsState(initial = emptyList())
    var screen by rememberSaveable { mutableStateOf(Screen.Clients) }
    var screenHistory by remember { mutableStateOf<List<Screen>>(emptyList()) }
    var clientSubScreen by rememberSaveable { mutableStateOf(ClientSubScreen.List) }
    var settingsSubScreen by rememberSaveable { mutableStateOf(SettingsSubScreen.Menu) }
    var appointmentEditorOpen by remember { mutableStateOf(false) }
    var financeEditorOpen by remember { mutableStateOf(false) }
    var settingsEditorOpen by remember { mutableStateOf(false) }
    var requestedAppointmentId by rememberSaveable { mutableStateOf<Long?>(null) }
    var requestedAppointmentEditId by rememberSaveable { mutableStateOf<Long?>(null) }
    var requestedAppointmentClientId by rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedClientId by rememberSaveable { mutableStateOf<Long?>(null) }
    val showMainChrome = (screen != Screen.Appointments || !appointmentEditorOpen) &&
        (screen != Screen.Finance || !financeEditorOpen) &&
        (screen != Screen.Settings || !settingsEditorOpen) &&
        (screen != Screen.Clients ||
            clientSubScreen == ClientSubScreen.List ||
            clientSubScreen == ClientSubScreen.Import)
    val showTopChrome = showMainChrome && screen != Screen.Appointments
    val currentTopBarTitle = when {
        screen == Screen.Settings -> settingsSubScreen.label
        else -> screen.label
    }
    val showTopBack = screen == Screen.Settings && settingsSubScreen != SettingsSubScreen.Menu
    val shouldHandleBack = screen != Screen.Clients ||
        clientSubScreen != ClientSubScreen.List ||
        settingsSubScreen != SettingsSubScreen.Menu ||
        screenHistory.isNotEmpty()
    var confirmationClock by remember { mutableStateOf(System.currentTimeMillis()) }
    val pendingConfirmation = remember(outbox, confirmationClock) {
        outbox.firstOrNull { outboxNeedsConfirmation(it, confirmationClock) }
    }

    DisposableEffect(context) {
        val lifecycleOwner = context as? LifecycleOwner
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                dynamicColorRefreshKey++
                confirmationClock = System.currentTimeMillis()
            }
        }
        lifecycleOwner?.lifecycle?.addObserver(observer)
        onDispose {
            lifecycleOwner?.lifecycle?.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(15000)
            confirmationClock = System.currentTimeMillis()
        }
    }

    fun navigateTo(target: Screen) {
        if (target == screen &&
            clientSubScreen == ClientSubScreen.List &&
            (target != Screen.Settings || settingsSubScreen == SettingsSubScreen.Menu)
        ) return
        screenHistory = if (target == screen) screenHistory else screenHistory + screen
        screen = target
        clientSubScreen = ClientSubScreen.List
        settingsSubScreen = SettingsSubScreen.Menu
        appointmentEditorOpen = false
        financeEditorOpen = false
        settingsEditorOpen = false
        selectedClientId = null
    }

    fun navigateBack() {
        if (screen == Screen.Clients && clientSubScreen != ClientSubScreen.List) {
            clientSubScreen = ClientSubScreen.List
            selectedClientId = null
            return
        }
        if (screen == Screen.Settings && settingsSubScreen != SettingsSubScreen.Menu) {
            settingsSubScreen = SettingsSubScreen.Menu
            return
        }
        if (screenHistory.isNotEmpty()) {
            val previous = screenHistory.last()
            screenHistory = screenHistory.dropLast(1)
            screen = previous
            clientSubScreen = ClientSubScreen.List
            settingsSubScreen = SettingsSubScreen.Menu
            appointmentEditorOpen = false
            financeEditorOpen = false
            settingsEditorOpen = false
            selectedClientId = null
            return
        }
        if (screen != Screen.Clients) {
            screen = Screen.Clients
            clientSubScreen = ClientSubScreen.List
            settingsSubScreen = SettingsSubScreen.Menu
            appointmentEditorOpen = false
            financeEditorOpen = false
            settingsEditorOpen = false
            selectedClientId = null
        }
    }

    BackHandler(enabled = shouldHandleBack) {
        navigateBack()
    }

    LaunchedEffect(viewModel.message) {
        val shownMessage = viewModel.message
        if (shownMessage.isNotBlank()) {
            delay(4000)
            viewModel.clearMessage(shownMessage)
        }
    }

    SideEffect {
        if (!view.isInEditMode) {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            window.statusBarColor = colorScheme.surface.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(
            topBar = {
                if (showTopChrome) {
                    TopAppBar(
                        title = { Text(currentTopBarTitle, fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            if (showTopBack) {
                                IconButton(onClick = { settingsSubScreen = SettingsSubScreen.Menu }) {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            },
            bottomBar = {
                if (showMainChrome) {
                    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                        Screen.values().forEach { item ->
                            NavigationBarItem(
                                selected = item == screen,
                                onClick = { navigateTo(item) },
                                icon = { BottomNavIcon(item) },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                MessageBar(message = viewModel.message)
                Box(modifier = Modifier.fillMaxSize()) {
                    when (screen) {
                        Screen.Clients -> ClientsScreen(
                            viewModel = viewModel,
                            clients = clients,
                            appointments = appointments,
                            financeTransactions = financeTransactions,
                            subScreen = clientSubScreen,
                            selectedClientId = selectedClientId,
                            onOpenClient = {
                                selectedClientId = it
                                clientSubScreen = ClientSubScreen.Detail
                            },
                            onSubScreenChange = { clientSubScreen = it },
                            onOpenAppointment = { appointmentId ->
                                screenHistory = screenHistory + screen
                                screen = Screen.Appointments
                                clientSubScreen = ClientSubScreen.List
                                settingsSubScreen = SettingsSubScreen.Menu
                                selectedClientId = null
                                appointmentEditorOpen = true
                                requestedAppointmentEditId = appointmentId
                            },
                            onCreateAppointment = { clientId ->
                                screenHistory = screenHistory + screen
                                screen = Screen.Appointments
                                clientSubScreen = ClientSubScreen.List
                                settingsSubScreen = SettingsSubScreen.Menu
                                selectedClientId = null
                                appointmentEditorOpen = true
                                requestedAppointmentClientId = clientId
                            }
                        )
                        Screen.Appointments -> AppointmentsScreen(
                            viewModel = viewModel,
                            clients = clients,
                            appointments = appointments,
                            services = services,
                            financeTransactions = financeTransactions,
                            requestedAppointmentId = requestedAppointmentId,
                            onRequestedAppointmentConsumed = { requestedAppointmentId = null },
                            requestedAppointmentEditId = requestedAppointmentEditId,
                            onRequestedAppointmentEditConsumed = { requestedAppointmentEditId = null },
                            requestedAppointmentClientId = requestedAppointmentClientId,
                            onRequestedAppointmentClientConsumed = { requestedAppointmentClientId = null },
                            onEditorOpenChange = { appointmentEditorOpen = it }
                        )
                        Screen.Finance -> FinanceScreen(
                            viewModel = viewModel,
                            appointments = appointments,
                            transactions = financeTransactions,
                            onEditorOpenChange = { financeEditorOpen = it }
                        )
                        Screen.Settings -> SettingsScreen(
                            viewModel = viewModel,
                            services = services,
                            syncSettings = syncSettings,
                            rules = rules,
                            outbox = outbox,
                            subScreen = settingsSubScreen,
                            onEditorOpenChange = { settingsEditorOpen = it },
                            onSubScreenChange = { settingsSubScreen = it }
                        )
                    }
                }
            }
        }

        pendingConfirmation?.let { confirmation ->
            AutomationConfirmationDialog(
                outbox = confirmation,
                onSend = { viewModel.sendNow(confirmation.id) },
                onSkip = { viewModel.skipOutbox(confirmation.id) }
            )
        }
    }
}

private enum class Screen(val label: String) {
    Clients("Клиенты"),
    Appointments("Записи"),
    Finance("Финансы"),
    Settings("Настройки")
}

private enum class ClientSubScreen {
    List,
    Import,
    Form,
    Detail
}

private enum class SettingsSubScreen(val label: String) {
    Menu("Настройки"),
    Branding("Иконка"),
    Integrations("Интеграции"),
    Sync("Синхронизация"),
    Services("Услуги"),
    Automation("Автоматизация"),
    Queue("Очередь задач"),
    Backup("Резервные копии"),
    Updates("Проверка обновлений")
}

private enum class AppointmentCalendarView(val label: String) {
    Day("День"),
    ThreeDays("3 дня"),
    Week("Неделя"),
    Month("Месяц")
}

private sealed class AppointmentConflictAction {
    abstract val conflictingAppointmentId: Long
    abstract val newStartAt: String
    abstract val newDurationMinutes: Int

    data class SaveAndMoveConflicting(
        override val conflictingAppointmentId: Long,
        val currentStartAt: String,
        val currentDurationMinutes: Int
    ) : AppointmentConflictAction() {
        override val newStartAt: String = currentStartAt
        override val newDurationMinutes: Int = currentDurationMinutes
    }

    data class MoveAndMoveConflicting(
        val movingAppointmentId: Long,
        val movingStartAt: String,
        val movingDurationMinutes: Int,
        override val conflictingAppointmentId: Long,
        val movingWasCancelled: Boolean
    ) : AppointmentConflictAction() {
        override val newStartAt: String = movingStartAt
        override val newDurationMinutes: Int = movingDurationMinutes
    }
}

@Composable
private fun BottomNavIcon(screen: Screen) {
    when (screen) {
        Screen.Clients -> Icon(Icons.Filled.Person, contentDescription = screen.label)
        Screen.Appointments -> Icon(Icons.Filled.DateRange, contentDescription = screen.label)
        Screen.Finance -> Icon(Icons.Filled.Payments, contentDescription = screen.label)
        Screen.Settings -> Icon(Icons.Filled.Settings, contentDescription = screen.label)
    }
}

@Composable
private fun AppIconPreview(
    state: AppIconState,
    modifier: Modifier = Modifier.size(48.dp),
    contentDescription: String? = null
) {
    var image by remember(state.path, state.updatedAt) { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(state.path, state.updatedAt) {
        image = if (state.path.isBlank()) {
            null
        } else {
            withContext(Dispatchers.IO) {
                runCatching {
                    BitmapFactory.decodeFile(state.path)?.asImageBitmap()
                }.getOrNull()
            }
        }
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        val loadedImage = image
        if (loadedImage != null) {
            Image(
                bitmap = loadedImage,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                Icons.Filled.Business,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun MessageBar(message: String) {
    if (message.isBlank()) return
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

private fun appColorScheme(context: Context, darkTheme: Boolean) = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> systemMaterialYouColorScheme(context, darkTheme)
    darkTheme -> darkColorScheme()
    else -> lightColorScheme()
}

private fun systemMaterialYouColorScheme(context: Context, darkTheme: Boolean) = if (darkTheme) {
    darkColorScheme(
        primary = systemColor(context, android.R.color.system_accent1_300),
        onPrimary = systemColor(context, android.R.color.system_accent1_900),
        primaryContainer = systemColor(context, android.R.color.system_accent1_700),
        onPrimaryContainer = systemColor(context, android.R.color.system_accent1_100),
        inversePrimary = systemColor(context, android.R.color.system_accent1_600),
        secondary = systemColor(context, android.R.color.system_accent1_300),
        onSecondary = systemColor(context, android.R.color.system_accent1_900),
        secondaryContainer = systemColor(context, android.R.color.system_accent1_700),
        onSecondaryContainer = systemColor(context, android.R.color.system_accent1_100),
        tertiary = systemColor(context, android.R.color.system_accent3_300),
        onTertiary = systemColor(context, android.R.color.system_accent3_900),
        tertiaryContainer = systemColor(context, android.R.color.system_accent3_700),
        onTertiaryContainer = systemColor(context, android.R.color.system_accent3_100),
        background = systemColor(context, android.R.color.system_neutral1_900),
        onBackground = systemColor(context, android.R.color.system_neutral1_100),
        surface = systemColor(context, android.R.color.system_neutral1_900),
        onSurface = systemColor(context, android.R.color.system_neutral1_100),
        surfaceVariant = systemColor(context, android.R.color.system_neutral2_700),
        onSurfaceVariant = systemColor(context, android.R.color.system_neutral2_200),
        outline = systemColor(context, android.R.color.system_neutral2_400),
        outlineVariant = systemColor(context, android.R.color.system_neutral2_700),
        inverseSurface = systemColor(context, android.R.color.system_neutral1_100),
        inverseOnSurface = systemColor(context, android.R.color.system_neutral1_900),
        surfaceTint = systemColor(context, android.R.color.system_accent1_300),
        surfaceDim = systemColor(context, android.R.color.system_neutral1_1000),
        surfaceBright = systemColor(context, android.R.color.system_neutral1_700),
        surfaceContainerLowest = systemColor(context, android.R.color.system_neutral1_1000),
        surfaceContainerLow = systemColor(context, android.R.color.system_neutral1_900),
        surfaceContainer = systemColor(context, android.R.color.system_neutral1_800),
        surfaceContainerHigh = systemColor(context, android.R.color.system_neutral1_700),
        surfaceContainerHighest = systemColor(context, android.R.color.system_neutral1_700)
    )
} else {
    lightColorScheme(
        primary = systemColor(context, android.R.color.system_accent1_600),
        onPrimary = systemColor(context, android.R.color.system_accent1_0),
        primaryContainer = systemColor(context, android.R.color.system_accent1_100),
        onPrimaryContainer = systemColor(context, android.R.color.system_accent1_900),
        inversePrimary = systemColor(context, android.R.color.system_accent1_300),
        secondary = systemColor(context, android.R.color.system_accent1_600),
        onSecondary = systemColor(context, android.R.color.system_accent1_0),
        secondaryContainer = systemColor(context, android.R.color.system_accent1_100),
        onSecondaryContainer = systemColor(context, android.R.color.system_accent1_900),
        tertiary = systemColor(context, android.R.color.system_accent3_600),
        onTertiary = systemColor(context, android.R.color.system_accent3_0),
        tertiaryContainer = systemColor(context, android.R.color.system_accent3_100),
        onTertiaryContainer = systemColor(context, android.R.color.system_accent3_900),
        background = systemColor(context, android.R.color.system_neutral1_10),
        onBackground = systemColor(context, android.R.color.system_neutral1_900),
        surface = systemColor(context, android.R.color.system_neutral1_10),
        onSurface = systemColor(context, android.R.color.system_neutral1_900),
        surfaceVariant = systemColor(context, android.R.color.system_neutral2_100),
        onSurfaceVariant = systemColor(context, android.R.color.system_neutral2_700),
        outline = systemColor(context, android.R.color.system_neutral2_500),
        outlineVariant = systemColor(context, android.R.color.system_neutral2_200),
        inverseSurface = systemColor(context, android.R.color.system_neutral1_800),
        inverseOnSurface = systemColor(context, android.R.color.system_neutral1_50),
        surfaceTint = systemColor(context, android.R.color.system_accent1_600),
        surfaceDim = systemColor(context, android.R.color.system_neutral1_100),
        surfaceBright = systemColor(context, android.R.color.system_neutral1_10),
        surfaceContainerLowest = systemColor(context, android.R.color.system_neutral1_0),
        surfaceContainerLow = systemColor(context, android.R.color.system_neutral1_50),
        surfaceContainer = systemColor(context, android.R.color.system_neutral1_50),
        surfaceContainerHigh = systemColor(context, android.R.color.system_neutral1_100),
        surfaceContainerHighest = systemColor(context, android.R.color.system_neutral1_200)
    )
}

private fun systemColor(context: Context, colorRes: Int): Color = Color(context.getColor(colorRes))

@Composable
private fun ClientsScreen(
    viewModel: AppViewModel,
    clients: List<ClientEntity>,
    appointments: List<AppointmentRow>,
    financeTransactions: List<FinanceTransactionEntity>,
    subScreen: ClientSubScreen,
    selectedClientId: Long?,
    onOpenClient: (Long) -> Unit,
    onSubScreenChange: (ClientSubScreen) -> Unit,
    onOpenAppointment: (Long) -> Unit,
    onCreateAppointment: (Long) -> Unit
) {
    if (subScreen == ClientSubScreen.Import) {
        ContactImportScreen(viewModel, onBack = { onSubScreenChange(ClientSubScreen.List) })
        return
    }
    if (subScreen == ClientSubScreen.Form) {
        ClientFormScreen(viewModel, onBack = { onSubScreenChange(ClientSubScreen.List) })
        return
    }
    if (subScreen == ClientSubScreen.Detail) {
        val selectedClient = clients.firstOrNull { it.id == selectedClientId }
        if (selectedClient == null) {
            EmptyText("Клиент не найден.")
        } else {
            ClientDetailScreen(
                viewModel = viewModel,
                client = selectedClient,
                appointments = appointments.filter { it.clientId == selectedClient.id },
                financeTransactions = financeTransactions,
                onBack = { onSubScreenChange(ClientSubScreen.List) },
                onOpenAppointment = onOpenAppointment,
                onCreateAppointment = onCreateAppointment
            )
        }
        return
    }

    var query by remember { mutableStateOf("") }
    val filteredClients = clients
        .filter {
            query.isBlank() ||
                it.name.contains(query, ignoreCase = true) ||
                it.phone.contains(query, ignoreCase = true) ||
                it.email.contains(query, ignoreCase = true) ||
                it.company.contains(query, ignoreCase = true)
        }
        .sortedBy { it.name.lowercase(Locale.getDefault()) }
    val groupedClients = filteredClients.groupBy { clientSection(it.name) }.toSortedMap()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    placeholder = { Text("Поиск в клиентах") },
                    singleLine = true,
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 6.dp),
                ) {
                    OutlinedButton(
                        onClick = { onSubScreenChange(ClientSubScreen.Import) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Из контактов")
                    }
                }
            }
            item {
                Text(
                    text = "Все клиенты (${clients.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 6.dp)
                )
            }
            if (filteredClients.isEmpty()) {
                item { EmptyText(if (query.isBlank()) "Пока нет клиентов." else "Ничего не найдено.") }
            } else {
                groupedClients.forEach { (section, group) ->
                    item(key = "section-$section") {
                        Text(
                            text = section,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp, top = 10.dp, bottom = 2.dp)
                        )
                    }
                    items(group, key = { it.id }) { client ->
                        ClientListRow(client, onClick = { onOpenClient(client.id) })
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { onSubScreenChange(ClientSubScreen.Form) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Добавить клиента")
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ClientFormScreen(viewModel: AppViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var givenName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var familyName by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var phoneHome by remember { mutableStateOf("") }
    var phoneWork by remember { mutableStateOf("") }
    var phoneOther by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var emailWork by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var addressWork by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var social by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf("") }
    var pendingManualAdd by remember { mutableStateOf(false) }
    var showPhoneHome by remember { mutableStateOf(false) }
    var showPhoneWork by remember { mutableStateOf(false) }
    var showPhoneOther by remember { mutableStateOf(false) }
    var showEmailPersonal by remember { mutableStateOf(false) }
    var showEmailWork by remember { mutableStateOf(false) }
    var showBirthday by remember { mutableStateOf(false) }
    var showBirthdayPicker by remember { mutableStateOf(false) }
    var showAddressHome by remember { mutableStateOf(false) }
    var showAddressWork by remember { mutableStateOf(false) }
    var showDisplayName by remember { mutableStateOf(false) }
    var showMiddleName by remember { mutableStateOf(false) }
    var showNickname by remember { mutableStateOf(false) }
    var showJobTitle by remember { mutableStateOf(false) }
    var showWebsite by remember { mutableStateOf(false) }
    var showSocial by remember { mutableStateOf(false) }
    var showOtherPicker by remember { mutableStateOf(false) }
    fun currentDraft() = ClientDraft(
        name = name,
        givenName = givenName,
        middleName = middleName,
        familyName = familyName,
        nickname = nickname,
        phone = phone,
        phoneHome = phoneHome,
        phoneWork = phoneWork,
        phoneOther = phoneOther,
        email = email,
        emailWork = emailWork,
        address = address,
        addressWork = addressWork,
        company = company,
        jobTitle = jobTitle,
        website = website,
        birthday = birthday,
        social = social,
        notes = notes,
        photoUri = photoUri
    )
    fun clearForm() {
        name = ""
        givenName = ""
        middleName = ""
        familyName = ""
        nickname = ""
        phone = ""
        phoneHome = ""
        phoneWork = ""
        phoneOther = ""
        email = ""
        emailWork = ""
        address = ""
        addressWork = ""
        company = ""
        jobTitle = ""
        website = ""
        birthday = ""
        social = ""
        notes = ""
        photoUri = ""
    }
    val contactPermissions = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
    val addContactPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (pendingManualAdd) {
            val canSync = contactPermissions.all { permission ->
                context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
            }
            viewModel.addClient(context, currentDraft(), syncContact = canSync)
            clearForm()
            pendingManualAdd = false
            onBack()
        }
    }
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            photoUri = uri.toString()
        }
    }
    fun saveClient() {
        if (currentDraft().displayName().isBlank()) {
            viewModel.addClient(context, currentDraft(), syncContact = false)
            return
        }
        viewModel.addClient(context, currentDraft(), syncContact = false)
        clearForm()
        onBack()
    }
    fun addNextPhoneField() {
        when {
            !showPhoneHome -> showPhoneHome = true
            !showPhoneWork -> showPhoneWork = true
            !showPhoneOther -> showPhoneOther = true
        }
    }
    fun addNextEmailField() {
        when {
            !showEmailPersonal -> showEmailPersonal = true
            !showEmailWork -> showEmailWork = true
        }
    }
    fun addNextAddressField() {
        when {
            !showAddressHome -> showAddressHome = true
            !showAddressWork -> showAddressWork = true
        }
    }
    val hasAnyEmailField = showEmailPersonal || showEmailWork
    val hasAnyAddressField = showAddressHome || showAddressWork
    val hasAnyOtherField = showDisplayName || showMiddleName || showNickname || showJobTitle || showWebsite || showSocial
    val hasHiddenOtherField = !showDisplayName || !showMiddleName || !showNickname || !showJobTitle || !showWebsite || !showSocial
    if (showBirthdayPicker) {
        val birthdayPickerState = rememberDatePickerState(
            initialSelectedDateMillis = birthdayToMillis(birthday) ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showBirthdayPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        birthdayPickerState.selectedDateMillis?.let { selected ->
                            birthday = formatBirthdayMillis(selected)
                            showBirthday = true
                        }
                        showBirthdayPicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBirthdayPicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = birthdayPickerState)
        }
    }
    if (showOtherPicker) {
        ModalBottomSheet(onDismissRequest = { showOtherPicker = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Выберите поля, которые нужно добавить",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                if (!showDisplayName) FieldPickerOption("Отображаемое имя") { showDisplayName = true; showOtherPicker = false }
                if (!showMiddleName) FieldPickerOption("Отчество / второе имя") { showMiddleName = true; showOtherPicker = false }
                if (!showNickname) FieldPickerOption("Никнейм") { showNickname = true; showOtherPicker = false }
                if (!showJobTitle) FieldPickerOption("Должность") { showJobTitle = true; showOtherPicker = false }
                if (!showWebsite) FieldPickerOption("Сайт") { showWebsite = true; showOtherPicker = false }
                if (!showSocial) FieldPickerOption("Соцсети / мессенджеры") { showSocial = true; showOtherPicker = false }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(112.dp),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.Close, contentDescription = "Закрыть")
                    }
                },
                title = { Text("Создать\nклиента") },
                actions = {
                    Button(
                        onClick = { saveClient() },
                        shape = RoundedCornerShape(28.dp),
                        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp)
                    ) {
                        Text("Сохранить")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 32.dp, end = 32.dp, top = 26.dp, bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ClientPhotoButton(
                    photoUri = photoUri,
                    displayName = currentDraft().displayName(),
                    onClick = { photoPicker.launch(arrayOf("image/*")) }
                )
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    ClientFormField(givenName, { givenName = it }, "Имя")
                    ClientFormField(familyName, { familyName = it }, "Фамилия")
                    ClientFormField(company, { company = it }, "Организация")
                    ClientFormField(phone, { phone = it }, "Телефон (Мобильный)", KeyboardType.Phone)
                    if (!showPhoneHome && !showPhoneWork && !showPhoneOther) {
                        AddInlineFieldButton("Добавить телефон", Icons.Filled.Phone, ::addNextPhoneField)
                    }
                }
            }
            if (showPhoneHome || showPhoneWork || showPhoneOther) {
                item {
                    ContactFieldSection("Дополнительные телефоны") {
                        if (showPhoneHome) {
                            RemovableContactField(onRemove = { phoneHome = ""; showPhoneHome = false }) {
                                ClientFormField(phoneHome, { phoneHome = it }, "Телефон (Домашний)", KeyboardType.Phone, modifier = it)
                            }
                        }
                        if (showPhoneWork) {
                            RemovableContactField(onRemove = { phoneWork = ""; showPhoneWork = false }) {
                                ClientFormField(phoneWork, { phoneWork = it }, "Телефон (Рабочий)", KeyboardType.Phone, modifier = it)
                            }
                        }
                        if (showPhoneOther) {
                            RemovableContactField(onRemove = { phoneOther = ""; showPhoneOther = false }) {
                                ClientFormField(phoneOther, { phoneOther = it }, "Телефон (Другой)", KeyboardType.Phone, modifier = it)
                            }
                        }
                        if (!showPhoneHome || !showPhoneWork || !showPhoneOther) {
                            AddInlineFieldButton("Добавить телефон", Icons.Filled.Phone, ::addNextPhoneField)
                        }
                    }
                }
            }
            if (showEmailPersonal || showEmailWork) {
                item {
                    ContactFieldSection("Почта") {
                        if (showEmailPersonal) {
                            RemovableContactField(onRemove = { email = ""; showEmailPersonal = false }) {
                                ClientFormField(email, { email = it }, "Адрес эл. почты (Личный)", KeyboardType.Email, modifier = it)
                            }
                        }
                        if (showEmailWork) {
                            RemovableContactField(onRemove = { emailWork = ""; showEmailWork = false }) {
                                ClientFormField(emailWork, { emailWork = it }, "Адрес эл. почты (Рабочий)", KeyboardType.Email, modifier = it)
                            }
                        }
                        if (!showEmailPersonal || !showEmailWork) {
                            AddInlineFieldButton("Добавить адрес эл. почты", Icons.Filled.Email, ::addNextEmailField)
                        }
                    }
                }
            }
            if (showBirthday) {
                item {
                    ContactFieldSection("Знаменательные даты") {
                        RemovableContactField(onRemove = { birthday = ""; showBirthday = false }) {
                            BirthdayPickerField(
                                birthday = birthday,
                                onClick = { showBirthdayPicker = true },
                                modifier = it
                            )
                        }
                    }
                }
            }
            if (showAddressHome || showAddressWork) {
                item {
                    ContactFieldSection("Адреса") {
                        if (showAddressHome) {
                            RemovableContactField(onRemove = { address = ""; showAddressHome = false }) {
                                ClientFormField(address, { address = it }, "Адрес (Домашний)", modifier = it)
                            }
                        }
                        if (showAddressWork) {
                            RemovableContactField(onRemove = { addressWork = ""; showAddressWork = false }) {
                                ClientFormField(addressWork, { addressWork = it }, "Адрес (Рабочий)", modifier = it)
                            }
                        }
                        if (!showAddressHome || !showAddressWork) {
                            AddInlineFieldButton("Указать адрес", Icons.Filled.Place, ::addNextAddressField)
                        }
                    }
                }
            }
            if (showDisplayName || showMiddleName || showNickname || showJobTitle || showWebsite || showSocial) {
                item {
                    ContactFieldSection("Дополнительно") {
                        if (showDisplayName) {
                            RemovableContactField(onRemove = { name = ""; showDisplayName = false }) {
                                ClientFormField(name, { name = it }, "Отображаемое имя", modifier = it)
                            }
                        }
                        if (showMiddleName) {
                            RemovableContactField(onRemove = { middleName = ""; showMiddleName = false }) {
                                ClientFormField(middleName, { middleName = it }, "Отчество / второе имя", modifier = it)
                            }
                        }
                        if (showNickname) {
                            RemovableContactField(onRemove = { nickname = ""; showNickname = false }) {
                                ClientFormField(nickname, { nickname = it }, "Никнейм", modifier = it)
                            }
                        }
                        if (showJobTitle) {
                            RemovableContactField(onRemove = { jobTitle = ""; showJobTitle = false }) {
                                ClientFormField(jobTitle, { jobTitle = it }, "Должность", modifier = it)
                            }
                        }
                        if (showWebsite) {
                            RemovableContactField(onRemove = { website = ""; showWebsite = false }) {
                                ClientFormField(website, { website = it }, "Сайт", KeyboardType.Uri, modifier = it)
                            }
                        }
                        if (showSocial) {
                            RemovableContactField(onRemove = { social = ""; showSocial = false }) {
                                ClientFormField(social, { social = it }, "Соцсети / мессенджеры", modifier = it)
                            }
                        }
                        if (hasHiddenOtherField) {
                            AddInlineFieldButton("Добавить другое поле", Icons.Filled.Add, { showOtherPicker = true })
                        }
                    }
                }
            }
            item {
                ClientFormField(notes, { notes = it }, "Примечания", minLines = 3)
            }
            if (!hasAnyEmailField || !showBirthday || !hasAnyAddressField || (hasHiddenOtherField && !hasAnyOtherField)) {
                item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Добавить данные",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        if (!hasAnyEmailField) {
                            AddContactDataButton("Письмо", Icons.Filled.Email, ::addNextEmailField, Modifier.weight(1f))
                        }
                        if (!showBirthday) {
                            AddContactDataButton("День рождения", Icons.Filled.Cake, { showBirthdayPicker = true }, Modifier.weight(1f))
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        if (!hasAnyAddressField) {
                            AddContactDataButton("Адрес", Icons.Filled.Place, ::addNextAddressField, Modifier.weight(1f))
                        }
                        if (hasHiddenOtherField && !hasAnyOtherField) {
                            AddContactDataButton("Другое", Icons.Filled.Add, { showOtherPicker = true }, Modifier.weight(1f))
                        }
                    }
                }
                }
            }
        }
    }
}

@Composable
private fun ClientPhotoButton(photoUri: String, displayName: String, onClick: () -> Unit) {
    var image by remember(photoUri) { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current
    LaunchedEffect(photoUri) {
        image = if (photoUri.isBlank()) {
            null
        } else {
            withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openInputStream(Uri.parse(photoUri))?.use { stream ->
                        BitmapFactory.decodeStream(stream)?.asImageBitmap()
                    }
                }.getOrNull()
            }
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Surface(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .clickable(onClick = onClick),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                val loadedImage = image
                if (loadedImage != null) {
                    Image(
                        bitmap = loadedImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (displayName.isNotBlank()) {
                    Text(initials(displayName), style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Filled.AddAPhoto, contentDescription = null, modifier = Modifier.size(52.dp))
                }
            }
        }
        TextButton(onClick = onClick) {
            Text("Добавить фото")
        }
    }
}

@Composable
private fun ClientFormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = minLines == 1,
        minLines = minLines,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun BirthdayPickerField(birthday: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .height(72.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("День рождения", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = birthday.ifBlank { "Выбрать дату" },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Icon(Icons.Filled.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ContactFieldSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        content()
    }
}

@Composable
private fun RemovableContactField(onRemove: () -> Unit, content: @Composable (Modifier) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content(Modifier.weight(1f))
        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Filled.RemoveCircleOutline,
                contentDescription = "Удалить поле",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun AddInlineFieldButton(label: String, icon: ImageVector, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Icon(icon, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(label)
    }
}

@Composable
private fun FieldPickerOption(label: String, onClick: () -> Unit) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp)
    )
}

@Composable
private fun AddContactDataButton(label: String, icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        Icon(icon, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(label)
    }
}

@Composable
private fun ClientListRow(client: ClientEntity, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContactAvatar(name = client.name, photoUri = client.contactPhotoUri)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(client.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = clientSubtitle(client),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ClientDetailScreen(
    viewModel: AppViewModel,
    client: ClientEntity,
    appointments: List<AppointmentRow>,
    financeTransactions: List<FinanceTransactionEntity>,
    onBack: () -> Unit,
    onOpenAppointment: (Long) -> Unit,
    onCreateAppointment: (Long) -> Unit
) {
    val context = LocalContext.current
    val hasCalendarPermissions =
        context.checkSelfPermission(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED &&
            context.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
    val primaryPhone = clientPrimaryPhone(client)
    val primaryEmail = firstFilled(client.email, client.emailWork)
    val sortedAppointments = appointments.sortedWith(
        compareByDescending<AppointmentRow> { parseAppointmentMillis(it.startAt) ?: Long.MIN_VALUE }
            .thenByDescending { it.id }
    )
    var showAllAppointments by remember(client.id) { mutableStateOf(false) }
    var pendingCallPhone by remember(client.id) { mutableStateOf("") }
    var pendingVideoCall by remember(client.id) { mutableStateOf(false) }
    var selectedAppointmentId by remember(client.id) { mutableStateOf<Long?>(null) }
    var movingAppointmentId by remember(client.id) { mutableStateOf<Long?>(null) }
    val selectedAppointment = selectedAppointmentId?.let { id -> appointments.firstOrNull { it.id == id } }
    val movingAppointment = movingAppointmentId?.let { id -> appointments.firstOrNull { it.id == id } }
    val callPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        val phone = pendingCallPhone.ifBlank { primaryPhone }
        if (granted) {
            if (pendingVideoCall) startCarrierVideoCall(context, phone) else startPhoneCall(context, phone)
        } else {
            if (!pendingVideoCall) openDialer(context, phone)
        }
        pendingCallPhone = ""
        pendingVideoCall = false
    }
    fun callPhone(phone: String, video: Boolean = false) {
        if (phone.isBlank()) return
        if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            if (video) startCarrierVideoCall(context, phone) else startPhoneCall(context, phone)
        } else {
            pendingCallPhone = phone
            pendingVideoCall = video
            callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
    }
    fun videoPhone(phone: String) {
        if (phone.isBlank()) return
        if (!startMeetContactVideoCall(context, client, phone) && !startMeetExternalPhoneCall(context, phone)) {
            callPhone(phone, video = true)
        }
    }

    BackHandler(enabled = selectedAppointment != null) {
        selectedAppointmentId = null
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 18.dp, bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    LargeContactAvatar(name = client.name, photoUri = client.contactPhotoUri)
                }
                item {
                    Text(
                        text = client.name,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ContactActionButton(
                            label = "Вызов",
                            icon = Icons.Filled.Phone,
                            enabled = primaryPhone.isNotBlank(),
                            modifier = Modifier.weight(1f),
                            onClick = { callPhone(primaryPhone) }
                        )
                        ContactActionButton(
                            label = "SMS",
                            icon = Icons.Filled.Sms,
                            enabled = primaryPhone.isNotBlank(),
                            modifier = Modifier.weight(1f),
                            onClick = { openSms(context, primaryPhone) }
                        )
                        ContactActionButton(
                            label = "Видео",
                            icon = Icons.Filled.Videocam,
                            enabled = primaryPhone.isNotBlank(),
                            modifier = Modifier.weight(1f),
                            onClick = { videoPhone(primaryPhone) }
                        )
                        ContactActionButton(
                            label = "Письмо",
                            icon = Icons.Filled.Email,
                            enabled = primaryEmail.isNotBlank(),
                            modifier = Modifier.weight(1f),
                            onClick = { openEmail(context, primaryEmail) }
                        )
                    }
                }
                if (clientPhones(client).isNotEmpty()) {
                    item {
                        ContactDetailGroup {
                            clientPhones(client).forEach { (label, phone) ->
                                PhoneDetailRow(label = label, phone = phone, onCall = { callPhone(phone) })
                            }
                        }
                    }
                }
                if (clientEmails(client).isNotEmpty()) {
                    item {
                        ContactDetailGroup {
                            clientEmails(client).forEach { (label, email) ->
                                ContactDetailRow(
                                    icon = Icons.Filled.Email,
                                    title = email,
                                    subtitle = label,
                                    onClick = { openEmail(context, email) }
                                )
                            }
                        }
                    }
                }
                if (client.company.isNotBlank() || client.jobTitle.isNotBlank()) {
                    item {
                        ContactDetailGroup {
                            ContactDetailRow(
                                icon = Icons.Filled.Business,
                                title = firstFilled(client.company, client.jobTitle),
                                subtitle = listOf(client.company, client.jobTitle)
                                    .filter { it.isNotBlank() }
                                    .joinToString(" / ")
                            )
                        }
                    }
                }
                if (clientAddresses(client).isNotEmpty()) {
                    item {
                        ContactDetailGroup {
                            clientAddresses(client).forEach { (label, address) ->
                                ContactDetailRow(Icons.Filled.Place, address, label)
                            }
                        }
                    }
                }
                if (client.birthday.isNotBlank() || client.website.isNotBlank() || client.social.isNotBlank() || client.nickname.isNotBlank()) {
                    item {
                        ContactDetailGroup {
                            if (client.birthday.isNotBlank()) {
                                ContactDetailRow(Icons.Filled.Cake, client.birthday, "День рождения")
                            }
                            if (client.website.isNotBlank()) {
                                ContactDetailRow(Icons.Filled.Public, client.website, "Сайт")
                            }
                            if (client.social.isNotBlank()) {
                                ContactDetailRow(Icons.Filled.Chat, client.social, "Соцсети / мессенджеры")
                            }
                            if (client.nickname.isNotBlank()) {
                                ContactDetailRow(Icons.Filled.Person, client.nickname, "Никнейм")
                            }
                        }
                    }
                }
                if (client.notes.isNotBlank()) {
                    item {
                        ContactDetailGroup {
                            ContactDetailRow(Icons.Filled.Notes, client.notes, "Примечания")
                        }
                    }
                }
                if (sortedAppointments.isNotEmpty() || financeTransactions.any { it.clientId == client.id }) {
                    item {
                        ClientFinanceSection(
                            client = client,
                            appointments = sortedAppointments,
                            transactions = financeTransactions
                        )
                    }
                }
                if (sortedAppointments.isNotEmpty()) {
                    item {
                        ClientAppointmentsSection(
                            appointments = sortedAppointments,
                            expanded = showAllAppointments,
                            onToggleExpanded = { showAllAppointments = !showAllAppointments },
                            onAppointmentClick = { selectedAppointmentId = it.id }
                        )
                    }
                }
                item {
                    Button(
                        onClick = { onCreateAppointment(client.id) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Создать запись")
                    }
                }
            }
        }
        selectedAppointment?.let { appointment ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surface
            ) {
                AppointmentDetailScreen(
                    appointment = appointment,
                    client = client,
                    hasCalendarPermissions = hasCalendarPermissions,
                    onBack = { selectedAppointmentId = null },
                    onEdit = { onOpenAppointment(appointment.id) },
                    onMove = { movingAppointmentId = appointment.id },
                    onCancel = { viewModel.cancelAppointment(context, appointment.id) },
                    onStatusChange = { status -> viewModel.updateAppointmentStatus(context, appointment.id, status) },
                    onClientClick = { selectedAppointmentId = null },
                    onRecordPayment = { amount, method, paidAt, payInFull ->
                        viewModel.recordAppointmentPayment(
                            appointmentId = appointment.id,
                            amount = amount,
                            paymentMethod = method,
                            paidAt = paidAt,
                            payInFull = payInFull
                        )
                    }
                )
            }
        }
        movingAppointment?.let { appointment ->
            AppointmentMoveSheet(
                appointment = appointment,
                title = if (appointmentIsCancelled(appointment)) "Перенести и возобновить" else "Перенести",
                onDismiss = { movingAppointmentId = null },
                onSave = { date, time ->
                    val startAt = formatAppointmentLocalDateTime(date, time)
                    if (appointmentIsCancelled(appointment)) {
                        viewModel.rescheduleAndReactivateAppointment(context, appointment.id, startAt)
                    } else {
                        viewModel.rescheduleAppointment(
                            context = context,
                            appointmentId = appointment.id,
                            startAt = startAt
                        )
                    }
                    movingAppointmentId = null
                }
            )
        }
    }
}

@Composable
private fun LargeContactAvatar(name: String, photoUri: String) {
    val context = LocalContext.current
    var image by remember(photoUri) { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(photoUri) {
        image = if (photoUri.isBlank()) {
            null
        } else {
            withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openInputStream(Uri.parse(photoUri))?.use { stream ->
                        BitmapFactory.decodeStream(stream)?.asImageBitmap()
                    }
                }.getOrNull()
            }
        }
    }
    Box(
        modifier = Modifier
            .size(172.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        val loadedImage = image
        if (loadedImage != null) {
            Image(
                bitmap = loadedImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = initials(name),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ContactActionButton(
    label: String,
    icon: ImageVector,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(28.dp))
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ContactDetailGroup(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            content = content
        )
    }
}

@Composable
private fun PhoneDetailRow(label: String, phone: String, onCall: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCall)
            .padding(start = 18.dp, end = 8.dp, top = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(Icons.Filled.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(phone, style = MaterialTheme.typography.titleMedium)
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(onClick = { openSms(context, phone) }) {
            Icon(Icons.Filled.Sms, contentDescription = "SMS")
        }
    }
}

@Composable
private fun ContactDetailRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null
) {
    val rowModifier = Modifier
        .fillMaxWidth()
        .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
        .padding(horizontal = 18.dp, vertical = 14.dp)
    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            if (subtitle.isNotBlank()) {
                Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun ClientFinanceSection(
    client: ClientEntity,
    appointments: List<AppointmentRow>,
    transactions: List<FinanceTransactionEntity>
) {
    val totals = remember(client.id, appointments, transactions) {
        clientFinanceTotals(client.id, appointments, transactions)
    }
    ContactDetailGroup {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(Icons.Filled.Payments, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Финансы", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Column(
            modifier = Modifier.padding(start = 58.dp, end = 18.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.fillMaxWidth()) {
                ClientFinanceMetric("Начислено", totals.accruedCents, Modifier.weight(1f))
                ClientFinanceMetric("Оплачено", totals.paidCents, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.fillMaxWidth()) {
                ClientFinanceMetric("Долг", totals.debtCents, Modifier.weight(1f), emphasize = totals.debtCents > 0)
                ClientFinanceMetric("Расходы", totals.expenseCents, Modifier.weight(1f))
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Итого",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        formatMoney(totals.totalCents),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun ClientFinanceMetric(
    label: String,
    amountCents: Long,
    modifier: Modifier = Modifier,
    emphasize: Boolean = false
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        Text(
            formatMoney(amountCents),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (emphasize) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ClientAppointmentsSection(
    appointments: List<AppointmentRow>,
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    onAppointmentClick: (AppointmentRow) -> Unit
) {
    val visibleAppointments = if (expanded) appointments else appointments.take(1)
    ContactDetailGroup {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 8.dp, top = 12.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Event, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(16.dp))
            Text("Записи", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            if (appointments.size > 1) {
                TextButton(onClick = onToggleExpanded) {
                    Text(if (expanded) "Скрыть" else "Все")
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null
                    )
                }
            }
        }
        visibleAppointments.forEachIndexed { index, appointment ->
            AppointmentDetailItem(
                appointment = appointment,
                subtitle = if (!expanded && appointments.size > 1) {
                    "Последняя запись. Всего: ${appointments.size}"
                } else {
                    "Запись ${index + 1} из ${appointments.size}"
                },
                onClick = { onAppointmentClick(appointment) }
            )
        }
    }
}

@Composable
private fun AppointmentDetailItem(appointment: AppointmentRow, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.width(24.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(appointment.service, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(appointment.startAt, color = MaterialTheme.colorScheme.onSurfaceVariant)
            val details = listOf(appointment.price, appointmentUiStatusLabel(appointment))
                .filter { it.isNotBlank() }
                .joinToString(" / ")
            if (details.isNotBlank()) {
                Text(details, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            }
            if (appointment.notes.isNotBlank()) {
                Text(appointment.notes, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            }
            Text(subtitle, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun ContactImportScreen(viewModel: AppViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    val selected = remember { mutableStateListOf<String>() }
    var hasPermission by remember {
        mutableStateOf(context.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        hasPermission = granted
        if (granted) viewModel.loadContacts(context)
    }

    LaunchedEffect(hasPermission) {
        if (hasPermission) viewModel.loadContacts(context)
    }

    val contacts = viewModel.contacts.filter {
        query.isBlank() ||
            it.name.contains(query, true) ||
            it.phone.contains(query, true) ||
            it.email.contains(query, true) ||
            it.company.contains(query, true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("Назад") }
            Text("Контакты телефона", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        if (!hasPermission) {
            Text("Нужен доступ к контактам, чтобы связать клиентов с телефонной книгой.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))
            Button(onClick = { permissionLauncher.launch(Manifest.permission.READ_CONTACTS) }) {
                Text("Разрешить контакты")
            }
            return
        }
        OutlinedTextField(query, { query = it }, label = { Text("Поиск") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    viewModel.importContacts(viewModel.contacts.filter { selected.contains(it.key) })
                    selected.clear()
                    onBack()
                }
            ) {
                Text("Добавить как клиентов (${selected.size})")
            }
            OutlinedButton(onClick = { selected.clear() }) { Text("Снять выбор") }
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(contacts, key = { it.key }) { contact ->
                val checked = selected.contains(contact.key)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (checked) selected.remove(contact.key) else selected.add(contact.key)
                        }
                        .background(MaterialTheme.colorScheme.surfaceContainerLow, RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = checked, onCheckedChange = {
                        if (it) selected.add(contact.key) else selected.remove(contact.key)
                    })
                    ContactAvatar(name = contact.name, photoUri = contact.photoUri, modifier = Modifier.padding(end = 10.dp))
                    Column {
                        Text(contact.name, fontWeight = FontWeight.SemiBold)
                        val details = listOf(contact.phone, contact.email, contact.company)
                            .filter { it.isNotBlank() }
                            .joinToString(" / ")
                        Text(details.ifBlank { "Без телефона и email" }, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppointmentsScreen(
    viewModel: AppViewModel,
    clients: List<ClientEntity>,
    appointments: List<AppointmentRow>,
    services: List<ServiceEntity>,
    financeTransactions: List<FinanceTransactionEntity>,
    requestedAppointmentId: Long?,
    onRequestedAppointmentConsumed: () -> Unit,
    requestedAppointmentEditId: Long?,
    onRequestedAppointmentEditConsumed: () -> Unit,
    requestedAppointmentClientId: Long?,
    onRequestedAppointmentClientConsumed: () -> Unit,
    onEditorOpenChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val calendarPermissions = arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
    val hasCalendarPermissions = calendarPermissions.all { permission ->
        context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    var calendarView by rememberSaveable { mutableStateOf(AppointmentCalendarView.Month) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var searchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    var showCreateSheet by remember { mutableStateOf(false) }
    var showClientPicker by remember { mutableStateOf(false) }
    var showCalendarDatePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var serviceCreatorTargetId by remember { mutableStateOf<Int?>(null) }
    var selectedAppointmentId by remember { mutableStateOf<Long?>(null) }
    var selectedClientProfileId by remember { mutableStateOf<Long?>(null) }
    var editingAppointmentId by remember { mutableStateOf<Long?>(null) }
    var movingAppointmentId by remember { mutableStateOf<Long?>(null) }
    var appointmentConflict by remember { mutableStateOf<AppointmentConflictAction?>(null) }

    var selectedClientId by remember(clients) { mutableStateOf(0L) }
    var appointmentDate by remember { mutableStateOf(LocalDate.now()) }
    var appointmentTime by remember { mutableStateOf("10:00") }
    var notes by remember { mutableStateOf("") }
    var clientSearch by remember { mutableStateOf("") }
    var serviceDraftKey by remember { mutableStateOf(1) }
    val serviceDrafts = remember { mutableStateListOf(AppointmentServiceDraft(localId = 0)) }

    fun blankServiceDraft(): AppointmentServiceDraft =
        AppointmentServiceDraft(localId = serviceDraftKey++).also {
            if (serviceDraftKey == Int.MAX_VALUE) serviceDraftKey = 1
        }

    fun updateServiceDraft(localId: Int, updated: AppointmentServiceDraft) {
        val index = serviceDrafts.indexOfFirst { it.localId == localId }
        if (index >= 0) serviceDrafts[index] = updated
    }

    fun removeServiceDraft(localId: Int) {
        if (serviceDrafts.size > 1) {
            serviceDrafts.removeAll { it.localId == localId }
        } else {
            updateServiceDraft(localId, serviceDrafts.first().copy(serviceId = 0, serviceName = "", price = "", durationMinutes = 60))
        }
    }

    fun resetDraft() {
        editingAppointmentId = null
        selectedClientId = 0L
        appointmentDate = selectedDate
        appointmentTime = "10:00"
        notes = ""
        clientSearch = ""
        serviceDrafts.clear()
        serviceDrafts.add(AppointmentServiceDraft(localId = 0))
        serviceCreatorTargetId = null
        showClientPicker = false
    }

    fun saveDraft(skipConflictCheck: Boolean = false): Long? {
        val startAt = formatAppointmentLocalDateTime(appointmentDate, appointmentTime)
        val editId = editingAppointmentId
        val totalDuration = serviceDrafts
            .filter { it.isSelected }
            .sumOf { it.durationMinutes.coerceAtLeast(15) }
            .coerceAtLeast(15)
        if (!skipConflictCheck) {
            val conflict = findAppointmentConflict(
                appointments = appointments,
                startAt = startAt,
                durationMinutes = totalDuration,
                excludeAppointmentId = editId
            )
            if (conflict != null) {
                appointmentConflict = AppointmentConflictAction.SaveAndMoveConflicting(
                    conflictingAppointmentId = conflict.id,
                    currentStartAt = startAt,
                    currentDurationMinutes = totalDuration
                )
                return null
            }
        }
        if (editId == null) {
            viewModel.addAppointment(
                context = context,
                clientId = selectedClientId,
                services = serviceDrafts.toList(),
                startAt = startAt,
                notes = notes,
                syncCalendar = false
            )
        } else {
            viewModel.updateAppointment(
                context = context,
                appointmentId = editId,
                clientId = selectedClientId,
                services = serviceDrafts.toList(),
                startAt = startAt,
                notes = notes
            )
        }
        selectedDate = appointmentDate
        showCreateSheet = false
        onEditorOpenChange(false)
        resetDraft()
        return editId
    }

    fun openAppointmentDetails(appointment: AppointmentRow) {
        selectedAppointmentId = appointment.id
        selectedClientProfileId = null
        onEditorOpenChange(true)
    }

    fun closeAppointmentDetails() {
        selectedAppointmentId = null
        selectedClientProfileId = null
        onEditorOpenChange(false)
    }

    fun openAppointmentEditor(appointment: AppointmentRow) {
        val parsedDateTime = parseAppointmentDateTime(appointment.startAt)
        editingAppointmentId = appointment.id
        selectedAppointmentId = null
        selectedClientId = appointment.clientId
        appointmentDate = parsedDateTime?.toLocalDate() ?: selectedDate
        appointmentTime = parsedDateTime?.toLocalTime()?.let { "%02d:%02d".format(Locale.US, it.hour, it.minute) } ?: "10:00"
        notes = appointment.notes
        clientSearch = ""
        serviceCreatorTargetId = null
        showClientPicker = false
        serviceDrafts.clear()
        serviceDrafts.add(
            AppointmentServiceDraft(
                localId = 0,
                serviceId = 0,
                serviceName = appointment.service,
                price = formatMoneyPlain(appointment.priceCents),
                durationMinutes = appointment.durationMinutes.coerceAtLeast(15)
            )
        )
        serviceDraftKey = 1
        viewModel.loadAppointmentServicesForEdit(appointment) { drafts ->
            if (editingAppointmentId == appointment.id && showCreateSheet) {
                serviceDrafts.clear()
                serviceDrafts.addAll(drafts)
                serviceDraftKey = (drafts.maxOfOrNull { it.localId } ?: -1) + 1
            }
        }
        showCreateSheet = true
        onEditorOpenChange(true)
    }

    fun openNewAppointmentAt(date: LocalDate, time: LocalTime = LocalTime.of(10, 0)) {
        resetDraft()
        selectedAppointmentId = null
        selectedClientProfileId = null
        selectedDate = date
        appointmentDate = date
        appointmentTime = "%02d:%02d".format(Locale.US, time.hour, time.minute)
        showCreateSheet = true
        onEditorOpenChange(true)
    }

    fun openNewAppointmentForClient(clientId: Long) {
        resetDraft()
        selectedAppointmentId = null
        selectedClientProfileId = null
        selectedClientId = clientId
        appointmentDate = LocalDate.now()
        selectedDate = appointmentDate
        appointmentTime = "10:00"
        calendarView = AppointmentCalendarView.Day
        showCreateSheet = true
        onEditorOpenChange(true)
    }

    fun requestCloseEditor() {
        showCreateSheet = false
        onEditorOpenChange(false)
        resetDraft()
    }

    val visibleAppointments = remember(appointments, searchQuery) {
        appointments.filter { it.matchesAppointmentSearch(searchQuery) }
    }
    val selectedAppointment = remember(selectedAppointmentId, appointments) {
        selectedAppointmentId?.let { id -> appointments.firstOrNull { it.id == id } }
    }
    val selectedClientProfile = remember(selectedClientProfileId, clients) {
        selectedClientProfileId?.let { id -> clients.firstOrNull { it.id == id } }
    }
    val movingAppointment = remember(movingAppointmentId, appointments) {
        movingAppointmentId?.let { id -> appointments.firstOrNull { it.id == id } }
    }

    LaunchedEffect(requestedAppointmentId, appointments) {
        val requestedId = requestedAppointmentId ?: return@LaunchedEffect
        if (appointments.any { it.id == requestedId }) {
            selectedAppointmentId = requestedId
            showCreateSheet = false
            showClientPicker = false
            onEditorOpenChange(true)
            onRequestedAppointmentConsumed()
        }
    }

    LaunchedEffect(requestedAppointmentEditId, appointments) {
        val requestedId = requestedAppointmentEditId ?: return@LaunchedEffect
        val requestedAppointment = appointments.firstOrNull { it.id == requestedId } ?: return@LaunchedEffect
        openAppointmentEditor(requestedAppointment)
        onRequestedAppointmentEditConsumed()
    }

    LaunchedEffect(requestedAppointmentClientId, clients) {
        val requestedClientId = requestedAppointmentClientId ?: return@LaunchedEffect
        if (clients.any { it.id == requestedClientId }) {
            openNewAppointmentForClient(requestedClientId)
            onRequestedAppointmentClientConsumed()
        }
    }

    BackHandler(enabled = showCreateSheet || selectedClientProfileId != null || selectedAppointmentId != null) {
        if (selectedClientProfileId != null) {
            selectedClientProfileId = null
        } else if (showCreateSheet) {
            requestCloseEditor()
        } else {
            closeAppointmentDetails()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppointmentsCalendarHeader(
                calendarView = calendarView,
                selectedDate = selectedDate,
                searchActive = searchActive,
                searchQuery = searchQuery,
                onSearchActiveChange = {
                    searchActive = it
                    if (!it) searchQuery = ""
                },
                onSearchQueryChange = { searchQuery = it },
                onToday = {
                    selectedDate = LocalDate.now()
                },
                onPreviousPeriod = {
                    selectedDate = selectedDate.shiftCalendarPeriod(calendarView, -1)
                },
                onNextPeriod = {
                    selectedDate = selectedDate.shiftCalendarPeriod(calendarView, 1)
                },
                onPickDate = {
                    showCalendarDatePicker = true
                },
                onViewChange = { calendarView = it }
            )

            if (clients.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    EmptyText("Сначала добавь клиента.")
                }
            } else {
                when (calendarView) {
                    AppointmentCalendarView.Day,
                    AppointmentCalendarView.ThreeDays,
                    AppointmentCalendarView.Week -> AppointmentTimelineView(
                        calendarView = calendarView,
                        selectedDate = selectedDate,
                        appointments = visibleAppointments,
                        hasCalendarPermissions = hasCalendarPermissions,
                        onAppointmentClick = { openAppointmentDetails(it) },
                        onEmptySlotClick = { date, time -> openNewAppointmentAt(date, time) }
                    )
                    AppointmentCalendarView.Month -> AppointmentMonthView(
                        selectedDate = selectedDate,
                        appointments = visibleAppointments,
                        hasCalendarPermissions = hasCalendarPermissions,
                        onAppointmentClick = { openAppointmentDetails(it) },
                        onDayClick = {
                            selectedDate = it
                            calendarView = AppointmentCalendarView.Day
                        }
                    )
                }
            }
        }

        selectedAppointment?.let { appointment ->
            if (!showCreateSheet) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    AppointmentDetailScreen(
                        appointment = appointment,
                        client = clients.firstOrNull { it.id == appointment.clientId },
                        hasCalendarPermissions = hasCalendarPermissions,
                        onBack = { closeAppointmentDetails() },
                        onEdit = { openAppointmentEditor(appointment) },
                        onMove = { movingAppointmentId = appointment.id },
                        onCancel = { viewModel.cancelAppointment(context, appointment.id) },
                        onStatusChange = { status -> viewModel.updateAppointmentStatus(context, appointment.id, status) },
                        onClientClick = { selectedClientProfileId = appointment.clientId },
                        onRecordPayment = { amount, method, paidAt, payInFull ->
                            viewModel.recordAppointmentPayment(
                                appointmentId = appointment.id,
                                amount = amount,
                                paymentMethod = method,
                                paidAt = paidAt,
                                payInFull = payInFull
                            )
                        }
                    )
                }
            }
        }

        selectedClientProfile?.let { client ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surface
            ) {
                ClientDetailScreen(
                    viewModel = viewModel,
                    client = client,
                    appointments = appointments.filter { it.clientId == client.id },
                    financeTransactions = financeTransactions,
                    onBack = { selectedClientProfileId = null },
                    onOpenAppointment = { appointmentId ->
                        selectedClientProfileId = null
                        appointments.firstOrNull { it.id == appointmentId }?.let { openAppointmentEditor(it) }
                    },
                    onCreateAppointment = { clientId ->
                        openNewAppointmentForClient(clientId)
                    }
                )
            }
        }

        if (clients.isNotEmpty() && selectedAppointment == null && !showCreateSheet) {
            FloatingActionButton(
                onClick = { openNewAppointmentAt(selectedDate) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Создать запись")
            }
        }

        if (showCreateSheet) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surface
            ) {
                if (showClientPicker) {
                    AppointmentClientPicker(
                        clients = clients,
                        selectedClientId = selectedClientId,
                        query = clientSearch,
                        onQueryChange = { clientSearch = it },
                        onBack = { showClientPicker = false },
                        onSelect = {
                            selectedClientId = it
                            showClientPicker = false
                        }
                    )
                } else {
                    val currentEditingAppointmentId = editingAppointmentId
                    AppointmentEditorContent(
                        title = if (editingAppointmentId == null) "Новая\nзапись" else "Изменить\nзапись",
                        clients = clients,
                        services = services,
                        selectedClientId = selectedClientId,
                        serviceDrafts = serviceDrafts,
                        appointmentDate = appointmentDate,
                        appointmentTime = appointmentTime,
                        notes = notes,
                        onClose = { requestCloseEditor() },
                        onOpenClientPicker = { showClientPicker = true },
                        onServiceDraftChange = { localId, draft ->
                            updateServiceDraft(localId, draft)
                        },
                        onAddService = {
                            serviceDrafts.add(blankServiceDraft())
                        },
                        onRemoveService = { removeServiceDraft(it) },
                        onCreateService = { serviceCreatorTargetId = it },
                        onOpenDatePicker = { showDatePicker = true },
                        onOpenTimePicker = { showTimePicker = true },
                        onNotesChange = { notes = it },
                        onSave = {
                            saveDraft()
                        },
                        onDelete = currentEditingAppointmentId?.let { appointmentId ->
                            {
                                viewModel.deleteAppointment(context, appointmentId)
                                showCreateSheet = false
                                onEditorOpenChange(false)
                                resetDraft()
                            }
                        }
                    )
                }
            }
        }
    }

    movingAppointment?.let { appointment ->
        AppointmentMoveSheet(
            appointment = appointment,
            title = if (appointmentIsCancelled(appointment)) "Перенести и возобновить" else "Перенести",
            onDismiss = { movingAppointmentId = null },
            onSave = { date, time ->
                val startAt = formatAppointmentLocalDateTime(date, time)
                val conflict = findAppointmentConflict(
                    appointments = appointments,
                    startAt = startAt,
                    durationMinutes = appointment.durationMinutes.coerceAtLeast(15),
                    excludeAppointmentId = appointment.id
                )
                if (conflict != null) {
                    appointmentConflict = AppointmentConflictAction.MoveAndMoveConflicting(
                        movingAppointmentId = appointment.id,
                        movingStartAt = startAt,
                        movingDurationMinutes = appointment.durationMinutes.coerceAtLeast(15),
                        conflictingAppointmentId = conflict.id,
                        movingWasCancelled = appointmentIsCancelled(appointment)
                    )
                    return@AppointmentMoveSheet
                }
                if (appointmentIsCancelled(appointment)) {
                    viewModel.rescheduleAndReactivateAppointment(context, appointment.id, startAt)
                } else {
                    viewModel.rescheduleAppointment(context, appointment.id, startAt)
                }
                selectedDate = date
                calendarView = AppointmentCalendarView.Day
                movingAppointmentId = null
            }
        )
    }

    appointmentConflict?.let { conflict ->
        val conflictingAppointment = appointments.firstOrNull { it.id == conflict.conflictingAppointmentId }
        AppointmentConflictDialog(
            conflictingAppointment = conflictingAppointment,
            newStartAt = conflict.newStartAt,
            newDurationMinutes = conflict.newDurationMinutes,
            onKeepEditing = { appointmentConflict = null },
            onSaveAndMoveConflicting = {
                when (conflict) {
                    is AppointmentConflictAction.SaveAndMoveConflicting -> {
                        appointmentConflict = null
                        saveDraft(skipConflictCheck = true)
                        movingAppointmentId = conflict.conflictingAppointmentId
                    }
                    is AppointmentConflictAction.MoveAndMoveConflicting -> {
                        appointmentConflict = null
                        if (conflict.movingWasCancelled) {
                            viewModel.rescheduleAndReactivateAppointment(context, conflict.movingAppointmentId, conflict.movingStartAt)
                        } else {
                            viewModel.rescheduleAppointment(context, conflict.movingAppointmentId, conflict.movingStartAt)
                        }
                        parseAppointmentDateTime(conflict.movingStartAt)?.toLocalDate()?.let { selectedDate = it }
                        calendarView = AppointmentCalendarView.Day
                        movingAppointmentId = conflict.conflictingAppointmentId
                    }
                }
            }
        )
    }

    serviceCreatorTargetId?.let { targetId ->
        var newServiceName by remember(targetId) { mutableStateOf("") }
        var newServicePrice by remember(targetId) { mutableStateOf("") }
        var newServiceDuration by remember(targetId) { mutableStateOf(60) }
        ModalBottomSheet(onDismissRequest = { serviceCreatorTargetId = null }) {
            ServiceEditorContent(
                title = "Новая услуга",
                name = newServiceName,
                price = newServicePrice,
                durationMinutes = newServiceDuration,
                onNameChange = { newServiceName = it },
                onPriceChange = { newServicePrice = it },
                onDurationChange = { newServiceDuration = it },
                onCancel = { serviceCreatorTargetId = null },
                onSave = {
                    viewModel.saveService(newServiceName, newServicePrice, newServiceDuration) { saved ->
                        val draft = serviceDrafts.firstOrNull { it.localId == targetId }
                        if (draft != null) {
                            updateServiceDraft(
                                targetId,
                                draft.copy(
                                    serviceId = saved.id,
                                    serviceName = saved.name,
                                    price = formatMoneyPlain(saved.priceCents),
                                    durationMinutes = saved.durationMinutes
                                )
                            )
                        }
                        serviceCreatorTargetId = null
                    }
                }
            )
        }
    }

    if (showCalendarDatePicker) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate.toPickerMillis())
        DatePickerDialog(
            onDismissRequest = { showCalendarDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        pickerState.selectedDateMillis?.let {
                            selectedDate = pickerMillisToLocalDate(it)
                        }
                        showCalendarDatePicker = false
                    }
                ) { Text("ОК") }
            },
            dismissButton = {
                TextButton(onClick = { showCalendarDatePicker = false }) { Text("Отмена") }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }

    if (showDatePicker) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = appointmentDate.toPickerMillis())
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        pickerState.selectedDateMillis?.let {
                            appointmentDate = pickerMillisToLocalDate(it)
                        }
                        showDatePicker = false
                    }
                ) { Text("ОК") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }

    if (showTimePicker) {
        val parsedTime = remember(appointmentTime) {
            runCatching { LocalTime.parse(appointmentTime) }.getOrElse { LocalTime.of(10, 0) }
        }
        val timePickerState = rememberTimePickerState(
            initialHour = parsedTime.hour,
            initialMinute = parsedTime.minute,
            is24Hour = android.text.format.DateFormat.is24HourFormat(context)
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Выберите время") },
            text = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TimePicker(state = timePickerState)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Отмена") }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        appointmentTime = "%02d:%02d".format(Locale.US, timePickerState.hour, timePickerState.minute)
                        showTimePicker = false
                    }
                ) { Text("ОК") }
            }
        )
    }

}

@Composable
private fun AppointmentsCalendarHeader(
    calendarView: AppointmentCalendarView,
    selectedDate: LocalDate,
    searchActive: Boolean,
    searchQuery: String,
    onSearchActiveChange: (Boolean) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onToday: () -> Unit,
    onPreviousPeriod: () -> Unit,
    onNextPeriod: () -> Unit,
    onPickDate: () -> Unit,
    onViewChange: (AppointmentCalendarView) -> Unit
) {
    var viewMenuExpanded by remember { mutableStateOf(false) }
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onPreviousPeriod) {
                    Icon(Icons.Filled.ChevronLeft, contentDescription = "Предыдущий период")
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    TextButton(onClick = { viewMenuExpanded = true }) {
                        Text(
                            calendarPeriodTitle(calendarView, selectedDate),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(Icons.Filled.ExpandMore, contentDescription = "Выбрать вид")
                    }
                    DropdownMenu(expanded = viewMenuExpanded, onDismissRequest = { viewMenuExpanded = false }) {
                        AppointmentCalendarView.values().forEach { view ->
                            DropdownMenuItem(
                                text = { Text(view.label) },
                                onClick = {
                                    onViewChange(view)
                                    viewMenuExpanded = false
                                }
                            )
                        }
                    }
                }
                IconButton(onClick = onNextPeriod) {
                    Icon(Icons.Filled.ChevronRight, contentDescription = "Следующий период")
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onToday) {
                    Text("Сегодня")
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onSearchActiveChange(!searchActive) }) {
                    Icon(Icons.Filled.Search, contentDescription = "Поиск")
                }
                IconButton(onClick = onPickDate) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Выбрать дату")
                }
            }
            if (searchActive) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    label = { Text("Поиск записей") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(Icons.Filled.Close, contentDescription = "Очистить")
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun AppointmentTimelineView(
    calendarView: AppointmentCalendarView,
    selectedDate: LocalDate,
    appointments: List<AppointmentRow>,
    hasCalendarPermissions: Boolean,
    onAppointmentClick: (AppointmentRow) -> Unit,
    onEmptySlotClick: (LocalDate, LocalTime) -> Unit
) {
    val dates = visibleTimelineDates(calendarView, selectedDate)
    val hourHeight = 82.dp
    val timelineHeight = hourHeight * 24
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 56.dp, end = 8.dp, top = 8.dp, bottom = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            dates.forEach { date ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        date.format(dayOfWeekFormatter),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (date == LocalDate.now()) FontWeight.Bold else FontWeight.Normal,
                        color = if (date == LocalDate.now()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(timelineHeight)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    (0..23).forEach { hour ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(hourHeight)
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                "%02d:00".format(hour),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .width(48.dp)
                                    .padding(top = 6.dp)
                            )
                            dates.forEach { date ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(hourHeight)
                                        .padding(1.dp)
                                        .background(
                                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
                                            RoundedCornerShape(6.dp)
                                        )
                                        .clickable {
                                            onEmptySlotClick(date, LocalTime.of(hour, 0))
                                        }
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 56.dp, end = 8.dp)
                ) {
                    dates.forEach { date ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(horizontal = 1.dp)
                        ) {
                            appointments
                                .mapNotNull { appointment ->
                                    parseAppointmentDateTime(appointment.startAt)
                                        ?.takeIf { it.toLocalDate() == date }
                                        ?.let { start -> appointment to start }
                                }
                                .sortedBy { it.second.toLocalTime() }
                                .forEach { (appointment, start) ->
                                    val startMinute = start.hour * 60 + start.minute
                                    val visibleDuration = appointment.durationMinutes
                                        .coerceAtLeast(15)
                                        .coerceAtMost((24 * 60 - startMinute).coerceAtLeast(15))
                                    val topOffset = hourHeight * (startMinute / 60f)
                                    val cardHeight = (hourHeight * (visibleDuration / 60f)).coerceAtLeast(58.dp)
                                    AppointmentTimelineCard(
                                        appointment = appointment,
                                        hasCalendarPermissions = hasCalendarPermissions,
                                        onClick = onAppointmentClick,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .offset(y = topOffset)
                                            .height(cardHeight)
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                        }
                    }
                }
            }
            Spacer(Modifier.height(96.dp))
        }
    }
}

@Composable
private fun AppointmentTimelineCard(
    appointment: AppointmentRow,
    hasCalendarPermissions: Boolean,
    onClick: (AppointmentRow) -> Unit,
    modifier: Modifier = Modifier
) {
    val isCancelled = appointment.status == APPOINTMENT_STATUS_CANCELLED
    Surface(
        color = if (isCancelled) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
        contentColor = if (isCancelled) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick(appointment) }
    ) {
        Column(modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)) {
            Text(
                appointment.service.ifBlank { "Запись" },
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "${appointmentTimeRange(appointment)} · ${appointment.clientName}",
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (isCancelled) {
                Text(
                    "отменена",
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun AppointmentMonthView(
    selectedDate: LocalDate,
    appointments: List<AppointmentRow>,
    hasCalendarPermissions: Boolean,
    onAppointmentClick: (AppointmentRow) -> Unit,
    onDayClick: (LocalDate) -> Unit
) {
    val firstDay = selectedDate.withDayOfMonth(1)
    val gridStart = firstDay.minusDays((firstDay.dayOfWeek.value - 1).toLong())
    val days = (0 until 42).map { gridStart.plusDays(it.toLong()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 6.dp, vertical = 8.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            (1..7).map { java.time.DayOfWeek.of(it) }.forEach { day ->
                Text(
                    day.getDisplayName(java.time.format.TextStyle.SHORT, ruLocale),
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        days.chunked(7).forEach { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEach { day ->
                    val dayAppointments = appointments.filter {
                        parseAppointmentDateTime(it.startAt)?.toLocalDate() == day
                    }
                    AppointmentMonthDayCell(
                        day = day,
                        selectedMonth = selectedDate.month,
                        appointments = dayAppointments,
                        hasCalendarPermissions = hasCalendarPermissions,
                        onAppointmentClick = onAppointmentClick,
                        onClick = { onDayClick(day) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        Spacer(Modifier.height(96.dp))
    }
}

@Composable
private fun AppointmentMonthDayCell(
    day: LocalDate,
    selectedMonth: java.time.Month,
    appointments: List<AppointmentRow>,
    hasCalendarPermissions: Boolean,
    onAppointmentClick: (AppointmentRow) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isToday = day == LocalDate.now()
    val inMonth = day.month == selectedMonth
    val background = if (isToday) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (inMonth) 0.22f else 0.10f)
    }
    Column(
        modifier = modifier
            .height(108.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            day.dayOfMonth.toString(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
            color = if (inMonth) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
        appointments.take(2).forEach { appointment ->
            val isCancelled = appointment.status == APPOINTMENT_STATUS_CANCELLED
            Surface(
                color = when {
                    isCancelled -> MaterialTheme.colorScheme.errorContainer
                    appointment.calendarEventId > 0 -> MaterialTheme.colorScheme.tertiaryContainer
                    else -> MaterialTheme.colorScheme.primaryContainer
                },
                contentColor = when {
                    isCancelled -> MaterialTheme.colorScheme.onErrorContainer
                    appointment.calendarEventId > 0 -> MaterialTheme.colorScheme.onTertiaryContainer
                    else -> MaterialTheme.colorScheme.onPrimaryContainer
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
                    .clickable { onAppointmentClick(appointment) }
            ) {
                Text(
                    "${appointmentTimeOnly(appointment)} ${if (isCancelled) "Отменена: " else ""}${appointment.service}",
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }
        }
        if (appointments.size > 2) {
            Text(
                "+${appointments.size - 2}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AppointmentDetailScreen(
    appointment: AppointmentRow,
    client: ClientEntity?,
    hasCalendarPermissions: Boolean,
    onBack: () -> Unit,
    onEdit: (() -> Unit)?,
    onMove: (() -> Unit)?,
    onCancel: (() -> Unit)?,
    onStatusChange: ((String) -> Unit)?,
    onClientClick: (() -> Unit)? = null,
    onRecordPayment: ((String, String, String, Boolean) -> Unit)? = null
) {
    val isCancelled = appointment.status == APPOINTMENT_STATUS_CANCELLED
    val canMoveOrCancel = appointmentCanMoveOrCancel(appointment)
    var showStatusSheet by remember(appointment.id, appointment.status, appointment.startAt) { mutableStateOf(false) }
    var showPaymentSheet by remember(appointment.id) { mutableStateOf(false) }
    var showPaymentDatePicker by remember(appointment.id) { mutableStateOf(false) }
    var paymentAmount by remember(appointment.id) { mutableStateOf("") }
    var paymentMethod by remember(appointment.id) { mutableStateOf(paymentMethodOptions.first()) }
    var paymentDate by remember(appointment.id) { mutableStateOf(LocalDate.now()) }
    val canRecordPayment = onRecordPayment != null &&
        appointment.priceCents > 0L &&
        appointmentRemainingCents(appointment) > 0L &&
        !appointmentIsCancelled(appointment)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Запись", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (onEdit != null) {
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Filled.Edit, contentDescription = "Изменить запись")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ContactAvatar(
                        name = client?.name ?: appointment.clientName,
                        photoUri = client?.contactPhotoUri.orEmpty(),
                        modifier = Modifier.size(112.dp)
                    )
                    Text(
                        appointment.service.ifBlank { "Запись" },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        appointment.clientName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
            item {
                DetailCard {
                    AppointmentDetailRow(
                        icon = Icons.Filled.Person,
                        title = appointment.clientName,
                        subtitle = listOf(client?.phone.orEmpty(), client?.email.orEmpty())
                            .filter { it.isNotBlank() }
                            .joinToString(" · ")
                            .ifBlank { "Клиент" },
                        onClick = onClientClick,
                        action = if (onClientClick != null) {
                            {
                                Icon(Icons.Filled.ChevronRight, contentDescription = null)
                            }
                        } else {
                            null
                        }
                    )
                    AppointmentDetailRow(
                        icon = Icons.Filled.Event,
                        title = formatAppointmentDateTimeForDetail(appointment),
                        subtitle = "${appointmentTimeRange(appointment)} · ${formatDurationForUi(appointment.durationMinutes)}"
                    )
                    AppointmentDetailRow(
                        icon = Icons.Filled.Payments,
                        title = formatMoney(appointment.priceCents),
                        subtitle = paymentDetailText(appointment),
                        action = if (canRecordPayment) {
                            {
                                IconButton(
                                    onClick = {
                                        paymentAmount = formatMoneyPlain(appointmentRemainingCents(appointment))
                                        paymentMethod = appointment.paymentMethod.ifBlank { paymentMethodOptions.first() }
                                        paymentDate = LocalDate.now()
                                        showPaymentSheet = true
                                    }
                                ) {
                                    Icon(Icons.Filled.ExpandMore, contentDescription = "Изменить оплату")
                                }
                            }
                        } else {
                            null
                        }
                    )
                    AppointmentDetailRow(
                        icon = Icons.Filled.Sync,
                        title = "Календарь",
                        subtitle = calendarStatusLabel(appointment, hasCalendarPermissions)
                    )
                    AppointmentDetailRow(
                        icon = Icons.Filled.Event,
                        title = "Статус",
                        subtitle = appointmentUiStatusLabel(appointment),
                        action = if (onStatusChange != null) {
                            {
                                IconButton(onClick = { showStatusSheet = true }) {
                                    Icon(Icons.Filled.ExpandMore, contentDescription = "Изменить статус")
                                }
                            }
                        } else {
                            null
                        }
                    )
                }
            }
            if (appointment.notes.isNotBlank()) {
                item {
                    DetailCard {
                        AppointmentDetailRow(
                            icon = Icons.Filled.Notes,
                            title = "Комментарий",
                            subtitle = appointment.notes
                        )
                    }
                }
            }
            if (isCancelled && onMove != null) {
                item {
                    Button(
                        onClick = onMove,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp)
                    ) {
                        Icon(Icons.Filled.AccessTime, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Перенести и возобновить", maxLines = 1)
                    }
                }
            } else if ((onMove != null || onCancel != null) && canMoveOrCancel) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (onCancel != null) {
                            Button(
                                onClick = onCancel,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(28.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp)
                            ) {
                                Icon(Icons.Filled.Close, contentDescription = null)
                                Spacer(Modifier.width(6.dp))
                                Text("Отменить", maxLines = 1)
                            }
                        }
                        if (onMove != null) {
                            OutlinedButton(
                                onClick = onMove,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(28.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp)
                            ) {
                                Icon(Icons.Filled.AccessTime, contentDescription = null)
                                Spacer(Modifier.width(6.dp))
                                Text("Перенести", maxLines = 1)
                            }
                        }
                    }
                }
            }
        }
    }
    if (showPaymentSheet && onRecordPayment != null) {
        val paymentSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showPaymentSheet = false },
            sheetState = paymentSheetState
        ) {
            AppointmentPaymentEditor(
                appointment = appointment,
                amount = paymentAmount,
                paymentMethod = paymentMethod,
                paidAt = paymentDate,
                onAmountChange = { paymentAmount = it },
                onMethodChange = { paymentMethod = it },
                onOpenDatePicker = { showPaymentDatePicker = true },
                onSave = {
                    onRecordPayment(paymentAmount, paymentMethod, financeDateString(paymentDate), false)
                    showPaymentSheet = false
                },
                onPayInFull = {
                    onRecordPayment(paymentAmount, paymentMethod, financeDateString(paymentDate), true)
                    showPaymentSheet = false
                }
            )
        }
    }
    if (showPaymentDatePicker) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = paymentDate.toPickerMillis())
        DatePickerDialog(
            onDismissRequest = { showPaymentDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        pickerState.selectedDateMillis?.let { paymentDate = pickerMillisToLocalDate(it) }
                        showPaymentDatePicker = false
                    }
                ) {
                    Text("ОК")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPaymentDatePicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
    if (showStatusSheet && onStatusChange != null) {
        AppointmentStatusSheet(
            appointment = appointment,
            onDismiss = { showStatusSheet = false },
            onStatusSelected = { status ->
                onStatusChange(status)
                showStatusSheet = false
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AppointmentMoveSheet(
    appointment: AppointmentRow,
    title: String = "Перенести",
    onDismiss: () -> Unit,
    onSave: (LocalDate, String) -> Unit
) {
    val context = LocalContext.current
    val initialDateTime = remember(appointment.id, appointment.startAt) {
        parseAppointmentDateTime(appointment.startAt)
    }
    var date by remember(appointment.id, appointment.startAt) {
        mutableStateOf(initialDateTime?.toLocalDate() ?: LocalDate.now())
    }
    var time by remember(appointment.id, appointment.startAt) {
        mutableStateOf(initialDateTime?.toLocalTime()?.let { "%02d:%02d".format(Locale.US, it.hour, it.minute) } ?: "10:00")
    }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                appointment.service.ifBlank { "Запись" },
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                appointment.clientName,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { showDatePicker = true }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Filled.DateRange, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(formatAppointmentDateForUi(date), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                OutlinedButton(onClick = { showTimePicker = true }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Filled.AccessTime, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(time)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                    Text("Отмена")
                }
                Button(
                    onClick = { onSave(date, time) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("Сохранить")
                }
            }
        }
    }

    if (showDatePicker) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = date.toPickerMillis())
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        pickerState.selectedDateMillis?.let {
                            date = pickerMillisToLocalDate(it)
                        }
                        showDatePicker = false
                    }
                ) { Text("ОК") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }

    if (showTimePicker) {
        val parsedTime = remember(time) {
            runCatching { LocalTime.parse(time) }.getOrElse { LocalTime.of(10, 0) }
        }
        val timePickerState = rememberTimePickerState(
            initialHour = parsedTime.hour,
            initialMinute = parsedTime.minute,
            is24Hour = android.text.format.DateFormat.is24HourFormat(context)
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Выберите время") },
            text = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TimePicker(state = timePickerState)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Отмена") }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        time = "%02d:%02d".format(Locale.US, timePickerState.hour, timePickerState.minute)
                        showTimePicker = false
                    }
                ) { Text("ОК") }
            }
        )
    }
}

@Composable
private fun DetailCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            content = content
        )
    }
}

@Composable
private fun AppointmentDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    action: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            if (subtitle.isNotBlank()) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (action != null) {
            Spacer(Modifier.width(8.dp))
            action()
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AppointmentStatusSheet(
    appointment: AppointmentRow,
    onDismiss: () -> Unit,
    onStatusSelected: (String) -> Unit
) {
    val activeStatus = appointmentActiveUiStatusLabel(appointment)
    val selectedPersistedStatus = if (appointmentIsCancelled(appointment)) {
        APPOINTMENT_STATUS_CANCELLED
    } else {
        APPOINTMENT_STATUS_PLANNED
    }
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Статус записи", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            AppointmentStatusOption(
                icon = Icons.Filled.Event,
                title = activeStatus,
                subtitle = "Активная запись. Статус меняется по времени записи.",
                selected = selectedPersistedStatus == APPOINTMENT_STATUS_PLANNED,
                onClick = { onStatusSelected(APPOINTMENT_STATUS_PLANNED) }
            )
            AppointmentStatusOption(
                icon = Icons.Filled.Close,
                title = APPOINTMENT_STATUS_CANCELLED,
                subtitle = "Отменить запись, убрать долг и обновить календарь.",
                selected = selectedPersistedStatus == APPOINTMENT_STATUS_CANCELLED,
                onClick = { onStatusSelected(APPOINTMENT_STATUS_CANCELLED) }
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AppointmentStatusOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        color = if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
        contentColor = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(icon, contentDescription = null)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (selected) {
                Text("Выбран", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AppointmentEditorContent(
    title: String,
    clients: List<ClientEntity>,
    services: List<ServiceEntity>,
    selectedClientId: Long,
    serviceDrafts: List<AppointmentServiceDraft>,
    appointmentDate: LocalDate,
    appointmentTime: String,
    notes: String,
    onClose: () -> Unit,
    onOpenClientPicker: () -> Unit,
    onServiceDraftChange: (Int, AppointmentServiceDraft) -> Unit,
    onAddService: () -> Unit,
    onRemoveService: (Int) -> Unit,
    onCreateService: (Int) -> Unit,
    onOpenDatePicker: () -> Unit,
    onOpenTimePicker: () -> Unit,
    onNotesChange: (String) -> Unit,
    onSave: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val selectedClient = clients.firstOrNull { it.id == selectedClientId }
    val selectedServices = serviceDrafts.filter { it.isSelected }
    val duplicateServiceIds = selectedServices
        .map { it.serviceId }
        .filter { it > 0 }
        .groupingBy { it }
        .eachCount()
        .filterValues { it > 1 }
        .keys
    val totalDuration = selectedServices.sumOf { it.durationMinutes.coerceAtLeast(15) }
    val totalPrice = selectedServices.sumOf { it.priceCents }
    val canSave = selectedClientId != 0L && selectedServices.isNotEmpty() && duplicateServiceIds.isEmpty()
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(112.dp),
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Filled.Close, contentDescription = "Закрыть")
                    }
                },
                title = { Text(title) },
                actions = {
                    Button(
                        onClick = onSave,
                        enabled = canSave,
                        shape = RoundedCornerShape(28.dp),
                        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp)
                    ) {
                        Text("Сохранить")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 32.dp, end = 32.dp, top = 26.dp, bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AppointmentClientRow(
                    client = selectedClient,
                    onClick = onOpenClientPicker
                )
            }
            item {
                Text(
                    "Услуги",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            items(serviceDrafts, key = { it.localId }) { draft ->
                val unavailableServiceIds = selectedServices
                    .filter { it.localId != draft.localId }
                    .map { it.serviceId }
                    .toSet()
                AppointmentServiceDraftBlock(
                    draft = draft,
                    services = services.filter { service -> service.id !in unavailableServiceIds },
                    canRemove = serviceDrafts.size > 1,
                    onChange = { onServiceDraftChange(draft.localId, it) },
                    onRemove = { onRemoveService(draft.localId) },
                    onCreateService = { onCreateService(draft.localId) }
                )
            }
            item {
                OutlinedButton(onClick = onAddService, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Добавить услугу")
                }
            }
            item {
                InfoCard {
                    Text("Итого", fontWeight = FontWeight.Bold)
                    Text("Длительность: ${if (selectedServices.isEmpty()) 0 else totalDuration} мин")
                    Text("Цена: ${formatMoney(totalPrice)}")
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = onOpenDatePicker, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.DateRange, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(formatAppointmentDateForUi(appointmentDate), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    OutlinedButton(onClick = onOpenTimePicker, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.AccessTime, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(appointmentTime)
                    }
                }
            }
            item {
                AppointmentNotesField(
                    notes = notes,
                    onNotesChange = onNotesChange
                )
            }
            if (onDelete != null) {
                item {
                    Button(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Icon(Icons.Filled.RemoveCircleOutline, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Удалить запись")
                    }
                }
            }
        }
    }
    if (showDeleteConfirm && onDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Удалить запись?") },
            text = { Text("Запись будет удалена из CRM. Если она связана с календарем и есть разрешение, событие календаря тоже будет удалено.") },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Отмена")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDelete()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text("Удалить")
                }
            }
        )
    }
}

@Composable
private fun AppointmentConflictDialog(
    conflictingAppointment: AppointmentRow?,
    newStartAt: String,
    newDurationMinutes: Int,
    onKeepEditing: () -> Unit,
    onSaveAndMoveConflicting: () -> Unit
) {
    val newStart = parseAppointmentDateTime(newStartAt)
    val newTime = if (newStart != null) {
        val end = newStart.plusMinutes(newDurationMinutes.coerceAtLeast(15).toLong())
        "${newStart.toLocalDate()} ${"%02d:%02d".format(Locale.US, newStart.hour, newStart.minute)}-${"%02d:%02d".format(Locale.US, end.hour, end.minute)}"
    } else {
        newStartAt
    }
    val conflictText = conflictingAppointment?.let {
        "${it.service} · ${it.clientName} · ${appointmentTimeRange(it)}"
    } ?: "другая запись"

    AlertDialog(
        onDismissRequest = onKeepEditing,
        title = { Text("Время уже занято") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Эта запись попадает на интервал: $newTime.")
                Text("В это время уже есть запись: $conflictText.")
                Text(
                    "Можно вернуться к редактированию или сохранить текущую запись и сразу открыть перенос пересекающейся записи.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onKeepEditing) {
                Text("Редактировать")
            }
        },
        confirmButton = {
            Button(onClick = onSaveAndMoveConflicting) {
                Text("Сохранить и перенести")
            }
        }
    )
}

@Composable
private fun AppointmentNotesField(
    notes: String,
    onNotesChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(notes.isNotBlank()) }
    LaunchedEffect(notes) {
        if (notes.isNotBlank()) expanded = true
    }
    if (!expanded && notes.isBlank()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .clickable { expanded = true },
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Notes, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Добавить заметку",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    } else {
        RemovableContactField(
            onRemove = {
                onNotesChange("")
                expanded = false
            }
        ) { fieldModifier ->
            OutlinedTextField(
                value = notes,
                onValueChange = onNotesChange,
                label = { Text("Заметка к записи") },
                leadingIcon = { Icon(Icons.Filled.Notes, contentDescription = null) },
                minLines = 3,
                modifier = fieldModifier
            )
        }
    }
}

@Composable
private fun AppointmentServiceDraftBlock(
    draft: AppointmentServiceDraft,
    services: List<ServiceEntity>,
    canRemove: Boolean,
    onChange: (AppointmentServiceDraft) -> Unit,
    onRemove: () -> Unit,
    onCreateService: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Услуга",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                if (canRemove) {
                    IconButton(onClick = onRemove, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Filled.RemoveCircleOutline, contentDescription = "Удалить услугу")
                    }
                }
            }
            ServicePickerButton(
                value = draft.serviceName.ifBlank { "Выбрать услугу" },
                services = services,
                onCreateService = onCreateService,
                onSelect = { service ->
                    onChange(
                        draft.copy(
                            serviceId = service.id,
                            serviceName = service.name,
                            price = formatMoneyPlain(service.priceCents),
                            durationMinutes = service.durationMinutes.coerceAtLeast(15)
                        )
                    )
                }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = draft.price,
                    onValueChange = { onChange(draft.copy(price = it)) },
                    label = { Text("Цена, ₽") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                DurationPickerField(
                    label = "Длительность",
                    durationMinutes = draft.durationMinutes,
                    onDurationChange = { onChange(draft.copy(durationMinutes = it)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ServicePickerButton(
    value: String,
    services: List<ServiceEntity>,
    onCreateService: () -> Unit,
    onSelect: (ServiceEntity) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(value, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("v")
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Создать услугу") },
                onClick = {
                    expanded = false
                    onCreateService()
                }
            )
            services.forEach { service ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(service.name)
                            Text(
                                "${formatMoney(service.priceCents)} · ${service.durationMinutes} мин",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        onSelect(service)
                    }
                )
            }
        }
    }
}

@Composable
private fun DurationPickerField(
    label: String,
    durationMinutes: Int,
    onDurationChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        OutlinedButton(onClick = { showPicker = true }, modifier = Modifier.fillMaxWidth()) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(formatDurationForUi(durationMinutes), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Icon(Icons.Filled.ExpandMore, contentDescription = null)
            }
        }
    }
    if (showPicker) {
        DurationPickerDialog(
            initialMinutes = durationMinutes,
            onDismiss = { showPicker = false },
            onConfirm = {
                onDurationChange(it)
                showPicker = false
            }
        )
    }
}

@Composable
private fun DurationPickerDialog(
    initialMinutes: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var selectedMinutes by remember(initialMinutes) {
        mutableStateOf(initialMinutes.coerceIn(15, 720).roundDurationToStep())
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Длительность услуги") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = formatDurationForUi(selectedMinutes),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { selectedMinutes = (selectedMinutes - 15).coerceAtLeast(15) },
                        enabled = selectedMinutes > 15,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("-15 мин")
                    }
                    OutlinedButton(
                        onClick = { selectedMinutes = (selectedMinutes + 15).coerceAtMost(720) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("+15 мин")
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    appointmentDurationOptions.chunked(3).forEach { rowOptions ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            rowOptions.forEach { option ->
                                val selected = selectedMinutes == option
                                if (selected) {
                                    Button(
                                        onClick = { selectedMinutes = option },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(formatDurationForUi(option), maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    }
                                } else {
                                    OutlinedButton(
                                        onClick = { selectedMinutes = option },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(formatDurationForUi(option), maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    }
                                }
                            }
                            repeat(3 - rowOptions.size) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedMinutes.coerceAtLeast(15)) }) {
                Text("ОК")
            }
        }
    )
}

private fun Int.roundDurationToStep(): Int {
    val step = 15
    return (((this + step - 1) / step) * step).coerceIn(step, 720)
}

private fun formatDurationForUi(minutes: Int): String {
    val safeMinutes = minutes.coerceAtLeast(0)
    val hours = safeMinutes / 60
    val restMinutes = safeMinutes % 60
    return when {
        hours == 0 -> "$restMinutes мин"
        restMinutes == 0 -> "$hours ч"
        else -> "$hours ч $restMinutes мин"
    }
}

@Composable
private fun ServiceEditorContent(
    title: String,
    name: String,
    price: String,
    durationMinutes: Int,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onDurationChange: (Int) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            IconButton(onClick = onCancel) {
                Icon(Icons.Filled.Close, contentDescription = "Закрыть")
            }
        }
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Название услуги") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = price,
                onValueChange = onPriceChange,
                label = { Text("Цена, ₽") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            DurationPickerField(
                label = "Длительность",
                durationMinutes = durationMinutes,
                onDurationChange = onDurationChange,
                modifier = Modifier.weight(1f)
            )
        }
        Button(
            onClick = onSave,
            enabled = name.trim().isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }
        Spacer(Modifier.height(12.dp))
    }
}

data class FinanceSummary(
    val accruedCents: Long,
    val paidCents: Long,
    val debtCents: Long,
    val expenseCents: Long
) {
    val totalCents: Long get() = paidCents - expenseCents
}

private data class ClientFinanceTotals(
    val accruedCents: Long,
    val paidCents: Long,
    val debtCents: Long,
    val expenseCents: Long
) {
    val totalCents: Long get() = paidCents - expenseCents
}

data class FinanceJournalItem(
    val key: String,
    val kind: String,
    val title: String,
    val subtitle: String,
    val amountCents: Long,
    val date: String,
    val appointment: AppointmentRow? = null,
    val isDebt: Boolean = false
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FinanceScreen(
    viewModel: AppViewModel,
    appointments: List<AppointmentRow>,
    transactions: List<FinanceTransactionEntity>,
    onEditorOpenChange: (Boolean) -> Unit
) {
    var selectedMonth by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var filter by remember { mutableStateOf(financeFilterOptions.first()) }
    var showTransactionSheet by remember { mutableStateOf(false) }
    var showPaymentSheet by remember { mutableStateOf(false) }
    var showTransactionDatePicker by remember { mutableStateOf(false) }
    var showPaymentDatePicker by remember { mutableStateOf(false) }
    var transactionType by remember { mutableStateOf(FINANCE_TYPE_EXPENSE) }
    var transactionAmount by remember { mutableStateOf("") }
    var transactionTitle by remember { mutableStateOf("") }
    var transactionCategory by remember { mutableStateOf("") }
    var transactionDate by remember { mutableStateOf(LocalDate.now()) }
    var transactionMethod by remember { mutableStateOf(paymentMethodOptions.first()) }
    var transactionNotes by remember { mutableStateOf("") }
    var selectedPaymentAppointment by remember { mutableStateOf<AppointmentRow?>(null) }
    var paymentAmount by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf(paymentMethodOptions.first()) }
    var paymentDate by remember { mutableStateOf(LocalDate.now()) }

    val summary = remember(appointments, transactions, selectedMonth) {
        financeSummaryForMonth(appointments, transactions, selectedMonth)
    }
    val journalItems = remember(appointments, transactions, selectedMonth, filter) {
        financeJournalForMonth(appointments, transactions, selectedMonth)
            .filter { item ->
                when (filter) {
                    "Доходы" -> item.kind == FINANCE_TYPE_INCOME
                    "Расходы" -> item.kind == FINANCE_TYPE_EXPENSE
                    "Долги" -> item.isDebt
                    else -> true
                }
            }
            .sortedWith(compareByDescending<FinanceJournalItem> { parseFinanceLocalDate(it.date) ?: LocalDate.MIN }.thenByDescending { it.key })
    }

    fun openTransactionEditor() {
        showTransactionSheet = true
        onEditorOpenChange(true)
    }

    fun closeTransactionEditor() {
        showTransactionSheet = false
        onEditorOpenChange(false)
    }

    fun saveTransactionDraft() {
        viewModel.addFinanceTransaction(
            type = transactionType,
            amount = transactionAmount,
            title = transactionTitle,
            category = transactionCategory,
            date = financeDateString(transactionDate),
            paymentMethod = transactionMethod,
            notes = transactionNotes
        )
        closeTransactionEditor()
    }

    BackHandler(enabled = showTransactionSheet) {
        closeTransactionEditor()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 112.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                FinanceMonthHeader(
                    selectedMonth = selectedMonth,
                    onPrevious = { selectedMonth = selectedMonth.minusMonths(1) },
                    onNext = { selectedMonth = selectedMonth.plusMonths(1) },
                    onCurrent = { selectedMonth = LocalDate.now().withDayOfMonth(1) }
                )
            }
            item { FinanceSummaryGrid(summary) }
            item {
                FinanceFilterRow(
                    selectedFilter = filter,
                    onFilterChange = { filter = it }
                )
            }
            if (journalItems.isEmpty()) {
                item { EmptyText("За этот месяц операций нет.") }
            } else {
                items(journalItems, key = { it.key }) { item ->
                    FinanceJournalRow(
                        item = item,
                        onClick = {
                            val appointment = item.appointment
                            if (appointment != null && item.isDebt) {
                                selectedPaymentAppointment = appointment
                                paymentAmount = formatMoneyPlain(appointmentRemainingCents(appointment))
                                paymentMethod = appointment.paymentMethod.ifBlank { paymentMethodOptions.first() }
                                paymentDate = LocalDate.now()
                                showPaymentSheet = true
                            }
                        }
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = {
                transactionType = FINANCE_TYPE_EXPENSE
                transactionAmount = ""
                transactionTitle = ""
                transactionCategory = ""
                transactionDate = LocalDate.now()
                transactionMethod = paymentMethodOptions.first()
                transactionNotes = ""
                openTransactionEditor()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Добавить операцию")
        }
    }

    if (showTransactionSheet) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            FinanceTransactionEditor(
                type = transactionType,
                amount = transactionAmount,
                title = transactionTitle,
                category = transactionCategory,
                date = transactionDate,
                paymentMethod = transactionMethod,
                notes = transactionNotes,
                onTypeChange = { transactionType = it },
                onAmountChange = { transactionAmount = it },
                onTitleChange = { transactionTitle = it },
                onCategoryChange = { transactionCategory = it },
                onOpenDatePicker = { showTransactionDatePicker = true },
                onPaymentMethodChange = { transactionMethod = it },
                onNotesChange = { transactionNotes = it },
                onClose = { closeTransactionEditor() },
                onSave = { saveTransactionDraft() }
            )
        }
    }

    selectedPaymentAppointment?.let { appointment ->
        if (showPaymentSheet) {
            val paymentSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = { showPaymentSheet = false },
                sheetState = paymentSheetState
            ) {
                AppointmentPaymentEditor(
                    appointment = appointment,
                    amount = paymentAmount,
                    paymentMethod = paymentMethod,
                    paidAt = paymentDate,
                    onAmountChange = { paymentAmount = it },
                    onMethodChange = { paymentMethod = it },
                    onOpenDatePicker = { showPaymentDatePicker = true },
                    onSave = {
                        viewModel.recordAppointmentPayment(
                            appointmentId = appointment.id,
                            amount = paymentAmount,
                            paymentMethod = paymentMethod,
                            paidAt = financeDateString(paymentDate),
                            payInFull = false
                        )
                        showPaymentSheet = false
                    },
                    onPayInFull = {
                        viewModel.recordAppointmentPayment(
                            appointmentId = appointment.id,
                            amount = paymentAmount,
                            paymentMethod = paymentMethod,
                            paidAt = financeDateString(paymentDate),
                            payInFull = true
                        )
                        showPaymentSheet = false
                    }
                )
            }
        }
    }

    if (showTransactionDatePicker) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = transactionDate.toPickerMillis())
        DatePickerDialog(
            onDismissRequest = { showTransactionDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        pickerState.selectedDateMillis?.let { transactionDate = pickerMillisToLocalDate(it) }
                        showTransactionDatePicker = false
                    }
                ) { Text("ОК") }
            },
            dismissButton = { TextButton(onClick = { showTransactionDatePicker = false }) { Text("Отмена") } }
        ) { DatePicker(state = pickerState) }
    }
    if (showPaymentDatePicker) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = paymentDate.toPickerMillis())
        DatePickerDialog(
            onDismissRequest = { showPaymentDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        pickerState.selectedDateMillis?.let { paymentDate = pickerMillisToLocalDate(it) }
                        showPaymentDatePicker = false
                    }
                ) { Text("ОК") }
            },
            dismissButton = { TextButton(onClick = { showPaymentDatePicker = false }) { Text("Отмена") } }
        ) { DatePicker(state = pickerState) }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FinanceFilterRow(
    selectedFilter: String,
    onFilterChange: (String) -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(28.dp))
                .clickable { showSheet = true },
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    financeFilterIcon(selectedFilter),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    financeFilterLabel(selectedFilter),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(Icons.Filled.ExpandMore, contentDescription = "Выбрать фильтр")
            }
        }
    }
    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                financeFilterOptions.forEach { option ->
                    val selected = option == selectedFilter
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .clickable {
                                onFilterChange(option)
                                showSheet = false
                            },
                        shape = RoundedCornerShape(18.dp),
                        color = if (selected) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                financeFilterIcon(option),
                                contentDescription = null,
                                tint = if (selected) {
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                            Spacer(Modifier.width(18.dp))
                            Text(
                                financeFilterLabel(option),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected) {
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FinanceMonthHeader(
    selectedMonth: LocalDate,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onCurrent: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = onPrevious) {
            Icon(Icons.Filled.ChevronLeft, contentDescription = "Предыдущий месяц")
        }
        Text(
            selectedMonth.format(monthTitleFormatter).replaceFirstChar { it.titlecase(ruLocale) },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onNext) {
            Icon(Icons.Filled.ChevronRight, contentDescription = "Следующий месяц")
        }
        TextButton(onClick = onCurrent) { Text("Этот") }
    }
}

@Composable
private fun FinanceSummaryGrid(summary: FinanceSummary) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            FinanceSummaryCard("Начислено", summary.accruedCents, Modifier.weight(1f))
            FinanceSummaryCard("Оплачено", summary.paidCents, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            FinanceSummaryCard("Долги", summary.debtCents, Modifier.weight(1f))
            FinanceSummaryCard("Расходы", summary.expenseCents, Modifier.weight(1f))
        }
        FinanceSummaryCard("Итого", summary.totalCents, Modifier.fillMaxWidth(), highlight = true)
    }
}

@Composable
private fun FinanceSummaryCard(title: String, amountCents: Long, modifier: Modifier = Modifier, highlight: Boolean = false) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = if (highlight) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(formatMoney(amountCents), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun FinanceJournalRow(item: FinanceJournalItem, onClick: () -> Unit) {
    val amountText = when {
        item.isDebt -> formatMoney(item.amountCents)
        item.kind == FINANCE_TYPE_EXPENSE -> "-${formatMoney(item.amountCents)}"
        else -> "+${formatMoney(item.amountCents)}"
    }
    val amountColor = when {
        item.isDebt -> MaterialTheme.colorScheme.error
        item.kind == FINANCE_TYPE_EXPENSE -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.primary
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = item.appointment != null && item.isDebt, onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(item.title, fontWeight = FontWeight.Bold)
                Text(item.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(financeDateDisplay(item.date), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(amountText, color = amountColor, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FinanceTransactionEditor(
    type: String,
    amount: String,
    title: String,
    category: String,
    date: LocalDate,
    paymentMethod: String,
    notes: String,
    onTypeChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onTitleChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onOpenDatePicker: () -> Unit,
    onPaymentMethodChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onClose: () -> Unit,
    onSave: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(112.dp),
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Filled.Close, contentDescription = "Закрыть")
                    }
                },
                title = { Text("Новая\nоперация") },
                actions = {
                    Button(
                        onClick = onSave,
                        enabled = amount.isNotBlank() && title.isNotBlank(),
                        shape = RoundedCornerShape(28.dp),
                        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp)
                    ) {
                        Text("Сохранить")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 32.dp, end = 32.dp, top = 26.dp, bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    financeTypeOptions.forEach { option ->
                        val label = if (option == FINANCE_TYPE_EXPENSE) "Расход" else "Доход"
                        if (type == option) {
                            Button(onClick = { onTypeChange(option) }, modifier = Modifier.weight(1f)) { Text(label) }
                        } else {
                            OutlinedButton(onClick = { onTypeChange(option) }, modifier = Modifier.weight(1f)) { Text(label) }
                        }
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = amount,
                    onValueChange = onAmountChange,
                    label = { Text("Сумма, ₽") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(value = title, onValueChange = onTitleChange, label = { Text("Название") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedTextField(value = category, onValueChange = onCategoryChange, label = { Text("Категория") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedButton(onClick = onOpenDatePicker, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Filled.DateRange, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(financeDateDisplay(financeDateString(date)))
                }
            }
            item {
                DropdownField("Способ оплаты", paymentMethod) { close ->
                    paymentMethodOptions.forEach { option ->
                        DropdownMenuItem(text = { Text(option) }, onClick = { onPaymentMethodChange(option); close() })
                    }
                }
            }
            item {
                OutlinedTextField(value = notes, onValueChange = onNotesChange, label = { Text("Комментарий") }, minLines = 3, modifier = Modifier.fillMaxWidth())
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun AppointmentPaymentEditor(
    appointment: AppointmentRow,
    amount: String,
    paymentMethod: String,
    paidAt: LocalDate,
    onAmountChange: (String) -> Unit,
    onMethodChange: (String) -> Unit,
    onOpenDatePicker: () -> Unit,
    onSave: () -> Unit,
    onPayInFull: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Оплата записи", style = MaterialTheme.typography.headlineSmall)
            Text("${appointment.service} · ${appointment.clientName}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Остаток: ${formatMoney(appointmentRemainingCents(appointment))}", fontWeight = FontWeight.Bold)
        }
        item {
            OutlinedTextField(
                value = amount,
                onValueChange = onAmountChange,
                label = { Text("Сумма оплаты, ₽") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            DropdownField("Способ оплаты", paymentMethod) { close ->
                paymentMethodOptions.forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = { onMethodChange(option); close() })
                }
            }
        }
        item {
            OutlinedButton(onClick = onOpenDatePicker, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.DateRange, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(financeDateDisplay(financeDateString(paidAt)))
            }
        }
        item {
            Button(onClick = onPayInFull, modifier = Modifier.fillMaxWidth()) {
                Text("Оплачено полностью")
            }
        }
        item {
            OutlinedButton(onClick = onSave, modifier = Modifier.fillMaxWidth(), enabled = amount.isNotBlank()) {
                Text("Сохранить оплату")
            }
        }
    }
}

@Composable
private fun AppointmentClickableRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            if (!subtitle.isNullOrBlank()) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null)
    }
}

@Composable
private fun AppointmentClientRow(
    client: ClientEntity?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (client != null) {
            ContactAvatar(name = client.name, photoUri = client.contactPhotoUri)
        } else {
            Icon(Icons.Filled.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(client?.name ?: "Выбрать клиента", style = MaterialTheme.typography.titleMedium)
            Text(
                client?.phone?.takeIf { it.isNotBlank() } ?: "Обязательное поле",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null)
    }
}

@Composable
private fun AppointmentClientPicker(
    clients: List<ClientEntity>,
    selectedClientId: Long,
    query: String,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit,
    onSelect: (Long) -> Unit
) {
    val filteredClients = clients.filter {
        val needle = query.trim().lowercase(Locale.getDefault())
        needle.isBlank() ||
            it.name.lowercase(Locale.getDefault()).contains(needle) ||
            it.phone.contains(needle)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
            }
            Text("Выберите клиента", style = MaterialTheme.typography.headlineSmall)
        }
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text("Поиск") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filteredClients) { client ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(client.id) }
                        .background(
                            if (client.id == selectedClientId) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ContactAvatar(name = client.name, photoUri = client.contactPhotoUri)
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(client.name, style = MaterialTheme.typography.titleMedium)
                        if (client.phone.isNotBlank()) {
                            Text(client.phone, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AutomationSettingsSection(
    viewModel: AppViewModel,
    rules: List<AutomationRuleEntity>,
    services: List<ServiceEntity>,
    onEditorOpenChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var editorRule by remember { mutableStateOf<AutomationRuleEntity?>(null) }
    var showEditor by remember { mutableStateOf(false) }
    var notificationPermissionGranted by remember { mutableStateOf(hasAutomationNotificationPermission(context)) }
    var smsPermissionGranted by remember {
        mutableStateOf(context.checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
    }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        notificationPermissionGranted = granted || hasAutomationNotificationPermission(context)
    }
    val smsPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        smsPermissionGranted = granted
    }

    fun openEditor(rule: AutomationRuleEntity?) {
        editorRule = rule
        showEditor = true
        onEditorOpenChange(true)
    }

    fun closeEditor() {
        showEditor = false
        editorRule = null
        onEditorOpenChange(false)
    }

    BackHandler(enabled = showEditor) {
        closeEditor()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                SectionTitle("Автоматизация")
                Text(
                    "Сценарии создают SMS-задачи в очереди. Отправка зависит от разрешения SMS и состояния телефона.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            item {
                Button(onClick = { openEditor(null) }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Создать сценарий")
                }
            }
            if (!smsPermissionGranted) {
                item {
                    InfoCard {
                        Text("SMS-разрешение отключено", fontWeight = FontWeight.Bold)
                        Text(
                            "Без SEND_SMS задачи останутся в очереди со статусом «Нужно разрешение».",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { smsPermissionLauncher.launch(Manifest.permission.SEND_SMS) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Разрешить SMS")
                        }
                    }
                }
            }
            if (Build.VERSION.SDK_INT >= 33 && !notificationPermissionGranted) {
                item {
                    InfoCard {
                        Text("Уведомления отключены", fontWeight = FontWeight.Bold)
                        Text(
                            "Сценарии с подтверждением останутся в очереди, но системный вопрос не появится.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Разрешить уведомления")
                        }
                    }
                }
            }
            item {
                SectionTitle("Сценарии (${rules.size})")
            }
            if (rules.isEmpty()) {
                item { EmptyText("Сценариев пока нет. Создай первый сценарий.") }
            }
            items(rules, key = { it.id }) { rule ->
                InfoCard {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Checkbox(
                            checked = rule.enabled,
                            onCheckedChange = { viewModel.toggleRule(rule, it) }
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(rule.name, fontWeight = FontWeight.Bold)
                            Text("${rule.channel} · ${automationTriggerLabel(rule.triggerType)} · ${automationRuleTimingText(rule)}")
                            Text(automationServiceFilterLabel(rule, services), color = MaterialTheme.colorScheme.onSurfaceVariant)
                            if (rule.askBeforeRun) {
                                Text("Перед отправкой нужно подтверждение", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                    Text("Шаблон: ${rule.message}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(onClick = { openEditor(rule) }, modifier = Modifier.weight(1f)) {
                            Text("Изменить")
                        }
                        OutlinedButton(onClick = { viewModel.deleteRule(rule) }, modifier = Modifier.weight(1f)) {
                            Text("Удалить")
                        }
                    }
                }
            }
        }

        if (showEditor) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surface
            ) {
                AutomationRuleEditorContent(
                    viewModel = viewModel,
                    rule = editorRule,
                    services = services,
                    onClose = { closeEditor() }
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AutomationRuleEditorContent(
    viewModel: AppViewModel,
    rule: AutomationRuleEntity?,
    services: List<ServiceEntity>,
    onClose: () -> Unit
) {
    var name by remember(rule?.id) { mutableStateOf(rule?.name.orEmpty()) }
    var triggerType by remember(rule?.id) {
        mutableStateOf(rule?.triggerType?.ifBlank { AUTOMATION_TRIGGER_NONE } ?: AUTOMATION_TRIGGER_BEFORE_APPOINTMENT)
    }
    var offsetMinutes by remember(rule?.id) { mutableStateOf((rule?.offsetMinutes ?: 1440).coerceAtLeast(0).toString()) }
    var retentionDays by remember(rule?.id) { mutableStateOf((rule?.retentionDays ?: 30).coerceAtLeast(1).toString()) }
    var askBeforeRun by remember(rule?.id) { mutableStateOf(rule?.askBeforeRun ?: false) }
    var template by remember(rule?.id) {
        mutableStateOf(rule?.message ?: "Здравствуйте, {client.name}! Напоминаем о записи: {appointment.service}, {appointment.date} в {appointment.time}.")
    }
    val selectedServiceIds = remember(rule?.id) {
        mutableStateListOf<Long>().apply {
            addAll(
                rule?.serviceFilterIds
                    ?.split(",")
                    ?.mapNotNull { it.trim().toLongOrNull() }
                    ?.filter { it > 0L }
                    .orEmpty()
            )
        }
    }
    var showTriggerSheet by remember { mutableStateOf(false) }
    var showServicesSheet by remember { mutableStateOf(false) }
    val serviceFilterText = if (selectedServiceIds.isEmpty()) {
        "Все услуги"
    } else {
        services.filter { it.id in selectedServiceIds }.joinToString(", ") { it.name }
            .ifBlank { "Выбрано: ${selectedServiceIds.size}" }
    }
    val canSave = name.isNotBlank() && template.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(112.dp),
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Filled.Close, contentDescription = "Закрыть")
                    }
                },
                title = { Text(if (rule == null) "Новый\nсценарий" else "Изменить\nсценарий") },
                actions = {
                    Button(
                        onClick = {
                            viewModel.saveRule(
                                name = name,
                                channel = CHANNEL_SMS,
                                triggerType = triggerType,
                                offsetMinutes = offsetMinutes.toIntOrNull() ?: 0,
                                retentionDays = retentionDays.toIntOrNull() ?: 0,
                                serviceFilterIds = selectedServiceIds.toSet(),
                                condition = if (triggerType == AUTOMATION_TRIGGER_DEBT_AFTER_APPOINTMENT) {
                                    AUTOMATION_CONDITION_DEBT_REMAINING
                                } else {
                                    ""
                                },
                                askBeforeRun = askBeforeRun,
                                messageTemplate = template,
                                ruleId = rule?.id
                            )
                            onClose()
                        },
                        enabled = canSave,
                        shape = RoundedCornerShape(28.dp),
                        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp)
                    ) {
                        Text("Сохранить")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 32.dp, end = 32.dp, top = 26.dp, bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                OutlinedTextField(name, { name = it }, label = { Text("Название сценария") }, modifier = Modifier.fillMaxWidth())
            }
            item {
                OutlinedButton(onClick = { showTriggerSheet = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(automationTriggerLabel(triggerType), modifier = Modifier.weight(1f))
                    Icon(Icons.Filled.ExpandMore, contentDescription = null)
                }
            }
            if (triggerType == AUTOMATION_TRIGGER_BEFORE_APPOINTMENT || triggerType == AUTOMATION_TRIGGER_AFTER_APPOINTMENT) {
                item {
                    OutlinedTextField(
                        value = offsetMinutes,
                        onValueChange = { offsetMinutes = it.filter(Char::isDigit) },
                        label = { Text("Смещение, минут") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            if (triggerType == AUTOMATION_TRIGGER_DEBT_AFTER_APPOINTMENT || triggerType == AUTOMATION_TRIGGER_RETENTION_BY_SERVICE) {
                item {
                    OutlinedTextField(
                        value = retentionDays,
                        onValueChange = { retentionDays = it.filter(Char::isDigit) },
                        label = { Text("Через сколько дней") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            item {
                OutlinedButton(onClick = { showServicesSheet = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(serviceFilterText, modifier = Modifier.weight(1f))
                    Icon(Icons.Filled.ExpandMore, contentDescription = null)
                }
            }
            item {
                InfoCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { askBeforeRun = !askBeforeRun },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = askBeforeRun,
                            onCheckedChange = { askBeforeRun = it }
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Спрашивать перед выполнением", fontWeight = FontWeight.Bold)
                            Text(
                                "В момент отправки появится системное уведомление с кнопками. Без разрешения на уведомления задача останется в очереди.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = template,
                    onValueChange = { template = it },
                    label = { Text("Шаблон сообщения") },
                    minLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Text("Доступные поля", fontWeight = FontWeight.Bold)
                Text(
                    "Нажми на поле, чтобы вставить его в шаблон. Старые {client}, {service}, {time} тоже работают.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            automationTemplateFields.chunked(2).forEach { rowFields ->
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        rowFields.forEach { field ->
                            OutlinedButton(
                                onClick = { template = template.trimEnd() + " " + field },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(field, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        if (rowFields.size == 1) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }

    if (showTriggerSheet) {
        ModalBottomSheet(onDismissRequest = { showTriggerSheet = false }) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Триггер", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                automationTriggerTypes.forEach { value ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                triggerType = value
                                showTriggerSheet = false
                            }
                            .background(
                                if (value == triggerType) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            )
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(automationTriggerLabel(value), modifier = Modifier.weight(1f))
                        if (value == triggerType) {
                            Icon(Icons.Filled.Check, contentDescription = null)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (showServicesSheet) {
        ModalBottomSheet(onDismissRequest = { showServicesSheet = false }) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Услуги для сценария", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Если ничего не выбрать, сценарий работает для всех услуг.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                services.forEach { service ->
                    val checked = service.id in selectedServiceIds
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (checked) {
                                    selectedServiceIds.remove(service.id)
                                } else if (service.id !in selectedServiceIds) {
                                    selectedServiceIds.add(service.id)
                                }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = {
                                if (it && service.id !in selectedServiceIds) {
                                    selectedServiceIds.add(service.id)
                                } else if (!it) {
                                    selectedServiceIds.remove(service.id)
                                }
                            }
                        )
                        Column {
                            Text(service.name, fontWeight = FontWeight.SemiBold)
                            Text(
                                "${formatMoney(service.priceCents)} · ${service.durationMinutes} мин",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { selectedServiceIds.clear() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Все услуги")
                    }
                    Button(
                        onClick = { showServicesSheet = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Готово")
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun QueueSettingsSection(viewModel: AppViewModel, outbox: List<OutboxRow>) {
    var selectedFilter by rememberSaveable { mutableStateOf(QueueFilter.All) }
    val filteredOutbox = remember(outbox, selectedFilter) {
        outbox.filter { selectedFilter.matches(it) }
    }
    QueueFilterRow(
        selectedFilter = selectedFilter,
        outbox = outbox,
        onFilterChange = { selectedFilter = it },
        onSendAll = { viewModel.sendAllQueued() }
    )
    if (filteredOutbox.isEmpty()) {
        EmptyText("Очередь пуста. Создай запись, чтобы активные сценарии добавили задачи.")
    }
    filteredOutbox.forEach {
        InfoCard {
            Text("${it.channel}: ${it.ruleName}", fontWeight = FontWeight.Bold)
            Text("Клиент: ${it.clientName}")
            Text("Когда: ${it.scheduledAt.ifBlank { "-" }}")
            Text("Статус: ${outboxDisplayStatus(it)}")
            Text("Текст: ${it.payload}")
            if (it.error.isNotBlank() && it.error != OUTBOX_CONFIRMATION_REQUIRED) {
                Text("Ошибка: ${it.error}", color = MaterialTheme.colorScheme.error)
            }
            if (it.sentAt.isNotBlank()) Text("Отправлено: ${it.sentAt}", color = MaterialTheme.colorScheme.primary)
            if (it.status != STATUS_SENT && it.status != STATUS_CANCELLED) {
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { viewModel.sendNow(it.id) }) { Text("Отправить сейчас") }
            }
        }
    }
}

private enum class QueueFilter(val label: String) {
    All("Все"),
    Waiting("Ждут"),
    NeedAction("Требуют действия"),
    Sent("Отправлены"),
    Problems("Ошибки");

    fun matches(row: OutboxRow): Boolean = when (this) {
        All -> true
        Waiting -> row.status == STATUS_QUEUED
        NeedAction -> row.status == STATUS_NEEDS_CONFIG || row.status == STATUS_WAITING_PERMISSION
        Sent -> row.status == STATUS_SENT
        Problems -> row.status == STATUS_FAILED || row.status == STATUS_CANCELLED
    }
}

private fun queueFilterLabel(filter: QueueFilter): String = when (filter) {
    QueueFilter.All -> "Все задачи"
    QueueFilter.Waiting -> "Ждут отправки"
    QueueFilter.NeedAction -> "Требуют действия"
    QueueFilter.Sent -> "Отправлены"
    QueueFilter.Problems -> "Ошибки и отмененные"
}

private fun queueFilterIcon(filter: QueueFilter): ImageVector = when (filter) {
    QueueFilter.Waiting -> Icons.Filled.AccessTime
    QueueFilter.NeedAction -> Icons.Filled.Sms
    QueueFilter.Sent -> Icons.Filled.Check
    QueueFilter.Problems -> Icons.Filled.RemoveCircleOutline
    QueueFilter.All -> Icons.Filled.Notes
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun QueueFilterRow(
    selectedFilter: QueueFilter,
    outbox: List<OutboxRow>,
    onFilterChange: (QueueFilter) -> Unit,
    onSendAll: () -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(28.dp))
                .clickable { showSheet = true },
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    queueFilterIcon(selectedFilter),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    queueFilterLabel(selectedFilter),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(Icons.Filled.ExpandMore, contentDescription = "Выбрать фильтр")
            }
        }
        Spacer(Modifier.width(10.dp))
        Button(onClick = onSendAll) {
            Text("Отправить все", maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }

    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                QueueFilter.values().forEach { filter ->
                    val selected = filter == selectedFilter
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .clickable {
                                onFilterChange(filter)
                                showSheet = false
                            },
                        shape = RoundedCornerShape(18.dp),
                        color = if (selected) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                queueFilterIcon(filter),
                                contentDescription = null,
                                tint = if (selected) {
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                            Spacer(Modifier.width(18.dp))
                            Text(
                                queueFilterLabel(filter),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected) {
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                outbox.count { filter.matches(it) }.toString(),
                                color = if (selected) {
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun AutomationConfirmationDialog(
    outbox: OutboxRow,
    onSend: () -> Unit,
    onSkip: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Отправить сообщение?") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("${outbox.ruleName} · ${outbox.channel}", fontWeight = FontWeight.Bold)
                Text("Клиент: ${outbox.clientName}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(outbox.payload)
            }
        },
        confirmButton = {
            Button(onClick = onSend) {
                Text("Отправить")
            }
        },
        dismissButton = {
            TextButton(onClick = onSkip) {
                Text("Пропустить")
            }
        }
    )
}

@Composable
private fun BackupSettingsSection(viewModel: AppViewModel) {
    val context = LocalContext.current
    val autoBackup = viewModel.autoBackupState
    val createBackup = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        if (uri != null) viewModel.exportBackup(context, uri)
    }
    val openBackup = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) viewModel.importBackup(context, uri)
    }
    val pickAutoBackupFolder = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        if (uri != null) viewModel.saveAutoBackupFolder(context, uri)
    }

    SectionTitle("Резервные копии")
    Text("Backup сохраняется как JSON-файл через системный выбор файла. Интернет не нужен.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    InfoCard {
        Text("Автоматический backup", fontWeight = FontWeight.Bold)
        Text(
            "Приложение будет сохранять JSON-копии в выбранную системную папку. Файлы создаются локально, без интернета.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        Text("Папка: ${if (autoBackup.folderUri.isBlank()) "не выбрана" else "выбрана"}")
        Text("Последний backup: ${autoBackup.lastRunAt.ifBlank { "-" }}", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        DropdownField("Периодичность", autoBackupModeLabel(autoBackup.mode)) { close ->
            autoBackupModeOptions.forEach { mode ->
                DropdownMenuItem(
                    text = { Text(autoBackupModeLabel(mode)) },
                    onClick = {
                        viewModel.saveAutoBackupMode(mode)
                        close()
                    }
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { pickAutoBackupFolder.launch(null) }, modifier = Modifier.weight(1f)) {
                Text(if (autoBackup.folderUri.isBlank()) "Выбрать папку" else "Сменить папку")
            }
            Button(
                onClick = { viewModel.runAutoBackupNow(context) },
                enabled = autoBackup.folderUri.isNotBlank(),
                modifier = Modifier.weight(1f)
            ) {
                Text("Создать сейчас")
            }
        }
    }
    InfoCard {
        Text("В базе сейчас", fontWeight = FontWeight.Bold)
        Text("Клиентов: ${viewModel.counts.clients}")
        Text("Записей: ${viewModel.counts.appointments}")
        Text("Сценариев: ${viewModel.counts.rules}")
        Text("Задач в очереди: ${viewModel.counts.outbox}")
    }
    Button(
        onClick = { createBackup.launch("offline-beauty-crm-backup-${fileStamp()}.json") },
        modifier = Modifier.fillMaxWidth()
    ) { Text("Экспорт backup") }
    OutlinedButton(
        onClick = { openBackup.launch(arrayOf("application/json")) },
        modifier = Modifier.fillMaxWidth()
    ) { Text("Импорт backup") }
}

@Composable
private fun UpdatesSettingsSection() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var state by remember { mutableStateOf<UpdateCheckState>(UpdateCheckState.Idle) }
    var downloadedApk by remember { mutableStateOf<File?>(null) }
    var isDownloading by rememberSaveable { mutableStateOf(false) }
    var downloadMessage by rememberSaveable { mutableStateOf("") }
    val currentVersion = remember { GitHubUpdates.currentVersion(context) }

    fun checkUpdates() {
        state = UpdateCheckState.Checking
        downloadedApk = null
        downloadMessage = ""
        scope.launch {
            state = GitHubUpdates.check(context.applicationContext)
        }
    }

    LaunchedEffect(Unit) {
        state = UpdateCheckState.Checking
        state = GitHubUpdates.check(context.applicationContext)
    }

    SectionTitle("Проверка обновлений")
    Text(
        "Приложение проверяет стабильные GitHub Releases репозитория IgorNadein/12609. Если в релизе есть APK, его можно скачать и установить через системный установщик Android.",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    InfoCard {
        Text("Установленная версия", fontWeight = FontWeight.Bold)
        Text("${currentVersion.versionName} · code ${currentVersion.versionCode}", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }

    when (val currentState = state) {
        UpdateCheckState.Idle,
        UpdateCheckState.Checking -> InfoCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(28.dp))
                Column {
                    Text("Проверяю обновления", fontWeight = FontWeight.Bold)
                    Text("Нужен интернет", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        is UpdateCheckState.Current -> InfoCard {
            Text("Установлена последняя версия", fontWeight = FontWeight.Bold)
            Text(
                "Последний релиз: ${currentState.release.tagName}${formatGitHubPublishedAt(currentState.release.publishedAt).let { if (it.isBlank()) "" else " · $it" }}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Button(onClick = { checkUpdates() }, modifier = Modifier.fillMaxWidth()) {
                Text("Проверить снова")
            }
        }

        is UpdateCheckState.Available -> InfoCard {
            Text("Доступно обновление ${currentState.release.tagName}", fontWeight = FontWeight.Bold)
            Text(
                "Сейчас установлена ${currentState.currentVersion.versionName}. APK: ${currentState.release.apkName}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            val publishedAt = formatGitHubPublishedAt(currentState.release.publishedAt)
            if (publishedAt.isNotBlank()) {
                Text("Опубликовано: $publishedAt", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (currentState.release.body.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    currentState.release.body,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 8,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (downloadMessage.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(downloadMessage, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = {
                    scope.launch {
                        isDownloading = true
                        downloadMessage = ""
                        try {
                            val apk = downloadedApk ?: GitHubUpdates.downloadApk(context.applicationContext, currentState.release)
                            downloadedApk = apk
                            GitHubUpdates.installApk(context.applicationContext, apk)
                            downloadMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                                !context.packageManager.canRequestPackageInstalls()
                            ) {
                                "Разреши установку из этого источника, затем вернись и нажми кнопку еще раз."
                            } else {
                                "Открыт системный установщик Android."
                            }
                        } catch (e: ActivityNotFoundException) {
                            downloadMessage = "Не удалось открыть системный установщик APK"
                        } catch (e: Exception) {
                            downloadMessage = "Не удалось скачать обновление: ${e.message ?: "ошибка"}"
                        } finally {
                            isDownloading = false
                        }
                    }
                },
                enabled = !isDownloading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isDownloading) "Скачиваю..." else "Скачать и установить")
            }
            OutlinedButton(
                onClick = {
                    runCatching { GitHubUpdates.openRelease(context.applicationContext, currentState.release) }
                        .onFailure { downloadMessage = "Не удалось открыть страницу релиза" }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Открыть релиз")
            }
            OutlinedButton(onClick = { checkUpdates() }, modifier = Modifier.fillMaxWidth()) {
                Text("Проверить снова")
            }
        }

        is UpdateCheckState.Error -> InfoCard {
            Text("Не удалось проверить обновления", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
            Text(currentState.message, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Button(onClick = { checkUpdates() }, modifier = Modifier.fillMaxWidth()) {
                Text("Проверить снова")
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ServicesSettingsSection(viewModel: AppViewModel, services: List<ServiceEntity>) {
    var editingService by remember { mutableStateOf<ServiceEntity?>(null) }
    var showEditor by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var durationMinutes by remember { mutableStateOf(60) }

    fun openEditor(service: ServiceEntity?) {
        editingService = service
        name = service?.name.orEmpty()
        price = service?.let { formatMoneyPlain(it.priceCents) }.orEmpty()
        durationMinutes = service?.durationMinutes ?: 60
        showEditor = true
    }

    SectionTitle("Услуги")
    Text(
        "Каталог используется при создании записей. Старые записи хранят снимок услуг и не меняются при редактировании каталога.",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Button(onClick = { openEditor(null) }, modifier = Modifier.fillMaxWidth()) {
        Icon(Icons.Filled.Add, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text("Создать услугу")
    }
    if (services.isEmpty()) {
        EmptyText("Каталог пуст. Создай первую услугу.")
    }
    services.forEach { service ->
        InfoCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(service.name, fontWeight = FontWeight.Bold)
                    Text("${formatMoney(service.priceCents)} · ${service.durationMinutes} мин", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                TextButton(onClick = { openEditor(service) }) {
                    Text("Изменить")
                }
            }
            OutlinedButton(onClick = { viewModel.archiveService(service) }, modifier = Modifier.fillMaxWidth()) {
                Text("Скрыть из каталога")
            }
        }
    }

    if (showEditor) {
        ModalBottomSheet(onDismissRequest = { showEditor = false }) {
            ServiceEditorContent(
                title = if (editingService == null) "Новая услуга" else "Изменить услугу",
                name = name,
                price = price,
                durationMinutes = durationMinutes,
                onNameChange = { name = it },
                onPriceChange = { price = it },
                onDurationChange = { durationMinutes = it },
                onCancel = { showEditor = false },
                onSave = {
                    viewModel.saveService(
                        name = name,
                        price = price,
                        durationMinutes = durationMinutes,
                        serviceId = editingService?.id
                    )
                    showEditor = false
                }
            )
        }
    }
}

@Composable
private fun SyncSettingsSection(viewModel: AppViewModel, syncSettings: List<SyncSettingsEntity>) {
    val context = LocalContext.current
    val contactPermissions = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
    val calendarPermissions = arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
    fun hasPermissions(permissions: Array<String>): Boolean = permissions.all { permission ->
        context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }
    var contactPermissionsGranted by remember { mutableStateOf(hasPermissions(contactPermissions)) }
    var calendarPermissionsGranted by remember { mutableStateOf(hasPermissions(calendarPermissions)) }
    val contactPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        contactPermissionsGranted = hasPermissions(contactPermissions)
    }
    val calendarPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        calendarPermissionsGranted = hasPermissions(calendarPermissions)
    }
    val contactsSetting = syncSettingFor(syncSettings, SYNC_RESOURCE_CONTACTS)
    val calendarSetting = syncSettingFor(syncSettings, SYNC_RESOURCE_CALENDAR)
    var writableCalendars by remember { mutableStateOf(emptyList<SystemCalendar>()) }

    LaunchedEffect(calendarPermissionsGranted) {
        writableCalendars = if (calendarPermissionsGranted) {
            withContext(Dispatchers.IO) { CalendarSync.writableCalendars(context.applicationContext) }
        } else {
            emptyList()
        }
    }

    SectionTitle("Синхронизация")
    Text(
        "Формы клиентов и записей всегда сохраняют данные локально. Разрешения и режимы обмена с системными контактами и календарем настраиваются здесь.",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(Modifier.height(12.dp))
    SyncResourceCard(
        title = "Контакты",
        subtitle = "Системная телефонная книга Android",
        icon = Icons.Filled.Person,
        setting = contactsSetting,
        permissionsGranted = contactPermissionsGranted,
        permissionsText = "READ_CONTACTS / WRITE_CONTACTS",
        onModeChange = { viewModel.saveSyncMode(SYNC_RESOURCE_CONTACTS, it) },
        onRequestPermissions = { contactPermissionLauncher.launch(contactPermissions) },
        onSyncNow = { viewModel.syncContacts(context) }
    )
    SyncResourceCard(
        title = "Календарь",
        subtitle = "Системный календарь Android",
        icon = Icons.Filled.DateRange,
        setting = calendarSetting,
        permissionsGranted = calendarPermissionsGranted,
        permissionsText = "READ_CALENDAR / WRITE_CALENDAR",
        onModeChange = { viewModel.saveSyncMode(SYNC_RESOURCE_CALENDAR, it) },
        onRequestPermissions = { calendarPermissionLauncher.launch(calendarPermissions) },
        onSyncNow = { viewModel.syncCalendar(context) }
    ) {
        CalendarTargetSelector(
            permissionsGranted = calendarPermissionsGranted,
            calendars = writableCalendars,
            setting = calendarSetting,
            onCalendarChange = { viewModel.saveCalendarTarget(it) }
        )
    }
}

@Composable
private fun SyncResourceCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    setting: SyncSettingsEntity,
    permissionsGranted: Boolean,
    permissionsText: String,
    onModeChange: (String) -> Unit,
    onRequestPermissions: () -> Unit,
    onSyncNow: () -> Unit,
    extraContent: @Composable ColumnScope.() -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            DropdownField("Режим", syncModeLabel(setting.resource, setting.mode)) { close ->
                syncModeOptions.forEach { mode ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(syncModeLabel(setting.resource, mode))
                                Text(
                                    syncModeDescription(setting.resource, mode),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        onClick = {
                            onModeChange(mode)
                            close()
                        }
                    )
                }
            }
            Text(syncModeDescription(setting.resource, setting.mode), color = MaterialTheme.colorScheme.onSurfaceVariant)
            extraContent()
            InfoCard {
                Text(if (permissionsGranted) "Разрешения выданы" else "Нужны разрешения", fontWeight = FontWeight.Bold)
                Text(permissionsText)
                Text("Последний запуск: ${setting.lastRunAt.ifBlank { "еще не запускалась" }}")
            }
            if (!permissionsGranted) {
                OutlinedButton(
                    onClick = onRequestPermissions,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Разрешить")
                }
            }
            Button(
                onClick = onSyncNow,
                enabled = setting.mode != SYNC_MODE_OFF,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Синхронизировать сейчас")
            }
        }
    }
}

@Composable
private fun CalendarTargetSelector(
    permissionsGranted: Boolean,
    calendars: List<SystemCalendar>,
    setting: SyncSettingsEntity,
    onCalendarChange: (Long?) -> Unit
) {
    if (!permissionsGranted) return
    val selectedId = setting.selectedSystemId.toLongOrNull()
    val selectedCalendar = calendars.firstOrNull { it.id == selectedId }
    val autoCalendar = CalendarSync.defaultWritableCalendar(calendars)
    val value = when {
        selectedCalendar != null -> calendarDisplayTitle(selectedCalendar)
        selectedId != null -> "Выбранный календарь недоступен"
        autoCalendar != null -> "Авто: ${calendarDisplayTitle(autoCalendar)}"
        else -> "Нет календаря для записи"
    }

    DropdownField("Календарь для записей", value) { close ->
        DropdownMenuItem(
            text = {
                Column {
                    Text("Авто: локальный календарь телефона")
                    Text(
                        autoCalendar?.let { calendarDisplaySubtitle(it) } ?: "Если локального нет, будет выбран основной доступный календарь",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            onClick = {
                onCalendarChange(null)
                close()
            }
        )
        calendars.forEach { calendar ->
            DropdownMenuItem(
                text = {
                    Column {
                        Text(calendarDisplayTitle(calendar))
                        Text(
                            calendarDisplaySubtitle(calendar),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                onClick = {
                    onCalendarChange(calendar.id)
                    close()
                }
            )
        }
    }
    Text(
        if (selectedId == null) {
            "Новые записи будут сохраняться в локальный календарь телефона, если он доступен."
        } else {
            "Новые записи будут сохраняться только в выбранный календарь."
        },
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun SettingsScreen(
    viewModel: AppViewModel,
    services: List<ServiceEntity>,
    syncSettings: List<SyncSettingsEntity>,
    rules: List<AutomationRuleEntity>,
    outbox: List<OutboxRow>,
    subScreen: SettingsSubScreen,
    onEditorOpenChange: (Boolean) -> Unit,
    onSubScreenChange: (SettingsSubScreen) -> Unit
) {
    when (subScreen) {
        SettingsSubScreen.Menu -> SettingsMenuScreen(
            services = services,
            rules = rules,
            outbox = outbox,
            syncSettings = syncSettings,
            appIconState = viewModel.appIconState,
            onOpen = onSubScreenChange
        )
        SettingsSubScreen.Branding -> ScrollPage {
            AppIconSettingsSection(viewModel)
        }
        SettingsSubScreen.Integrations -> ScrollPage {
            BookingBotsSettingsSection()
        }
        SettingsSubScreen.Sync -> ScrollPage {
            SyncSettingsSection(viewModel, syncSettings)
        }
        SettingsSubScreen.Automation ->
            AutomationSettingsSection(
                viewModel = viewModel,
                rules = rules,
                services = services,
                onEditorOpenChange = onEditorOpenChange
            )
        SettingsSubScreen.Services -> ScrollPage {
            ServicesSettingsSection(viewModel, services)
        }
        SettingsSubScreen.Queue -> ScrollPage {
            QueueSettingsSection(viewModel, outbox)
        }
        SettingsSubScreen.Backup -> ScrollPage {
            BackupSettingsSection(viewModel)
        }
        SettingsSubScreen.Updates -> ScrollPage {
            UpdatesSettingsSection()
        }
    }
}

@Composable
private fun SettingsMenuScreen(
    services: List<ServiceEntity>,
    rules: List<AutomationRuleEntity>,
    outbox: List<OutboxRow>,
    syncSettings: List<SyncSettingsEntity>,
    appIconState: AppIconState,
    onOpen: (SettingsSubScreen) -> Unit
) {
    val contactsMode = syncModeLabel(SYNC_RESOURCE_CONTACTS, syncSettingFor(syncSettings, SYNC_RESOURCE_CONTACTS).mode)
    val calendarMode = syncModeLabel(SYNC_RESOURCE_CALENDAR, syncSettingFor(syncSettings, SYNC_RESOURCE_CALENDAR).mode)
    val currentVersion = GitHubUpdates.currentVersion(LocalContext.current)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Управление приложением",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
            )
        }
        item {
            SettingsMenuItem(
                title = "Иконка приложения",
                subtitle = if (appIconState.isSet) "Изображение выбрано, можно создать ярлык" else "Фото из галереи для ярлыка на главном экране",
                icon = Icons.Filled.AddAPhoto,
                onClick = { onOpen(SettingsSubScreen.Branding) }
            )
        }
        item {
            SettingsMenuItem(
                title = "Интеграции",
                subtitle = "Боты для самостоятельной записи клиентов",
                icon = Icons.Filled.Public,
                onClick = { onOpen(SettingsSubScreen.Integrations) }
            )
        }
        item {
            SettingsMenuItem(
                title = "Синхронизация",
                subtitle = "Контакты: $contactsMode · Календарь: $calendarMode",
                icon = Icons.Filled.Sync,
                onClick = { onOpen(SettingsSubScreen.Sync) }
            )
        }
        item {
            SettingsMenuItem(
                title = "Услуги",
                subtitle = "В каталоге: ${services.size}",
                icon = Icons.Filled.Payments,
                onClick = { onOpen(SettingsSubScreen.Services) }
            )
        }
        item {
            SettingsMenuItem(
                title = "Автоматизация",
                subtitle = "Сценариев: ${rules.size}",
                icon = Icons.Filled.Event,
                onClick = { onOpen(SettingsSubScreen.Automation) }
            )
        }
        item {
            SettingsMenuItem(
                title = "Очередь задач",
                subtitle = "Задач: ${outbox.size}",
                icon = Icons.Filled.Notes,
                onClick = { onOpen(SettingsSubScreen.Queue) }
            )
        }
        item {
            SettingsMenuItem(
                title = "Резервные копии",
                subtitle = "Экспорт и импорт локальной базы JSON",
                icon = Icons.Filled.Settings,
                onClick = { onOpen(SettingsSubScreen.Backup) }
            )
        }
        item {
            SettingsMenuItem(
                title = "Проверка обновлений",
                subtitle = "Установлена версия ${currentVersion.versionName}",
                icon = Icons.Filled.Sync,
                onClick = { onOpen(SettingsSubScreen.Updates) }
            )
        }
    }
}

@Composable
private fun AppIconSettingsSection(viewModel: AppViewModel) {
    val context = LocalContext.current
    val iconState = viewModel.appIconState
    val iconPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            viewModel.saveAppIcon(context, uri)
        }
    }

    SectionTitle("Иконка приложения")
    InfoCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppIconPreview(
                state = iconState,
                modifier = Modifier.size(96.dp),
                contentDescription = "Выбранная иконка приложения"
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Выбранное изображение", fontWeight = FontWeight.Bold)
                Text(
                    text = if (iconState.isSet) "Готово для ярлыка на главном экране" else "Пока используется стандартная иконка",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { iconPicker.launch(arrayOf("image/*")) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.AddAPhoto, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(if (iconState.isSet) "Сменить фото" else "Выбрать фото")
        }
        Button(
            onClick = { viewModel.createAppIconShortcut(context) },
            enabled = iconState.isSet,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Business, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Создать ярлык с иконкой")
        }
        OutlinedButton(
            onClick = { viewModel.resetAppIcon() },
            enabled = iconState.isSet,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Close, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Сбросить изображение")
        }
    }
}

@Composable
private fun SettingsMenuItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun BookingBotsSettingsSection() {
    SectionTitle("Боты записи")
    Text(
        "Здесь будут настраиваться боты, через которые клиент сам выбирает услугу и свободное время. Это не каналы автоматической рассылки.",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(Modifier.height(12.dp))
    InfoCard {
        Text("Telegram-бот записи", fontWeight = FontWeight.Bold)
        Text(
            "План: бот показывает услуги, свободные окна и создает заявку на запись. Специалист подтверждает ее в CRM.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) {
            Text("Настройка позже")
        }
    }
    InfoCard {
        Text("Боты соцсетей", fontWeight = FontWeight.Bold)
        Text(
            "WhatsApp/соцсети будут подключаться как входящие заявки на запись, а не как автоуправление установленными приложениями.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) {
            Text("Настройка позже")
        }
    }
    InfoCard {
        Text("Авто-SMS", fontWeight = FontWeight.Bold)
        Text(
            "SMS-сценарии теперь настраиваются в разделе «Автоматизация». Здесь остаются только боты для ручной записи клиентов.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ContactAvatar(name: String, photoUri: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var image by remember(photoUri) { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(photoUri) {
        image = if (photoUri.isBlank()) {
            null
        } else {
            withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openInputStream(Uri.parse(photoUri))?.use { stream ->
                        BitmapFactory.decodeStream(stream)?.asImageBitmap()
                    }
                }.getOrNull()
            }
        }
    }
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        val loadedImage = image
        if (loadedImage != null) {
            Image(
                bitmap = loadedImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = initials(name),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ScrollPage(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = content
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
}

@Composable
private fun EmptyText(text: String) {
    Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 12.dp))
}

@Composable
private fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp), content = content)
    }
}

@Composable
private fun DropdownField(label: String, value: String, menu: @Composable (close: () -> Unit) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(value)
                Text("v")
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            menu { expanded = false }
        }
    }
}

private fun readContacts(context: Context): List<ContactCandidate> = ContactsSync.readCandidates(context)

private fun normalizePhone(value: String): String {
    val digits = value.filter { it.isDigit() }.toMutableList()
    if (digits.size == 11 && digits.first() == '8') digits[0] = '7'
    return digits.joinToString("")
}

private fun parseAppointmentMillis(value: String): Long? {
    val formats = listOf("yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss")
    return formats.firstNotNullOfOrNull { pattern ->
        runCatching {
            SimpleDateFormat(pattern, Locale.US).apply { isLenient = false }.parse(value.trim())?.time
        }.getOrNull()
    }
}

private fun formatAppointmentMillis(value: Long): String =
    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date(value))

private fun parseAppointmentDateTime(value: String): LocalDateTime? {
    val millis = parseAppointmentMillis(value) ?: return null
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime()
}

private fun formatAppointmentLocalDateTime(date: LocalDate, time: String): String {
    val parsedTime = runCatching { LocalTime.parse(time) }.getOrDefault(LocalTime.of(10, 0))
    return LocalDateTime.of(date, parsedTime).format(appointmentStorageFormatter)
}

private fun parseMoneyToCents(value: String): Long {
    val cleaned = value
        .trim()
        .lowercase(Locale.getDefault())
        .replace("₽", "")
        .replace("руб.", "")
        .replace("руб", "")
        .replace("р.", "")
        .replace("р", "")
        .replace(" ", "")
        .replace(",", ".")
    if (cleaned.isBlank()) return 0L
    val match = Regex("""\d+(?:\.\d{1,2})?""").find(cleaned)?.value ?: return 0L
    val parts = match.split(".")
    val rubles = parts.getOrNull(0)?.toLongOrNull() ?: 0L
    val cents = parts.getOrNull(1)?.padEnd(2, '0')?.take(2)?.toLongOrNull() ?: 0L
    return rubles * 100L + cents
}

private fun formatMoney(cents: Long): String {
    val sign = if (cents < 0) "-" else ""
    val absolute = kotlin.math.abs(cents)
    val rubles = absolute / 100L
    val kopeks = absolute % 100L
    return if (kopeks == 0L) {
        "$sign$rubles ₽"
    } else {
        "$sign$rubles.${"%02d".format(kopeks)} ₽"
    }
}

private fun formatMoneyPlain(cents: Long): String {
    val absolute = cents.coerceAtLeast(0)
    val rubles = absolute / 100L
    val kopeks = absolute % 100L
    return if (kopeks == 0L) rubles.toString() else "$rubles.${"%02d".format(kopeks)}"
}

private fun financeDateString(date: LocalDate): String =
    date.format(DateTimeFormatter.ISO_LOCAL_DATE)

private fun parseFinanceLocalDate(value: String): LocalDate? =
    runCatching { LocalDate.parse(value.take(10)) }.getOrNull()
        ?: parseAppointmentDateTime(value)?.toLocalDate()

private fun financeDateDisplay(value: String): String =
    parseFinanceLocalDate(value)?.format(dayMonthFormatter) ?: value

private fun isInFinanceMonth(date: LocalDate?, selectedMonth: LocalDate): Boolean =
    date != null && date.year == selectedMonth.year && date.month == selectedMonth.month

private fun appointmentIsCancelled(appointment: AppointmentRow): Boolean =
    appointment.status == APPOINTMENT_STATUS_CANCELLED

private fun appointmentBillableCents(appointment: AppointmentRow): Long =
    if (appointmentIsCancelled(appointment)) 0L else appointment.priceCents

private fun appointmentRemainingCents(appointment: AppointmentRow): Long =
    (appointmentBillableCents(appointment) - appointment.paidAmountCents).coerceAtLeast(0)

private fun appointmentBillableCents(appointment: AppointmentEntity): Long =
    if (appointment.status == APPOINTMENT_STATUS_CANCELLED) 0L else appointment.priceCents

private fun appointmentRemainingCents(appointment: AppointmentEntity): Long =
    (appointmentBillableCents(appointment) - appointment.paidAmountCents).coerceAtLeast(0)

private fun appointmentEndDateTime(appointment: AppointmentEntity): LocalDateTime? =
    parseAppointmentDateTime(appointment.startAt)
        ?.plusMinutes(appointment.durationMinutes.coerceAtLeast(15).toLong())

private fun formatDurationMinutes(minutes: Int): String {
    val safeMinutes = minutes.coerceAtLeast(0)
    val hours = safeMinutes / 60
    val rest = safeMinutes % 60
    return when {
        hours > 0 && rest > 0 -> "$hours ч $rest мин"
        hours > 0 -> "$hours ч"
        else -> "$rest мин"
    }
}

private fun clientFinanceTotals(
    clientId: Long,
    appointments: List<AppointmentRow>,
    transactions: List<FinanceTransactionEntity>
): ClientFinanceTotals {
    val appointmentIds = appointments.map { it.id }.toSet()
    val clientTransactions = transactions.filter { transaction ->
        transaction.clientId == clientId || transaction.appointmentId?.let { it in appointmentIds } == true
    }
    val accrued = appointments.sumOf { appointmentBillableCents(it) }
    val paidFromAppointments = appointments.sumOf { it.paidAmountCents }
    val manualIncome = clientTransactions
        .filter { it.type == FINANCE_TYPE_INCOME }
        .sumOf { it.amountCents }
    val expenses = clientTransactions
        .filter { it.type == FINANCE_TYPE_EXPENSE }
        .sumOf { it.amountCents }
    return ClientFinanceTotals(
        accruedCents = accrued,
        paidCents = paidFromAppointments + manualIncome,
        debtCents = appointments.sumOf { appointmentRemainingCents(it) },
        expenseCents = expenses
    )
}

private fun financeSummaryForMonth(
    appointments: List<AppointmentRow>,
    transactions: List<FinanceTransactionEntity>,
    selectedMonth: LocalDate
): FinanceSummary {
    val monthAppointments = appointments.filter { isInFinanceMonth(parseAppointmentDateTime(it.startAt)?.toLocalDate(), selectedMonth) }
    val accrued = monthAppointments.sumOf { appointmentBillableCents(it) }
    val debt = monthAppointments.sumOf { appointmentRemainingCents(it) }
    val appointmentPaid = appointments
        .filter { it.paidAmountCents > 0 && isInFinanceMonth(parseFinanceLocalDate(it.paidAt), selectedMonth) }
        .sumOf { it.paidAmountCents }
    val manualIncome = transactions
        .filter { it.type == FINANCE_TYPE_INCOME && isInFinanceMonth(parseFinanceLocalDate(it.date), selectedMonth) }
        .sumOf { it.amountCents }
    val expenses = transactions
        .filter { it.type == FINANCE_TYPE_EXPENSE && isInFinanceMonth(parseFinanceLocalDate(it.date), selectedMonth) }
        .sumOf { it.amountCents }
    return FinanceSummary(
        accruedCents = accrued,
        paidCents = appointmentPaid + manualIncome,
        debtCents = debt,
        expenseCents = expenses
    )
}

private fun financeJournalForMonth(
    appointments: List<AppointmentRow>,
    transactions: List<FinanceTransactionEntity>,
    selectedMonth: LocalDate
): List<FinanceJournalItem> = buildList {
    appointments.forEach { appointment ->
        val appointmentDate = parseAppointmentDateTime(appointment.startAt)?.toLocalDate()
        if (appointment.priceCents > 0 && appointment.paidAmountCents > 0 && isInFinanceMonth(parseFinanceLocalDate(appointment.paidAt), selectedMonth)) {
            add(
                FinanceJournalItem(
                    key = "appointment-paid-${appointment.id}",
                    kind = FINANCE_TYPE_INCOME,
                    title = appointment.service.ifBlank { "Оплата записи" },
                    subtitle = "${appointment.clientName} · ${paymentStatusLabel(appointment.paymentStatus)}",
                    amountCents = appointment.paidAmountCents,
                    date = appointment.paidAt.ifBlank { appointment.startAt },
                    appointment = appointment
                )
            )
        }
        val remaining = appointmentRemainingCents(appointment)
        if (appointmentBillableCents(appointment) > 0 && remaining > 0 && isInFinanceMonth(appointmentDate, selectedMonth)) {
            add(
                FinanceJournalItem(
                    key = "appointment-debt-${appointment.id}",
                    kind = "debt",
                    title = appointment.service.ifBlank { "Неоплаченная запись" },
                    subtitle = "${appointment.clientName} · ${paymentStatusLabel(appointment.paymentStatus)}",
                    amountCents = remaining,
                    date = appointment.startAt,
                    appointment = appointment,
                    isDebt = true
                )
            )
        }
    }
    transactions.forEach { transaction ->
        if (isInFinanceMonth(parseFinanceLocalDate(transaction.date), selectedMonth)) {
            add(
                FinanceJournalItem(
                    key = "transaction-${transaction.id}",
                    kind = transaction.type,
                    title = transaction.title,
                    subtitle = listOf(transaction.category, transaction.paymentMethod).filter { it.isNotBlank() }.joinToString(" · "),
                    amountCents = transaction.amountCents,
                    date = transaction.date
                )
            )
        }
    }
}

private fun LocalDate.toPickerMillis(): Long =
    atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun pickerMillisToLocalDate(value: Long): LocalDate =
    Instant.ofEpochMilli(value).atZone(ZoneOffset.UTC).toLocalDate()

private fun LocalDate.shiftCalendarPeriod(calendarView: AppointmentCalendarView, direction: Int): LocalDate {
    val step = direction.toLong()
    return when (calendarView) {
        AppointmentCalendarView.Day -> plusDays(step)
        AppointmentCalendarView.ThreeDays -> plusDays(step * 3)
        AppointmentCalendarView.Week -> plusWeeks(step)
        AppointmentCalendarView.Month -> plusMonths(step)
    }
}

private fun visibleTimelineDates(calendarView: AppointmentCalendarView, selectedDate: LocalDate): List<LocalDate> {
    return when (calendarView) {
        AppointmentCalendarView.Day -> listOf(selectedDate)
        AppointmentCalendarView.ThreeDays -> (0 until 3).map { selectedDate.plusDays(it.toLong()) }
        AppointmentCalendarView.Week -> {
            val start = selectedDate.minusDays((selectedDate.dayOfWeek.value - 1).toLong())
            (0 until 7).map { start.plusDays(it.toLong()) }
        }
        AppointmentCalendarView.Month -> listOf(selectedDate)
    }
}

private fun calendarPeriodTitle(calendarView: AppointmentCalendarView, selectedDate: LocalDate): String {
    return when (calendarView) {
        AppointmentCalendarView.Day -> selectedDate.format(dayMonthFormatter)
        AppointmentCalendarView.ThreeDays -> {
            val end = selectedDate.plusDays(2)
            "${selectedDate.format(dayMonthFormatter)} - ${end.format(dayMonthFormatter)}"
        }
        AppointmentCalendarView.Week -> {
            val start = selectedDate.minusDays((selectedDate.dayOfWeek.value - 1).toLong())
            val end = start.plusDays(6)
            "${start.format(dayMonthFormatter)} - ${end.format(dayMonthFormatter)}"
        }
        AppointmentCalendarView.Month -> selectedDate.format(monthTitleFormatter).replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(ruLocale) else it.toString()
        }
    }
}

private fun formatAppointmentDateForUi(date: LocalDate): String =
    date.format(DateTimeFormatter.ofPattern("d MMM yyyy", ruLocale))

private fun appointmentTimeOnly(appointment: AppointmentRow): String =
    parseAppointmentDateTime(appointment.startAt)?.toLocalTime()?.toString() ?: appointment.startAt

private fun appointmentTimeRange(appointment: AppointmentRow): String {
    val start = parseAppointmentDateTime(appointment.startAt) ?: return appointment.startAt
    val end = start.plusMinutes(appointment.durationMinutes.coerceAtLeast(15).toLong())
    return "${start.toLocalTime()}-${end.toLocalTime()}"
}

private fun findAppointmentConflict(
    appointments: List<AppointmentRow>,
    startAt: String,
    durationMinutes: Int,
    excludeAppointmentId: Long? = null
): AppointmentRow? {
    val start = parseAppointmentDateTime(startAt) ?: return null
    val end = start.plusMinutes(durationMinutes.coerceAtLeast(15).toLong())
    return appointments
        .asSequence()
        .filter { it.id != excludeAppointmentId }
        .filter { !appointmentIsCancelled(it) }
        .mapNotNull { appointment ->
            val otherStart = parseAppointmentDateTime(appointment.startAt) ?: return@mapNotNull null
            val otherEnd = otherStart.plusMinutes(appointment.durationMinutes.coerceAtLeast(15).toLong())
            if (start < otherEnd && end > otherStart) appointment else null
        }
        .minByOrNull { parseAppointmentMillis(it.startAt) ?: Long.MAX_VALUE }
}

private fun appointmentUiStatusLabel(appointment: AppointmentRow, now: LocalDateTime = LocalDateTime.now()): String {
    if (appointmentIsCancelled(appointment)) return APPOINTMENT_STATUS_CANCELLED
    val start = parseAppointmentDateTime(appointment.startAt)
        ?: return appointment.status.ifBlank { APPOINTMENT_STATUS_PLANNED }
    val end = start.plusMinutes(appointment.durationMinutes.coerceAtLeast(15).toLong())
    return when {
        now.isBefore(start) -> APPOINTMENT_STATUS_PLANNED
        now.isBefore(end) -> APPOINTMENT_STATUS_IN_PROGRESS
        else -> APPOINTMENT_STATUS_PAST
    }
}

private fun appointmentEntityUiStatusLabel(appointment: AppointmentEntity, now: LocalDateTime = LocalDateTime.now()): String {
    if (appointment.status == APPOINTMENT_STATUS_CANCELLED) return APPOINTMENT_STATUS_CANCELLED
    val start = parseAppointmentDateTime(appointment.startAt)
        ?: return appointment.status.ifBlank { APPOINTMENT_STATUS_PLANNED }
    val end = start.plusMinutes(appointment.durationMinutes.coerceAtLeast(15).toLong())
    return when {
        now.isBefore(start) -> APPOINTMENT_STATUS_PLANNED
        now.isBefore(end) -> APPOINTMENT_STATUS_IN_PROGRESS
        else -> APPOINTMENT_STATUS_PAST
    }
}

private fun appointmentCanMoveOrCancel(appointment: AppointmentRow): Boolean =
    appointmentUiStatusLabel(appointment).let { status ->
        status == APPOINTMENT_STATUS_PLANNED || status == APPOINTMENT_STATUS_IN_PROGRESS
    }

private fun appointmentEntityCanHavePendingTasks(appointment: AppointmentEntity): Boolean =
    appointmentEntityUiStatusLabel(appointment).let { status ->
        status == APPOINTMENT_STATUS_PLANNED || status == APPOINTMENT_STATUS_IN_PROGRESS
    }

private fun appointmentActiveUiStatusLabel(appointment: AppointmentRow): String =
    appointmentUiStatusLabel(appointment.copy(status = APPOINTMENT_STATUS_PLANNED))

private fun formatAppointmentDateTimeForDetail(appointment: AppointmentRow): String =
    parseAppointmentDateTime(appointment.startAt)
        ?.toLocalDate()
        ?.format(DateTimeFormatter.ofPattern("d MMMM yyyy, EEEE", ruLocale))
        ?: appointment.startAt

private fun paymentDetailText(appointment: AppointmentRow): String {
    if (appointmentIsCancelled(appointment)) {
        val parts = mutableListOf("отменена, не учитывается в долгах")
        if (appointment.paidAmountCents > 0) {
            parts += "оплачено ${formatMoney(appointment.paidAmountCents)}"
        }
        if (appointment.paymentMethod.isNotBlank()) {
            parts += appointment.paymentMethod
        }
        return parts.joinToString(" · ")
    }
    val parts = mutableListOf(paymentStatusLabel(appointment.paymentStatus))
    if (appointment.paidAmountCents > 0) {
        parts += "оплачено ${formatMoney(appointment.paidAmountCents)}"
    }
    val debt = appointmentRemainingCents(appointment)
    if (debt > 0) {
        parts += "долг ${formatMoney(debt)}"
    }
    if (appointment.paymentMethod.isNotBlank()) {
        parts += appointment.paymentMethod
    }
    return parts.joinToString(" · ")
}

private fun financeFilterLabel(filter: String): String = when (filter) {
    "Все" -> "Все операции"
    "Доходы" -> "Доходы"
    "Расходы" -> "Расходы"
    "Долги" -> "Долги"
    else -> filter
}

private fun financeFilterIcon(filter: String): androidx.compose.ui.graphics.vector.ImageVector = when (filter) {
    "Доходы" -> Icons.Filled.Add
    "Расходы" -> Icons.Filled.RemoveCircleOutline
    "Долги" -> Icons.Filled.Event
    else -> Icons.Filled.Payments
}

private fun calendarStatusLabel(appointment: AppointmentRow, hasCalendarPermissions: Boolean): String = when {
    appointmentIsCancelled(appointment) &&
        appointment.calendarEventId > 0 &&
        timestampMillis(appointment.calendarSyncedAt) >= timestampMillis(appointment.updatedAt) -> "отменена в календаре"
    appointmentIsCancelled(appointment) && appointment.calendarEventId > 0 -> "нужно обновить календарь"
    appointmentIsCancelled(appointment) -> "отменена локально"
    appointment.calendarEventId > 0 -> "связан"
    !hasCalendarPermissions -> "нужно разрешение"
    else -> "не связан"
}

private fun calendarDisplayTitle(calendar: SystemCalendar): String {
    val name = calendar.displayName.ifBlank { calendar.accountName.ifBlank { "Календарь ${calendar.id}" } }
    val badges = listOfNotNull(
        "локальный".takeIf { calendar.isLocal },
        "основной".takeIf { calendar.isPrimary }
    )
    return if (badges.isEmpty()) name else "$name (${badges.joinToString(", ")})"
}

private fun calendarDisplaySubtitle(calendar: SystemCalendar): String {
    val account = calendar.accountName.ifBlank { calendar.ownerAccount }
    val source = when {
        calendar.isLocal -> "Телефон"
        calendar.accountType.contains("google", ignoreCase = true) -> "Google"
        calendar.accountType.contains("samsung", ignoreCase = true) -> "Samsung"
        calendar.accountType.isNotBlank() -> calendar.accountType
        else -> "Системный календарь"
    }
    return listOf(source, account).filter { it.isNotBlank() }.joinToString(" · ")
}

private fun AppointmentRow.matchesAppointmentSearch(query: String): Boolean {
    val needle = query.trim().lowercase(Locale.getDefault())
    if (needle.isBlank()) return true
    return service.lowercase(Locale.getDefault()).contains(needle) ||
        clientName.lowercase(Locale.getDefault()).contains(needle) ||
        notes.lowercase(Locale.getDefault()).contains(needle) ||
        status.lowercase(Locale.getDefault()).contains(needle)
}

private fun parseVersionParts(value: String): List<Int>? {
    val clean = value.trim().removePrefix("v").removePrefix("V")
    if (clean.isBlank()) return null
    val parts = clean.split(".", "-", "_")
        .mapNotNull { part -> part.takeWhile { it.isDigit() }.takeIf { it.isNotBlank() }?.toIntOrNull() }
    return parts.takeIf { it.isNotEmpty() }
}

private fun compareVersionParts(left: List<Int>, right: List<Int>): Int {
    val size = maxOf(left.size, right.size)
    for (index in 0 until size) {
        val leftPart = left.getOrElse(index) { 0 }
        val rightPart = right.getOrElse(index) { 0 }
        if (leftPart != rightPart) return leftPart.compareTo(rightPart)
    }
    return 0
}

private fun formatGitHubPublishedAt(value: String): String {
    if (value.isBlank()) return ""
    return runCatching {
        Instant.parse(value)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", ruLocale))
    }.getOrDefault(value)
}

private fun now(): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
private fun fileStamp(): String = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date())

private fun timestampMillis(value: String): Long =
    runCatching {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).apply { isLenient = false }.parse(value.trim())?.time ?: 0L
    }.getOrDefault(0L)

private fun defaultSyncSetting(resource: String): SyncSettingsEntity =
    SyncSettingsEntity(resource = resource, mode = SYNC_MODE_TWO_WAY)

private fun syncModeLabel(resource: String, mode: String): String = when (mode) {
    SYNC_MODE_OFF -> "Отключено"
    SYNC_MODE_MANUAL -> "Только вручную"
    SYNC_MODE_LOCAL_TO_SYSTEM -> when (resource) {
        SYNC_RESOURCE_CONTACTS -> "CRM -> Контакты"
        SYNC_RESOURCE_CALENDAR -> "CRM -> Календарь"
        else -> "CRM -> Система"
    }
    SYNC_MODE_SYSTEM_TO_LOCAL -> when (resource) {
        SYNC_RESOURCE_CONTACTS -> "Контакты -> CRM"
        SYNC_RESOURCE_CALENDAR -> "Связанные события -> CRM"
        else -> "Система -> CRM"
    }
    SYNC_MODE_TWO_WAY -> when (resource) {
        SYNC_RESOURCE_CONTACTS -> "Связанные контакты туда-обратно"
        SYNC_RESOURCE_CALENDAR -> "Связанные события туда-обратно"
        else -> "Связанные данные туда-обратно"
    }
    else -> mode
}

private fun syncModeDescription(resource: String, mode: String): String = when (mode) {
    SYNC_MODE_OFF -> "Ничего не отправляется и ручная кнопка выключена."
    SYNC_MODE_MANUAL -> "При сохранении ничего не синхронизируется; запуск только кнопкой на этом экране."
    SYNC_MODE_LOCAL_TO_SYSTEM -> when (resource) {
        SYNC_RESOURCE_CONTACTS -> "После сохранения клиента CRM создает или дополняет системный контакт, если разрешения уже выданы."
        SYNC_RESOURCE_CALENDAR -> "После сохранения записи CRM создает или обновляет событие календаря, если разрешения уже выданы."
        else -> "После сохранения CRM отправляет данные в системное приложение."
    }
    SYNC_MODE_SYSTEM_TO_LOCAL -> when (resource) {
        SYNC_RESOURCE_CONTACTS -> "Обновляет клиентов только из связанных или совпавших по телефону контактов. Всю телефонную книгу не импортирует."
        SYNC_RESOURCE_CALENDAR -> "Обновляет только записи, уже связанные с событиями календаря. Новые события календаря не импортирует."
        else -> "Обновляет только уже связанные данные CRM."
    }
    SYNC_MODE_TWO_WAY -> when (resource) {
        SYNC_RESOURCE_CONTACTS -> "Связанные или совпавшие по телефону контакты обновляются в обе стороны; при конфликте побеждает более свежая версия."
        SYNC_RESOURCE_CALENDAR -> "CRM пишет события в календарь и подтягивает правки только из уже связанных событий. Новые события календаря не импортирует."
        else -> "Обновляет уже связанные данные в обе стороны."
    }
    else -> ""
}

private fun syncResourceLabel(resource: String): String = when (resource) {
    SYNC_RESOURCE_CONTACTS -> "Контакты"
    SYNC_RESOURCE_CALENDAR -> "Календарь"
    else -> resource
}

private fun syncSettingFor(settings: List<SyncSettingsEntity>, resource: String): SyncSettingsEntity =
    settings.firstOrNull { it.resource == resource } ?: defaultSyncSetting(resource)

private fun statusLabel(status: String): String = when (status) {
    STATUS_QUEUED -> "В очереди"
    STATUS_NEEDS_CONFIG -> "Нужна настройка"
    STATUS_WAITING_PERMISSION -> "Нужно разрешение"
    STATUS_SENT -> "Отправлено"
    STATUS_FAILED -> "Ошибка"
    STATUS_CANCELLED -> "Отменено"
    else -> status
}

private fun autoBackupModeLabel(mode: String): String = when (mode) {
    AUTO_BACKUP_MODE_DAILY -> "Ежедневно"
    AUTO_BACKUP_MODE_WEEKLY -> "Еженедельно"
    else -> "Отключено"
}

private fun outboxDisplayStatus(row: OutboxRow): String {
    if (row.status == STATUS_NEEDS_CONFIG && row.error == OUTBOX_CONFIRMATION_REQUIRED) {
        return OUTBOX_CONFIRMATION_REQUIRED
    }
    if (row.status == STATUS_QUEUED) {
        val scheduledMillis = parseAppointmentMillis(row.scheduledAt)
        if (scheduledMillis != null && scheduledMillis > System.currentTimeMillis()) {
            return "Запланировано"
        }
    }
    return statusLabel(row.status)
}

private fun outboxNeedsConfirmation(row: OutboxRow, nowMillis: Long = System.currentTimeMillis()): Boolean =
    row.status == STATUS_NEEDS_CONFIG &&
        row.error == OUTBOX_CONFIRMATION_REQUIRED &&
        outboxDue(row.scheduledAt, nowMillis)

private fun outboxDue(scheduledAt: String, nowMillis: Long): Boolean =
    scheduledAt.isBlank() || (parseAppointmentMillis(scheduledAt)?.let { it <= nowMillis } ?: true)

private fun paymentStatusLabel(status: String): String = when (status) {
    PAYMENT_STATUS_PAID -> "оплачено"
    PAYMENT_STATUS_PARTIAL -> "частично оплачено"
    else -> "не оплачено"
}

private fun automationTriggerLabel(triggerType: String): String = when (triggerType) {
    AUTOMATION_TRIGGER_BEFORE_APPOINTMENT -> "До записи"
    AUTOMATION_TRIGGER_AFTER_APPOINTMENT -> "После записи"
    AUTOMATION_TRIGGER_APPOINTMENT_CREATED -> "Запись создана"
    AUTOMATION_TRIGGER_APPOINTMENT_RESCHEDULED -> "Запись перенесена"
    AUTOMATION_TRIGGER_APPOINTMENT_CANCELLED -> "Запись отменена"
    AUTOMATION_TRIGGER_APPOINTMENT_REACTIVATED -> "Запись возобновлена"
    AUTOMATION_TRIGGER_PAYMENT_RECEIVED -> "Оплата получена"
    AUTOMATION_TRIGGER_DEBT_AFTER_APPOINTMENT -> "Есть долг после записи"
    AUTOMATION_TRIGGER_RETENTION_BY_SERVICE -> "Возврат клиента"
    else -> "Ручная задача"
}

private fun automationRuleTimingText(rule: AutomationRuleEntity): String = when (rule.triggerType) {
    AUTOMATION_TRIGGER_BEFORE_APPOINTMENT -> "за ${formatDurationMinutes(rule.offsetMinutes)} до начала"
    AUTOMATION_TRIGGER_AFTER_APPOINTMENT -> "через ${formatDurationMinutes(rule.offsetMinutes)} после окончания"
    AUTOMATION_TRIGGER_DEBT_AFTER_APPOINTMENT -> "через ${rule.retentionDays.coerceAtLeast(1)} дн. после записи, если есть долг"
    AUTOMATION_TRIGGER_RETENTION_BY_SERVICE -> "через ${rule.retentionDays.coerceAtLeast(1)} дн. после последней записи"
    AUTOMATION_TRIGGER_NONE -> "создается вручную"
    else -> "сразу после события"
}

private fun automationServiceFilterLabel(rule: AutomationRuleEntity, services: List<ServiceEntity>): String {
    val ids = rule.serviceFilterIds
        .split(",")
        .mapNotNull { it.trim().toLongOrNull() }
        .toSet()
    if (ids.isEmpty()) return "Все услуги"
    val names = services.filter { it.id in ids }.map { it.name }
    return names.ifEmpty { ids.map { "Услуга #$it" } }.joinToString(", ")
}

private fun cleanClientNotes(value: String): String {
    val trimmed = value.trim()
    return if (trimmed in setOf("Импортировано из контактов", "Импортировано из телефонной книги")) "" else value
}

private fun clientSection(name: String): String {
    val char = name.trim().firstOrNull() ?: return "#"
    return if (char.isLetter()) char.uppercaseChar().toString() else "#"
}

private fun clientSubtitle(client: ClientEntity): String {
    return listOf(
        client.phone,
        client.email,
        listOf(client.company, client.jobTitle).filter { it.isNotBlank() }.joinToString(", ")
    ).filter { it.isNotBlank() }.joinToString(" / ").ifBlank {
        if (client.contactId > 0) "Связан с контактом телефона" else "Локальный клиент"
    }
}

private fun firstFilled(vararg values: String): String =
    values.firstOrNull { it.isNotBlank() }.orEmpty()

private fun clientPrimaryPhone(client: ClientEntity): String =
    firstFilled(client.phone, client.phoneHome, client.phoneWork, client.phoneOther)

private fun clientPhones(client: ClientEntity): List<Pair<String, String>> =
    listOf(
        "Мобильный" to client.phone,
        "Домашний" to client.phoneHome,
        "Рабочий" to client.phoneWork,
        "Другой" to client.phoneOther
    ).filter { it.second.isNotBlank() }

private fun clientEmails(client: ClientEntity): List<Pair<String, String>> =
    listOf(
        "Личный" to client.email,
        "Рабочий" to client.emailWork
    ).filter { it.second.isNotBlank() }

private fun clientAddresses(client: ClientEntity): List<Pair<String, String>> =
    listOf(
        "Домашний" to client.address,
        "Рабочий" to client.addressWork
    ).filter { it.second.isNotBlank() }

private fun openDialer(context: Context, phone: String) {
    if (phone.isBlank()) return
    runCatching {
        context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Uri.encode(phone)}")))
    }
}

private fun startPhoneCall(context: Context, phone: String) {
    if (phone.isBlank()) return
    runCatching {
        context.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:${Uri.encode(phone)}")))
    }.onFailure {
        openDialer(context, phone)
    }
}

private fun startCarrierVideoCall(context: Context, phone: String) {
    if (phone.isBlank()) return
    val callUri = Uri.parse("tel:${Uri.encode(phone)}")
    val extras = Bundle().apply {
        putInt(TelecomManager.EXTRA_START_CALL_WITH_VIDEO_STATE, VideoProfile.STATE_BIDIRECTIONAL)
    }
    runCatching {
        val telecomManager = context.getSystemService(TelecomManager::class.java)
        if (telecomManager != null) {
            telecomManager.placeCall(callUri, extras)
        } else {
            context.startActivity(
                Intent(Intent.ACTION_CALL, callUri)
                    .putExtra(TelecomManager.EXTRA_START_CALL_WITH_VIDEO_STATE, VideoProfile.STATE_BIDIRECTIONAL)
            )
        }
    }.onFailure {
        startPhoneCall(context, phone)
    }
}

private fun startMeetContactVideoCall(context: Context, client: ClientEntity, phone: String): Boolean {
    val intent = findMeetContactVideoIntent(context, client, phone) ?: return false
    return runCatching {
        context.startActivity(intent)
        true
    }.getOrDefault(false)
}

private fun startMeetExternalPhoneCall(context: Context, phone: String): Boolean {
    if (phone.isBlank()) return false
    val intent = Intent().apply {
        data = Uri.parse("tel:${Uri.encode(phone)}")
        setClassName(GOOGLE_MEET_PACKAGE, "$GOOGLE_MEET_PACKAGE.ExternalCallActivity")
    }
    return runCatching {
        context.startActivity(intent)
        true
    }.getOrDefault(false)
}

private fun findMeetContactVideoIntent(context: Context, client: ClientEntity, phone: String): Intent? {
    if (context.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) return null
    val contactIds = linkedSetOf<Long>()
    if (client.contactId > 0) contactIds.add(client.contactId)
    lookupContactIdByPhone(context, phone)?.let { contactIds.add(it) }

    contactIds.forEach { contactId ->
        findMeetDataAction(context, contactId, GOOGLE_MEET_PHONE_VIDEO_MIME)?.let { return it }
        findMeetDataAction(context, contactId, GOOGLE_MEET_EMAIL_VIDEO_MIME)?.let { return it }
    }
    return null
}

private fun lookupContactIdByPhone(context: Context, phone: String): Long? {
    if (phone.isBlank()) return null
    val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone))
    val cursor = context.contentResolver.query(
        uri,
        arrayOf(ContactsContract.PhoneLookup._ID),
        null,
        null,
        null
    ) ?: return null
    cursor.use {
        return if (it.moveToFirst()) it.getLong(0) else null
    }
}

private fun findMeetDataAction(context: Context, contactId: Long, mimeType: String): Intent? {
    val cursor = context.contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        arrayOf(ContactsContract.Data._ID, ContactsContract.Data.MIMETYPE),
        "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
        arrayOf(contactId.toString(), mimeType),
        null
    ) ?: return null
    cursor.use {
        if (!it.moveToFirst()) return null
        val dataId = it.getLong(0)
        val resolvedMimeType = it.getString(1)
        return Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, dataId), resolvedMimeType)
            setPackage(GOOGLE_MEET_PACKAGE)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
}

private fun openSms(context: Context, phone: String) {
    if (phone.isBlank()) return
    runCatching {
        context.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${Uri.encode(phone)}")))
    }
}

private fun openEmail(context: Context, email: String) {
    if (email.isBlank()) return
    runCatching {
        context.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${Uri.encode(email)}")))
    }
}

private fun initials(name: String): String {
    val parts = name.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
    val value = parts.mapNotNull { it.firstOrNull { char -> char.isLetter() }?.uppercaseChar() }.take(2).joinToString("")
    return value.ifBlank { "?" }
}

private fun birthdayToMillis(value: String): Long? =
    runCatching {
        LocalDate.parse(value.trim())
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    }.getOrNull()

private fun formatBirthdayMillis(value: Long): String {
    val date = pickerMillisToLocalDate(value)
    return String.format(Locale.US, "%04d-%02d-%02d", date.year, date.monthValue, date.dayOfMonth)
}

private fun ClientEntity.toMap() = mapOf(
    "id" to id,
    "name" to name,
    "givenName" to givenName,
    "middleName" to middleName,
    "familyName" to familyName,
    "nickname" to nickname,
    "phone" to phone,
    "phoneKey" to phoneKey,
    "phoneHome" to phoneHome,
    "phoneWork" to phoneWork,
    "phoneOther" to phoneOther,
    "email" to email,
    "emailWork" to emailWork,
    "address" to address,
    "addressWork" to addressWork,
    "company" to company,
    "jobTitle" to jobTitle,
    "website" to website,
    "birthday" to birthday,
    "contactId" to contactId,
    "contactLookupKey" to contactLookupKey,
    "contactSyncedAt" to contactSyncedAt,
    "contactPhotoUri" to contactPhotoUri,
    "social" to social,
    "notes" to notes,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt
)

private fun AppointmentEntity.toMap() = mapOf(
    "id" to id,
    "clientId" to clientId,
    "service" to service,
    "startAt" to startAt,
    "durationMinutes" to durationMinutes,
    "price" to price,
    "priceCents" to priceCents,
    "paidAmountCents" to paidAmountCents,
    "paymentStatus" to paymentStatus,
    "paymentMethod" to paymentMethod,
    "paidAt" to paidAt,
    "status" to status,
    "notes" to notes,
    "calendarEventId" to calendarEventId,
    "calendarSyncedAt" to calendarSyncedAt,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt
)

private fun ServiceEntity.toMap() = mapOf(
    "id" to id,
    "name" to name,
    "priceCents" to priceCents,
    "durationMinutes" to durationMinutes,
    "isActive" to isActive,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt
)

private fun AppointmentServiceEntity.toMap() = mapOf(
    "id" to id,
    "appointmentId" to appointmentId,
    "serviceId" to serviceId,
    "serviceName" to serviceName,
    "priceCents" to priceCents,
    "durationMinutes" to durationMinutes,
    "sortOrder" to sortOrder,
    "createdAt" to createdAt
)

private fun AutomationRuleEntity.toMap() = mapOf(
    "id" to id,
    "name" to name,
    "channel" to channel,
    "triggerName" to triggerName,
    "message" to message,
    "enabled" to enabled,
    "createdAt" to createdAt,
    "triggerType" to triggerType,
    "offsetMinutes" to offsetMinutes,
    "retentionDays" to retentionDays,
    "serviceFilterIds" to serviceFilterIds,
    "condition" to condition,
    "askBeforeRun" to askBeforeRun,
    "updatedAt" to updatedAt
)

private fun OutboxItemEntity.toMap() = mapOf(
    "id" to id,
    "ruleId" to ruleId,
    "clientId" to clientId,
    "appointmentId" to appointmentId,
    "channel" to channel,
    "payload" to payload,
    "status" to status,
    "scheduledAt" to scheduledAt,
    "error" to error,
    "createdAt" to createdAt,
    "sentAt" to sentAt,
    "dedupeKey" to dedupeKey
)

private fun ConnectorSettingsEntity.toMap() = mapOf(
    "channel" to channel,
    "telegramBotToken" to telegramBotToken,
    "telegramChatId" to telegramChatId,
    "enabled" to enabled,
    "updatedAt" to updatedAt
)

private fun SyncSettingsEntity.toMap() = mapOf(
    "resource" to resource,
    "mode" to mode,
    "updatedAt" to updatedAt,
    "lastRunAt" to lastRunAt,
    "selectedSystemId" to selectedSystemId
)

private fun FinanceTransactionEntity.toMap() = mapOf(
    "id" to id,
    "type" to type,
    "amountCents" to amountCents,
    "title" to title,
    "category" to category,
    "date" to date,
    "paymentMethod" to paymentMethod,
    "notes" to notes,
    "clientId" to clientId,
    "appointmentId" to appointmentId,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt
)

private fun JSONArray?.toClients(): List<ClientEntity> = buildList {
    val array = this@toClients ?: return@buildList
    for (i in 0 until array.length()) {
        val item = array.getJSONObject(i)
        val phone = item.optString("phone")
        add(
            ClientEntity(
                id = item.optLong("id"),
                name = item.optString("name"),
                givenName = item.optString("givenName"),
                middleName = item.optString("middleName"),
                familyName = item.optString("familyName"),
                nickname = item.optString("nickname"),
                phone = phone,
                phoneKey = item.optString("phoneKey", normalizePhone(phone)),
                phoneHome = item.optString("phoneHome"),
                phoneWork = item.optString("phoneWork"),
                phoneOther = item.optString("phoneOther"),
                email = item.optString("email"),
                emailWork = item.optString("emailWork"),
                address = item.optString("address"),
                addressWork = item.optString("addressWork"),
                company = item.optString("company"),
                jobTitle = item.optString("jobTitle"),
                website = item.optString("website"),
                birthday = item.optString("birthday"),
                contactId = item.optLong("contactId", 0),
                contactLookupKey = item.optString("contactLookupKey"),
                contactSyncedAt = item.optString("contactSyncedAt"),
                contactPhotoUri = item.optString("contactPhotoUri"),
                social = item.optString("social"),
                notes = cleanClientNotes(item.optString("notes")),
                createdAt = item.optString("createdAt", now()),
                updatedAt = item.optString("updatedAt", item.optString("createdAt", now()))
            )
        )
    }
}

private fun JSONArray?.toAppointments(): List<AppointmentEntity> = buildList {
    val array = this@toAppointments ?: return@buildList
    for (i in 0 until array.length()) {
        val item = array.getJSONObject(i)
        val legacyPrice = item.optString("price")
        add(
            AppointmentEntity(
                id = item.optLong("id"),
                clientId = item.optLong("clientId"),
                service = item.optString("service"),
                startAt = item.optString("startAt"),
                durationMinutes = item.optInt("durationMinutes", 60).coerceAtLeast(15),
                price = legacyPrice,
                priceCents = item.optLong("priceCents", parseMoneyToCents(legacyPrice)).coerceAtLeast(0),
                paidAmountCents = item.optLong("paidAmountCents", 0).coerceAtLeast(0),
                paymentStatus = item.optString("paymentStatus", PAYMENT_STATUS_UNPAID)
                    .takeIf { it in listOf(PAYMENT_STATUS_UNPAID, PAYMENT_STATUS_PARTIAL, PAYMENT_STATUS_PAID) }
                    ?: PAYMENT_STATUS_UNPAID,
                paymentMethod = item.optString("paymentMethod"),
                paidAt = item.optString("paidAt"),
                status = item.optString("status", "Запланирована"),
                notes = item.optString("notes"),
                calendarEventId = item.optLong("calendarEventId", 0),
                calendarSyncedAt = item.optString("calendarSyncedAt"),
                createdAt = item.optString("createdAt", now()),
                updatedAt = item.optString("updatedAt", item.optString("createdAt", now()))
            )
        )
    }
}

private fun JSONArray?.toServices(): List<ServiceEntity> = buildList {
    val array = this@toServices ?: return@buildList
    for (i in 0 until array.length()) {
        val item = array.getJSONObject(i)
        val name = item.optString("name").trim()
        if (name.isBlank()) continue
        add(
            ServiceEntity(
                id = item.optLong("id"),
                name = name,
                priceCents = item.optLong("priceCents", parseMoneyToCents(item.optString("price"))).coerceAtLeast(0),
                durationMinutes = item.optInt("durationMinutes", 60).coerceAtLeast(15),
                isActive = item.optBoolean("isActive", true),
                createdAt = item.optString("createdAt", now()),
                updatedAt = item.optString("updatedAt", item.optString("createdAt", now()))
            )
        )
    }
}

private fun JSONArray?.toAppointmentServices(appointments: List<AppointmentEntity>): List<AppointmentServiceEntity> = buildList {
    val array = this@toAppointmentServices
    if (array == null) {
        appointments.forEach { appointment ->
            if (appointment.service.isNotBlank()) {
                add(
                    AppointmentServiceEntity(
                        appointmentId = appointment.id,
                        serviceId = 0,
                        serviceName = appointment.service,
                        priceCents = appointment.priceCents,
                        durationMinutes = appointment.durationMinutes.coerceAtLeast(15),
                        sortOrder = 0,
                        createdAt = appointment.createdAt
                    )
                )
            }
        }
        return@buildList
    }
    for (i in 0 until array.length()) {
        val item = array.getJSONObject(i)
        val serviceName = item.optString("serviceName").trim()
        if (serviceName.isBlank()) continue
        add(
            AppointmentServiceEntity(
                id = item.optLong("id"),
                appointmentId = item.optLong("appointmentId"),
                serviceId = item.optLong("serviceId", 0),
                serviceName = serviceName,
                priceCents = item.optLong("priceCents", parseMoneyToCents(item.optString("price"))).coerceAtLeast(0),
                durationMinutes = item.optInt("durationMinutes", 60).coerceAtLeast(15),
                sortOrder = item.optInt("sortOrder", i),
                createdAt = item.optString("createdAt", now())
            )
        )
    }
}

private fun JSONArray?.toRules(): List<AutomationRuleEntity> = buildList {
    val array = this@toRules ?: return@buildList
    for (i in 0 until array.length()) {
        val item = array.getJSONObject(i)
        val triggerName = item.optString("triggerName")
        val legacyTriggerType = when (triggerName) {
            "За 24 часа до записи", "За 2 часа до записи" -> AUTOMATION_TRIGGER_BEFORE_APPOINTMENT
            "После визита" -> AUTOMATION_TRIGGER_AFTER_APPOINTMENT
            else -> AUTOMATION_TRIGGER_NONE
        }
        val legacyOffset = when (triggerName) {
            "За 24 часа до записи" -> 1440
            "За 2 часа до записи" -> 120
            else -> 0
        }
        add(
            AutomationRuleEntity(
                id = item.optLong("id"),
                name = item.optString("name"),
                channel = CHANNEL_SMS,
                triggerName = triggerName.ifBlank { automationTriggerLabel(item.optString("triggerType", legacyTriggerType)) },
                message = item.optString("message"),
                enabled = item.optBoolean("enabled", true),
                createdAt = item.optString("createdAt", now()),
                triggerType = item.optString("triggerType", legacyTriggerType).takeIf { it in automationTriggerTypes }
                    ?: legacyTriggerType,
                offsetMinutes = item.optInt("offsetMinutes", legacyOffset).coerceAtLeast(0),
                retentionDays = item.optInt("retentionDays", 0).coerceAtLeast(0),
                serviceFilterIds = item.optString("serviceFilterIds"),
                condition = item.optString("condition"),
                askBeforeRun = item.optBoolean("askBeforeRun", false),
                updatedAt = item.optString("updatedAt", item.optString("createdAt", now()))
            )
        )
    }
}

private fun JSONArray?.toOutbox(): List<OutboxItemEntity> = buildList {
    val array = this@toOutbox ?: return@buildList
    for (i in 0 until array.length()) {
        val item = array.getJSONObject(i)
        add(
            OutboxItemEntity(
                id = item.optLong("id"),
                ruleId = item.optNullableLong("ruleId"),
                clientId = item.optNullableLong("clientId"),
                appointmentId = item.optNullableLong("appointmentId"),
                channel = item.optString("channel").takeIf { it == CHANNEL_SMS } ?: CHANNEL_SMS,
                payload = item.optString("payload"),
                status = item.optString("status", STATUS_QUEUED),
                scheduledAt = item.optString("scheduledAt"),
                error = item.optString("error"),
                createdAt = item.optString("createdAt", now()),
                sentAt = item.optString("sentAt"),
                dedupeKey = item.optString("dedupeKey")
            )
        )
    }
}

private fun JSONArray?.toSettings(): List<ConnectorSettingsEntity> = buildList {
    val array = this@toSettings ?: return@buildList
    for (i in 0 until array.length()) {
        val item = array.getJSONObject(i)
        if (item.optString("channel") != CHANNEL_SMS) continue
        add(
            ConnectorSettingsEntity(
                channel = CHANNEL_SMS,
                telegramBotToken = item.optString("telegramBotToken"),
                telegramChatId = item.optString("telegramChatId"),
                enabled = item.optBoolean("enabled", false),
                updatedAt = item.optString("updatedAt", now())
            )
        )
    }
}

private fun JSONArray?.toSyncSettings(): List<SyncSettingsEntity> = buildList {
    val array = this@toSyncSettings
    if (array == null) {
        add(defaultSyncSetting(SYNC_RESOURCE_CONTACTS))
        add(defaultSyncSetting(SYNC_RESOURCE_CALENDAR))
        return@buildList
    }
    val seen = mutableSetOf<String>()
    for (i in 0 until array.length()) {
        val item = array.getJSONObject(i)
        val resource = item.optString("resource")
        if (resource !in listOf(SYNC_RESOURCE_CONTACTS, SYNC_RESOURCE_CALENDAR)) continue
        val mode = item.optString("mode", SYNC_MODE_TWO_WAY).takeIf { it in syncModeOptions } ?: SYNC_MODE_TWO_WAY
        add(
            SyncSettingsEntity(
                resource = resource,
                mode = mode,
                updatedAt = item.optString("updatedAt", now()),
                lastRunAt = item.optString("lastRunAt"),
                selectedSystemId = item.optString("selectedSystemId", item.optString("calendarId"))
            )
        )
        seen.add(resource)
    }
    if (SYNC_RESOURCE_CONTACTS !in seen) add(defaultSyncSetting(SYNC_RESOURCE_CONTACTS))
    if (SYNC_RESOURCE_CALENDAR !in seen) add(defaultSyncSetting(SYNC_RESOURCE_CALENDAR))
}

private fun JSONArray?.toFinanceTransactions(): List<FinanceTransactionEntity> = buildList {
    val array = this@toFinanceTransactions ?: return@buildList
    for (i in 0 until array.length()) {
        val item = array.getJSONObject(i)
        val amountCents = item.optLong("amountCents", 0).coerceAtLeast(0)
        if (amountCents <= 0L) continue
        add(
            FinanceTransactionEntity(
                id = item.optLong("id"),
                type = item.optString("type", FINANCE_TYPE_INCOME)
                    .takeIf { it in financeTypeOptions }
                    ?: FINANCE_TYPE_INCOME,
                amountCents = amountCents,
                title = item.optString("title"),
                category = item.optString("category"),
                date = item.optString("date", financeDateString(LocalDate.now())),
                paymentMethod = item.optString("paymentMethod"),
                notes = item.optString("notes"),
                clientId = item.optNullableLong("clientId"),
                appointmentId = item.optNullableLong("appointmentId"),
                createdAt = item.optString("createdAt", now()),
                updatedAt = item.optString("updatedAt", item.optString("createdAt", now()))
            )
        )
    }
}

private fun JSONObject.optNullableLong(name: String): Long? {
    if (!has(name) || isNull(name)) return null
    val value = optLong(name, 0L)
    return if (value == 0L) null else value
}
