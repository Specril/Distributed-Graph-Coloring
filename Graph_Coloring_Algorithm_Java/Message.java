import java.io.Serializable;

public class Message implements Serializable {
    int transmitterId;
    int color;

    public Message(int transmitterId, int color){
        this.transmitterId = transmitterId;
        this.color = color;
    }
}
