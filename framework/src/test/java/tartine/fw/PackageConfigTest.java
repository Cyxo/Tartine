package tartine.fw;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * The installed-app metadata seam: the host publishes manifest values through
 * {@link PackageConfig}, and {@code Context.getApplicationInfo} materialises
 * the framework's {@link ApplicationInfo} lazily (cached per context).
 */
class PackageConfigTest {

    @AfterEach
    void resetSeams() {
        PackageConfig.reset();
        PermissionManager.reset();
    }

    @Test
    void defaultsDescribeAnUnattachedContext() {
        Context context = new Context();

        ApplicationInfo info = context.getApplicationInfo();

        assertEquals("android", info.packageName);
        assertEquals("android", info.name);
        assertEquals(1, info.targetSdkVersion);
        assertEquals(0, info.flags);
        assertSame(info, context.getApplicationInfo());
    }

    @Test
    void installedManifestValuesMaterialiseAndCache() {
        PackageConfig.install("com.example.app", 33,
                ApplicationInfo.FLAG_SUPPORTS_RTL
                        | ApplicationInfo.FLAG_DEBUGGABLE);
        Context context = new Context();

        ApplicationInfo info = context.getApplicationInfo();

        assertEquals("com.example.app", info.packageName);
        assertEquals("com.example.app", info.name);
        assertEquals(33, info.targetSdkVersion);
        assertEquals(ApplicationInfo.FLAG_SUPPORTS_RTL
                | ApplicationInfo.FLAG_DEBUGGABLE, info.flags);
        // The context snapshots the manifest at first use, like AOSP's
        // ContextImpl (later installs do not mutate a live app object).
        PackageConfig.install("com.example.other", 36, 0);
        assertSame(info, context.getApplicationInfo());
        assertEquals("com.example.app",
                context.getApplicationInfo().packageName);
    }

    @Test
    void wrapperDelegatesToItsBase() {
        PackageConfig.install("com.example.app", 36, 0);
        Context base = new Context();
        ContextWrapper wrapper = new ContextWrapper(base);

        assertSame(base.getApplicationInfo(), wrapper.getApplicationInfo());
        assertEquals("com.example.app",
                wrapper.getApplicationInfo().packageName);
    }

    @Test
    void missingAndZeroValuesFallBack() {
        PackageConfig.install(null, 0, 0);

        assertEquals("", PackageConfig.packageName());
        assertEquals(1, PackageConfig.targetSdkVersion());
        // An empty package name still answers the platform default.
        assertEquals("android",
                new Context().getApplicationInfo().packageName);
    }
}
