package android.view;

import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.DisplayLuts;
import android.hardware.HardwareBuffer;
import android.hardware.SyncFence;
import android.os.Parcel;
import android.os.Parcelable;
import java.io.Closeable;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

    public class SurfaceControl implements Parcelable {

    public static final int BUFFER_TRANSFORM_IDENTITY = 0;
    public static final int BUFFER_TRANSFORM_MIRROR_HORIZONTAL = 1;
    public static final int BUFFER_TRANSFORM_MIRROR_VERTICAL = 2;
    public static final int BUFFER_TRANSFORM_ROTATE_180 = 3;
    public static final int BUFFER_TRANSFORM_ROTATE_270 = 7;
    public static final int BUFFER_TRANSFORM_ROTATE_90 = 4;
    public static Parcelable.Creator CREATOR;

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl;->describeContents()I");
    }

    protected void finalize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl;->finalize()V");
    }

    public boolean isValid() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl;->isValid()Z");
    }

    public void readFromParcel(Parcel arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl;->readFromParcel(Landroid/os/Parcel;)V");
    }

    public void release() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl;->release()V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
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

        public String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$JankData;->toString()Ljava/lang/String;");
        }
        }
        public static interface OnJankDataListener {

        public abstract void onJankDataAvailable(List<SurfaceControl.JankData> arg0);
        }
        public static class OnJankDataListenerRegistration {

        public void flush() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$OnJankDataListenerRegistration;->flush()V");
        }

        public void removeAfter(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$OnJankDataListenerRegistration;->removeAfter(J)V");
        }
        }
        public static class Transaction implements Closeable, Parcelable {

        public static Parcelable.Creator CREATOR;

        public Transaction() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;-><init>()V");
        }

        public SurfaceControl.Transaction addTransactionCommittedListener(Executor arg0, SurfaceControl.TransactionCommittedListener arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->addTransactionCommittedListener(Ljava/util/concurrent/Executor;Landroid/view/SurfaceControl$TransactionCommittedListener;)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction addTransactionCompletedListener(Executor arg0, Consumer<SurfaceControl.TransactionStats> arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->addTransactionCompletedListener(Ljava/util/concurrent/Executor;Ljava/util/function/Consumer;)Landroid/view/SurfaceControl$Transaction;");
        }

        public void apply() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->apply()V");
        }

        public SurfaceControl.Transaction clearFrameRate(SurfaceControl arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->clearFrameRate(Landroid/view/SurfaceControl;)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction clearTrustedPresentationCallback(SurfaceControl arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->clearTrustedPresentationCallback(Landroid/view/SurfaceControl;)Landroid/view/SurfaceControl$Transaction;");
        }

        public void close() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->close()V");
        }

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->describeContents()I");
        }

        public SurfaceControl.Transaction merge(SurfaceControl.Transaction arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->merge(Landroid/view/SurfaceControl$Transaction;)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction reparent(SurfaceControl arg0, SurfaceControl arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->reparent(Landroid/view/SurfaceControl;Landroid/view/SurfaceControl;)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setAlpha(SurfaceControl arg0, float arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setAlpha(Landroid/view/SurfaceControl;F)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setBuffer(SurfaceControl arg0, HardwareBuffer arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setBuffer(Landroid/view/SurfaceControl;Landroid/hardware/HardwareBuffer;)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setBuffer(SurfaceControl arg0, HardwareBuffer arg1, SyncFence arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setBuffer(Landroid/view/SurfaceControl;Landroid/hardware/HardwareBuffer;Landroid/hardware/SyncFence;)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setBuffer(SurfaceControl arg0, HardwareBuffer arg1, SyncFence arg2, Consumer<SyncFence> arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setBuffer(Landroid/view/SurfaceControl;Landroid/hardware/HardwareBuffer;Landroid/hardware/SyncFence;Ljava/util/function/Consumer;)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setBufferSize(SurfaceControl arg0, int arg1, int arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setBufferSize(Landroid/view/SurfaceControl;II)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setBufferTransform(SurfaceControl arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setBufferTransform(Landroid/view/SurfaceControl;I)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setContentPriority(SurfaceControl arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setContentPriority(Landroid/view/SurfaceControl;I)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setCrop(SurfaceControl arg0, Rect arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setCrop(Landroid/view/SurfaceControl;Landroid/graphics/Rect;)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setDamageRegion(SurfaceControl arg0, Region arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setDamageRegion(Landroid/view/SurfaceControl;Landroid/graphics/Region;)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setDataSpace(SurfaceControl arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setDataSpace(Landroid/view/SurfaceControl;I)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setDesiredHdrHeadroom(SurfaceControl arg0, float arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setDesiredHdrHeadroom(Landroid/view/SurfaceControl;F)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setDesiredPresentTimeNanos(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setDesiredPresentTimeNanos(J)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setExtendedRangeBrightness(SurfaceControl arg0, float arg1, float arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setExtendedRangeBrightness(Landroid/view/SurfaceControl;FF)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setFrameRate(SurfaceControl arg0, float arg1, int arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setFrameRate(Landroid/view/SurfaceControl;FI)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setFrameRate(SurfaceControl arg0, float arg1, int arg2, int arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setFrameRate(Landroid/view/SurfaceControl;FII)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setFrameTimeline(long arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setFrameTimeline(J)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setGeometry(SurfaceControl arg0, Rect arg1, Rect arg2, int arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setGeometry(Landroid/view/SurfaceControl;Landroid/graphics/Rect;Landroid/graphics/Rect;I)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setLayer(SurfaceControl arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setLayer(Landroid/view/SurfaceControl;I)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setLuts(SurfaceControl arg0, DisplayLuts arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setLuts(Landroid/view/SurfaceControl;Landroid/hardware/DisplayLuts;)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setOpaque(SurfaceControl arg0, boolean arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setOpaque(Landroid/view/SurfaceControl;Z)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setPosition(SurfaceControl arg0, float arg1, float arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setPosition(Landroid/view/SurfaceControl;FF)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setScale(SurfaceControl arg0, float arg1, float arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setScale(Landroid/view/SurfaceControl;FF)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setTrustedPresentationCallback(SurfaceControl arg0, SurfaceControl.TrustedPresentationThresholds arg1, Executor arg2, Consumer<Boolean> arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setTrustedPresentationCallback(Landroid/view/SurfaceControl;Landroid/view/SurfaceControl$TrustedPresentationThresholds;Ljava/util/concurrent/Executor;Ljava/util/function/Consumer;)Landroid/view/SurfaceControl$Transaction;");
        }

        public SurfaceControl.Transaction setVisibility(SurfaceControl arg0, boolean arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/SurfaceControl$Transaction;->setVisibility(Landroid/view/SurfaceControl;Z)Landroid/view/SurfaceControl$Transaction;");
        }

        public void writeToParcel(Parcel arg0, int arg1) {
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

        public SyncFence getPresentFence() {
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
