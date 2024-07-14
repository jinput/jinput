package net.java.games.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VersionTest {
    @Test
    public void testGetVersionReturnsDefaultValue() {
        Assertions.assertEquals("Unversioned", PomProperties.getVersion());
    }
}
