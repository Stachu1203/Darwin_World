package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {
    @Test
    void testToString() {
        Vector2d v = new Vector2d(1, 2);
        assertEquals("(1,2)", v.toString());
    }

    @Test
    void testEquals() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(1, 2);
        Vector2d v3 = new Vector2d(2, 1);

        assertEquals(v1, v2, "Wektory o tych samych współrzędnych powinny być równe");
        assertNotEquals(v1, v3, "Wektory o różnych współrzędnych nie powinny być równe");
        assertNotEquals(v1, "not a vector", "Porównanie z obiektem innej klasy powinno zwrócić false");
    }

    @Test
    void testPrecedesFollows() {
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(2, 2);
        Vector2d v3 = new Vector2d(1, 2);

        assertTrue(v1.precedes(v2));
        assertTrue(v1.precedes(v3));
        assertFalse(v2.precedes(v1));

        assertTrue(v2.follows(v1));
        assertTrue(v3.follows(v1));
        assertFalse(v1.follows(v2));
    }

    @Test
    void testUpperRightLowerLeft() {
        Vector2d v1 = new Vector2d(1, 5);
        Vector2d v2 = new Vector2d(3, 2);

        assertEquals(new Vector2d(3, 5), v1.upperRight(v2));
        assertEquals(new Vector2d(1, 2), v1.lowerLeft(v2));
    }

    @Test
    void testAddSubtract() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(-3, 4);

        assertEquals(new Vector2d(-2, 6), v1.add(v2));
        assertEquals(new Vector2d(4, -2), v1.subtract(v2));
    }

    @Test
    void testOpposite() {
        Vector2d v = new Vector2d(5, -10);
        assertEquals(new Vector2d(-5, 10), v.opposite());
    }

}