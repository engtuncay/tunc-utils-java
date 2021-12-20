package ozpasyazilim.utils.core;

import java.io.File;
import java.io.IOException;

public class FiCommand {

    public FiCommand() {
    }

    public static void openTable(File file) throws IOException {
        Runtime run = Runtime.getRuntime();
        String lcOSName = System.getProperty("os.name").toLowerCase();
        boolean MAC_OS_X = lcOSName.startsWith("mac os x");
        if (MAC_OS_X) {
            run.exec("open " + file);
        } else {
            run.exec("cmd.exe /c start " + file);
        }
    }
}
