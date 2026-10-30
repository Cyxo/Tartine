package tartine.sdk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Planner/rewriter rules for {@link Imports}. */
class ImportsTest {

    private static Imports.Universe universe(String... names) {
        Imports.Universe universe = new Imports.Universe();
        for (String name : names) {
            universe.addTopLevel(name);
        }
        return universe;
    }

    /** A compilation unit in package {@code a}, with a leading blank line. */
    private static String unit(String body) {
        return "package a;\n\n" + body;
    }

    @Test
    void samePackageAndJavaLangNeedNoImports() {
        String input = unit("class A {\n"
                + "    java.lang.String s;\n"
                + "    a.B b;\n"
                + "}\n");
        String out = Imports.shorten(input, "A",
                universe("a.A", "a.B", "java.lang.String"));
        assertEquals(unit("class A {\n"
                + "    String s;\n"
                + "    B b;\n"
                + "}\n"), out);
    }

    @Test
    void nestedChainsKeepTheirSuffix() {
        String input = unit("class A {\n"
                + "    android.graphics.Paint.Style style;\n"
                + "    java.util.Map.Entry<String, String> entry;\n"
                + "}\n");
        String out = Imports.shorten(input, "A",
                universe("a.A", "android.graphics.Paint", "java.util.Map"));
        assertEquals(unit("import android.graphics.Paint;\n"
                + "import java.util.Map;\n"
                + "\n"
                + "class A {\n"
                + "    Paint.Style style;\n"
                + "    Map.Entry<String, String> entry;\n"
                + "}\n"), out);
    }

    @Test
    void methodCallsAndArraysShortenToo() {
        String input = unit("class A {\n"
                + "    android.view.View[] views;\n"
                + "    void f() {\n"
                + "        android.os.Looper.prepareMainLooper();\n"
                + "    }\n"
                + "}\n");
        String out = Imports.shorten(input, "A",
                universe("a.A", "android.view.View", "android.os.Looper"));
        assertEquals(unit("import android.os.Looper;\n"
                + "import android.view.View;\n"
                + "\n"
                + "class A {\n"
                + "    View[] views;\n"
                + "    void f() {\n"
                + "        Looper.prepareMainLooper();\n"
                + "    }\n"
                + "}\n"), out);
    }

    @Test
    void duplicateSimpleNamesStayFullyQualified() {
        String input = unit("class A {\n"
                + "    a.b.C first;\n"
                + "    x.y.C second;\n"
                + "}\n");
        String out = Imports.shorten(input, "A", universe("a.A", "a.b.C", "x.y.C"));
        assertEquals(input, out);
    }

    @Test
    void fileTypeShadowsImports() {
        String input = "package a;\n\nclass C {\n    x.y.C other;\n}\n";
        String out = Imports.shorten(input, "C", universe("a.C", "x.y.C"));
        assertEquals(input, out);
    }

    @Test
    void nestedDeclarationShadowsImports() {
        String input = unit("class A {\n"
                + "    static class B {\n"
                + "    }\n"
                + "    x.y.B other;\n"
                + "}\n");
        String out = Imports.shorten(input, "A", universe("a.A", "x.y.B"));
        assertEquals(input, out);
    }

    @Test
    void samePackageTypeShadowsImports() {
        String input = unit("class A {\n    x.y.C other;\n}\n");
        String out = Imports.shorten(input, "A", universe("a.A", "a.C", "x.y.C"));
        assertEquals(input, out);
    }

    @Test
    void existingImportWinsOverAnotherType() {
        String input = unit("import x.y.C;\n\nclass A {\n    a.b.C other;\n}\n");
        String out = Imports.shorten(input, "A",
                universe("a.A", "a.b.C", "x.y.C"));
        assertEquals(input, out);
    }

    @Test
    void existingImportIsReused() {
        String input = unit("import a.b.C;\n\nclass A {\n    a.b.C value;\n}\n");
        String out = Imports.shorten(input, "A", universe("a.A", "a.b.C"));
        assertEquals(unit("import a.b.C;\n\nclass A {\n    C value;\n}\n"), out);
    }

