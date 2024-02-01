import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class Manager {
    private int numNodes = 0;
    private int maxDegree = 0;
    private ArrayList<Node> graphNodes;

    public Manager() {
        this.graphNodes = new ArrayList<Node>();
    }

    public void readInput(String path) {
        System.out.println("Called readInput in Manager!");
        Scanner scanner;
        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        int lineCounter = 1;
        while (scanner.hasNext()) {
            if (lineCounter == 1) {
                this.numNodes = Integer.parseInt(scanner.nextLine());
                System.out.println("numNodes: " + this.numNodes);
            } else if (lineCounter == 2) {
                this.maxDegree = Integer.parseInt(scanner.nextLine());
                System.out.println("maxDegree: " + this.maxDegree);
            } else {
                int nodeID = scanner.nextInt();
                // Extract the 2D array as a string
                String arrayString = scanner.nextLine().trim();
                // Remove brackets and split the 2D array into inner arrays
                String[] innerArrayStrings = arrayString.substring(2, arrayString.length() - 2).split("\\], \\[");
                // Process each inner array
                List<int[]> array = new ArrayList<>();
                for (String innerArrayString : innerArrayStrings) {
                    // Split the inner array into elements
                    String[] elements = innerArrayString.split(",");
                    // Convert elements to integers
                    int[] innerArray = new int[elements.length];
                    for (int i = 0; i < elements.length; i++) {
                        innerArray[i] = Integer.parseInt(elements[i].trim());
                    }
                    // Add the inner array to the list
                    array.add(innerArray);
                }
                Node node = new Node(nodeID, this.numNodes, this.maxDegree, array.toArray(new int[array.size()][]));
                this.graphNodes.add(node);
            }
            lineCounter++;
        }
        scanner.close();
        try {
            InitializeConnections();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // printGraphNodes();
    }

    public void InitializeConnections() throws IOException {
        for (Node node : graphNodes) {
            node.InitializeChannels();
        }
    }

    /*public String start() {
        ArrayList<Thread> threads = new ArrayList<>();
        try {
            InitializeConnections();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*for (Node node : graphNodes) {
            threads.add(new Thread(node));
        }
        for (Thread thread : threads) {
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Node node : graphNodes) {
            node.start();
            try {
                node.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        StringBuilder message = new StringBuilder();
        for (Node node : this.graphNodes) {
            message.append(node.getNodeId() + "," + node.getColor() + "\n");
        }
        return message.toString();
    }*/
    public String start() {
        ArrayList<Thread> threadList = new ArrayList<>();
        for(Node curr_node: graphNodes){
            Thread curr_thread = new Thread(curr_node);
            threadList.add(curr_thread);
            curr_thread.start();
        }
        for (Thread thread : threadList){
            try{
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        StringBuilder message = new StringBuilder();
        for (Node node : this.graphNodes) {
            message.append(node.getNodeId() + "," + node.getColor() + "\n");
        }
        return message.toString();
    }


    public String terminate() {
        StringBuilder message = new StringBuilder();
        for (Node node : this.graphNodes) {
            message.append(node.getNodeId() + "," + node.getColor() + "\n");
        }
        return message.toString();
    }

    public void printGraphNodes() {
        for (Node node : this.graphNodes) {
            node.printNode();
        }
    }
}
