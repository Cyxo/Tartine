package android.content;

    public class Intent implements java.lang.Cloneable, android.os.Parcelable {

    public static final java.lang.String ACTION_AIRPLANE_MODE_CHANGED = "android.intent.action.AIRPLANE_MODE";
    public static final java.lang.String ACTION_ALL_APPS = "android.intent.action.ALL_APPS";
    public static final java.lang.String ACTION_ANSWER = "android.intent.action.ANSWER";
    public static final java.lang.String ACTION_APPLICATION_LOCALE_CHANGED = "android.intent.action.APPLICATION_LOCALE_CHANGED";
    public static final java.lang.String ACTION_APPLICATION_PREFERENCES = "android.intent.action.APPLICATION_PREFERENCES";
    public static final java.lang.String ACTION_APPLICATION_RESTRICTIONS_CHANGED = "android.intent.action.APPLICATION_RESTRICTIONS_CHANGED";
    public static final java.lang.String ACTION_APP_ERROR = "android.intent.action.APP_ERROR";
    public static final java.lang.String ACTION_ASSIST = "android.intent.action.ASSIST";
    public static final java.lang.String ACTION_ATTACH_DATA = "android.intent.action.ATTACH_DATA";
    public static final java.lang.String ACTION_AUTO_REVOKE_PERMISSIONS = "android.intent.action.AUTO_REVOKE_PERMISSIONS";
    public static final java.lang.String ACTION_BATTERY_CHANGED = "android.intent.action.BATTERY_CHANGED";
    public static final java.lang.String ACTION_BATTERY_LOW = "android.intent.action.BATTERY_LOW";
    public static final java.lang.String ACTION_BATTERY_OKAY = "android.intent.action.BATTERY_OKAY";
    public static final java.lang.String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    public static final java.lang.String ACTION_BUG_REPORT = "android.intent.action.BUG_REPORT";
    public static final java.lang.String ACTION_CALL = "android.intent.action.CALL";
    public static final java.lang.String ACTION_CALL_BUTTON = "android.intent.action.CALL_BUTTON";
    public static final java.lang.String ACTION_CAMERA_BUTTON = "android.intent.action.CAMERA_BUTTON";
    public static final java.lang.String ACTION_CARRIER_SETUP = "android.intent.action.CARRIER_SETUP";
    public static final java.lang.String ACTION_CHOOSER = "android.intent.action.CHOOSER";
    public static final java.lang.String ACTION_CLOSE_SYSTEM_DIALOGS = "android.intent.action.CLOSE_SYSTEM_DIALOGS";
    public static final java.lang.String ACTION_CONFIGURATION_CHANGED = "android.intent.action.CONFIGURATION_CHANGED";
    public static final java.lang.String ACTION_CREATE_DOCUMENT = "android.intent.action.CREATE_DOCUMENT";
    public static final java.lang.String ACTION_CREATE_NOTE = "android.intent.action.CREATE_NOTE";
    public static final java.lang.String ACTION_CREATE_REMINDER = "android.intent.action.CREATE_REMINDER";
    public static final java.lang.String ACTION_CREATE_SHORTCUT = "android.intent.action.CREATE_SHORTCUT";
    public static final java.lang.String ACTION_DATE_CHANGED = "android.intent.action.DATE_CHANGED";
    public static final java.lang.String ACTION_DEFAULT = "android.intent.action.VIEW";
    public static final java.lang.String ACTION_DEFINE = "android.intent.action.DEFINE";
    public static final java.lang.String ACTION_DELETE = "android.intent.action.DELETE";
    public static final java.lang.String ACTION_DEVICE_STORAGE_LOW = "android.intent.action.DEVICE_STORAGE_LOW";
    public static final java.lang.String ACTION_DEVICE_STORAGE_OK = "android.intent.action.DEVICE_STORAGE_OK";
    public static final java.lang.String ACTION_DIAL = "android.intent.action.DIAL";
    public static final java.lang.String ACTION_DOCK_EVENT = "android.intent.action.DOCK_EVENT";
    public static final java.lang.String ACTION_DREAMING_STARTED = "android.intent.action.DREAMING_STARTED";
    public static final java.lang.String ACTION_DREAMING_STOPPED = "android.intent.action.DREAMING_STOPPED";
    public static final java.lang.String ACTION_EDIT = "android.intent.action.EDIT";
    public static final java.lang.String ACTION_EXTERNAL_APPLICATIONS_AVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE";
    public static final java.lang.String ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE";
    public static final java.lang.String ACTION_FACTORY_TEST = "android.intent.action.FACTORY_TEST";
    public static final java.lang.String ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT";
    public static final java.lang.String ACTION_GET_RESTRICTION_ENTRIES = "android.intent.action.GET_RESTRICTION_ENTRIES";
    public static final java.lang.String ACTION_GTALK_SERVICE_CONNECTED = "android.intent.action.GTALK_CONNECTED";
    public static final java.lang.String ACTION_GTALK_SERVICE_DISCONNECTED = "android.intent.action.GTALK_DISCONNECTED";
    public static final java.lang.String ACTION_HEADSET_PLUG = "android.intent.action.HEADSET_PLUG";
    public static final java.lang.String ACTION_INPUT_METHOD_CHANGED = "android.intent.action.INPUT_METHOD_CHANGED";
    public static final java.lang.String ACTION_INSERT = "android.intent.action.INSERT";
    public static final java.lang.String ACTION_INSERT_OR_EDIT = "android.intent.action.INSERT_OR_EDIT";
    public static final java.lang.String ACTION_INSTALL_FAILURE = "android.intent.action.INSTALL_FAILURE";
    public static final java.lang.String ACTION_INSTALL_PACKAGE = "android.intent.action.INSTALL_PACKAGE";
    public static final java.lang.String ACTION_LAUNCH_CAPTURE_CONTENT_ACTIVITY_FOR_NOTE = "android.intent.action.LAUNCH_CAPTURE_CONTENT_ACTIVITY_FOR_NOTE";
    public static final java.lang.String ACTION_LOCALE_CHANGED = "android.intent.action.LOCALE_CHANGED";
    public static final java.lang.String ACTION_LOCKED_BOOT_COMPLETED = "android.intent.action.LOCKED_BOOT_COMPLETED";
    public static final java.lang.String ACTION_MAIN = "android.intent.action.MAIN";
    public static final java.lang.String ACTION_MANAGED_PROFILE_ADDED = "android.intent.action.MANAGED_PROFILE_ADDED";
    public static final java.lang.String ACTION_MANAGED_PROFILE_AVAILABLE = "android.intent.action.MANAGED_PROFILE_AVAILABLE";
    public static final java.lang.String ACTION_MANAGED_PROFILE_REMOVED = "android.intent.action.MANAGED_PROFILE_REMOVED";
    public static final java.lang.String ACTION_MANAGED_PROFILE_UNAVAILABLE = "android.intent.action.MANAGED_PROFILE_UNAVAILABLE";
    public static final java.lang.String ACTION_MANAGED_PROFILE_UNLOCKED = "android.intent.action.MANAGED_PROFILE_UNLOCKED";
    public static final java.lang.String ACTION_MANAGE_NETWORK_USAGE = "android.intent.action.MANAGE_NETWORK_USAGE";
    public static final java.lang.String ACTION_MANAGE_PACKAGE_STORAGE = "android.intent.action.MANAGE_PACKAGE_STORAGE";
    public static final java.lang.String ACTION_MANAGE_UNUSED_APPS = "android.intent.action.MANAGE_UNUSED_APPS";
    public static final java.lang.String ACTION_MEDIA_BAD_REMOVAL = "android.intent.action.MEDIA_BAD_REMOVAL";
    public static final java.lang.String ACTION_MEDIA_BUTTON = "android.intent.action.MEDIA_BUTTON";
    public static final java.lang.String ACTION_MEDIA_CHECKING = "android.intent.action.MEDIA_CHECKING";
    public static final java.lang.String ACTION_MEDIA_EJECT = "android.intent.action.MEDIA_EJECT";
    public static final java.lang.String ACTION_MEDIA_MOUNTED = "android.intent.action.MEDIA_MOUNTED";
    public static final java.lang.String ACTION_MEDIA_NOFS = "android.intent.action.MEDIA_NOFS";
    public static final java.lang.String ACTION_MEDIA_REMOVED = "android.intent.action.MEDIA_REMOVED";
    public static final java.lang.String ACTION_MEDIA_SCANNER_FINISHED = "android.intent.action.MEDIA_SCANNER_FINISHED";
    public static final java.lang.String ACTION_MEDIA_SCANNER_SCAN_FILE = "android.intent.action.MEDIA_SCANNER_SCAN_FILE";
    public static final java.lang.String ACTION_MEDIA_SCANNER_STARTED = "android.intent.action.MEDIA_SCANNER_STARTED";
    public static final java.lang.String ACTION_MEDIA_SHARED = "android.intent.action.MEDIA_SHARED";
    public static final java.lang.String ACTION_MEDIA_UNMOUNTABLE = "android.intent.action.MEDIA_UNMOUNTABLE";
    public static final java.lang.String ACTION_MEDIA_UNMOUNTED = "android.intent.action.MEDIA_UNMOUNTED";
    public static final java.lang.String ACTION_MY_PACKAGE_REPLACED = "android.intent.action.MY_PACKAGE_REPLACED";
    public static final java.lang.String ACTION_MY_PACKAGE_SUSPENDED = "android.intent.action.MY_PACKAGE_SUSPENDED";
    public static final java.lang.String ACTION_MY_PACKAGE_UNSUSPENDED = "android.intent.action.MY_PACKAGE_UNSUSPENDED";
    public static final java.lang.String ACTION_NEW_OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL";
    public static final java.lang.String ACTION_OPEN_DOCUMENT = "android.intent.action.OPEN_DOCUMENT";
    public static final java.lang.String ACTION_OPEN_DOCUMENT_TREE = "android.intent.action.OPEN_DOCUMENT_TREE";
    public static final java.lang.String ACTION_PACKAGES_SUSPENDED = "android.intent.action.PACKAGES_SUSPENDED";
    public static final java.lang.String ACTION_PACKAGES_UNSUSPENDED = "android.intent.action.PACKAGES_UNSUSPENDED";
    public static final java.lang.String ACTION_PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED";
    public static final java.lang.String ACTION_PACKAGE_CHANGED = "android.intent.action.PACKAGE_CHANGED";
    public static final java.lang.String ACTION_PACKAGE_DATA_CLEARED = "android.intent.action.PACKAGE_DATA_CLEARED";
    public static final java.lang.String ACTION_PACKAGE_FIRST_LAUNCH = "android.intent.action.PACKAGE_FIRST_LAUNCH";
    public static final java.lang.String ACTION_PACKAGE_FULLY_REMOVED = "android.intent.action.PACKAGE_FULLY_REMOVED";
    public static final java.lang.String ACTION_PACKAGE_INSTALL = "android.intent.action.PACKAGE_INSTALL";
    public static final java.lang.String ACTION_PACKAGE_NEEDS_VERIFICATION = "android.intent.action.PACKAGE_NEEDS_VERIFICATION";
    public static final java.lang.String ACTION_PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED";
    public static final java.lang.String ACTION_PACKAGE_REPLACED = "android.intent.action.PACKAGE_REPLACED";
    public static final java.lang.String ACTION_PACKAGE_RESTARTED = "android.intent.action.PACKAGE_RESTARTED";
    public static final java.lang.String ACTION_PACKAGE_UNSTOPPED = "android.intent.action.PACKAGE_UNSTOPPED";
    public static final java.lang.String ACTION_PACKAGE_VERIFIED = "android.intent.action.PACKAGE_VERIFIED";
    public static final java.lang.String ACTION_PASTE = "android.intent.action.PASTE";
    public static final java.lang.String ACTION_PICK = "android.intent.action.PICK";
    public static final java.lang.String ACTION_PICK_ACTIVITY = "android.intent.action.PICK_ACTIVITY";
    public static final java.lang.String ACTION_POWER_CONNECTED = "android.intent.action.ACTION_POWER_CONNECTED";
    public static final java.lang.String ACTION_POWER_DISCONNECTED = "android.intent.action.ACTION_POWER_DISCONNECTED";
    public static final java.lang.String ACTION_POWER_USAGE_SUMMARY = "android.intent.action.POWER_USAGE_SUMMARY";
    public static final java.lang.String ACTION_PROCESS_TEXT = "android.intent.action.PROCESS_TEXT";
    public static final java.lang.String ACTION_PROFILE_ACCESSIBLE = "android.intent.action.PROFILE_ACCESSIBLE";
    public static final java.lang.String ACTION_PROFILE_ADDED = "android.intent.action.PROFILE_ADDED";
    public static final java.lang.String ACTION_PROFILE_AVAILABLE = "android.intent.action.PROFILE_AVAILABLE";
    public static final java.lang.String ACTION_PROFILE_INACCESSIBLE = "android.intent.action.PROFILE_INACCESSIBLE";
    public static final java.lang.String ACTION_PROFILE_REMOVED = "android.intent.action.PROFILE_REMOVED";
    public static final java.lang.String ACTION_PROFILE_UNAVAILABLE = "android.intent.action.PROFILE_UNAVAILABLE";
    public static final java.lang.String ACTION_PROVIDER_CHANGED = "android.intent.action.PROVIDER_CHANGED";
    public static final java.lang.String ACTION_QUICK_CLOCK = "android.intent.action.QUICK_CLOCK";
    public static final java.lang.String ACTION_QUICK_VIEW = "android.intent.action.QUICK_VIEW";
    public static final java.lang.String ACTION_REBOOT = "android.intent.action.REBOOT";
    public static final java.lang.String ACTION_RUN = "android.intent.action.RUN";
    public static final java.lang.String ACTION_SAFETY_CENTER = "android.intent.action.SAFETY_CENTER";
    public static final java.lang.String ACTION_SCREEN_OFF = "android.intent.action.SCREEN_OFF";
    public static final java.lang.String ACTION_SCREEN_ON = "android.intent.action.SCREEN_ON";
    public static final java.lang.String ACTION_SEARCH = "android.intent.action.SEARCH";
    public static final java.lang.String ACTION_SEARCH_LONG_PRESS = "android.intent.action.SEARCH_LONG_PRESS";
    public static final java.lang.String ACTION_SEND = "android.intent.action.SEND";
    public static final java.lang.String ACTION_SENDTO = "android.intent.action.SENDTO";
    public static final java.lang.String ACTION_SEND_MULTIPLE = "android.intent.action.SEND_MULTIPLE";
    public static final java.lang.String ACTION_SET_WALLPAPER = "android.intent.action.SET_WALLPAPER";
    public static final java.lang.String ACTION_SHOW_APP_INFO = "android.intent.action.SHOW_APP_INFO";
    public static final java.lang.String ACTION_SHOW_WORK_APPS = "android.intent.action.SHOW_WORK_APPS";
    public static final java.lang.String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";
    public static final java.lang.String ACTION_SYNC = "android.intent.action.SYNC";
    public static final java.lang.String ACTION_SYSTEM_TUTORIAL = "android.intent.action.SYSTEM_TUTORIAL";
    public static final java.lang.String ACTION_TIMEZONE_CHANGED = "android.intent.action.TIMEZONE_CHANGED";
    public static final java.lang.String ACTION_TIME_CHANGED = "android.intent.action.TIME_SET";
    public static final java.lang.String ACTION_TIME_TICK = "android.intent.action.TIME_TICK";
    public static final java.lang.String ACTION_TRANSLATE = "android.intent.action.TRANSLATE";
    public static final java.lang.String ACTION_UID_REMOVED = "android.intent.action.UID_REMOVED";
    public static final java.lang.String ACTION_UMS_CONNECTED = "android.intent.action.UMS_CONNECTED";
    public static final java.lang.String ACTION_UMS_DISCONNECTED = "android.intent.action.UMS_DISCONNECTED";
    public static final java.lang.String ACTION_UNARCHIVE_PACKAGE = "android.intent.action.UNARCHIVE_PACKAGE";
    public static final java.lang.String ACTION_UNINSTALL_PACKAGE = "android.intent.action.UNINSTALL_PACKAGE";
    public static final java.lang.String ACTION_USER_BACKGROUND = "android.intent.action.USER_BACKGROUND";
    public static final java.lang.String ACTION_USER_FOREGROUND = "android.intent.action.USER_FOREGROUND";
    public static final java.lang.String ACTION_USER_INITIALIZE = "android.intent.action.USER_INITIALIZE";
    public static final java.lang.String ACTION_USER_PRESENT = "android.intent.action.USER_PRESENT";
    public static final java.lang.String ACTION_USER_UNLOCKED = "android.intent.action.USER_UNLOCKED";
    public static final java.lang.String ACTION_VIEW = "android.intent.action.VIEW";
    public static final java.lang.String ACTION_VIEW_LOCUS = "android.intent.action.VIEW_LOCUS";
    public static final java.lang.String ACTION_VIEW_PERMISSION_USAGE = "android.intent.action.VIEW_PERMISSION_USAGE";
    public static final java.lang.String ACTION_VIEW_PERMISSION_USAGE_FOR_PERIOD = "android.intent.action.VIEW_PERMISSION_USAGE_FOR_PERIOD";
    public static final java.lang.String ACTION_VOICE_COMMAND = "android.intent.action.VOICE_COMMAND";
    public static final java.lang.String ACTION_WALLPAPER_CHANGED = "android.intent.action.WALLPAPER_CHANGED";
    public static final java.lang.String ACTION_WEB_SEARCH = "android.intent.action.WEB_SEARCH";
    public static final int CAPTURE_CONTENT_FOR_NOTE_BLOCKED_BY_ADMIN = 4;
    public static final int CAPTURE_CONTENT_FOR_NOTE_FAILED = 1;
    public static final int CAPTURE_CONTENT_FOR_NOTE_SUCCESS = 0;
    public static final int CAPTURE_CONTENT_FOR_NOTE_USER_CANCELED = 2;
    public static final int CAPTURE_CONTENT_FOR_NOTE_WINDOW_MODE_UNSUPPORTED = 3;
    public static final java.lang.String CATEGORY_ACCESSIBILITY_SHORTCUT_TARGET = "android.intent.category.ACCESSIBILITY_SHORTCUT_TARGET";
    public static final java.lang.String CATEGORY_ALTERNATIVE = "android.intent.category.ALTERNATIVE";
    public static final java.lang.String CATEGORY_APP_BROWSER = "android.intent.category.APP_BROWSER";
    public static final java.lang.String CATEGORY_APP_CALCULATOR = "android.intent.category.APP_CALCULATOR";
    public static final java.lang.String CATEGORY_APP_CALENDAR = "android.intent.category.APP_CALENDAR";
    public static final java.lang.String CATEGORY_APP_CONTACTS = "android.intent.category.APP_CONTACTS";
    public static final java.lang.String CATEGORY_APP_EMAIL = "android.intent.category.APP_EMAIL";
    public static final java.lang.String CATEGORY_APP_FILES = "android.intent.category.APP_FILES";
    public static final java.lang.String CATEGORY_APP_FITNESS = "android.intent.category.APP_FITNESS";
    public static final java.lang.String CATEGORY_APP_GALLERY = "android.intent.category.APP_GALLERY";
    public static final java.lang.String CATEGORY_APP_MAPS = "android.intent.category.APP_MAPS";
    public static final java.lang.String CATEGORY_APP_MARKET = "android.intent.category.APP_MARKET";
    public static final java.lang.String CATEGORY_APP_MESSAGING = "android.intent.category.APP_MESSAGING";
    public static final java.lang.String CATEGORY_APP_MUSIC = "android.intent.category.APP_MUSIC";
    public static final java.lang.String CATEGORY_APP_WEATHER = "android.intent.category.APP_WEATHER";
    public static final java.lang.String CATEGORY_BROWSABLE = "android.intent.category.BROWSABLE";
    public static final java.lang.String CATEGORY_CAR_DOCK = "android.intent.category.CAR_DOCK";
    public static final java.lang.String CATEGORY_CAR_MODE = "android.intent.category.CAR_MODE";
    public static final java.lang.String CATEGORY_DEFAULT = "android.intent.category.DEFAULT";
    public static final java.lang.String CATEGORY_DESK_DOCK = "android.intent.category.DESK_DOCK";
    public static final java.lang.String CATEGORY_DEVELOPMENT_PREFERENCE = "android.intent.category.DEVELOPMENT_PREFERENCE";
    public static final java.lang.String CATEGORY_EMBED = "android.intent.category.EMBED";
    public static final java.lang.String CATEGORY_FRAMEWORK_INSTRUMENTATION_TEST = "android.intent.category.FRAMEWORK_INSTRUMENTATION_TEST";
    public static final java.lang.String CATEGORY_HE_DESK_DOCK = "android.intent.category.HE_DESK_DOCK";
    public static final java.lang.String CATEGORY_HOME = "android.intent.category.HOME";
    public static final java.lang.String CATEGORY_INFO = "android.intent.category.INFO";
    public static final java.lang.String CATEGORY_LAUNCHER = "android.intent.category.LAUNCHER";
    public static final java.lang.String CATEGORY_LEANBACK_LAUNCHER = "android.intent.category.LEANBACK_LAUNCHER";
    public static final java.lang.String CATEGORY_LE_DESK_DOCK = "android.intent.category.LE_DESK_DOCK";
    public static final java.lang.String CATEGORY_MONKEY = "android.intent.category.MONKEY";
    public static final java.lang.String CATEGORY_OPENABLE = "android.intent.category.OPENABLE";
    public static final java.lang.String CATEGORY_PREFERENCE = "android.intent.category.PREFERENCE";
    public static final java.lang.String CATEGORY_SAMPLE_CODE = "android.intent.category.SAMPLE_CODE";
    public static final java.lang.String CATEGORY_SECONDARY_HOME = "android.intent.category.SECONDARY_HOME";
    public static final java.lang.String CATEGORY_SELECTED_ALTERNATIVE = "android.intent.category.SELECTED_ALTERNATIVE";
    public static final java.lang.String CATEGORY_TAB = "android.intent.category.TAB";
    public static final java.lang.String CATEGORY_TEST = "android.intent.category.TEST";
    public static final java.lang.String CATEGORY_TYPED_OPENABLE = "android.intent.category.TYPED_OPENABLE";
    public static final java.lang.String CATEGORY_UNIT_TEST = "android.intent.category.UNIT_TEST";
    public static final java.lang.String CATEGORY_VOICE = "android.intent.category.VOICE";
    public static final java.lang.String CATEGORY_VR_HOME = "android.intent.category.VR_HOME";
    public static final int CHOOSER_CONTENT_TYPE_ALBUM = 1;
    public static android.os.Parcelable.Creator CREATOR;
    public static final java.lang.String EXTRA_ALARM_COUNT = "android.intent.extra.ALARM_COUNT";
    public static final java.lang.String EXTRA_ALLOW_MULTIPLE = "android.intent.extra.ALLOW_MULTIPLE";
    public static final java.lang.String EXTRA_ALLOW_REPLACE = "android.intent.extra.ALLOW_REPLACE";
    public static final java.lang.String EXTRA_ALTERNATE_INTENTS = "android.intent.extra.ALTERNATE_INTENTS";
    public static final java.lang.String EXTRA_ARCHIVAL = "android.intent.extra.ARCHIVAL";
    public static final java.lang.String EXTRA_ASSIST_CONTEXT = "android.intent.extra.ASSIST_CONTEXT";
    public static final java.lang.String EXTRA_ASSIST_INPUT_DEVICE_ID = "android.intent.extra.ASSIST_INPUT_DEVICE_ID";
    public static final java.lang.String EXTRA_ASSIST_INPUT_HINT_KEYBOARD = "android.intent.extra.ASSIST_INPUT_HINT_KEYBOARD";
    public static final java.lang.String EXTRA_ASSIST_PACKAGE = "android.intent.extra.ASSIST_PACKAGE";
    public static final java.lang.String EXTRA_ASSIST_UID = "android.intent.extra.ASSIST_UID";
    public static final java.lang.String EXTRA_ATTRIBUTION_TAGS = "android.intent.extra.ATTRIBUTION_TAGS";
    public static final java.lang.String EXTRA_AUTO_LAUNCH_SINGLE_CHOICE = "android.intent.extra.AUTO_LAUNCH_SINGLE_CHOICE";
    public static final java.lang.String EXTRA_BCC = "android.intent.extra.BCC";
    public static final java.lang.String EXTRA_BUG_REPORT = "android.intent.extra.BUG_REPORT";
    public static final java.lang.String EXTRA_CAPTURE_CONTENT_FOR_NOTE_STATUS_CODE = "android.intent.extra.CAPTURE_CONTENT_FOR_NOTE_STATUS_CODE";
    public static final java.lang.String EXTRA_CC = "android.intent.extra.CC";
    public static final java.lang.String EXTRA_CHANGED_COMPONENT_NAME = "android.intent.extra.changed_component_name";
    public static final java.lang.String EXTRA_CHANGED_COMPONENT_NAME_LIST = "android.intent.extra.changed_component_name_list";
    public static final java.lang.String EXTRA_CHANGED_PACKAGE_LIST = "android.intent.extra.changed_package_list";
    public static final java.lang.String EXTRA_CHANGED_UID_LIST = "android.intent.extra.changed_uid_list";
    public static final java.lang.String EXTRA_CHOOSER_ADDITIONAL_CONTENT_URI = "android.intent.extra.CHOOSER_ADDITIONAL_CONTENT_URI";
    public static final java.lang.String EXTRA_CHOOSER_CONTENT_TYPE_HINT = "android.intent.extra.CHOOSER_CONTENT_TYPE_HINT";
    public static final java.lang.String EXTRA_CHOOSER_CUSTOM_ACTIONS = "android.intent.extra.CHOOSER_CUSTOM_ACTIONS";
    public static final java.lang.String EXTRA_CHOOSER_FOCUSED_ITEM_POSITION = "android.intent.extra.CHOOSER_FOCUSED_ITEM_POSITION";
    public static final java.lang.String EXTRA_CHOOSER_MODIFY_SHARE_ACTION = "android.intent.extra.CHOOSER_MODIFY_SHARE_ACTION";
    public static final java.lang.String EXTRA_CHOOSER_REFINEMENT_INTENT_SENDER = "android.intent.extra.CHOOSER_REFINEMENT_INTENT_SENDER";
    public static final java.lang.String EXTRA_CHOOSER_RESULT = "android.intent.extra.CHOOSER_RESULT";
    public static final java.lang.String EXTRA_CHOOSER_RESULT_INTENT_SENDER = "android.intent.extra.CHOOSER_RESULT_INTENT_SENDER";
    public static final java.lang.String EXTRA_CHOOSER_TARGETS = "android.intent.extra.CHOOSER_TARGETS";
    public static final java.lang.String EXTRA_CHOSEN_COMPONENT = "android.intent.extra.CHOSEN_COMPONENT";
    public static final java.lang.String EXTRA_CHOSEN_COMPONENT_INTENT_SENDER = "android.intent.extra.CHOSEN_COMPONENT_INTENT_SENDER";
    public static final java.lang.String EXTRA_COMPONENT_NAME = "android.intent.extra.COMPONENT_NAME";
    public static final java.lang.String EXTRA_CONTENT_ANNOTATIONS = "android.intent.extra.CONTENT_ANNOTATIONS";
    public static final java.lang.String EXTRA_CONTENT_QUERY = "android.intent.extra.CONTENT_QUERY";
    public static final java.lang.String EXTRA_DATA_REMOVED = "android.intent.extra.DATA_REMOVED";
    public static final java.lang.String EXTRA_DOCK_STATE = "android.intent.extra.DOCK_STATE";
    public static final int EXTRA_DOCK_STATE_CAR = 2;
    public static final int EXTRA_DOCK_STATE_DESK = 1;
    public static final int EXTRA_DOCK_STATE_HE_DESK = 4;
    public static final int EXTRA_DOCK_STATE_LE_DESK = 3;
    public static final int EXTRA_DOCK_STATE_UNDOCKED = 0;
    public static final java.lang.String EXTRA_DONT_KILL_APP = "android.intent.extra.DONT_KILL_APP";
    public static final java.lang.String EXTRA_DURATION_MILLIS = "android.intent.extra.DURATION_MILLIS";
    public static final java.lang.String EXTRA_EMAIL = "android.intent.extra.EMAIL";
    public static final java.lang.String EXTRA_END_TIME = "android.intent.extra.END_TIME";
    public static final java.lang.String EXTRA_EXCLUDE_COMPONENTS = "android.intent.extra.EXCLUDE_COMPONENTS";
    public static final java.lang.String EXTRA_FROM_STORAGE = "android.intent.extra.FROM_STORAGE";
    public static final java.lang.String EXTRA_HTML_TEXT = "android.intent.extra.HTML_TEXT";
    public static final java.lang.String EXTRA_INDEX = "android.intent.extra.INDEX";
    public static final java.lang.String EXTRA_INITIAL_INTENTS = "android.intent.extra.INITIAL_INTENTS";
    public static final java.lang.String EXTRA_INSTALLER_PACKAGE_NAME = "android.intent.extra.INSTALLER_PACKAGE_NAME";
    public static final java.lang.String EXTRA_INTENT = "android.intent.extra.INTENT";
    public static final java.lang.String EXTRA_KEY_EVENT = "android.intent.extra.KEY_EVENT";
    public static final java.lang.String EXTRA_LOCALE_LIST = "android.intent.extra.LOCALE_LIST";
    public static final java.lang.String EXTRA_LOCAL_ONLY = "android.intent.extra.LOCAL_ONLY";
    public static final java.lang.String EXTRA_LOCUS_ID = "android.intent.extra.LOCUS_ID";
    public static final java.lang.String EXTRA_METADATA_TEXT = "android.intent.extra.METADATA_TEXT";
    public static final java.lang.String EXTRA_MIME_TYPES = "android.intent.extra.MIME_TYPES";
    public static final java.lang.String EXTRA_NOT_UNKNOWN_SOURCE = "android.intent.extra.NOT_UNKNOWN_SOURCE";
    public static final java.lang.String EXTRA_ORIGINATING_URI = "android.intent.extra.ORIGINATING_URI";
    public static final java.lang.String EXTRA_PACKAGES = "android.intent.extra.PACKAGES";
    public static final java.lang.String EXTRA_PACKAGE_NAME = "android.intent.extra.PACKAGE_NAME";
    public static final java.lang.String EXTRA_PERMISSION_GROUP_NAME = "android.intent.extra.PERMISSION_GROUP_NAME";
    public static final java.lang.String EXTRA_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";
    public static final java.lang.String EXTRA_PROCESS_TEXT = "android.intent.extra.PROCESS_TEXT";
    public static final java.lang.String EXTRA_PROCESS_TEXT_READONLY = "android.intent.extra.PROCESS_TEXT_READONLY";
    public static final java.lang.String EXTRA_QUICK_VIEW_FEATURES = "android.intent.extra.QUICK_VIEW_FEATURES";
    public static final java.lang.String EXTRA_QUIET_MODE = "android.intent.extra.QUIET_MODE";
    public static final java.lang.String EXTRA_REFERRER = "android.intent.extra.REFERRER";
    public static final java.lang.String EXTRA_REFERRER_NAME = "android.intent.extra.REFERRER_NAME";
    public static final java.lang.String EXTRA_REMOTE_INTENT_TOKEN = "android.intent.extra.remote_intent_token";
    public static final java.lang.String EXTRA_REPLACEMENT_EXTRAS = "android.intent.extra.REPLACEMENT_EXTRAS";
    public static final java.lang.String EXTRA_REPLACING = "android.intent.extra.REPLACING";
    public static final java.lang.String EXTRA_RESTRICTIONS_BUNDLE = "android.intent.extra.restrictions_bundle";
    public static final java.lang.String EXTRA_RESTRICTIONS_INTENT = "android.intent.extra.restrictions_intent";
    public static final java.lang.String EXTRA_RESTRICTIONS_LIST = "android.intent.extra.restrictions_list";
    public static final java.lang.String EXTRA_RESULT_RECEIVER = "android.intent.extra.RESULT_RECEIVER";
    public static final java.lang.String EXTRA_RETURN_RESULT = "android.intent.extra.RETURN_RESULT";
    public static final java.lang.String EXTRA_SHORTCUT_ICON = "android.intent.extra.shortcut.ICON";
    public static final java.lang.String EXTRA_SHORTCUT_ICON_RESOURCE = "android.intent.extra.shortcut.ICON_RESOURCE";
    public static final java.lang.String EXTRA_SHORTCUT_ID = "android.intent.extra.shortcut.ID";
    public static final java.lang.String EXTRA_SHORTCUT_INTENT = "android.intent.extra.shortcut.INTENT";
    public static final java.lang.String EXTRA_SHORTCUT_NAME = "android.intent.extra.shortcut.NAME";
    public static final java.lang.String EXTRA_SHUTDOWN_USERSPACE_ONLY = "android.intent.extra.SHUTDOWN_USERSPACE_ONLY";
    public static final java.lang.String EXTRA_SPLIT_NAME = "android.intent.extra.SPLIT_NAME";
    public static final java.lang.String EXTRA_START_TIME = "android.intent.extra.START_TIME";
    public static final java.lang.String EXTRA_STREAM = "android.intent.extra.STREAM";
    public static final java.lang.String EXTRA_SUBJECT = "android.intent.extra.SUBJECT";
    public static final java.lang.String EXTRA_SUSPENDED_PACKAGE_EXTRAS = "android.intent.extra.SUSPENDED_PACKAGE_EXTRAS";
    public static final java.lang.String EXTRA_TEMPLATE = "android.intent.extra.TEMPLATE";
    public static final java.lang.String EXTRA_TEXT = "android.intent.extra.TEXT";
    public static final java.lang.String EXTRA_TIME = "android.intent.extra.TIME";
    public static final java.lang.String EXTRA_TIMEZONE = "time-zone";
    public static final java.lang.String EXTRA_TITLE = "android.intent.extra.TITLE";
    public static final java.lang.String EXTRA_UID = "android.intent.extra.UID";
    public static final java.lang.String EXTRA_USER = "android.intent.extra.USER";
    public static final java.lang.String EXTRA_USER_INITIATED = "android.intent.extra.USER_INITIATED";
    public static final java.lang.String EXTRA_USE_STYLUS_MODE = "android.intent.extra.USE_STYLUS_MODE";
    public static final int FILL_IN_ACTION = 1;
    public static final int FILL_IN_CATEGORIES = 4;
    public static final int FILL_IN_CLIP_DATA = 128;
    public static final int FILL_IN_COMPONENT = 8;
    public static final int FILL_IN_DATA = 2;
    public static final int FILL_IN_IDENTIFIER = 256;
    public static final int FILL_IN_PACKAGE = 16;
    public static final int FILL_IN_SELECTOR = 64;
    public static final int FILL_IN_SOURCE_BOUNDS = 32;
    public static final int FLAG_ACTIVITY_BROUGHT_TO_FRONT = 4194304;
    public static final int FLAG_ACTIVITY_CLEAR_TASK = 32768;
    public static final int FLAG_ACTIVITY_CLEAR_TOP = 67108864;
    public static final int FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET = 524288;
    public static final int FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS = 8388608;
    public static final int FLAG_ACTIVITY_FORWARD_RESULT = 33554432;
    public static final int FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY = 1048576;
    public static final int FLAG_ACTIVITY_LAUNCH_ADJACENT = 4096;
    public static final int FLAG_ACTIVITY_MATCH_EXTERNAL = 2048;
    public static final int FLAG_ACTIVITY_MULTIPLE_TASK = 134217728;
    public static final int FLAG_ACTIVITY_NEW_DOCUMENT = 524288;
    public static final int FLAG_ACTIVITY_NEW_TASK = 268435456;
    public static final int FLAG_ACTIVITY_NO_ANIMATION = 65536;
    public static final int FLAG_ACTIVITY_NO_HISTORY = 1073741824;
    public static final int FLAG_ACTIVITY_NO_USER_ACTION = 262144;
    public static final int FLAG_ACTIVITY_PREVIOUS_IS_TOP = 16777216;
    public static final int FLAG_ACTIVITY_REORDER_TO_FRONT = 131072;
    public static final int FLAG_ACTIVITY_REQUIRE_DEFAULT = 512;
    public static final int FLAG_ACTIVITY_REQUIRE_NON_BROWSER = 1024;
    public static final int FLAG_ACTIVITY_RESET_TASK_IF_NEEDED = 2097152;
    public static final int FLAG_ACTIVITY_RETAIN_IN_RECENTS = 8192;
    public static final int FLAG_ACTIVITY_SINGLE_TOP = 536870912;
    public static final int FLAG_ACTIVITY_TASK_ON_HOME = 16384;
    public static final int FLAG_DEBUG_LOG_RESOLUTION = 8;
    public static final int FLAG_DIRECT_BOOT_AUTO = 256;
    public static final int FLAG_EXCLUDE_STOPPED_PACKAGES = 16;
    public static final int FLAG_FROM_BACKGROUND = 4;
    public static final int FLAG_GRANT_PERSISTABLE_URI_PERMISSION = 64;
    public static final int FLAG_GRANT_PREFIX_URI_PERMISSION = 128;
    public static final int FLAG_GRANT_READ_URI_PERMISSION = 1;
    public static final int FLAG_GRANT_WRITE_URI_PERMISSION = 2;
    public static final int FLAG_INCLUDE_STOPPED_PACKAGES = 32;
    public static final int FLAG_RECEIVER_FOREGROUND = 268435456;
    public static final int FLAG_RECEIVER_NO_ABORT = 134217728;
    public static final int FLAG_RECEIVER_REGISTERED_ONLY = 1073741824;
    public static final int FLAG_RECEIVER_REPLACE_PENDING = 536870912;
    public static final int FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS = 2097152;
    public static final java.lang.String METADATA_DOCK_HOME = "android.dock_home";
    public static final int URI_ALLOW_UNSAFE = 4;
    public static final int URI_ANDROID_APP_SCHEME = 2;
    public static final int URI_INTENT_SCHEME = 1;

    public Intent() {
        // AOSP frameworks-base core/java/android/content/Intent.java:7906-7908
        // Intent(): empty body. Nothing else to initialize on the shell.
    }

    public Intent(android.content.Context arg0, java.lang.Class arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V");
    }

    public Intent(android.content.Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;-><init>(Landroid/content/Intent;)V");
    }

    public Intent(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;-><init>(Ljava/lang/String;)V");
    }

    public Intent(java.lang.String arg0, android.net.Uri arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;-><init>(Ljava/lang/String;Landroid/net/Uri;)V");
    }

    public Intent(java.lang.String arg0, android.net.Uri arg1, android.content.Context arg2, java.lang.Class arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;-><init>(Ljava/lang/String;Landroid/net/Uri;Landroid/content/Context;Ljava/lang/Class;)V");
    }

    public android.content.Intent addCategory(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->addCategory(Ljava/lang/String;)Landroid/content/Intent;");
    }

    public android.content.Intent addFlags(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->addFlags(I)Landroid/content/Intent;");
    }

    public java.lang.Object clone() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->clone()Ljava/lang/Object;");
    }

    public android.content.Intent cloneFilter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->cloneFilter()Landroid/content/Intent;");
    }

    public static android.content.Intent createChooser(android.content.Intent arg0, java.lang.CharSequence arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->createChooser(Landroid/content/Intent;Ljava/lang/CharSequence;)Landroid/content/Intent;");
    }

    public static android.content.Intent createChooser(android.content.Intent arg0, java.lang.CharSequence arg1, android.content.IntentSender arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->createChooser(Landroid/content/Intent;Ljava/lang/CharSequence;Landroid/content/IntentSender;)Landroid/content/Intent;");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->describeContents()I");
    }

    public int fillIn(android.content.Intent arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->fillIn(Landroid/content/Intent;I)I");
    }

    public boolean filterEquals(android.content.Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->filterEquals(Landroid/content/Intent;)Z");
    }

    public int filterHashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->filterHashCode()I");
    }

    public java.lang.String getAction() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getAction()Ljava/lang/String;");
    }

    public boolean[] getBooleanArrayExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getBooleanArrayExtra(Ljava/lang/String;)[Z");
    }

    public boolean getBooleanExtra(java.lang.String arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getBooleanExtra(Ljava/lang/String;Z)Z");
    }

    public android.os.Bundle getBundleExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getBundleExtra(Ljava/lang/String;)Landroid/os/Bundle;");
    }

    public byte[] getByteArrayExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getByteArrayExtra(Ljava/lang/String;)[B");
    }

    public byte getByteExtra(java.lang.String arg0, byte arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getByteExtra(Ljava/lang/String;B)B");
    }

    public java.util.Set<java.lang.String> getCategories() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getCategories()Ljava/util/Set;");
    }

    public char[] getCharArrayExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getCharArrayExtra(Ljava/lang/String;)[C");
    }

    public char getCharExtra(java.lang.String arg0, char arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getCharExtra(Ljava/lang/String;C)C");
    }

    public java.lang.CharSequence[] getCharSequenceArrayExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getCharSequenceArrayExtra(Ljava/lang/String;)[Ljava/lang/CharSequence;");
    }

    public java.util.ArrayList<java.lang.CharSequence> getCharSequenceArrayListExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getCharSequenceArrayListExtra(Ljava/lang/String;)Ljava/util/ArrayList;");
    }

    public java.lang.CharSequence getCharSequenceExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getCharSequenceExtra(Ljava/lang/String;)Ljava/lang/CharSequence;");
    }

    public android.content.ClipData getClipData() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getClipData()Landroid/content/ClipData;");
    }

    public android.content.ComponentName getComponent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getComponent()Landroid/content/ComponentName;");
    }

    public android.net.Uri getData() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getData()Landroid/net/Uri;");
    }

    public java.lang.String getDataString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getDataString()Ljava/lang/String;");
    }

    public double[] getDoubleArrayExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getDoubleArrayExtra(Ljava/lang/String;)[D");
    }

    public double getDoubleExtra(java.lang.String arg0, double arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getDoubleExtra(Ljava/lang/String;D)D");
    }

    public android.os.Bundle getExtras() {
        // AOSP frameworks-base core/java/android/content/Intent.java
        // getExtras: (mExtras != null) ? new Bundle(mExtras) : null. The
        // shell Intent keeps no extras state (handless launch intent),
        // so the walk-through answers null (matches the interpreter's
        // empty-Intent builtin).
        return null;
    }

    public int getFlags() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getFlags()I");
    }

    public float[] getFloatArrayExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getFloatArrayExtra(Ljava/lang/String;)[F");
    }

    public float getFloatExtra(java.lang.String arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getFloatExtra(Ljava/lang/String;F)F");
    }

    public java.lang.String getIdentifier() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getIdentifier()Ljava/lang/String;");
    }

    public int[] getIntArrayExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getIntArrayExtra(Ljava/lang/String;)[I");
    }

    public int getIntExtra(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getIntExtra(Ljava/lang/String;I)I");
    }

    public java.util.ArrayList<java.lang.Integer> getIntegerArrayListExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getIntegerArrayListExtra(Ljava/lang/String;)Ljava/util/ArrayList;");
    }

    public static android.content.Intent getIntent(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getIntent(Ljava/lang/String;)Landroid/content/Intent;");
    }

    public static android.content.Intent getIntentOld(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getIntentOld(Ljava/lang/String;)Landroid/content/Intent;");
    }

    public long[] getLongArrayExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getLongArrayExtra(Ljava/lang/String;)[J");
    }

    public long getLongExtra(java.lang.String arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getLongExtra(Ljava/lang/String;J)J");
    }

    public java.lang.String getPackage() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getPackage()Ljava/lang/String;");
    }

    public android.os.Parcelable[] getParcelableArrayExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getParcelableArrayExtra(Ljava/lang/String;)[Landroid/os/Parcelable;");
    }

    public <T> T[] getParcelableArrayExtra(java.lang.String arg0, java.lang.Class<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getParcelableArrayExtra(Ljava/lang/String;Ljava/lang/Class;)[Ljava/lang/Object;");
    }

    public <T> java.util.ArrayList<T> getParcelableArrayListExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getParcelableArrayListExtra(Ljava/lang/String;)Ljava/util/ArrayList;");
    }

    public <T> java.util.ArrayList<T> getParcelableArrayListExtra(java.lang.String arg0, java.lang.Class<? extends T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getParcelableArrayListExtra(Ljava/lang/String;Ljava/lang/Class;)Ljava/util/ArrayList;");
    }

    public <T> T getParcelableExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getParcelableExtra(Ljava/lang/String;)Landroid/os/Parcelable;");
    }

    public <T> T getParcelableExtra(java.lang.String arg0, java.lang.Class<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getParcelableExtra(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;");
    }

    public java.lang.String getScheme() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getScheme()Ljava/lang/String;");
    }

    public android.content.Intent getSelector() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getSelector()Landroid/content/Intent;");
    }

    public java.io.Serializable getSerializableExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getSerializableExtra(Ljava/lang/String;)Ljava/io/Serializable;");
    }

    public <T> T getSerializableExtra(java.lang.String arg0, java.lang.Class<T> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getSerializableExtra(Ljava/lang/String;Ljava/lang/Class;)Ljava/io/Serializable;");
    }

    public short[] getShortArrayExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getShortArrayExtra(Ljava/lang/String;)[S");
    }

    public short getShortExtra(java.lang.String arg0, short arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getShortExtra(Ljava/lang/String;S)S");
    }

    public android.graphics.Rect getSourceBounds() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getSourceBounds()Landroid/graphics/Rect;");
    }

    public java.lang.String[] getStringArrayExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getStringArrayExtra(Ljava/lang/String;)[Ljava/lang/String;");
    }

    public java.util.ArrayList<java.lang.String> getStringArrayListExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getStringArrayListExtra(Ljava/lang/String;)Ljava/util/ArrayList;");
    }

    public java.lang.String getStringExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getStringExtra(Ljava/lang/String;)Ljava/lang/String;");
    }

    public java.lang.String getType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->getType()Ljava/lang/String;");
    }

    public boolean hasCategory(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->hasCategory(Ljava/lang/String;)Z");
    }

    public boolean hasExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->hasExtra(Ljava/lang/String;)Z");
    }

    public boolean hasFileDescriptors() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->hasFileDescriptors()Z");
    }

    public boolean isMismatchingFilter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->isMismatchingFilter()Z");
    }

    public static android.content.Intent makeMainActivity(android.content.ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->makeMainActivity(Landroid/content/ComponentName;)Landroid/content/Intent;");
    }

    public static android.content.Intent makeMainSelectorActivity(java.lang.String arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->makeMainSelectorActivity(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;");
    }

    public static android.content.Intent makeRestartActivityTask(android.content.ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->makeRestartActivityTask(Landroid/content/ComponentName;)Landroid/content/Intent;");
    }

    public static java.lang.String normalizeMimeType(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->normalizeMimeType(Ljava/lang/String;)Ljava/lang/String;");
    }

    public static android.content.Intent parseIntent(android.content.res.Resources arg0, org.xmlpull.v1.XmlPullParser arg1, android.util.AttributeSet arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->parseIntent(Landroid/content/res/Resources;Lorg/xmlpull/v1/XmlPullParser;Landroid/util/AttributeSet;)Landroid/content/Intent;");
    }

    public static android.content.Intent parseUri(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->parseUri(Ljava/lang/String;I)Landroid/content/Intent;");
    }

    public android.content.Intent putCharSequenceArrayListExtra(java.lang.String arg0, java.util.ArrayList<java.lang.CharSequence> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putCharSequenceArrayListExtra(Ljava/lang/String;Ljava/util/ArrayList;)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, android.os.Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;Landroid/os/Bundle;)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, android.os.Parcelable arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;Landroid/os/Parcelable;)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, android.os.Parcelable[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;[Landroid/os/Parcelable;)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;Z)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, boolean[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;[Z)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, byte arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;B)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, byte[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;[B)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, char arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;C)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, char[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;[C)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, double arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;D)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, double[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;[D)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, float arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;F)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, float[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;[F)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;I)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, int[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;[I)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, java.io.Serializable arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;Ljava/io/Serializable;)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, java.lang.CharSequence arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;Ljava/lang/CharSequence;)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, java.lang.CharSequence[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;[Ljava/lang/CharSequence;)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, java.lang.String[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, long arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;J)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, long[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;[J)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, short arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;S)Landroid/content/Intent;");
    }

    public android.content.Intent putExtra(java.lang.String arg0, short[] arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtra(Ljava/lang/String;[S)Landroid/content/Intent;");
    }

    public android.content.Intent putExtras(android.content.Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtras(Landroid/content/Intent;)Landroid/content/Intent;");
    }

    public android.content.Intent putExtras(android.os.Bundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putExtras(Landroid/os/Bundle;)Landroid/content/Intent;");
    }

    public android.content.Intent putIntegerArrayListExtra(java.lang.String arg0, java.util.ArrayList<java.lang.Integer> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putIntegerArrayListExtra(Ljava/lang/String;Ljava/util/ArrayList;)Landroid/content/Intent;");
    }

    public android.content.Intent putParcelableArrayListExtra(java.lang.String arg0, java.util.ArrayList<? extends android.os.Parcelable> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putParcelableArrayListExtra(Ljava/lang/String;Ljava/util/ArrayList;)Landroid/content/Intent;");
    }

    public android.content.Intent putStringArrayListExtra(java.lang.String arg0, java.util.ArrayList<java.lang.String> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->putStringArrayListExtra(Ljava/lang/String;Ljava/util/ArrayList;)Landroid/content/Intent;");
    }

    public void readFromParcel(android.os.Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->readFromParcel(Landroid/os/Parcel;)V");
    }

    public void removeCategory(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->removeCategory(Ljava/lang/String;)V");
    }

    public void removeExtra(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->removeExtra(Ljava/lang/String;)V");
    }

    public void removeFlags(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->removeFlags(I)V");
    }

    public void removeLaunchSecurityProtection() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->removeLaunchSecurityProtection()V");
    }

    public android.content.Intent replaceExtras(android.content.Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->replaceExtras(Landroid/content/Intent;)Landroid/content/Intent;");
    }

    public android.content.Intent replaceExtras(android.os.Bundle arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->replaceExtras(Landroid/os/Bundle;)Landroid/content/Intent;");
    }

    public android.content.ComponentName resolveActivity(android.content.pm.PackageManager arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->resolveActivity(Landroid/content/pm/PackageManager;)Landroid/content/ComponentName;");
    }

    public android.content.pm.ActivityInfo resolveActivityInfo(android.content.pm.PackageManager arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->resolveActivityInfo(Landroid/content/pm/PackageManager;I)Landroid/content/pm/ActivityInfo;");
    }

    public java.lang.String resolveType(android.content.ContentResolver arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->resolveType(Landroid/content/ContentResolver;)Ljava/lang/String;");
    }

    public java.lang.String resolveType(android.content.Context arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->resolveType(Landroid/content/Context;)Ljava/lang/String;");
    }

    public java.lang.String resolveTypeIfNeeded(android.content.ContentResolver arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->resolveTypeIfNeeded(Landroid/content/ContentResolver;)Ljava/lang/String;");
    }

    public android.content.Intent setAction(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setAction(Ljava/lang/String;)Landroid/content/Intent;");
    }

    public android.content.Intent setClass(android.content.Context arg0, java.lang.Class<?> arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setClass(Landroid/content/Context;Ljava/lang/Class;)Landroid/content/Intent;");
    }

    public android.content.Intent setClassName(android.content.Context arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setClassName(Landroid/content/Context;Ljava/lang/String;)Landroid/content/Intent;");
    }

    public android.content.Intent setClassName(java.lang.String arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setClassName(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;");
    }

    public void setClipData(android.content.ClipData arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setClipData(Landroid/content/ClipData;)V");
    }

    public android.content.Intent setComponent(android.content.ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setComponent(Landroid/content/ComponentName;)Landroid/content/Intent;");
    }

    public android.content.Intent setData(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setData(Landroid/net/Uri;)Landroid/content/Intent;");
    }

    public android.content.Intent setDataAndNormalize(android.net.Uri arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setDataAndNormalize(Landroid/net/Uri;)Landroid/content/Intent;");
    }

    public android.content.Intent setDataAndType(android.net.Uri arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setDataAndType(Landroid/net/Uri;Ljava/lang/String;)Landroid/content/Intent;");
    }

    public android.content.Intent setDataAndTypeAndNormalize(android.net.Uri arg0, java.lang.String arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setDataAndTypeAndNormalize(Landroid/net/Uri;Ljava/lang/String;)Landroid/content/Intent;");
    }

    public void setExtrasClassLoader(java.lang.ClassLoader arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setExtrasClassLoader(Ljava/lang/ClassLoader;)V");
    }

    public android.content.Intent setFlags(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setFlags(I)Landroid/content/Intent;");
    }

    public android.content.Intent setIdentifier(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setIdentifier(Ljava/lang/String;)Landroid/content/Intent;");
    }

    public android.content.Intent setPackage(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setPackage(Ljava/lang/String;)Landroid/content/Intent;");
    }

    public void setSelector(android.content.Intent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setSelector(Landroid/content/Intent;)V");
    }

    public void setSourceBounds(android.graphics.Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setSourceBounds(Landroid/graphics/Rect;)V");
    }

    public android.content.Intent setType(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setType(Ljava/lang/String;)Landroid/content/Intent;");
    }

    public android.content.Intent setTypeAndNormalize(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->setTypeAndNormalize(Ljava/lang/String;)Landroid/content/Intent;");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->toString()Ljava/lang/String;");
    }

    public java.lang.String toURI() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->toURI()Ljava/lang/String;");
    }

    public java.lang.String toUri(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->toUri(I)Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/Intent;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
