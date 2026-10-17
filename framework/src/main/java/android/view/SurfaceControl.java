package android.view;

    public class SurfaceControl implements android.os.Parcelable {

    public static final int BUFFER_TRANSFORM_IDENTITY = 0;
    public static final int BUFFER_TRANSFORM_MIRROR_HORIZONTAL = 1;
    public static final int BUFFER_TRANSFORM_MIRROR_VERTICAL = 2;
    public static final int BUFFER_TRANSFORM_ROTATE_180 = 3;
    public static final int BUFFER_TRANSFORM_ROTATE_270 = 7;
    public static final int BUFFER_TRANSFORM_ROTATE_90 = 4;
    public static android.os.Parcelable.Creator CREATOR;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl;->describeContents()I");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl;->finalize()V");
    }

    public boolean isValid() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl;->isValid()Z");
    }

    public void readFromParcel(android.os.Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl;->readFromParcel(Landroid/os/Parcel;)V");
    }

    public void release() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl;->release()V");
    }

    public java.lang.String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(android.os.Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static class JankData {

        public static final int JANK_APPLICATION = 2;
        public static final int JANK_COMPOSER = 1;
        public static final int JANK_NONE = 0;
        public static final int JANK_OTHER = 4;

        public long getActualAppFrameTimeNanos() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$JankData;->getActualAppFrameTimeNanos()J");
        }

        public int getJankType() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$JankData;->getJankType()I");
        }

        public long getScheduledAppFrameTimeNanos() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$JankData;->getScheduledAppFrameTimeNanos()J");
        }

        public long getVsyncId() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$JankData;->getVsyncId()J");
        }

        public java.lang.String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$JankData;->toString()Ljava/lang/String;");
        }
        }
        public static interface OnJankDataListener {

        public abstract void onJankDataAvailable(java.util.List<android.view.SurfaceControl.JankData> arg0);
        }
        public static class OnJankDataListenerRegistration {

        public void flush() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$OnJankDataListenerRegistration;->flush()V");
        }

        public void removeAfter(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$OnJankDataListenerRegistration;->removeAfter(J)V");
        }
        }
        public static class Transaction implements java.io.Closeable, android.os.Parcelable {

        public static android.os.Parcelable.Creator CREATOR;

        public Transaction() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;-><init>()V");
        }

        public android.view.SurfaceControl.Transaction addTransactionCommittedListener(java.util.concurrent.Executor arg0, android.view.SurfaceControl.TransactionCommittedListener arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->addTransactionCommittedListener(Ljava/util/concurrent/Executor;Landroid/view/SurfaceControl$TransactionCommittedListener;)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction addTransactionCompletedListener(java.util.concurrent.Executor arg0, java.util.function.Consumer<android.view.SurfaceControl.TransactionStats> arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->addTransactionCompletedListener(Ljava/util/concurrent/Executor;Ljava/util/function/Consumer;)Landroid/view/SurfaceControl$Transaction;");
        }

        public void apply() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->apply()V");
        }

        public android.view.SurfaceControl.Transaction clearFrameRate(android.view.SurfaceControl arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->clearFrameRate(Landroid/view/SurfaceControl;)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction clearTrustedPresentationCallback(android.view.SurfaceControl arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->clearTrustedPresentationCallback(Landroid/view/SurfaceControl;)Landroid/view/SurfaceControl$Transaction;");
        }

        public void close() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->close()V");
        }

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->describeContents()I");
        }

        public android.view.SurfaceControl.Transaction merge(android.view.SurfaceControl.Transaction arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->merge(Landroid/view/SurfaceControl$Transaction;)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction reparent(android.view.SurfaceControl arg0, android.view.SurfaceControl arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->reparent(Landroid/view/SurfaceControl;Landroid/view/SurfaceControl;)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setAlpha(android.view.SurfaceControl arg0, float arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setAlpha(Landroid/view/SurfaceControl;F)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setBuffer(android.view.SurfaceControl arg0, android.hardware.HardwareBuffer arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setBuffer(Landroid/view/SurfaceControl;Landroid/hardware/HardwareBuffer;)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setBuffer(android.view.SurfaceControl arg0, android.hardware.HardwareBuffer arg1, android.hardware.SyncFence arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setBuffer(Landroid/view/SurfaceControl;Landroid/hardware/HardwareBuffer;Landroid/hardware/SyncFence;)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setBuffer(android.view.SurfaceControl arg0, android.hardware.HardwareBuffer arg1, android.hardware.SyncFence arg2, java.util.function.Consumer<android.hardware.SyncFence> arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setBuffer(Landroid/view/SurfaceControl;Landroid/hardware/HardwareBuffer;Landroid/hardware/SyncFence;Ljava/util/function/Consumer;)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setBufferSize(android.view.SurfaceControl arg0, int arg1, int arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setBufferSize(Landroid/view/SurfaceControl;II)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setBufferTransform(android.view.SurfaceControl arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setBufferTransform(Landroid/view/SurfaceControl;I)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setContentPriority(android.view.SurfaceControl arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setContentPriority(Landroid/view/SurfaceControl;I)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setCrop(android.view.SurfaceControl arg0, android.graphics.Rect arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setCrop(Landroid/view/SurfaceControl;Landroid/graphics/Rect;)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setDamageRegion(android.view.SurfaceControl arg0, android.graphics.Region arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setDamageRegion(Landroid/view/SurfaceControl;Landroid/graphics/Region;)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setDataSpace(android.view.SurfaceControl arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setDataSpace(Landroid/view/SurfaceControl;I)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setDesiredHdrHeadroom(android.view.SurfaceControl arg0, float arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setDesiredHdrHeadroom(Landroid/view/SurfaceControl;F)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setDesiredPresentTimeNanos(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setDesiredPresentTimeNanos(J)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setExtendedRangeBrightness(android.view.SurfaceControl arg0, float arg1, float arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setExtendedRangeBrightness(Landroid/view/SurfaceControl;FF)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setFrameRate(android.view.SurfaceControl arg0, float arg1, int arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setFrameRate(Landroid/view/SurfaceControl;FI)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setFrameRate(android.view.SurfaceControl arg0, float arg1, int arg2, int arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setFrameRate(Landroid/view/SurfaceControl;FII)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setFrameTimeline(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setFrameTimeline(J)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setGeometry(android.view.SurfaceControl arg0, android.graphics.Rect arg1, android.graphics.Rect arg2, int arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setGeometry(Landroid/view/SurfaceControl;Landroid/graphics/Rect;Landroid/graphics/Rect;I)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setLayer(android.view.SurfaceControl arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setLayer(Landroid/view/SurfaceControl;I)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setLuts(android.view.SurfaceControl arg0, android.hardware.DisplayLuts arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setLuts(Landroid/view/SurfaceControl;Landroid/hardware/DisplayLuts;)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setOpaque(android.view.SurfaceControl arg0, boolean arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setOpaque(Landroid/view/SurfaceControl;Z)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setPosition(android.view.SurfaceControl arg0, float arg1, float arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setPosition(Landroid/view/SurfaceControl;FF)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setScale(android.view.SurfaceControl arg0, float arg1, float arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setScale(Landroid/view/SurfaceControl;FF)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setTrustedPresentationCallback(android.view.SurfaceControl arg0, android.view.SurfaceControl.TrustedPresentationThresholds arg1, java.util.concurrent.Executor arg2, java.util.function.Consumer<java.lang.Boolean> arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setTrustedPresentationCallback(Landroid/view/SurfaceControl;Landroid/view/SurfaceControl$TrustedPresentationThresholds;Ljava/util/concurrent/Executor;Ljava/util/function/Consumer;)Landroid/view/SurfaceControl$Transaction;");
        }

        public android.view.SurfaceControl.Transaction setVisibility(android.view.SurfaceControl arg0, boolean arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setVisibility(Landroid/view/SurfaceControl;Z)Landroid/view/SurfaceControl$Transaction;");
        }

        public void writeToParcel(android.os.Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
        public static interface TransactionCommittedListener {

        public abstract void onTransactionCommitted();
        }
        public static class TransactionStats {

        public long getLatchTimeNanos() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$TransactionStats;->getLatchTimeNanos()J");
        }

        public android.hardware.SyncFence getPresentFence() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$TransactionStats;->getPresentFence()Landroid/hardware/SyncFence;");
        }
        }
        public static class TrustedPresentationThresholds {

        public TrustedPresentationThresholds(float arg0, float arg1, int arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$TrustedPresentationThresholds;-><init>(FFI)V");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public TrustedPresentationThresholds() {
            }
        }
    }
