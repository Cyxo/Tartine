package tartine.guest;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import tartine.tx.TranslatedLoader;

/**
 * Guest-side reflection bridge.
 *
 * <p>The translator routes reflection lookups and member access here instead
 * of denying them outright. A member is visible when its <em>declaring
 * class</em> is either a guest class loaded by the {@link TranslatedLoader}
 * or a framework class from the shell's {@code android.*} API. Real host
 * classes ({@code java.*}, the shell itself) stay outside the guest universe.
 * Member access ({@code invoke}, {@code newInstance}, field get/set) refuses
 * a host member with a named {@link SandboxDenied}, and the check runs again
 * there because a {@code Method} object can outlive the lookup that produced
 * it.
 *
 * <p>Receiver classes are checked for the declared-member lookups
 * ({@code getDeclared*}, {@code newInstance}) because those enumerate the
 * receiver's own members. The plural public lookups ({@code getMethods},
 * {@code getFields}, {@code getClasses}) filter host-declared results, and
 * the inheritance-aware singular lookups ({@code getMethod}, {@code getField})
 * answer Java's "not found" ({@code NoSuchMethodException}/
 * {@code NoSuchFieldException}) instead of leaking a host member.
 *
 * <p>{@code setAccessible} stays guest-only: apps may call the framework's
 * public API reflectively (kotlinx-coroutines probes
 * {@code Handler.createAsync}), but private framework state keeps its
 * AOSP access checks.
 */
public final class GuestReflect {
    private GuestReflect() {
    }

    private static boolean isGuest(Class<?> type) {
        return type != null && type.getClassLoader() instanceof TranslatedLoader;
    }

    /**
     * The framework module's {@code android.*} classes are the guest's
     * Android API, not host internals. Reflection may look them up and call
     * their public methods (kotlinx-coroutines probes the API-28
     * {@code Handler.createAsync} factory this way); private framework state
     * still needs {@code setAccessible}, which stays guest-only.
     */
    private static boolean isFramework(Class<?> type) {
        return type != null && !isGuest(type)
                && type.getName().startsWith("android.");
    }

    /** Guest-visible classes: the app's own and the framework's API. */
    private static boolean isVisible(Class<?> type) {
        return isGuest(type) || isFramework(type);
    }

    private static void requireGuestClass(Class<?> type, String operation) {
        if (!isGuest(type)) {
            throw new SandboxDenied(operation + " on host class "
                    + (type == null ? "null" : type.getName())
                    + " is not available");
        }
    }

    /** Lookup receiver for the reflective class queries. */
    private static void requireLookupClass(Class<?> type, String operation) {
        if (!isVisible(type)) {
            throw new SandboxDenied(operation + " on host class "
                    + (type == null ? "null" : type.getName())
                    + " is not available");
        }
    }

    /** Member check for invocation: guest members plus the framework API. */
    private static void requireInvocableMember(Member member, String operation) {
        Class<?> declaring = member.getDeclaringClass();
        if (!isVisible(declaring)) {
            throw new SandboxDenied(operation + " on host member "
                    + declaring.getName() + "." + member.getName()
                    + " is not available");
        }
    }

    private static void requireGuestAccessible(AccessibleObject object,
            String operation) {
        if (object instanceof Member member) {
            requireInvocableMember(member, operation);
            return;
        }
        throw new SandboxDenied(operation
                + " on host reflection object is not available");
    }

    // ------------------------------------------------------------------
    // Class lookups
    // ------------------------------------------------------------------

    public static Method getDeclaredMethod(Class<?> type, String name,
            Class<?>... parameterTypes) throws NoSuchMethodException {
        requireLookupClass(type, "Class.getDeclaredMethod");
        return type.getDeclaredMethod(name, parameterTypes);
    }

    public static Method[] getDeclaredMethods(Class<?> type) {
        requireLookupClass(type, "Class.getDeclaredMethods");
        return type.getDeclaredMethods();
    }

    public static Field getDeclaredField(Class<?> type, String name)
            throws NoSuchFieldException {
        requireLookupClass(type, "Class.getDeclaredField");
        return type.getDeclaredField(name);
    }

    public static Field[] getDeclaredFields(Class<?> type) {
        requireLookupClass(type, "Class.getDeclaredFields");
        return type.getDeclaredFields();
    }

    public static Constructor<?> getDeclaredConstructor(Class<?> type,
            Class<?>... parameterTypes) throws NoSuchMethodException {
        requireLookupClass(type, "Class.getDeclaredConstructor");
        return type.getDeclaredConstructor(parameterTypes);
    }

    public static Constructor<?>[] getDeclaredConstructors(Class<?> type) {
        requireLookupClass(type, "Class.getDeclaredConstructors");
        return type.getDeclaredConstructors();
    }

    public static Class<?>[] getDeclaredClasses(Class<?> type) {
        requireLookupClass(type, "Class.getDeclaredClasses");
        return type.getDeclaredClasses();
    }

    /** Reflective lookup restricted to guest-visible methods. */
    public static Method getMethod(Class<?> type, String name,
            Class<?>... parameterTypes) throws NoSuchMethodException {
        requireLookupClass(type, "Class.getMethod");
        Method method = type.getMethod(name, parameterTypes);
        // Host members are outside the guest universe, so the
        // inheritance-aware lookup behaves like Java's "not found" rather
        // than leaking a host Method that invoke would refuse anyway.
        if (!isVisible(method.getDeclaringClass())) {
            throw new NoSuchMethodException("host method "
                    + method.getDeclaringClass().getName() + "." + name
                    + " is not visible to guest reflection");
        }
        return method;
    }

