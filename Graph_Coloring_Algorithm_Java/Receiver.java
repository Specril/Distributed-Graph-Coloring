import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
    private ServerSocket initializingSocket; // dock socket waiting for a connection
    private Socket connectionSocket; // connection socket to transmitter
    private ObjectInputStream inputMessage; // the input stream
    private int portNumber; // the servers port

    public Receiver(int portNumber) throws IOException {
        this.portNumber = portNumber;
        this.initializingSocket = new ServerSocket(portNumber);
    }

    public void Connect() throws IOException {
        this.connectionSocket = initializingSocket.accept();
        this.inputMessage = new ObjectInputStream(connectionSocket.getInputStream());
    }

    public Object getInputMessage() throws IOException, ClassNotFoundException {
        return inputMessage.readObject();
    }
}
