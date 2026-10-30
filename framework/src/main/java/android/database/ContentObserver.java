package android.database;

import android.net.Uri;
import android.os.Handler;
import java.util.Collection;

    public class ContentObserver {

    public ContentObserver(Handler arg0) {
        // no-op
    }

    public boolean deliverSelfNotifications() {
        return false;
    }

    public final void dispatchChange(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/ContentObserver;->dispatchChange(Z)V");
    }

    public final void dispatchChange(boolean arg0, Uri arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/ContentObserver;->dispatchChange(ZLandroid/net/Uri;)V");
    }

    public final void dispatchChange(boolean arg0, Uri arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/ContentObserver;->dispatchChange(ZLandroid/net/Uri;I)V");
    }

    public final void dispatchChange(boolean arg0, Collection<Uri> arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/database/ContentObserver;->dispatchChange(ZLjava/util/Collection;I)V");
    }

    public void onChange(boolean arg0) {
    }

    public void onChange(boolean arg0, Uri arg1) {
    }

    public void onChange(boolean arg0, Uri arg1, int arg2) {
    }

    public void onChange(boolean arg0, Collection<Uri> arg1, int arg2) {
    }

        /** Scratch mirror constructor (SkeletonGen). */
        public ContentObserver() {
        }
    }
