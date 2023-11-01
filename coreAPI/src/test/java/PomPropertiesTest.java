import net.java.games.input.PomProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PomPropertiesTest {
    @Test
    public void testGetArtifactIdReturnsDefaultValue() {
        Assertions.assertEquals("Unknown", PomProperties.getArtifactId());
    }

    @Test
    public void testGetGroupIdReturnsDefaultValue() {
        Assertions.assertEquals("Unknown", PomProperties.getGroupId());
    }

    @Test
    public void testGetVersionReturnsDefaultValue() {
        Assertions.assertEquals("Unversioned", PomProperties.getVersion());
    }
}
