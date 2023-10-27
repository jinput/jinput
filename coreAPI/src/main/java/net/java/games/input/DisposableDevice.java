package net.java.games.input;

import java.io.IOException;

/**
 * @author Capitrium
 */
public interface DisposableDevice {
    void close() throws IOException;
}
