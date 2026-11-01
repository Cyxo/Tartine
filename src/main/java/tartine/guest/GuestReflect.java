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
 * of denying them outright. The bridge allows a member only when its
 * <em>declaring class</em> was loaded by the guest {@link TranslatedLoader};
 * host-declared members are refused with a named {@link SandboxDenied}. The
 * check runs both at lookup and again at invoke/access, because a guest class
 * can inherit a host member and a {@code Method} object can outlive the
 * lookup that produced it.
 *
 * <p>Receiver classes are checked for the declared-member lookups
 * ({@code getDeclared*}, {@code newInstance}) because those enumerate the
 * receiver's own members. Public lookup helpers ({@code getMethod},
 * {@code getField}, {@code getMethods}, {@code getFields}, {@code getClasses})
 * filter or reject host-declared results.
 */
public final class GuestReflect {
    private GuestReflect() {
    }

    private static boolean isGuest(Class<?> type) {
        return type != null && type.getClassLoader() instanceof TranslatedLoader;
    }

    private static void requireGuestClass(Class<?> type, String operation) {
        if (!isGuest(type)) {
            throw new SandboxDenied(operation + " on host class "
                    + (type == null ? "null" : type.getName())
                    + " is not available");
        }
    }

    private static void requireGuestMember(Member member, String operation) {
        Class<?> declaring = member.getDeclaringClass();
        if (!isGuest(declaring)) {
            throw new SandboxDenied(operation + " on host member "
                    + declaring.getName() + "." + member.getName()
                    + " is not available");
        }
    }

    private static void requireGuestAccessible(AccessibleObject object,
            String operation) {
        if (object instanceof Member member) {
            requireGuestMember(member, operation);
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
        requireGuestClass(type, "Class.getDeclaredMethod");
        return type.getDeclaredMethod(name, parameterTypes);
    }

    public static Method[] getDeclaredMethods(Class<?> type) {
        requireGuestClass(type, "Class.getDeclaredMethods");
        return type.getDeclaredMethods();
    }

    public static Field getDeclaredField(Class<?> type, String name)
            throws NoSuchFieldException {
        requireGuestClass(type, "Class.getDeclaredField");
        return type.getDeclaredField(name);
    }

    public static Field[] getDeclaredFields(Class<?> type) {
        requireGuestClass(type, "Class.getDeclaredFields");
        return type.getDeclaredFields();
    }

    public static Constructor<?> getDeclaredConstructor(Class<?> type,
            Class<?>... parameterTypes) throws NoSuchMethodException {
        requireGuestClass(type, "Class.getDeclaredConstructor");
        return type.getDeclaredConstructor(parameterTypes);
    }

    public static Constructor<?>[] getDeclaredConstructors(Class<?> type) {
        requireGuestClass(type, "Class.getDeclaredConstructors");
        return type.getDeclaredConstructors();
    }

    public static Class<?>[] getDeclaredClasses(Class<?> type) {
        requireGuestClass(type, "Class.getDeclaredClasses");
        return type.getDeclaredClasses();
    }

    public static Method getMethod(Class<?> type, String name,
            Class<?>... parameterTypes) throws NoSuchMethodException {
        requireGuestClass(type, "Class.getMethod");
        Method method = type.getMethod(name, parameterTypes);
        requireGuestMember(method, "Class.getMethod");
        return method;
    }

    public static Method[] getMethods(Class<?> type) {
        requireGuestClass(type, "Class.getMethods");
        return Arrays.stream(type.getMethods())
                .filter(method -> isGuest(method.getDeclaringClass()))
                .toArray(Method[]::new);
    }

    public static Field getField(Class<?> type, String name)
            throws NoSuchFieldException {
        requireGuestClass(type, "Class.getField");
        Field field = type.getField(name);
        requireGuestMember(field, "Class.getField");
        return field;
    }

    public static Field[] getFields(Class<?> type) {
        requireGuestClass(type, "Class.getFields");
        return Arrays.stream(type.getFields())
                .filter(field -> isGuest(field.getDeclaringClass()))
                .toArray(Field[]::new);
    }

    public static Constructor<?> getConstructor(Class<?> type,
            Class<?>... parameterTypes) throws NoSuchMethodException {
        requireGuestClass(type, "Class.getConstructor");
        return type.getConstructor(parameterTypes);
    }

    public static Constructor<?>[] getConstructors(Class<?> type) {
        requireGuestClass(type, "Class.getConstructors");
        return type.getConstructors();
    }

    public static Class<?>[] getClasses(Class<?> type) {
        requireGuestClass(type, "Class.getClasses");
        return Arrays.stream(type.getClasses())
                .filter(GuestReflect::isGuest)
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

    // ------------------------------------------------------------------
    // Member access
    // ------------------------------------------------------------------

    public static Object invoke(Method method, Object receiver, Object... args)
            throws IllegalAccessException, InvocationTargetException {
        if (method == null) {
            throw new NullPointerException("method");
        }
        requireGuestMember(method, "Method.invoke");
        return method.invoke(receiver, args);
    }

    public static Object newInstance(Constructor<?> constructor, Object... args)
            throws InstantiationException, IllegalAccessException,
            InvocationTargetException {
        if (constructor == null) {
            throw new NullPointerException("constructor");
        }
        requireGuestMember(constructor, "Constructor.newInstance");
        return constructor.newInstance(args);
    }

    public static Object get(Field field, Object receiver)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.get");
        return field.get(receiver);
    }

    public static boolean getBoolean(Field field, Object receiver)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.getBoolean");
        return field.getBoolean(receiver);
    }

    public static byte getByte(Field field, Object receiver)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.getByte");
        return field.getByte(receiver);
    }

    public static char getChar(Field field, Object receiver)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.getChar");
        return field.getChar(receiver);
    }

    public static short getShort(Field field, Object receiver)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.getShort");
        return field.getShort(receiver);
    }

    public static int getInt(Field field, Object receiver)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.getInt");
        return field.getInt(receiver);
    }

    public static long getLong(Field field, Object receiver)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.getLong");
        return field.getLong(receiver);
    }

    public static float getFloat(Field field, Object receiver)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.getFloat");
        return field.getFloat(receiver);
    }

    public static double getDouble(Field field, Object receiver)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.getDouble");
        return field.getDouble(receiver);
    }

    public static void set(Field field, Object receiver, Object value)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.set");
        field.set(receiver, value);
    }

    public static void setBoolean(Field field, Object receiver, boolean value)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.setBoolean");
        field.setBoolean(receiver, value);
    }

    public static void setByte(Field field, Object receiver, byte value)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.setByte");
        field.setByte(receiver, value);
    }

    public static void setChar(Field field, Object receiver, char value)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.setChar");
        field.setChar(receiver, value);
    }

    public static void setShort(Field field, Object receiver, short value)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.setShort");
        field.setShort(receiver, value);
    }

    public static void setInt(Field field, Object receiver, int value)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.setInt");
        field.setInt(receiver, value);
    }

    public static void setLong(Field field, Object receiver, long value)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.setLong");
        field.setLong(receiver, value);
    }

    public static void setFloat(Field field, Object receiver, float value)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.setFloat");
        field.setFloat(receiver, value);
    }

    public static void setDouble(Field field, Object receiver, double value)
            throws IllegalAccessException {
        requireGuestMember(field, "Field.setDouble");
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
