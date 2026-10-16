package tartine.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

import tartine.dex.DexClass;
import tartine.dex.DexMethod;

/**
 * A DEX {@code Runnable}/{@code Callable} entry point for host executors.
 * Runs inline on the calling thread via the owning interpreter.
 */
public final class VmTask implements Runnable {
    private final Interpreter vm;
    private final VmObject target;
    private final DexMethod entry;

    public VmTask(Interpreter vm, VmObject target, DexMethod entry) {
        this.vm = vm;
        this.target = target;
        this.entry = entry;
    }

    /** Resolves a no-arg {@code run}/{@code call} method, walking superclasses. */
    public static VmTask runnable(Interpreter vm, Object target, String method) {
        if (!(target instanceof VmObject)) {
            throw new ClassCastException();
        }
        VmObject object = (VmObject) target;
        DexClass cls = object.type;
        java.util.Set<String> seen = new java.util.HashSet<>();
        while (cls != null && seen.add(cls.type)) {
            for (DexMethod candidate : cls.methods) {
                if (candidate.name.equals(method) && candidate.parameterTypes.isEmpty()) {
                    return new VmTask(vm, object, candidate);
                }
            }
            cls = cls.superclass == null ? null : vm.findClass(cls.superclass);
        }
        throw new VmException("no such method: " + object.type.type + "->" + method);
    }

    public Object call() {
        return vm.invoke(target.type, entry, target);
    }

    @Override
    public void run() {
        call();
    }

    /** Inline executor results, always complete. */
    public static final class DoneFuture implements java.util.concurrent.Future<Object> {
        private final Object value;
        private final Throwable failure;

        public DoneFuture(Object value, Throwable failure) {
            this.value = value;
            this.failure = failure;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public Object get() throws java.util.concurrent.ExecutionException {
            if (failure != null) {
                throw new java.util.concurrent.ExecutionException(failure);
            }
            return value;
        }

        @Override
        public Object get(long timeout, TimeUnit unit)
                throws java.util.concurrent.ExecutionException {
            return get();
        }
    }

    /**
     * Same-thread executor: tasks run inline at submission. submit/invokeAll
     * come from {@link AbstractExecutorService} on top of {@link #execute}.
     */
    public static final class DirectExecutorService extends AbstractExecutorService {
        private boolean shutdown;

        @Override
        public void execute(Runnable task) {
            if (shutdown) {
                throw new java.util.concurrent.RejectedExecutionException();
            }
            task.run();
        }

        @Override
        public void shutdown() {
            shutdown = true;
        }

        @Override
        public List<Runnable> shutdownNow() {
            shutdown = true;
            return new ArrayList<>();
        }

        @Override
        public boolean isShutdown() {
            return shutdown;
        }

        @Override
        public boolean isTerminated() {
            return shutdown;
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) {
            return true;
        }
    }
}
