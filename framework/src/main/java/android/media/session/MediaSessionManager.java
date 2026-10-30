package android.media.session;

import android.content.ComponentName;
import android.media.Session2Token;
import android.os.Handler;
import java.util.List;
import java.util.concurrent.Executor;

    public class MediaSessionManager {

    public void addOnActiveSessionsChangedListener(MediaSessionManager.OnActiveSessionsChangedListener arg0, ComponentName arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->addOnActiveSessionsChangedListener(Landroid/media/session/MediaSessionManager$OnActiveSessionsChangedListener;Landroid/content/ComponentName;)V");
    }

    public void addOnActiveSessionsChangedListener(MediaSessionManager.OnActiveSessionsChangedListener arg0, ComponentName arg1, Handler arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->addOnActiveSessionsChangedListener(Landroid/media/session/MediaSessionManager$OnActiveSessionsChangedListener;Landroid/content/ComponentName;Landroid/os/Handler;)V");
    }

    public void addOnMediaKeyEventSessionChangedListener(Executor arg0, MediaSessionManager.OnMediaKeyEventSessionChangedListener arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->addOnMediaKeyEventSessionChangedListener(Ljava/util/concurrent/Executor;Landroid/media/session/MediaSessionManager$OnMediaKeyEventSessionChangedListener;)V");
    }

    public void addOnSession2TokensChangedListener(MediaSessionManager.OnSession2TokensChangedListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->addOnSession2TokensChangedListener(Landroid/media/session/MediaSessionManager$OnSession2TokensChangedListener;)V");
    }

    public void addOnSession2TokensChangedListener(MediaSessionManager.OnSession2TokensChangedListener arg0, Handler arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->addOnSession2TokensChangedListener(Landroid/media/session/MediaSessionManager$OnSession2TokensChangedListener;Landroid/os/Handler;)V");
    }

    public List<MediaController> getActiveSessions(ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->getActiveSessions(Landroid/content/ComponentName;)Ljava/util/List;");
    }

    public MediaSession.Token getMediaKeyEventSession() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->getMediaKeyEventSession()Landroid/media/session/MediaSession$Token;");
    }

    public String getMediaKeyEventSessionPackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->getMediaKeyEventSessionPackageName()Ljava/lang/String;");
    }

    public List<Session2Token> getSession2Tokens() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->getSession2Tokens()Ljava/util/List;");
    }

    public boolean isTrustedForMediaControl(MediaSessionManager.RemoteUserInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->isTrustedForMediaControl(Landroid/media/session/MediaSessionManager$RemoteUserInfo;)Z");
    }

    public void notifySession2Created(Session2Token arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->notifySession2Created(Landroid/media/Session2Token;)V");
    }

    public void removeOnActiveSessionsChangedListener(MediaSessionManager.OnActiveSessionsChangedListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->removeOnActiveSessionsChangedListener(Landroid/media/session/MediaSessionManager$OnActiveSessionsChangedListener;)V");
    }

    public void removeOnMediaKeyEventSessionChangedListener(MediaSessionManager.OnMediaKeyEventSessionChangedListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->removeOnMediaKeyEventSessionChangedListener(Landroid/media/session/MediaSessionManager$OnMediaKeyEventSessionChangedListener;)V");
    }

    public void removeOnSession2TokensChangedListener(MediaSessionManager.OnSession2TokensChangedListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->removeOnSession2TokensChangedListener(Landroid/media/session/MediaSessionManager$OnSession2TokensChangedListener;)V");
    }
        public static interface OnActiveSessionsChangedListener {

        public abstract void onActiveSessionsChanged(List<MediaController> arg0);
        }
        public static interface OnMediaKeyEventSessionChangedListener {

        public abstract void onMediaKeyEventSessionChanged(String arg0, MediaSession.Token arg1);
        }
        public static interface OnSession2TokensChangedListener {

        public abstract void onSession2TokensChanged(List<Session2Token> arg0);
        }
        public static class RemoteUserInfo {

        public RemoteUserInfo(String arg0, int arg1, int arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager$RemoteUserInfo;-><init>(Ljava/lang/String;II)V");
        }

        public boolean equals(Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager$RemoteUserInfo;->equals(Ljava/lang/Object;)Z");
        }

        public String getPackageName() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager$RemoteUserInfo;->getPackageName()Ljava/lang/String;");
        }

        public int getPid() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager$RemoteUserInfo;->getPid()I");
        }

        public int getUid() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager$RemoteUserInfo;->getUid()I");
        }

        public int hashCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager$RemoteUserInfo;->hashCode()I");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public RemoteUserInfo() {
            }
        }
    }
