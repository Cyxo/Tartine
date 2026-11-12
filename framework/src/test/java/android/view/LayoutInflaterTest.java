package android.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import android.content.Context;
import android.util.AttributeSet;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tartine.fw.AppResources;
import tartine.fw.FakeAppResourceHost;
import tartine.fw.XmlNode;

/**
 * Inflation over a hand-built document: class-named elements build the tree,
 * the parent container generates each child's layout params, {@code
 * onFinishInflate} runs once, and a {@code Factory2} gets first refusal. The
 * unported {@code android.widget} toolkit surfaces as the named "Unable to
 * inflate view" boundary instead of a silent empty tree.
 */
class LayoutInflaterTest {

    private static final int LAYOUT = 0x7F030000;
    private static final int GENERATED_WIDTH = 123;
    private static final int GENERATED_HEIGHT = 456;
    private static final String LEAF = "android.view.FakeLeaf";
    private static final String CONTAINER = "android.view.FakeContainer";

    private final FakeAppResourceHost host = new FakeAppResourceHost();

    @BeforeEach
    void installHost() {
        AppResources.install(host);
    }

    @AfterEach
    void clearHost() {
        AppResources.install(null);
    }

    @Test
    void inflatesTheNamedTree() {
        host.putDocument(LAYOUT, container(leaf()));

        View root = new LayoutInflater(new Context()).inflate(LAYOUT, null);

        assertTrue(root instanceof FakeContainer);
        FakeContainer group = (FakeContainer) root;
        assertEquals(1, group.getChildCount());
        assertTrue(group.getChildAt(0) instanceof FakeLeaf);
        assertSame(group.getChildAt(0), group.attached.get(0));
    }

    @Test
    void childLayoutParamsComeFromTheParent() {
        host.putDocument(LAYOUT, container(leaf()));

        FakeContainer group = (FakeContainer) new LayoutInflater(new Context())
                .inflate(LAYOUT, null);

        View child = group.getChildAt(0);
        assertEquals(GENERATED_WIDTH, child.getLayoutParams().width);
        assertEquals(GENERATED_HEIGHT, child.getLayoutParams().height);
        assertEquals(1, group.generateParamsCount);
    }

    @Test
    void finishInflateRunsOnceOnTheContainer() {
        host.putDocument(LAYOUT, container(leaf()));

        FakeContainer group = (FakeContainer) new LayoutInflater(new Context())
                .inflate(LAYOUT, null);

        assertEquals(1, group.finishInflateCount);
    }

    @Test
    void factory2GetsFirstRefusal() {
        host.putDocument(LAYOUT, leaf());
        LayoutInflater inflater = new LayoutInflater(new Context());
        FakeLeaf replacement = new FakeLeaf(new Context(), null);
        inflater.setFactory2(new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(View parent, String name, Context context,
                    AttributeSet attrs) {
                return LEAF.equals(name) ? replacement : null;
            }

            @Override
            public View onCreateView(String name, Context context,
                    AttributeSet attrs) {
                return null;
            }
        });

        assertSame(replacement, inflater.inflate(LAYOUT, null));
    }

    @Test
    void unportedWidgetReportsTheBoundary() {
        host.putDocument(LAYOUT,
                new XmlNode(null, "LinearLayout", List.of(), List.of()));

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> new LayoutInflater(new Context()).inflate(LAYOUT, null));

        assertTrue(error.getMessage().contains("Unable to inflate view: LinearLayout"),
                error.getMessage());
    }

    private static XmlNode leaf() {
        return new XmlNode(null, LEAF, List.of(), List.of());
    }

    private static XmlNode container(XmlNode... children) {
        return new XmlNode(null, CONTAINER, List.of(), List.of(children));
    }
}
