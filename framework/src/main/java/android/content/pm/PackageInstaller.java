package android.content.pm;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.icu.util.ULocale;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.UserHandle;
import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

    public class PackageInstaller {

    public static final String ACTION_SESSION_COMMITTED = "android.content.pm.action.SESSION_COMMITTED";
    public static final String ACTION_SESSION_DETAILS = "android.content.pm.action.SESSION_DETAILS";
    public static final String ACTION_SESSION_UPDATED = "android.content.pm.action.SESSION_UPDATED";
    public static final String EXTRA_INSTALL_CONSTRAINTS = "android.content.pm.extra.INSTALL_CONSTRAINTS";
    public static final String EXTRA_INSTALL_CONSTRAINTS_RESULT = "android.content.pm.extra.INSTALL_CONSTRAINTS_RESULT";
    public static final String EXTRA_OTHER_PACKAGE_NAME = "android.content.pm.extra.OTHER_PACKAGE_NAME";
    public static final String EXTRA_PACKAGE_NAME = "android.content.pm.extra.PACKAGE_NAME";
    public static final String EXTRA_PRE_APPROVAL = "android.content.pm.extra.PRE_APPROVAL";
    public static final String EXTRA_SESSION = "android.content.pm.extra.SESSION";
    public static final String EXTRA_SESSION_ID = "android.content.pm.extra.SESSION_ID";
    public static final String EXTRA_STATUS = "android.content.pm.extra.STATUS";
    public static final String EXTRA_STATUS_MESSAGE = "android.content.pm.extra.STATUS_MESSAGE";
    public static final String EXTRA_STORAGE_PATH = "android.content.pm.extra.STORAGE_PATH";
    public static final String EXTRA_UNARCHIVE_ALL_USERS = "android.content.pm.extra.UNARCHIVE_ALL_USERS";
    public static final String EXTRA_UNARCHIVE_ID = "android.content.pm.extra.UNARCHIVE_ID";
    public static final String EXTRA_UNARCHIVE_PACKAGE_NAME = "android.content.pm.extra.UNARCHIVE_PACKAGE_NAME";
    public static final String EXTRA_UNARCHIVE_STATUS = "android.content.pm.extra.UNARCHIVE_STATUS";
    public static final int PACKAGE_SOURCE_DOWNLOADED_FILE = 4;
    public static final int PACKAGE_SOURCE_LOCAL_FILE = 3;
    public static final int PACKAGE_SOURCE_OTHER = 1;
    public static final int PACKAGE_SOURCE_STORE = 2;
    public static final int PACKAGE_SOURCE_UNSPECIFIED = 0;
    public static final int STATUS_FAILURE = 1;
    public static final int STATUS_FAILURE_ABORTED = 3;
    public static final int STATUS_FAILURE_BLOCKED = 2;
    public static final int STATUS_FAILURE_CONFLICT = 5;
    public static final int STATUS_FAILURE_INCOMPATIBLE = 7;
    public static final int STATUS_FAILURE_INVALID = 4;
    public static final int STATUS_FAILURE_STORAGE = 6;
    public static final int STATUS_FAILURE_TIMEOUT = 8;
    public static final int STATUS_PENDING_USER_ACTION = -1;
    public static final int STATUS_SUCCESS = 0;
    public static final int UNARCHIVAL_ERROR_INSTALLER_DISABLED = 4;
    public static final int UNARCHIVAL_ERROR_INSTALLER_UNINSTALLED = 5;
    public static final int UNARCHIVAL_ERROR_INSUFFICIENT_STORAGE = 2;
    public static final int UNARCHIVAL_ERROR_NO_CONNECTIVITY = 3;
    public static final int UNARCHIVAL_ERROR_USER_ACTION_NEEDED = 1;
    public static final int UNARCHIVAL_GENERIC_ERROR = 100;
    public static final int UNARCHIVAL_OK = 0;

    public void abandonSession(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->abandonSession(I)V");
    }

    public void checkInstallConstraints(List<String> arg0, PackageInstaller.InstallConstraints arg1, Executor arg2, Consumer<PackageInstaller.InstallConstraintsResult> arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->checkInstallConstraints(Ljava/util/List;Landroid/content/pm/PackageInstaller$InstallConstraints;Ljava/util/concurrent/Executor;Ljava/util/function/Consumer;)V");
    }

    public void commitSessionAfterInstallConstraintsAreMet(int arg0, IntentSender arg1, PackageInstaller.InstallConstraints arg2, long arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->commitSessionAfterInstallConstraintsAreMet(ILandroid/content/IntentSender;Landroid/content/pm/PackageInstaller$InstallConstraints;J)V");
    }

    public int createSession(PackageInstaller.SessionParams arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->createSession(Landroid/content/pm/PackageInstaller$SessionParams;)I");
    }

    public PackageInstaller.SessionInfo getActiveStagedSession() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->getActiveStagedSession()Landroid/content/pm/PackageInstaller$SessionInfo;");
    }

    public List<PackageInstaller.SessionInfo> getActiveStagedSessions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->getActiveStagedSessions()Ljava/util/List;");
    }

    public List<PackageInstaller.SessionInfo> getAllSessions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->getAllSessions()Ljava/util/List;");
    }

    public List<PackageInstaller.SessionInfo> getMySessions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->getMySessions()Ljava/util/List;");
    }

    public PackageInstaller.SessionInfo getSessionInfo(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->getSessionInfo(I)Landroid/content/pm/PackageInstaller$SessionInfo;");
    }

    public List<PackageInstaller.SessionInfo> getStagedSessions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->getStagedSessions()Ljava/util/List;");
    }

    public void installExistingPackage(String arg0, int arg1, IntentSender arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->installExistingPackage(Ljava/lang/String;ILandroid/content/IntentSender;)V");
    }

    public void installPackageArchived(ArchivedPackageInfo arg0, PackageInstaller.SessionParams arg1, IntentSender arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->installPackageArchived(Landroid/content/pm/ArchivedPackageInfo;Landroid/content/pm/PackageInstaller$SessionParams;Landroid/content/IntentSender;)V");
    }

    public PackageInstaller.Session openSession(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->openSession(I)Landroid/content/pm/PackageInstaller$Session;");
    }

    public void registerSessionCallback(PackageInstaller.SessionCallback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->registerSessionCallback(Landroid/content/pm/PackageInstaller$SessionCallback;)V");
    }

    public void registerSessionCallback(PackageInstaller.SessionCallback arg0, Handler arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->registerSessionCallback(Landroid/content/pm/PackageInstaller$SessionCallback;Landroid/os/Handler;)V");
    }

    public void reportUnarchivalState(PackageInstaller.UnarchivalState arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->reportUnarchivalState(Landroid/content/pm/PackageInstaller$UnarchivalState;)V");
    }

    public void reportUnarchivalStatus(int arg0, int arg1, long arg2, PendingIntent arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->reportUnarchivalStatus(IIJLandroid/app/PendingIntent;)V");
    }

    public void requestArchive(String arg0, IntentSender arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->requestArchive(Ljava/lang/String;Landroid/content/IntentSender;)V");
    }

    public void requestUnarchive(String arg0, IntentSender arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->requestUnarchive(Ljava/lang/String;Landroid/content/IntentSender;)V");
    }

    public void uninstall(VersionedPackage arg0, IntentSender arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->uninstall(Landroid/content/pm/VersionedPackage;Landroid/content/IntentSender;)V");
    }

    public void uninstall(VersionedPackage arg0, int arg1, IntentSender arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->uninstall(Landroid/content/pm/VersionedPackage;ILandroid/content/IntentSender;)V");
    }

    public void uninstall(String arg0, IntentSender arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->uninstall(Ljava/lang/String;Landroid/content/IntentSender;)V");
    }

    public void uninstallExistingPackage(String arg0, IntentSender arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->uninstallExistingPackage(Ljava/lang/String;Landroid/content/IntentSender;)V");
    }

    public void unregisterSessionCallback(PackageInstaller.SessionCallback arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->unregisterSessionCallback(Landroid/content/pm/PackageInstaller$SessionCallback;)V");
    }

    public void updateSessionAppIcon(int arg0, Bitmap arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->updateSessionAppIcon(ILandroid/graphics/Bitmap;)V");
    }

    public void updateSessionAppLabel(int arg0, CharSequence arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->updateSessionAppLabel(ILjava/lang/CharSequence;)V");
    }

    public void waitForInstallConstraints(List<String> arg0, PackageInstaller.InstallConstraints arg1, IntentSender arg2, long arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller;->waitForInstallConstraints(Ljava/util/List;Landroid/content/pm/PackageInstaller$InstallConstraints;Landroid/content/IntentSender;J)V");
    }
        public static class InstallConstraints implements Parcelable {

        public static Parcelable.Creator CREATOR;
        public static PackageInstaller.InstallConstraints GENTLE_UPDATE;

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$InstallConstraints;->describeContents()I");
        }

        public boolean equals(Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$InstallConstraints;->equals(Ljava/lang/Object;)Z");
        }

        public int hashCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$InstallConstraints;->hashCode()I");
        }

        public boolean isAppNotForegroundRequired() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$InstallConstraints;->isAppNotForegroundRequired()Z");
        }

        public boolean isAppNotInteractingRequired() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$InstallConstraints;->isAppNotInteractingRequired()Z");
        }

        public boolean isAppNotTopVisibleRequired() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$InstallConstraints;->isAppNotTopVisibleRequired()Z");
        }

        public boolean isDeviceIdleRequired() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$InstallConstraints;->isDeviceIdleRequired()Z");
        }

        public boolean isNotInCallRequired() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$InstallConstraints;->isNotInCallRequired()Z");
        }

        public void writeToParcel(Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$InstallConstraints;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
        public static class InstallConstraintsResult implements Parcelable {

        public static Parcelable.Creator CREATOR;

        public boolean areAllConstraintsSatisfied() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$InstallConstraintsResult;->areAllConstraintsSatisfied()Z");
        }

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$InstallConstraintsResult;->describeContents()I");
        }

        public void writeToParcel(Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$InstallConstraintsResult;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
        public static class PreapprovalDetails implements Parcelable {

        public static Parcelable.Creator CREATOR;

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$PreapprovalDetails;->describeContents()I");
        }

        public Bitmap getIcon() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$PreapprovalDetails;->getIcon()Landroid/graphics/Bitmap;");
        }

        public CharSequence getLabel() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$PreapprovalDetails;->getLabel()Ljava/lang/CharSequence;");
        }

        public ULocale getLocale() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$PreapprovalDetails;->getLocale()Landroid/icu/util/ULocale;");
        }

        public String getPackageName() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$PreapprovalDetails;->getPackageName()Ljava/lang/String;");
        }

        public String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$PreapprovalDetails;->toString()Ljava/lang/String;");
        }

        public void writeToParcel(Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$PreapprovalDetails;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
        public static class Session implements Closeable {

        public void abandon() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->abandon()V");
        }

        public void addChildSessionId(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->addChildSessionId(I)V");
        }

        public void close() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->close()V");
        }

        public void commit(IntentSender arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->commit(Landroid/content/IntentSender;)V");
        }

        public void fsync(OutputStream arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->fsync(Ljava/io/OutputStream;)V");
        }

        public PersistableBundle getAppMetadata() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->getAppMetadata()Landroid/os/PersistableBundle;");
        }

        public int[] getChildSessionIds() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->getChildSessionIds()[I");
        }

        public String[] getNames() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->getNames()[Ljava/lang/String;");
        }

        public int getParentSessionId() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->getParentSessionId()I");
        }

        public boolean isApplicationEnabledSettingPersistent() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->isApplicationEnabledSettingPersistent()Z");
        }

        public boolean isMultiPackage() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->isMultiPackage()Z");
        }

        public boolean isRequestUpdateOwnership() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->isRequestUpdateOwnership()Z");
        }

        public boolean isStaged() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->isStaged()Z");
        }

        public InputStream openRead(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->openRead(Ljava/lang/String;)Ljava/io/InputStream;");
        }

        public OutputStream openWrite(String arg0, long arg1, long arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->openWrite(Ljava/lang/String;JJ)Ljava/io/OutputStream;");
        }

        public void removeChildSessionId(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->removeChildSessionId(I)V");
        }

        public void removeSplit(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->removeSplit(Ljava/lang/String;)V");
        }

        public void requestChecksums(String arg0, int arg1, List<Certificate> arg2, Executor arg3, PackageManager.OnChecksumsReadyListener arg4) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->requestChecksums(Ljava/lang/String;ILjava/util/List;Ljava/util/concurrent/Executor;Landroid/content/pm/PackageManager$OnChecksumsReadyListener;)V");
        }

        public void requestUserPreapproval(PackageInstaller.PreapprovalDetails arg0, IntentSender arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->requestUserPreapproval(Landroid/content/pm/PackageInstaller$PreapprovalDetails;Landroid/content/IntentSender;)V");
        }

        public void setAppMetadata(PersistableBundle arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->setAppMetadata(Landroid/os/PersistableBundle;)V");
        }

        public void setChecksums(String arg0, List<Checksum> arg1, byte[] arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->setChecksums(Ljava/lang/String;Ljava/util/List;[B)V");
        }

        public void setStagingProgress(float arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->setStagingProgress(F)V");
        }

        public void transfer(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$Session;->transfer(Ljava/lang/String;)V");
        }
        }
        public static class SessionCallback {

        public SessionCallback() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionCallback;-><init>()V");
        }

        public void onActiveChanged(int arg0, boolean arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionCallback;->onActiveChanged(IZ)V");
        }

        public void onBadgingChanged(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionCallback;->onBadgingChanged(I)V");
        }

        public void onCreated(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionCallback;->onCreated(I)V");
        }

        public void onFinished(int arg0, boolean arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionCallback;->onFinished(IZ)V");
        }

        public void onProgressChanged(int arg0, float arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionCallback;->onProgressChanged(IF)V");
        }
        }
        public static class SessionInfo implements Parcelable {

        public static Parcelable.Creator CREATOR;
        public static final int INVALID_ID = -1;
        public static final int SESSION_ACTIVATION_FAILED = 2;
        public static final int SESSION_CONFLICT = 4;
        public static final int SESSION_NO_ERROR = 0;
        public static final int SESSION_UNKNOWN_ERROR = 3;
        public static final int SESSION_VERIFICATION_FAILED = 1;
        public static final int STAGED_SESSION_ACTIVATION_FAILED = 2;
        public static final int STAGED_SESSION_CONFLICT = 4;
        public static final int STAGED_SESSION_NO_ERROR = 0;
        public static final int STAGED_SESSION_UNKNOWN = 3;
        public static final int STAGED_SESSION_VERIFICATION_FAILED = 1;

        public Intent createDetailsIntent() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->createDetailsIntent()Landroid/content/Intent;");
        }

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->describeContents()I");
        }

        public Bitmap getAppIcon() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getAppIcon()Landroid/graphics/Bitmap;");
        }

        public CharSequence getAppLabel() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getAppLabel()Ljava/lang/CharSequence;");
        }

        public String getAppPackageName() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getAppPackageName()Ljava/lang/String;");
        }

        public int[] getChildSessionIds() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getChildSessionIds()[I");
        }

        public long getCreatedMillis() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getCreatedMillis()J");
        }

        public boolean getDontKillApp() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getDontKillApp()Z");
        }

        public int getInstallLocation() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getInstallLocation()I");
        }

        public int getInstallReason() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getInstallReason()I");
        }

        public String getInstallerAttributionTag() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getInstallerAttributionTag()Ljava/lang/String;");
        }

        public String getInstallerPackageName() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getInstallerPackageName()Ljava/lang/String;");
        }

        public int getInstallerUid() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getInstallerUid()I");
        }

        public int getMode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getMode()I");
        }

        public int getOriginatingUid() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getOriginatingUid()I");
        }

        public Uri getOriginatingUri() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getOriginatingUri()Landroid/net/Uri;");
        }

        public int getPackageSource() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getPackageSource()I");
        }

        public int getParentSessionId() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getParentSessionId()I");
        }

        public float getProgress() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getProgress()F");
        }

        public Uri getReferrerUri() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getReferrerUri()Landroid/net/Uri;");
        }

        public int getRequireUserAction() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getRequireUserAction()I");
        }

        public int getSessionId() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getSessionId()I");
        }

        public long getSize() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getSize()J");
        }

        public int getStagedSessionErrorCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getStagedSessionErrorCode()I");
        }

        public String getStagedSessionErrorMessage() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getStagedSessionErrorMessage()Ljava/lang/String;");
        }

        public long getUpdatedMillis() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getUpdatedMillis()J");
        }

        public UserHandle getUser() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->getUser()Landroid/os/UserHandle;");
        }

        public boolean hasParentSessionId() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->hasParentSessionId()Z");
        }

        public boolean isActive() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->isActive()Z");
        }

        public boolean isApplicationEnabledSettingPersistent() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->isApplicationEnabledSettingPersistent()Z");
        }

        public boolean isAutoInstallDependenciesEnabled() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->isAutoInstallDependenciesEnabled()Z");
        }

        public boolean isCommitted() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->isCommitted()Z");
        }

        public boolean isMultiPackage() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->isMultiPackage()Z");
        }

        public boolean isPreApprovalRequested() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->isPreApprovalRequested()Z");
        }

        public boolean isRequestUpdateOwnership() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->isRequestUpdateOwnership()Z");
        }

        public boolean isSealed() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->isSealed()Z");
        }

        public boolean isStaged() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->isStaged()Z");
        }

        public boolean isStagedSessionActive() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->isStagedSessionActive()Z");
        }

        public boolean isStagedSessionApplied() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->isStagedSessionApplied()Z");
        }

        public boolean isStagedSessionFailed() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->isStagedSessionFailed()Z");
        }

        public boolean isStagedSessionReady() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->isStagedSessionReady()Z");
        }

        public boolean isUnarchival() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->isUnarchival()Z");
        }

        public void writeToParcel(Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionInfo;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
        public static class SessionParams implements Parcelable {

        public static Parcelable.Creator CREATOR;
        public static final int MODE_FULL_INSTALL = 1;
        public static final int MODE_INHERIT_EXISTING = 2;
        public static final int PERMISSION_STATE_DEFAULT = 0;
        public static final int PERMISSION_STATE_DENIED = 2;
        public static final int PERMISSION_STATE_GRANTED = 1;
        public static Set RESTRICTED_PERMISSIONS_ALL;
        public static final int USER_ACTION_NOT_REQUIRED = 2;
        public static final int USER_ACTION_REQUIRED = 1;
        public static final int USER_ACTION_UNSPECIFIED = 0;

        public SessionParams(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;-><init>(I)V");
        }

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->describeContents()I");
        }

        public void setAppIcon(Bitmap arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setAppIcon(Landroid/graphics/Bitmap;)V");
        }

        public void setAppLabel(CharSequence arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setAppLabel(Ljava/lang/CharSequence;)V");
        }

        public void setAppPackageName(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setAppPackageName(Ljava/lang/String;)V");
        }

        public void setApplicationEnabledSettingPersistent() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setApplicationEnabledSettingPersistent()V");
        }

        public void setAutoInstallDependenciesEnabled(boolean arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setAutoInstallDependenciesEnabled(Z)V");
        }

        public void setAutoRevokePermissionsMode(boolean arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setAutoRevokePermissionsMode(Z)V");
        }

        public void setDontKillApp(boolean arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setDontKillApp(Z)V");
        }

        public void setInstallLocation(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setInstallLocation(I)V");
        }

        public void setInstallReason(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setInstallReason(I)V");
        }

        public void setInstallScenario(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setInstallScenario(I)V");
        }

        public void setInstallerPackageName(String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setInstallerPackageName(Ljava/lang/String;)V");
        }

        public void setMultiPackage() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setMultiPackage()V");
        }

        public void setOriginatingUid(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setOriginatingUid(I)V");
        }

        public void setOriginatingUri(Uri arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setOriginatingUri(Landroid/net/Uri;)V");
        }

        public void setPackageSource(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setPackageSource(I)V");
        }

        public PackageInstaller.SessionParams setPermissionState(String arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setPermissionState(Ljava/lang/String;I)Landroid/content/pm/PackageInstaller$SessionParams;");
        }

        public void setReferrerUri(Uri arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setReferrerUri(Landroid/net/Uri;)V");
        }

        public void setRequestUpdateOwnership(boolean arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setRequestUpdateOwnership(Z)V");
        }

        public void setRequireUserAction(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setRequireUserAction(I)V");
        }

        public void setSize(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setSize(J)V");
        }

        public void setUnarchiveId(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setUnarchiveId(I)V");
        }

        public void setWhitelistedRestrictedPermissions(Set<String> arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->setWhitelistedRestrictedPermissions(Ljava/util/Set;)V");
        }

        public void writeToParcel(Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$SessionParams;->writeToParcel(Landroid/os/Parcel;I)V");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public SessionParams() {
            }
        }
        public static class UnarchivalState {

        public static PackageInstaller.UnarchivalState createGenericErrorState(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$UnarchivalState;->createGenericErrorState(I)Landroid/content/pm/PackageInstaller$UnarchivalState;");
        }

        public static PackageInstaller.UnarchivalState createInsufficientStorageState(int arg0, long arg1, PendingIntent arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$UnarchivalState;->createInsufficientStorageState(IJLandroid/app/PendingIntent;)Landroid/content/pm/PackageInstaller$UnarchivalState;");
        }

        public static PackageInstaller.UnarchivalState createNoConnectivityState(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$UnarchivalState;->createNoConnectivityState(I)Landroid/content/pm/PackageInstaller$UnarchivalState;");
        }

        public static PackageInstaller.UnarchivalState createOkState(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$UnarchivalState;->createOkState(I)Landroid/content/pm/PackageInstaller$UnarchivalState;");
        }

        public static PackageInstaller.UnarchivalState createUserActionRequiredState(int arg0, PendingIntent arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/content/pm/PackageInstaller$UnarchivalState;->createUserActionRequiredState(ILandroid/app/PendingIntent;)Landroid/content/pm/PackageInstaller$UnarchivalState;");
        }
        }
    }
