import java.util.ArrayList;
import java.util.List;

public class BipRed {
    Kattio io;
    Node[] graph;
    int e, x, y;

    public static void main(String[] args){
        new BipRed();
        

    }



    void readBipartiteGraph() {
        // Läs antal hörn och kanter
        x = io.getInt();
        y = io.getInt();

        //lägger till s och t
        int v = x + y + 2;
        graph = new Node[v];

        //skapa ny nod-instans för alla noder
        for (int i = 0; i < v; i++) {
            graph[i] = new Node(i);
        }

        //lägg till kant från s till x-klassen
        for (int i = 1; i <= x; i++) {
            graph[0].addEdge(graph[i]);
        }
        //lägg till kant från y klassen till t
        for (int i = x + 1; i < v - 1; i++) {
            graph[i].addEdge(graph[v-1]);
        }


        e = io.getInt();

        // Läs in kanterna
        for (int i = 0; i < e; ++i) {
            int a = io.getInt();
            int b = io.getInt();

            graph[a].addEdge(graph[b]);
        }

        e += x + y;
    }


    void writeFlowGraph() {
        //vi har redan sparat antal noder i graph arrayen
        int v = graph.length;
        //källa
        int s = 0;
        //sänka
        int t = graph.length - 1;

        // Skriv ut antal hörn och kanter samt källa och sänka
        io.println(v+1);
        io.println((s+1) + " " + (t+1));
        io.println(e);
        for (int i = 0; i < v; ++i) {
            //kant från a till b med kapacitet c
            int a = i;
            for (Node n : graph[i].getNeighborList())
            {
                int b = n.getNr();
                int c = 1;

                io.println((a+1) + " " + (b+1) + " " + c);
            }
            
        }
        // Var noggrann med att flusha utdata när flödesgrafen skrivits ut!
        io.flush();

        // Debugutskrift
        System.err.println("Skickade iväg flödesgrafen");
    }

    List<Edge> readMaxFlowSolution() {
        // Läs in antal hörn, kanter, källa, sänka, och totalt flöde
        // (Antal hörn, källa och sänka borde vara samma som vi i grafen vi
        // skickade iväg)
        int v = io.getInt();
        int s = io.getInt();
        int t = io.getInt();
        int totflow = io.getInt();
        int e = io.getInt();
        List<Edge> edges = new ArrayList<>(v);

        for (int i = 0; i < e; ++i) {
            // Flöde f från a till b
            int a = io.getInt();
            int b = io.getInt();
            int f = io.getInt(); // bryr oss ej om detta

            // Edges
            if (a != s && b != t) {
                edges.add(new Edge((a-1), (b-1)));
            }
        }
        return edges;
    }

    void writeBipMatchSolution(List<Edge> edges) {
        int maxMatch = edges.size();

        // Skriv ut antal hörn och storleken på matchningen
        io.println(x + " " + y);
        io.println(maxMatch);

        for (Edge edge : edges) {
            io.println(edge.printEdge());
        }
        io.flush();
    }

    BipRed() {
        io = new Kattio(System.in, System.out);

        readBipartiteGraph();

        writeFlowGraph();

        writeBipMatchSolution(readMaxFlowSolution());

        // debugutskrift
        System.err.println("Bipred avslutar\n");

        // Kom ihåg att stänga ner Kattio-klassen
        io.close();
    }
}