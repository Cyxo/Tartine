package android.media.session;

    public class MediaSessionManager {

    public void addOnActiveSessionsChangedListener(android.media.session.MediaSessionManager.OnActiveSessionsChangedListener arg0, android.content.ComponentName arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->addOnActiveSessionsChangedListener(Landroid/media/session/MediaSessionManager$OnActiveSessionsChangedListener;Landroid/content/ComponentName;)V");
    }

    public void addOnActiveSessionsChangedListener(android.media.session.MediaSessionManager.OnActiveSessionsChangedListener arg0, android.content.ComponentName arg1, android.os.Handler arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->addOnActiveSessionsChangedListener(Landroid/media/session/MediaSessionManager$OnActiveSessionsChangedListener;Landroid/content/ComponentName;Landroid/os/Handler;)V");
    }

    public void addOnMediaKeyEventSessionChangedListener(java.util.concurrent.Executor arg0, android.media.session.MediaSessionManager.OnMediaKeyEventSessionChangedListener arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->addOnMediaKeyEventSessionChangedListener(Ljava/util/concurrent/Executor;Landroid/media/session/MediaSessionManager$OnMediaKeyEventSessionChangedListener;)V");
    }

    public void addOnSession2TokensChangedListener(android.media.session.MediaSessionManager.OnSession2TokensChangedListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->addOnSession2TokensChangedListener(Landroid/media/session/MediaSessionManager$OnSession2TokensChangedListener;)V");
    }

    public void addOnSession2TokensChangedListener(android.media.session.MediaSessionManager.OnSession2TokensChangedListener arg0, android.os.Handler arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->addOnSession2TokensChangedListener(Landroid/media/session/MediaSessionManager$OnSession2TokensChangedListener;Landroid/os/Handler;)V");
    }

    public java.util.List<android.media.session.MediaController> getActiveSessions(android.content.ComponentName arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->getActiveSessions(Landroid/content/ComponentName;)Ljava/util/List;");
    }

    public android.media.session.MediaSession.Token getMediaKeyEventSession() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->getMediaKeyEventSession()Landroid/media/session/MediaSession$Token;");
    }

    public java.lang.String getMediaKeyEventSessionPackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->getMediaKeyEventSessionPackageName()Ljava/lang/String;");
    }

    public java.util.List<android.media.Session2Token> getSession2Tokens() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->getSession2Tokens()Ljava/util/List;");
    }

    public boolean isTrustedForMediaControl(android.media.session.MediaSessionManager.RemoteUserInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->isTrustedForMediaControl(Landroid/media/session/MediaSessionManager$RemoteUserInfo;)Z");
    }

    public void notifySession2Created(android.media.Session2Token arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->notifySession2Created(Landroid/media/Session2Token;)V");
    }

    public void removeOnActiveSessionsChangedListener(android.media.session.MediaSessionManager.OnActiveSessionsChangedListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->removeOnActiveSessionsChangedListener(Landroid/media/session/MediaSessionManager$OnActiveSessionsChangedListener;)V");
    }

    public void removeOnMediaKeyEventSessionChangedListener(android.media.session.MediaSessionManager.OnMediaKeyEventSessionChangedListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->removeOnMediaKeyEventSessionChangedListener(Landroid/media/session/MediaSessionManager$OnMediaKeyEventSessionChangedListener;)V");
    }

    public void removeOnSession2TokensChangedListener(android.media.session.MediaSessionManager.OnSession2TokensChangedListener arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager;->removeOnSession2TokensChangedListener(Landroid/media/session/MediaSessionManager$OnSession2TokensChangedListener;)V");
    }
        public static interface OnActiveSessionsChangedListener {

        public abstract void onActiveSessionsChanged(java.util.List<android.media.session.MediaController> arg0);
        }
        public static interface OnMediaKeyEventSessionChangedListener {

        public abstract void onMediaKeyEventSessionChanged(java.lang.String arg0, android.media.session.MediaSession.Token arg1);
        }
        public static interface OnSession2TokensChangedListener {

        public abstract void onSession2TokensChanged(java.util.List<android.media.Session2Token> arg0);
        }
        public static class RemoteUserInfo {

        public RemoteUserInfo(java.lang.String arg0, int arg1, int arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager$RemoteUserInfo;-><init>(Ljava/lang/String;II)V");
        }

        public boolean equals(java.lang.Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/media/session/MediaSessionManager$RemoteUserInfo;->equals(Ljava/lang/Object;)Z");
        }

        public java.lang.String getPackageName() {
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
