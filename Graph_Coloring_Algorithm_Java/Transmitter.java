import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Transmitter {
    Socket connectionSocket; // connection to the receiver
    ObjectOutputStream outputMessage; // the output stream
    int portNumber; // the port number

    public Transmitter(int portNumber) throws IOException {
        this.connectionSocket = new Socket("localhost", portNumber);
        this.portNumber = portNumber;
        this.outputMessage = new ObjectOutputStream(connectionSocket.getOutputStream());
    }

    public void SendMessageOnChannel(Message message) throws IOException {
        outputMessage.writeObject(message);
    }
}


