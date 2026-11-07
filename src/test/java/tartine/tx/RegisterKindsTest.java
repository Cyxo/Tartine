package tartine.tx;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Pure-rule pins for the extracted register-kind lattice. */
final class RegisterKindsTest {

    @Test
    void conflictingSlotsUnifyReferenceAndIntWideMixes() {
        Map<Integer, Set<Character>> stores = Map.of(
                0, Set.of(RegisterKinds.INT),
                1, Set.of(RegisterKinds.OBJ, RegisterKinds.INT),
                2, Set.of(RegisterKinds.OBJ, RegisterKinds.LONG),
                3, Set.of(RegisterKinds.OBJ, RegisterKinds.FLOAT),
                4, Set.of(RegisterKinds.OBJ, RegisterKinds.DOUBLE),
                5, Set.of(RegisterKinds.INT, RegisterKinds.LONG),
                6, Set.of(RegisterKinds.INT, RegisterKinds.FLOAT),
                7, Set.of(RegisterKinds.LONG, RegisterKinds.DOUBLE),
                8, Set.of(RegisterKinds.FLOAT, RegisterKinds.LONG),
                9, Set.of(RegisterKinds.FLOAT, RegisterKinds.DOUBLE));

        assertEquals(Set.of(1, 2, 3, 4, 5, 6, 7),
                RegisterKinds.conflicting(stores));
    }

    @Test
    void joinKeepsAgreedAndUnifiesBoxedMixes() {
        assertEquals(RegisterKinds.INT,
                RegisterKinds.join(Set.of(RegisterKinds.INT)));
        assertEquals(RegisterKinds.OBJ,
                RegisterKinds.join(Set.of(RegisterKinds.OBJ,
                        RegisterKinds.INT)));
        assertEquals(RegisterKinds.OBJ,
                RegisterKinds.join(Set.of(RegisterKinds.OBJ,
                        RegisterKinds.LONG, RegisterKinds.FLOAT)));
        assertEquals(RegisterKinds.OBJ,
                RegisterKinds.join(Set.of(RegisterKinds.INT,
                        RegisterKinds.LONG)));
        assertEquals(RegisterKinds.OBJ,
                RegisterKinds.join(Set.of(RegisterKinds.LONG,
                        RegisterKinds.DOUBLE)));
        // A register unified for another reason can show only this pair at
        // one join; the boxing lane already handled its stores.
        assertEquals(RegisterKinds.OBJ,
                RegisterKinds.join(Set.of(RegisterKinds.FLOAT,
                        RegisterKinds.LONG)));
    }

    @Test
    void joinRejectsUnboxedNumericMixes() {
        assertEquals(RegisterKinds.CONFLICT,
                RegisterKinds.join(Set.of(RegisterKinds.FLOAT,
                        RegisterKinds.DOUBLE)));
        assertEquals(RegisterKinds.CONFLICT,
                RegisterKinds.join(Set.of(RegisterKinds.INT,
                        RegisterKinds.DOUBLE)));
    }
}
