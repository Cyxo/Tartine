package android.os;

    public class Debug {

    public static final int SHOW_CLASSLOADER = 2;
    public static final int SHOW_FULL_DETAIL = 1;
    public static final int SHOW_INITIALIZED = 4;
    public static final int TRACE_COUNT_ALLOCS = 1;

    public static void attachJvmtiAgent(java.lang.String arg0, java.lang.String arg1, java.lang.ClassLoader arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->attachJvmtiAgent(Ljava/lang/String;Ljava/lang/String;Ljava/lang/ClassLoader;)V");
    }

    public static void changeDebugPort(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->changeDebugPort(I)V");
    }

    public static void dumpHprofData(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->dumpHprofData(Ljava/lang/String;)V");
    }

    public static boolean dumpService(java.lang.String arg0, java.io.FileDescriptor arg1, java.lang.String[] arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->dumpService(Ljava/lang/String;Ljava/io/FileDescriptor;[Ljava/lang/String;)Z");
    }

    public static void enableEmulatorTraceOutput() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->enableEmulatorTraceOutput()V");
    }

    public static int getBinderDeathObjectCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getBinderDeathObjectCount()I");
    }

    public static int getBinderLocalObjectCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getBinderLocalObjectCount()I");
    }

    public static int getBinderProxyObjectCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getBinderProxyObjectCount()I");
    }

    public static int getBinderReceivedTransactions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getBinderReceivedTransactions()I");
    }

    public static int getBinderSentTransactions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getBinderSentTransactions()I");
    }

    public static int getGlobalAllocCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getGlobalAllocCount()I");
    }

    public static int getGlobalAllocSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getGlobalAllocSize()I");
    }

    public static int getGlobalClassInitCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getGlobalClassInitCount()I");
    }

    public static int getGlobalClassInitTime() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getGlobalClassInitTime()I");
    }

    public static int getGlobalExternalAllocCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getGlobalExternalAllocCount()I");
    }

    public static int getGlobalExternalAllocSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getGlobalExternalAllocSize()I");
    }

    public static int getGlobalExternalFreedCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getGlobalExternalFreedCount()I");
    }

    public static int getGlobalExternalFreedSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getGlobalExternalFreedSize()I");
    }

    public static int getGlobalFreedCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getGlobalFreedCount()I");
    }

    public static int getGlobalFreedSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getGlobalFreedSize()I");
    }

    public static int getGlobalGcInvocationCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getGlobalGcInvocationCount()I");
    }

    public static int getLoadedClassCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getLoadedClassCount()I");
    }

    public static void getMemoryInfo(android.os.Debug.MemoryInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getMemoryInfo(Landroid/os/Debug$MemoryInfo;)V");
    }

    public static long getNativeHeapAllocatedSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getNativeHeapAllocatedSize()J");
    }

    public static long getNativeHeapFreeSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getNativeHeapFreeSize()J");
    }

    public static long getNativeHeapSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getNativeHeapSize()J");
    }

    public static long getPss() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getPss()J");
    }

    public static long getRss() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getRss()J");
    }

    public static java.lang.String getRuntimeStat(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getRuntimeStat(Ljava/lang/String;)Ljava/lang/String;");
    }

    public static java.util.Map<java.lang.String, java.lang.String> getRuntimeStats() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getRuntimeStats()Ljava/util/Map;");
    }

    public static int getThreadAllocCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getThreadAllocCount()I");
    }

    public static int getThreadAllocSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getThreadAllocSize()I");
    }

    public static int getThreadExternalAllocCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getThreadExternalAllocCount()I");
    }

    public static int getThreadExternalAllocSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getThreadExternalAllocSize()I");
    }

    public static int getThreadGcInvocationCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->getThreadGcInvocationCount()I");
    }

    public static boolean isDebuggerConnected() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->isDebuggerConnected()Z");
    }

    public static void printLoadedClasses(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->printLoadedClasses(I)V");
    }

    public static void resetAllCounts() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetAllCounts()V");
    }

    public static void resetGlobalAllocCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetGlobalAllocCount()V");
    }

    public static void resetGlobalAllocSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetGlobalAllocSize()V");
    }

    public static void resetGlobalClassInitCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetGlobalClassInitCount()V");
    }

    public static void resetGlobalClassInitTime() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetGlobalClassInitTime()V");
    }

    public static void resetGlobalExternalAllocCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetGlobalExternalAllocCount()V");
    }

    public static void resetGlobalExternalAllocSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetGlobalExternalAllocSize()V");
    }

    public static void resetGlobalExternalFreedCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetGlobalExternalFreedCount()V");
    }

    public static void resetGlobalExternalFreedSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetGlobalExternalFreedSize()V");
    }

    public static void resetGlobalFreedCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetGlobalFreedCount()V");
    }

    public static void resetGlobalFreedSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetGlobalFreedSize()V");
    }

    public static void resetGlobalGcInvocationCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetGlobalGcInvocationCount()V");
    }

    public static void resetThreadAllocCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetThreadAllocCount()V");
    }

    public static void resetThreadAllocSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetThreadAllocSize()V");
    }

    public static void resetThreadExternalAllocCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetThreadExternalAllocCount()V");
    }

    public static void resetThreadExternalAllocSize() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetThreadExternalAllocSize()V");
    }

    public static void resetThreadGcInvocationCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->resetThreadGcInvocationCount()V");
    }

    public static int setAllocationLimit(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->setAllocationLimit(I)I");
    }

    public static int setGlobalAllocationLimit(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->setGlobalAllocationLimit(I)I");
    }

    public static void startAllocCounting() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->startAllocCounting()V");
    }

    public static void startMethodTracing() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->startMethodTracing()V");
    }

    public static void startMethodTracing(java.lang.String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->startMethodTracing(Ljava/lang/String;)V");
    }

    public static void startMethodTracing(java.lang.String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->startMethodTracing(Ljava/lang/String;I)V");
    }

    public static void startMethodTracing(java.lang.String arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->startMethodTracing(Ljava/lang/String;II)V");
    }

    public static void startMethodTracingSampling(java.lang.String arg0, int arg1, int arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->startMethodTracingSampling(Ljava/lang/String;II)V");
    }

    public static void startNativeTracing() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->startNativeTracing()V");
    }

    public static void stopAllocCounting() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->stopAllocCounting()V");
    }

    public static void stopMethodTracing() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->stopMethodTracing()V");
    }

    public static void stopNativeTracing() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->stopNativeTracing()V");
    }

    public static long threadCpuTimeNanos() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->threadCpuTimeNanos()J");
    }

    public static void waitForDebugger() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->waitForDebugger()V");
    }

    public static boolean waitingForDebugger() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug;->waitingForDebugger()Z");
    }
        public static class MemoryInfo implements android.os.Parcelable {

        public static android.os.Parcelable.Creator CREATOR;
        public int dalvikPrivateDirty;
        public int dalvikPss;
        public int dalvikSharedDirty;
        public int nativePrivateDirty;
        public int nativePss;
        public int nativeSharedDirty;
        public int otherPrivateDirty;
        public int otherPss;
        public int otherSharedDirty;

        public MemoryInfo() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug$MemoryInfo;-><init>()V");
        }

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug$MemoryInfo;->describeContents()I");
        }

        public java.lang.String getMemoryStat(java.lang.String arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug$MemoryInfo;->getMemoryStat(Ljava/lang/String;)Ljava/lang/String;");
        }

        public java.util.Map<java.lang.String, java.lang.String> getMemoryStats() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug$MemoryInfo;->getMemoryStats()Ljava/util/Map;");
        }

        public int getTotalPrivateClean() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug$MemoryInfo;->getTotalPrivateClean()I");
        }

        public int getTotalPrivateDirty() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug$MemoryInfo;->getTotalPrivateDirty()I");
        }

        public int getTotalPss() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug$MemoryInfo;->getTotalPss()I");
        }

        public int getTotalSharedClean() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug$MemoryInfo;->getTotalSharedClean()I");
        }

        public int getTotalSharedDirty() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug$MemoryInfo;->getTotalSharedDirty()I");
        }

        public int getTotalSwappablePss() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug$MemoryInfo;->getTotalSwappablePss()I");
        }

        public void readFromParcel(android.os.Parcel arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug$MemoryInfo;->readFromParcel(Landroid/os/Parcel;)V");
        }

        public void writeToParcel(android.os.Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/os/Debug$MemoryInfo;->writeToParcel(Landroid/os/Parcel;I)V");
        }
        }
    }
