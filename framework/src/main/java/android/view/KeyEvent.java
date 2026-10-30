package android.view;

import android.os.Parcel;
import android.os.Parcelable;

    public class KeyEvent extends InputEvent implements Parcelable {

    public static final int ACTION_DOWN = 0;
    public static final int ACTION_MULTIPLE = 2;
    public static final int ACTION_UP = 1;
    public static Parcelable.Creator CREATOR;
    public static final int FLAG_CANCELED = 32;
    public static final int FLAG_CANCELED_LONG_PRESS = 256;
    public static final int FLAG_EDITOR_ACTION = 16;
    public static final int FLAG_FALLBACK = 1024;
    public static final int FLAG_FROM_SYSTEM = 8;
    public static final int FLAG_KEEP_TOUCH_MODE = 4;
    public static final int FLAG_LONG_PRESS = 128;
    public static final int FLAG_SOFT_KEYBOARD = 2;
    public static final int FLAG_TRACKING = 512;
    public static final int FLAG_VIRTUAL_HARD_KEY = 64;
    public static final int FLAG_WOKE_HERE = 1;
    public static final int KEYCODE_0 = 7;
    public static final int KEYCODE_1 = 8;
    public static final int KEYCODE_11 = 227;
    public static final int KEYCODE_12 = 228;
    public static final int KEYCODE_2 = 9;
    public static final int KEYCODE_3 = 10;
    public static final int KEYCODE_3D_MODE = 206;
    public static final int KEYCODE_4 = 11;
    public static final int KEYCODE_5 = 12;
    public static final int KEYCODE_6 = 13;
    public static final int KEYCODE_7 = 14;
    public static final int KEYCODE_8 = 15;
    public static final int KEYCODE_9 = 16;
    public static final int KEYCODE_A = 29;
    public static final int KEYCODE_ALL_APPS = 284;
    public static final int KEYCODE_ALT_LEFT = 57;
    public static final int KEYCODE_ALT_RIGHT = 58;
    public static final int KEYCODE_APOSTROPHE = 75;
    public static final int KEYCODE_APP_SWITCH = 187;
    public static final int KEYCODE_ASSIST = 219;
    public static final int KEYCODE_AT = 77;
    public static final int KEYCODE_AVR_INPUT = 182;
    public static final int KEYCODE_AVR_POWER = 181;
    public static final int KEYCODE_B = 30;
    public static final int KEYCODE_BACK = 4;
    public static final int KEYCODE_BACKSLASH = 73;
    public static final int KEYCODE_BOOKMARK = 174;
    public static final int KEYCODE_BREAK = 121;
    public static final int KEYCODE_BRIGHTNESS_DOWN = 220;
    public static final int KEYCODE_BRIGHTNESS_UP = 221;
    public static final int KEYCODE_BUTTON_1 = 188;
    public static final int KEYCODE_BUTTON_10 = 197;
    public static final int KEYCODE_BUTTON_11 = 198;
    public static final int KEYCODE_BUTTON_12 = 199;
    public static final int KEYCODE_BUTTON_13 = 200;
    public static final int KEYCODE_BUTTON_14 = 201;
    public static final int KEYCODE_BUTTON_15 = 202;
    public static final int KEYCODE_BUTTON_16 = 203;
    public static final int KEYCODE_BUTTON_2 = 189;
    public static final int KEYCODE_BUTTON_3 = 190;
    public static final int KEYCODE_BUTTON_4 = 191;
    public static final int KEYCODE_BUTTON_5 = 192;
    public static final int KEYCODE_BUTTON_6 = 193;
    public static final int KEYCODE_BUTTON_7 = 194;
    public static final int KEYCODE_BUTTON_8 = 195;
    public static final int KEYCODE_BUTTON_9 = 196;
    public static final int KEYCODE_BUTTON_A = 96;
    public static final int KEYCODE_BUTTON_B = 97;
    public static final int KEYCODE_BUTTON_C = 98;
    public static final int KEYCODE_BUTTON_L1 = 102;
    public static final int KEYCODE_BUTTON_L2 = 104;
    public static final int KEYCODE_BUTTON_MODE = 110;
    public static final int KEYCODE_BUTTON_R1 = 103;
    public static final int KEYCODE_BUTTON_R2 = 105;
    public static final int KEYCODE_BUTTON_SELECT = 109;
    public static final int KEYCODE_BUTTON_START = 108;
    public static final int KEYCODE_BUTTON_THUMBL = 106;
    public static final int KEYCODE_BUTTON_THUMBR = 107;
    public static final int KEYCODE_BUTTON_X = 99;
    public static final int KEYCODE_BUTTON_Y = 100;
    public static final int KEYCODE_BUTTON_Z = 101;
    public static final int KEYCODE_C = 31;
    public static final int KEYCODE_CALCULATOR = 210;
    public static final int KEYCODE_CALENDAR = 208;
    public static final int KEYCODE_CALL = 5;
    public static final int KEYCODE_CAMERA = 27;
    public static final int KEYCODE_CAPS_LOCK = 115;
    public static final int KEYCODE_CAPTIONS = 175;
    public static final int KEYCODE_CHANNEL_DOWN = 167;
    public static final int KEYCODE_CHANNEL_UP = 166;
    public static final int KEYCODE_CLEAR = 28;
    public static final int KEYCODE_CLOSE = 321;
    public static final int KEYCODE_COMMA = 55;
    public static final int KEYCODE_CONTACTS = 207;
    public static final int KEYCODE_COPY = 278;
    public static final int KEYCODE_CTRL_LEFT = 113;
    public static final int KEYCODE_CTRL_RIGHT = 114;
    public static final int KEYCODE_CUT = 277;
    public static final int KEYCODE_D = 32;
    public static final int KEYCODE_DEL = 67;
    public static final int KEYCODE_DEMO_APP_1 = 301;
    public static final int KEYCODE_DEMO_APP_2 = 302;
    public static final int KEYCODE_DEMO_APP_3 = 303;
    public static final int KEYCODE_DEMO_APP_4 = 304;
    public static final int KEYCODE_DICTATE = 319;
    public static final int KEYCODE_DO_NOT_DISTURB = 322;
    public static final int KEYCODE_DPAD_CENTER = 23;
    public static final int KEYCODE_DPAD_DOWN = 20;
    public static final int KEYCODE_DPAD_DOWN_LEFT = 269;
    public static final int KEYCODE_DPAD_DOWN_RIGHT = 271;
    public static final int KEYCODE_DPAD_LEFT = 21;
    public static final int KEYCODE_DPAD_RIGHT = 22;
    public static final int KEYCODE_DPAD_UP = 19;
    public static final int KEYCODE_DPAD_UP_LEFT = 268;
    public static final int KEYCODE_DPAD_UP_RIGHT = 270;
    public static final int KEYCODE_DVR = 173;
    public static final int KEYCODE_E = 33;
    public static final int KEYCODE_EISU = 212;
    public static final int KEYCODE_EMOJI_PICKER = 317;
    public static final int KEYCODE_ENDCALL = 6;
    public static final int KEYCODE_ENTER = 66;
    public static final int KEYCODE_ENVELOPE = 65;
    public static final int KEYCODE_EQUALS = 70;
    public static final int KEYCODE_ESCAPE = 111;
    public static final int KEYCODE_EXPLORER = 64;
    public static final int KEYCODE_F = 34;
    public static final int KEYCODE_F1 = 131;
    public static final int KEYCODE_F10 = 140;
    public static final int KEYCODE_F11 = 141;
    public static final int KEYCODE_F12 = 142;
    public static final int KEYCODE_F13 = 326;
    public static final int KEYCODE_F14 = 327;
    public static final int KEYCODE_F15 = 328;
    public static final int KEYCODE_F16 = 329;
    public static final int KEYCODE_F17 = 330;
    public static final int KEYCODE_F18 = 331;
    public static final int KEYCODE_F19 = 332;
    public static final int KEYCODE_F2 = 132;
    public static final int KEYCODE_F20 = 333;
    public static final int KEYCODE_F21 = 334;
    public static final int KEYCODE_F22 = 335;
    public static final int KEYCODE_F23 = 336;
    public static final int KEYCODE_F24 = 337;
    public static final int KEYCODE_F3 = 133;
    public static final int KEYCODE_F4 = 134;
    public static final int KEYCODE_F5 = 135;
    public static final int KEYCODE_F6 = 136;
    public static final int KEYCODE_F7 = 137;
    public static final int KEYCODE_F8 = 138;
    public static final int KEYCODE_F9 = 139;
    public static final int KEYCODE_FEATURED_APP_1 = 297;
    public static final int KEYCODE_FEATURED_APP_2 = 298;
    public static final int KEYCODE_FEATURED_APP_3 = 299;
    public static final int KEYCODE_FEATURED_APP_4 = 300;
    public static final int KEYCODE_FOCUS = 80;
    public static final int KEYCODE_FORWARD = 125;
    public static final int KEYCODE_FORWARD_DEL = 112;
    public static final int KEYCODE_FULLSCREEN = 325;
    public static final int KEYCODE_FUNCTION = 119;
    public static final int KEYCODE_G = 35;
    public static final int KEYCODE_GRAVE = 68;
    public static final int KEYCODE_GUIDE = 172;
    public static final int KEYCODE_H = 36;
    public static final int KEYCODE_HEADSETHOOK = 79;
    public static final int KEYCODE_HELP = 259;
    public static final int KEYCODE_HENKAN = 214;
    public static final int KEYCODE_HOME = 3;
    public static final int KEYCODE_I = 37;
    public static final int KEYCODE_INFO = 165;
    public static final int KEYCODE_INSERT = 124;
    public static final int KEYCODE_J = 38;
    public static final int KEYCODE_K = 39;
    public static final int KEYCODE_KANA = 218;
    public static final int KEYCODE_KATAKANA_HIRAGANA = 215;
    public static final int KEYCODE_KEYBOARD_BACKLIGHT_DOWN = 305;
    public static final int KEYCODE_KEYBOARD_BACKLIGHT_TOGGLE = 307;
    public static final int KEYCODE_KEYBOARD_BACKLIGHT_UP = 306;
    public static final int KEYCODE_L = 40;
    public static final int KEYCODE_LANGUAGE_SWITCH = 204;
    public static final int KEYCODE_LAST_CHANNEL = 229;
    public static final int KEYCODE_LEFT_BRACKET = 71;
    public static final int KEYCODE_LOCK = 324;
    public static final int KEYCODE_M = 41;
    public static final int KEYCODE_MACRO_1 = 313;
    public static final int KEYCODE_MACRO_2 = 314;
    public static final int KEYCODE_MACRO_3 = 315;
    public static final int KEYCODE_MACRO_4 = 316;
    public static final int KEYCODE_MANNER_MODE = 205;
    public static final int KEYCODE_MEDIA_AUDIO_TRACK = 222;
    public static final int KEYCODE_MEDIA_CLOSE = 128;
    public static final int KEYCODE_MEDIA_EJECT = 129;
    public static final int KEYCODE_MEDIA_FAST_FORWARD = 90;
    public static final int KEYCODE_MEDIA_NEXT = 87;
    public static final int KEYCODE_MEDIA_PAUSE = 127;
    public static final int KEYCODE_MEDIA_PLAY = 126;
    public static final int KEYCODE_MEDIA_PLAY_PAUSE = 85;
    public static final int KEYCODE_MEDIA_PREVIOUS = 88;
    public static final int KEYCODE_MEDIA_RECORD = 130;
    public static final int KEYCODE_MEDIA_REWIND = 89;
    public static final int KEYCODE_MEDIA_SKIP_BACKWARD = 273;
    public static final int KEYCODE_MEDIA_SKIP_FORWARD = 272;
    public static final int KEYCODE_MEDIA_STEP_BACKWARD = 275;
    public static final int KEYCODE_MEDIA_STEP_FORWARD = 274;
    public static final int KEYCODE_MEDIA_STOP = 86;
    public static final int KEYCODE_MEDIA_TOP_MENU = 226;
    public static final int KEYCODE_MENU = 82;
    public static final int KEYCODE_META_LEFT = 117;
    public static final int KEYCODE_META_RIGHT = 118;
    public static final int KEYCODE_MINUS = 69;
    public static final int KEYCODE_MOVE_END = 123;
    public static final int KEYCODE_MOVE_HOME = 122;
    public static final int KEYCODE_MUHENKAN = 213;
    public static final int KEYCODE_MUSIC = 209;
    public static final int KEYCODE_MUTE = 91;
    public static final int KEYCODE_N = 42;
    public static final int KEYCODE_NAVIGATE_IN = 262;
    public static final int KEYCODE_NAVIGATE_NEXT = 261;
    public static final int KEYCODE_NAVIGATE_OUT = 263;
    public static final int KEYCODE_NAVIGATE_PREVIOUS = 260;
    public static final int KEYCODE_NEW = 320;
    public static final int KEYCODE_NOTIFICATION = 83;
    public static final int KEYCODE_NUM = 78;
    public static final int KEYCODE_NUMPAD_0 = 144;
    public static final int KEYCODE_NUMPAD_1 = 145;
    public static final int KEYCODE_NUMPAD_2 = 146;
    public static final int KEYCODE_NUMPAD_3 = 147;
    public static final int KEYCODE_NUMPAD_4 = 148;
    public static final int KEYCODE_NUMPAD_5 = 149;
    public static final int KEYCODE_NUMPAD_6 = 150;
    public static final int KEYCODE_NUMPAD_7 = 151;
    public static final int KEYCODE_NUMPAD_8 = 152;
    public static final int KEYCODE_NUMPAD_9 = 153;
    public static final int KEYCODE_NUMPAD_ADD = 157;
    public static final int KEYCODE_NUMPAD_COMMA = 159;
    public static final int KEYCODE_NUMPAD_DIVIDE = 154;
    public static final int KEYCODE_NUMPAD_DOT = 158;
    public static final int KEYCODE_NUMPAD_ENTER = 160;
    public static final int KEYCODE_NUMPAD_EQUALS = 161;
    public static final int KEYCODE_NUMPAD_LEFT_PAREN = 162;
    public static final int KEYCODE_NUMPAD_MULTIPLY = 155;
    public static final int KEYCODE_NUMPAD_RIGHT_PAREN = 163;
    public static final int KEYCODE_NUMPAD_SUBTRACT = 156;
    public static final int KEYCODE_NUM_LOCK = 143;
    public static final int KEYCODE_O = 43;
    public static final int KEYCODE_P = 44;
    public static final int KEYCODE_PAGE_DOWN = 93;
    public static final int KEYCODE_PAGE_UP = 92;
    public static final int KEYCODE_PAIRING = 225;
    public static final int KEYCODE_PASTE = 279;
    public static final int KEYCODE_PERIOD = 56;
    public static final int KEYCODE_PICTSYMBOLS = 94;
    public static final int KEYCODE_PLUS = 81;
    public static final int KEYCODE_POUND = 18;
    public static final int KEYCODE_POWER = 26;
    public static final int KEYCODE_PRINT = 323;
    public static final int KEYCODE_PROFILE_SWITCH = 288;
    public static final int KEYCODE_PROG_BLUE = 186;
    public static final int KEYCODE_PROG_GREEN = 184;
    public static final int KEYCODE_PROG_RED = 183;
    public static final int KEYCODE_PROG_YELLOW = 185;
    public static final int KEYCODE_Q = 45;
    public static final int KEYCODE_R = 46;
    public static final int KEYCODE_RECENT_APPS = 312;
    public static final int KEYCODE_REFRESH = 285;
    public static final int KEYCODE_RIGHT_BRACKET = 72;
    public static final int KEYCODE_RO = 217;
    public static final int KEYCODE_S = 47;
    public static final int KEYCODE_SCREENSHOT = 318;
    public static final int KEYCODE_SCROLL_LOCK = 116;
    public static final int KEYCODE_SEARCH = 84;
    public static final int KEYCODE_SEMICOLON = 74;
    public static final int KEYCODE_SETTINGS = 176;
    public static final int KEYCODE_SHIFT_LEFT = 59;
    public static final int KEYCODE_SHIFT_RIGHT = 60;
    public static final int KEYCODE_SLASH = 76;
    public static final int KEYCODE_SLEEP = 223;
    public static final int KEYCODE_SOFT_LEFT = 1;
    public static final int KEYCODE_SOFT_RIGHT = 2;
    public static final int KEYCODE_SOFT_SLEEP = 276;
    public static final int KEYCODE_SPACE = 62;
    public static final int KEYCODE_STAR = 17;
    public static final int KEYCODE_STB_INPUT = 180;
    public static final int KEYCODE_STB_POWER = 179;
    public static final int KEYCODE_STEM_1 = 265;
    public static final int KEYCODE_STEM_2 = 266;
    public static final int KEYCODE_STEM_3 = 267;
    public static final int KEYCODE_STEM_PRIMARY = 264;
    public static final int KEYCODE_STYLUS_BUTTON_PRIMARY = 308;
    public static final int KEYCODE_STYLUS_BUTTON_SECONDARY = 309;
    public static final int KEYCODE_STYLUS_BUTTON_TAIL = 311;
    public static final int KEYCODE_STYLUS_BUTTON_TERTIARY = 310;
    public static final int KEYCODE_SWITCH_CHARSET = 95;
    public static final int KEYCODE_SYM = 63;
    public static final int KEYCODE_SYSRQ = 120;
    public static final int KEYCODE_SYSTEM_NAVIGATION_DOWN = 281;
    public static final int KEYCODE_SYSTEM_NAVIGATION_LEFT = 282;
    public static final int KEYCODE_SYSTEM_NAVIGATION_RIGHT = 283;
    public static final int KEYCODE_SYSTEM_NAVIGATION_UP = 280;
    public static final int KEYCODE_T = 48;
    public static final int KEYCODE_TAB = 61;
    public static final int KEYCODE_THUMBS_DOWN = 287;
    public static final int KEYCODE_THUMBS_UP = 286;
    public static final int KEYCODE_TV = 170;
    public static final int KEYCODE_TV_ANTENNA_CABLE = 242;
    public static final int KEYCODE_TV_AUDIO_DESCRIPTION = 252;
    public static final int KEYCODE_TV_AUDIO_DESCRIPTION_MIX_DOWN = 254;
    public static final int KEYCODE_TV_AUDIO_DESCRIPTION_MIX_UP = 253;
    public static final int KEYCODE_TV_CONTENTS_MENU = 256;
    public static final int KEYCODE_TV_DATA_SERVICE = 230;
    public static final int KEYCODE_TV_INPUT = 178;
    public static final int KEYCODE_TV_INPUT_COMPONENT_1 = 249;
    public static final int KEYCODE_TV_INPUT_COMPONENT_2 = 250;
    public static final int KEYCODE_TV_INPUT_COMPOSITE_1 = 247;
    public static final int KEYCODE_TV_INPUT_COMPOSITE_2 = 248;
    public static final int KEYCODE_TV_INPUT_HDMI_1 = 243;
    public static final int KEYCODE_TV_INPUT_HDMI_2 = 244;
    public static final int KEYCODE_TV_INPUT_HDMI_3 = 245;
    public static final int KEYCODE_TV_INPUT_HDMI_4 = 246;
    public static final int KEYCODE_TV_INPUT_VGA_1 = 251;
    public static final int KEYCODE_TV_MEDIA_CONTEXT_MENU = 257;
    public static final int KEYCODE_TV_NETWORK = 241;
    public static final int KEYCODE_TV_NUMBER_ENTRY = 234;
    public static final int KEYCODE_TV_POWER = 177;
    public static final int KEYCODE_TV_RADIO_SERVICE = 232;
    public static final int KEYCODE_TV_SATELLITE = 237;
    public static final int KEYCODE_TV_SATELLITE_BS = 238;
    public static final int KEYCODE_TV_SATELLITE_CS = 239;
    public static final int KEYCODE_TV_SATELLITE_SERVICE = 240;
    public static final int KEYCODE_TV_TELETEXT = 233;
    public static final int KEYCODE_TV_TERRESTRIAL_ANALOG = 235;
    public static final int KEYCODE_TV_TERRESTRIAL_DIGITAL = 236;
    public static final int KEYCODE_TV_TIMER_PROGRAMMING = 258;
    public static final int KEYCODE_TV_ZOOM_MODE = 255;
    public static final int KEYCODE_U = 49;
    public static final int KEYCODE_UNKNOWN = 0;
    public static final int KEYCODE_V = 50;
    public static final int KEYCODE_VIDEO_APP_1 = 289;
    public static final int KEYCODE_VIDEO_APP_2 = 290;
    public static final int KEYCODE_VIDEO_APP_3 = 291;
    public static final int KEYCODE_VIDEO_APP_4 = 292;
    public static final int KEYCODE_VIDEO_APP_5 = 293;
    public static final int KEYCODE_VIDEO_APP_6 = 294;
    public static final int KEYCODE_VIDEO_APP_7 = 295;
    public static final int KEYCODE_VIDEO_APP_8 = 296;
    public static final int KEYCODE_VOICE_ASSIST = 231;
    public static final int KEYCODE_VOLUME_DOWN = 25;
    public static final int KEYCODE_VOLUME_MUTE = 164;
    public static final int KEYCODE_VOLUME_UP = 24;
    public static final int KEYCODE_W = 51;
    public static final int KEYCODE_WAKEUP = 224;
    public static final int KEYCODE_WINDOW = 171;
    public static final int KEYCODE_X = 52;
    public static final int KEYCODE_Y = 53;
    public static final int KEYCODE_YEN = 216;
    public static final int KEYCODE_Z = 54;
    public static final int KEYCODE_ZENKAKU_HANKAKU = 211;
    public static final int KEYCODE_ZOOM_IN = 168;
    public static final int KEYCODE_ZOOM_OUT = 169;
    public static final int MAX_KEYCODE = 84;
    public static final int META_ALT_LEFT_ON = 16;
    public static final int META_ALT_MASK = 50;
    public static final int META_ALT_ON = 2;
    public static final int META_ALT_RIGHT_ON = 32;
    public static final int META_CAPS_LOCK_ON = 1048576;
    public static final int META_CTRL_LEFT_ON = 8192;
    public static final int META_CTRL_MASK = 28672;
    public static final int META_CTRL_ON = 4096;
    public static final int META_CTRL_RIGHT_ON = 16384;
    public static final int META_FUNCTION_ON = 8;
    public static final int META_META_LEFT_ON = 131072;
    public static final int META_META_MASK = 458752;
    public static final int META_META_ON = 65536;
    public static final int META_META_RIGHT_ON = 262144;
    public static final int META_NUM_LOCK_ON = 2097152;
    public static final int META_SCROLL_LOCK_ON = 4194304;
    public static final int META_SHIFT_LEFT_ON = 64;
    public static final int META_SHIFT_MASK = 193;
    public static final int META_SHIFT_ON = 1;
    public static final int META_SHIFT_RIGHT_ON = 128;
    public static final int META_SYM_ON = 4;

    public KeyEvent(KeyEvent arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;-><init>(Landroid/view/KeyEvent;)V");
    }

    public KeyEvent(KeyEvent arg0, long arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;-><init>(Landroid/view/KeyEvent;JI)V");
    }

    public KeyEvent(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;-><init>(II)V");
    }

    public KeyEvent(long arg0, String arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;-><init>(JLjava/lang/String;II)V");
    }

    public KeyEvent(long arg0, long arg1, int arg2, int arg3, int arg4) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;-><init>(JJIII)V");
    }

    public KeyEvent(long arg0, long arg1, int arg2, int arg3, int arg4, int arg5) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;-><init>(JJIIII)V");
    }

    public KeyEvent(long arg0, long arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;-><init>(JJIIIIII)V");
    }

    public KeyEvent(long arg0, long arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;-><init>(JJIIIIIII)V");
    }

    public KeyEvent(long arg0, long arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;-><init>(JJIIIIIIII)V");
    }

    public static KeyEvent changeAction(KeyEvent arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->changeAction(Landroid/view/KeyEvent;I)Landroid/view/KeyEvent;");
    }

    public static KeyEvent changeFlags(KeyEvent arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->changeFlags(Landroid/view/KeyEvent;I)Landroid/view/KeyEvent;");
    }

    public static KeyEvent changeTimeRepeat(KeyEvent arg0, long arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->changeTimeRepeat(Landroid/view/KeyEvent;JI)Landroid/view/KeyEvent;");
    }

    public static KeyEvent changeTimeRepeat(KeyEvent arg0, long arg1, int arg2, int arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->changeTimeRepeat(Landroid/view/KeyEvent;JII)Landroid/view/KeyEvent;");
    }

    public final boolean dispatch(KeyEvent.Callback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->dispatch(Landroid/view/KeyEvent$Callback;)Z");
    }

    public final boolean dispatch(KeyEvent.Callback arg0, KeyEvent.DispatcherState arg1, Object arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->dispatch(Landroid/view/KeyEvent$Callback;Landroid/view/KeyEvent$DispatcherState;Ljava/lang/Object;)Z");
    }

    public final int getAction() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getAction()I");
    }

    public final String getCharacters() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getCharacters()Ljava/lang/String;");
    }

    public static int getDeadChar(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getDeadChar(II)I");
    }

    public final int getDeviceId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getDeviceId()I");
    }

    public char getDisplayLabel() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getDisplayLabel()C");
    }

    public final long getDownTime() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getDownTime()J");
    }

    public final long getEventTime() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getEventTime()J");
    }

    public final int getFlags() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getFlags()I");
    }

    public final KeyCharacterMap getKeyCharacterMap() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getKeyCharacterMap()Landroid/view/KeyCharacterMap;");
    }

    public final int getKeyCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getKeyCode()I");
    }

    public boolean getKeyData(KeyCharacterMap.KeyData arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getKeyData(Landroid/view/KeyCharacterMap$KeyData;)Z");
    }

    public char getMatch(char[] arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getMatch([C)C");
    }

    public char getMatch(char[] arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getMatch([CI)C");
    }

    public static int getMaxKeyCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getMaxKeyCode()I");
    }

    public final int getMetaState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getMetaState()I");
    }

    public static int getModifierMetaStateMask() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getModifierMetaStateMask()I");
    }

    public final int getModifiers() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getModifiers()I");
    }

    public char getNumber() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getNumber()C");
    }

    public final int getRepeatCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getRepeatCount()I");
    }

    public final int getScanCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getScanCode()I");
    }

    public final int getSource() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getSource()I");
    }

    public int getUnicodeChar() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getUnicodeChar()I");
    }

    public int getUnicodeChar(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->getUnicodeChar(I)I");
    }

    public final boolean hasModifiers(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->hasModifiers(I)Z");
    }

    public final boolean hasNoModifiers() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->hasNoModifiers()Z");
    }

    public final boolean isAltPressed() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isAltPressed()Z");
    }

    public final boolean isCanceled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isCanceled()Z");
    }

    public final boolean isCapsLockOn() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isCapsLockOn()Z");
    }

    public final boolean isCtrlPressed() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isCtrlPressed()Z");
    }

    public final boolean isFunctionPressed() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isFunctionPressed()Z");
    }

    public static final boolean isGamepadButton(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isGamepadButton(I)Z");
    }

    public final boolean isLongPress() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isLongPress()Z");
    }

    public static final boolean isMediaSessionKey(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isMediaSessionKey(I)Z");
    }

    public final boolean isMetaPressed() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isMetaPressed()Z");
    }

    public static boolean isModifierKey(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isModifierKey(I)Z");
    }

    public final boolean isNumLockOn() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isNumLockOn()Z");
    }

    public boolean isPrintingKey() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isPrintingKey()Z");
    }

    public final boolean isScrollLockOn() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isScrollLockOn()Z");
    }

    public final boolean isShiftPressed() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isShiftPressed()Z");
    }

    public final boolean isSymPressed() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isSymPressed()Z");
    }

    public final boolean isSystem() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isSystem()Z");
    }

    public final boolean isTracking() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->isTracking()Z");
    }

    public static int keyCodeFromString(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->keyCodeFromString(Ljava/lang/String;)I");
    }

    public static String keyCodeToString(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->keyCodeToString(I)Ljava/lang/String;");
    }

    public static boolean metaStateHasModifiers(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->metaStateHasModifiers(II)Z");
    }

    public static boolean metaStateHasNoModifiers(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->metaStateHasNoModifiers(I)Z");
    }

    public static int normalizeMetaState(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->normalizeMetaState(I)I");
    }

    public final void setSource(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->setSource(I)V");
    }

    public final void startTracking() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->startTracking()V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent;->writeToParcel(Landroid/os/Parcel;I)V");
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public KeyEvent() {
        }
        public static interface Callback {

        public abstract boolean onKeyDown(int arg0, KeyEvent arg1);

        public abstract boolean onKeyLongPress(int arg0, KeyEvent arg1);

        public abstract boolean onKeyMultiple(int arg0, int arg1, KeyEvent arg2);

        public abstract boolean onKeyUp(int arg0, KeyEvent arg1);
        }
        public static class DispatcherState {

        public DispatcherState() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent$DispatcherState;-><init>()V");
        }

        public void handleUpEvent(KeyEvent arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent$DispatcherState;->handleUpEvent(Landroid/view/KeyEvent;)V");
        }

        public boolean isTracking(KeyEvent arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent$DispatcherState;->isTracking(Landroid/view/KeyEvent;)Z");
        }

        public void performedLongPress(KeyEvent arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent$DispatcherState;->performedLongPress(Landroid/view/KeyEvent;)V");
        }

        public void reset() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent$DispatcherState;->reset()V");
        }

        public void reset(Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent$DispatcherState;->reset(Ljava/lang/Object;)V");
        }

        public void startTracking(KeyEvent arg0, Object arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/KeyEvent$DispatcherState;->startTracking(Landroid/view/KeyEvent;Ljava/lang/Object;)V");
        }
        }
    }
