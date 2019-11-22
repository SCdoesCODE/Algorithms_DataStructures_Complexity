import java.util.LinkedList;

public class Node {
    LinkedList<Node> neighborList;
    int nodeNr;
    

    Node (int nodeNr) {
        this.nodeNr = nodeNr;
        neighborList = new LinkedList<>();

    }


    public int getNr() {
        return nodeNr;
    }

    public void addEdge(Node node){
        neighborList.add(node);

    }
    public LinkedList<Node> getNeighborList()
    {
        return neighborList;
    }
}