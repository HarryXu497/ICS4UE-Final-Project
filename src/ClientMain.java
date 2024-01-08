import ui.client.ClientApplication;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) {
        try (ClientApplication clientApplication = new ClientApplication()) {
            clientApplication.run();
        } catch (IOException e) {
            System.out.println("Something went wrong.");
        }
    }
}
