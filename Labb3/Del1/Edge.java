public class Edge {
    int fromNode;
    int toNode;

    public Edge(int fromNode, int toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }


    public String printEdge() {
        return fromNode + " " + toNode;
    }
}