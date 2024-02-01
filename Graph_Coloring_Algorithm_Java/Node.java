import jdk.management.jfr.RecordingInfo;

import java.io.IOException;
import java.util.ArrayList;

public class Node extends Thread {
    private int id;
    private int color;
    private int numNodes;
    private int maxDeg;
    private int[][] neighbors;
    private ArrayList<Receiver> inputs;
    private ArrayList<Transmitter> outputs;

    public Node(int id, int numNodes, int maxDeg, int[][] neighbors) {
        this.id = id;
        this.color = id;
        this.numNodes = numNodes;
        this.maxDeg = maxDeg;
        this.neighbors = neighbors;
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        // your code here
    }

    @Override
    public void run() {
        // Reduce algorithm
        boolean firstRound = true;
        while (true) {
            boolean undecidedNeighbor = false;
            ArrayList<Integer> neighborColors = new ArrayList<Integer>();
            if (firstRound) {
                FloodMessage(new Message(id, -1)); // undecided is coded as -1
                firstRound = false;
                continue;
            } else {
                for (Receiver neighbor : inputs) {
                    try {
                        Message message = (Message) neighbor.getInputMessage();
                        neighborColors.add(message.color);
                        if (message.color == -1 && message.transmitterId > id) {
                            FloodMessage(new Message(id, -1)); // current node still doesn't pick a color
                            undecidedNeighbor = true;
                            break;
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!undecidedNeighbor) {
                for (int i = 0; i < maxDeg + 1; i++) {
                    if (!neighborColors.contains(i)) {
                        color = i;
                        FloodMessage(new Message(id, color));
                        return;
                    }
                }
            }
        }
    }

    public void printNode() {
        System.out.println("Node: " + this.id + " Color: " + this.color + " numNodes: " + this.numNodes + " maxDeg: " + this.maxDeg);
        for (int[] array : neighbors) {
            System.out.println(java.util.Arrays.toString(array));
        }
    }

    public int getColor() {
        return color;
    }

    public int getNodeId() {
        return id;
    }

    public void InitializeChannels() {
        for (int[] neighbor : neighbors) { // initialize connection streams to neighbors through designated ports
            int outPort = neighbor[1];
            int inPort = neighbor[2];
            try {
                Receiver input = new Receiver(inPort);
                inputs.add(input);
                input.Connect(); // connect each receiver to port
                Transmitter output = new Transmitter(outPort);
                outputs.add(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void FloodMessage(Message message) {
        for (Transmitter output : outputs) {
            try {
                output.SendMessageOnChannel(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