    @Test
    void javaLangIsShadowedByAnImport() {
        String input = unit("import x.y.String;\n\nclass A {\n    java.lang.String s;\n}\n");
        String out = Imports.shorten(input, "A",
                universe("a.A", "java.lang.String", "x.y.String"));
        assertEquals(input, out);
    }

    @Test
    void javaLangIsShadowedByAnOnDemandImport() {
        String input = unit("import x.y.*;\n\nclass A {\n    java.lang.String s;\n}\n");
        String out = Imports.shorten(input, "A",
                universe("a.A", "java.lang.String", "x.y.String"));
        assertEquals(input, out);
    }

    @Test
    void staticImportsOccupyTheirSimpleName() {
        String input = unit("import static x.y.C.D;\n\nclass A {\n    x.y.D value;\n}\n");
        String out = Imports.shorten(input, "A",
                universe("a.A", "x.y.C", "x.y.D"));
        assertEquals(input, out);
    }

    @Test
    void commentsAndLiteralsAreNeverTouched() {
        String input = unit("class A {\n"
                + "    // android.view.View in a comment\n"
                + "    String s = \"android.view.View\";\n"
                + "    char c = 'x';\n"
                + "    android.view.View view;\n"
                + "}\n");
        String out = Imports.shorten(input, "A",
                universe("a.A", "android.view.View"));
        assertEquals(unit("import android.view.View;\n"
                + "\n"
                + "class A {\n"
                + "    // android.view.View in a comment\n"
                + "    String s = \"android.view.View\";\n"
                + "    char c = 'x';\n"
                + "    View view;\n"
                + "}\n"), out);
    }

    @Test
    void textBlocksAreNeverTouched() {
        String input = unit("class A {\n"
                + "    String s = \"\"\"\n"
                + "        android.view.View\n"
                + "        \"\"\";\n"
                + "}\n");
        String out = Imports.shorten(input, "A", universe("a.A", "android.view.View"));
        assertEquals(input, out);
    }

    @Test
    void importsAreMergedAndSorted() {
        String input = unit("import z.Z;\n"
                + "import a.b.C;\n"
                + "\n"
                + "class A {\n"
                + "    x.y.D value;\n"
                + "}\n");
        String out = Imports.shorten(input, "A",
                universe("a.A", "a.b.C", "x.y.D", "z.Z"));
        assertEquals(unit("import a.b.C;\n"
                + "import x.y.D;\n"
                + "import z.Z;\n"
                + "\n"
                + "class A {\n"
                + "    D value;\n"
                + "}\n"), out);
    }

    @Test
    void staticImportsStayInTheirOwnBlock() {
        String input = unit("import static z.Z.z;\n"
                + "import x.y.D;\n"
                + "\n"
                + "class A {\n"
                + "    x.y.C value;\n"
                + "}\n");
        String out = Imports.shorten(input, "A",
                universe("a.A", "x.y.C", "x.y.D", "z.Z"));
        assertEquals(unit("import x.y.C;\n"
                + "import x.y.D;\n"
                + "\n"
                + "import static z.Z.z;\n"
                + "\n"
                + "class A {\n"
                + "    C value;\n"
                + "}\n"), out);
    }

    @Test
    void crlfIsPreserved() {
        String input = "package a;\r\n\r\nclass A {\r\n"
                + "    android.view.View view;\r\n"
                + "}\r\n";
        String out = Imports.shorten(input, "A", universe("a.A", "android.view.View"));
        assertEquals("package a;\r\n\r\nimport android.view.View;\r\n\r\n"
                + "class A {\r\n    View view;\r\n}\r\n", out);
    }

    @Test
    void filesWithoutPackageOrImportsAreHandled() {
        String input = "class A {\n    android.view.View view;\n}\n";
        String out = Imports.shorten(input, "A", universe("android.view.View"));
        assertEquals("import android.view.View;\n\n"
                + "class A {\n    View view;\n}\n", out);
    }

    @Test
    void headerOnlyFilesKeepTheirShape() {
        String input = "/** Package docs. */\npackage a;\n";
        assertEquals(input, Imports.shorten(input, "package-info", universe()));
    }
}
