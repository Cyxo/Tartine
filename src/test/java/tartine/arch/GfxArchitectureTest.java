package tartine.arch;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

/**
 * Keeps the renderer seam honest: the {@code android.*} framework draws
 * through {@code gfx-api} only and must never reach around it into a backend
 * implementation. The backend module ({@code gfx-skija}) is the sole place
 * allowed to import {@code io.github.humbleui}.
 */
class GfxArchitectureTest {

    private static final JavaClasses FRAMEWORK_CLASSES = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("android");

    @Test
    void importsTheFrameworkClasses() {
        assertTrue(FRAMEWORK_CLASSES.stream().anyMatch(javaClass ->
                        javaClass.getName().equals("android.graphics.Canvas")),
                "expected the framework module on the test classpath");
    }

    @Test
    void frameworkDoesNotDependOnARendererImplementation() {
        noClasses()
                .that().resideInAPackage("android..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("io.github.humbleui..",
                        "tartine.gfx.skija..")
                .because("the framework draws through gfx-api only; the"
                        + " installed backend owns the renderer")
                .check(FRAMEWORK_CLASSES);
    }
}