    /** Guest-visible public methods; host members are filtered out. */
    public static Method[] getMethods(Class<?> type) {
        requireLookupClass(type, "Class.getMethods");
        return Arrays.stream(type.getMethods())
                .filter(method -> isVisible(method.getDeclaringClass()))
                .toArray(Method[]::new);
    }

    /** Reflective field lookup restricted to guest-visible fields. */
    public static Field getField(Class<?> type, String name)
            throws NoSuchFieldException {
        requireLookupClass(type, "Class.getField");
        Field field = type.getField(name);
        if (!isVisible(field.getDeclaringClass())) {
            throw new NoSuchFieldException("host field "
                    + field.getDeclaringClass().getName() + "." + name
                    + " is not visible to guest reflection");
        }
        return field;
    }

    /** Guest-visible public fields; host members are filtered out. */
    public static Field[] getFields(Class<?> type) {
        requireLookupClass(type, "Class.getFields");
        return Arrays.stream(type.getFields())
                .filter(field -> isVisible(field.getDeclaringClass()))
                .toArray(Field[]::new);
    }

    public static Constructor<?> getConstructor(Class<?> type,
            Class<?>... parameterTypes) throws NoSuchMethodException {
        requireLookupClass(type, "Class.getConstructor");
        return type.getConstructor(parameterTypes);
    }

    public static Constructor<?>[] getConstructors(Class<?> type) {
        requireLookupClass(type, "Class.getConstructors");
        return type.getConstructors();
    }

    /** Guest-visible member classes. */
    public static Class<?>[] getClasses(Class<?> type) {
        requireLookupClass(type, "Class.getClasses");
        return Arrays.stream(type.getClasses())
                .filter(GuestReflect::isVisible)
                .toArray(Class<?>[]::new);
    }

    /**
     * Guest classloader query. The host loader is wrapped in
     * {@link GuestClassLoader} so the value is assignable where translated
     * signatures expect a {@code ClassLoader}, but resource and parent
     * lookups stay guest-safe.
     */
    public static GuestClassLoader getClassLoader(Class<?> type) {
        requireGuestClass(type, "Class.getClassLoader");
        if (!(type.getClassLoader() instanceof TranslatedLoader loader)) {
            throw new SandboxDenied("Class.getClassLoader on host class "
                    + type.getName() + " is not available");
        }
        return new GuestClassLoader(loader);
    }

    @SuppressWarnings("deprecation") // Class.newInstance is the DEX surface
    public static Object newInstance(Class<?> type)
            throws InstantiationException, IllegalAccessException {
        requireGuestClass(type, "Class.newInstance");
        return type.newInstance();
    }

    /** Reflection entry for {@code Constructor.newInstance}. */
    public static Object newInstance(Constructor<?> constructor, Object... args)
            throws InstantiationException, IllegalAccessException,
            InvocationTargetException {
        if (constructor == null) {
            throw new NullPointerException("constructor");
        }
        requireInvocableMember(constructor, "Constructor.newInstance");
        return constructor.newInstance(args);
    }

    // ------------------------------------------------------------------
    // Member access
    // ------------------------------------------------------------------

    /** Reflection entry for {@code Method.invoke}; host members are denied. */
    public static Object invoke(Method method, Object receiver, Object... args)
            throws IllegalAccessException, InvocationTargetException {
        if (method == null) {
            throw new NullPointerException("method");
        }
        requireInvocableMember(method, "Method.invoke");
        return method.invoke(receiver, args);
    }

    public static Object get(Field field, Object receiver)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.get");
        return field.get(receiver);
    }

    public static boolean getBoolean(Field field, Object receiver)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.getBoolean");
        return field.getBoolean(receiver);
    }

    public static byte getByte(Field field, Object receiver)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.getByte");
        return field.getByte(receiver);
    }

    public static char getChar(Field field, Object receiver)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.getChar");
        return field.getChar(receiver);
    }

    public static short getShort(Field field, Object receiver)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.getShort");
        return field.getShort(receiver);
    }

    public static int getInt(Field field, Object receiver)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.getInt");
        return field.getInt(receiver);
    }

    public static long getLong(Field field, Object receiver)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.getLong");
        return field.getLong(receiver);
    }

    public static float getFloat(Field field, Object receiver)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.getFloat");
        return field.getFloat(receiver);
    }

    public static double getDouble(Field field, Object receiver)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.getDouble");
        return field.getDouble(receiver);
    }

    public static void set(Field field, Object receiver, Object value)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.set");
        field.set(receiver, value);
    }

    public static void setBoolean(Field field, Object receiver, boolean value)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.setBoolean");
        field.setBoolean(receiver, value);
    }

    public static void setByte(Field field, Object receiver, byte value)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.setByte");
        field.setByte(receiver, value);
    }

    public static void setChar(Field field, Object receiver, char value)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.setChar");
        field.setChar(receiver, value);
    }

    public static void setShort(Field field, Object receiver, short value)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.setShort");
        field.setShort(receiver, value);
    }

    public static void setInt(Field field, Object receiver, int value)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.setInt");
        field.setInt(receiver, value);
    }

    public static void setLong(Field field, Object receiver, long value)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.setLong");
        field.setLong(receiver, value);
    }

    public static void setFloat(Field field, Object receiver, float value)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.setFloat");
        field.setFloat(receiver, value);
    }

    public static void setDouble(Field field, Object receiver, double value)
            throws IllegalAccessException {
        requireInvocableMember(field, "Field.setDouble");
        field.setDouble(receiver, value);
    }

    public static void setAccessible(AccessibleObject object, boolean flag) {
        requireGuestAccessible(object, "setAccessible");
        object.setAccessible(flag);
    }

    public static boolean trySetAccessible(AccessibleObject object) {
        requireGuestAccessible(object, "trySetAccessible");
        return object.trySetAccessible();
    }
}
