import java.util.LinkedList;
import java.util.HashMap;

public class Node {
    LinkedList<Node> neighborList;
    HashMap<Integer, Edge> edgeMap = new HashMap<Integer, Edge>();
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

    public void addEdge(Node node, Edge edge){
        edgeMap.put(node.nodeNr, edge);
        neighborList.add(node);
    }
    public LinkedList<Node> getNeighborList()
    {
        return neighborList;
    }
}

