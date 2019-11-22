public class Edge {
    int fromNode;
    int toNode;
    int capacity;
    int flow;
    int restCapacity;
    public Edge(int fromNode, int toNode, int capacity) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.capacity = capacity;
        flow = 0;
        restCapacity = capacity;
    }


    public String printEdge() {
        return fromNode + " " + toNode;
    }

    public int getFlow(){
        return flow;
    }

    public int getCapacity(){
        return capacity;
    }
}
