import ui.host.HostApplication;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        HostApplication host = new HostApplication();
        host.run();
    }
}
