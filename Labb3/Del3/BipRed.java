import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
public class BipRed {
    Kattio io;
    Node[] graph;
    Node[] bipartGraph;
    int e, x, y, v, s, t;
    int maxMatch;
    int matches;
    ArrayList<Integer> bipMatch = new ArrayList<Integer>();

    public static void main(String[] args){
        new BipRed();
    }

    int fordFulkerson(int s, int t){
      int u, v;

      int maxFlow = 0;
      int parent[] = new int[graph.length];
      while(bfs(s, t, parent)){
        int pathFlow = Integer.MAX_VALUE;


       for(v=t; v != s; v=parent[v]){
         u = parent[v];
          //System.out.println(u +" : " +  parent[v] + "parent v");
          pathFlow = Math.min(pathFlow, graph[u].edgeMap.get(v).restCapacity);
        }

        for(v=t; v != s; v=parent[v]){
          u = parent[v];

          graph[u].edgeMap.get(v).flow = graph[u].edgeMap.get(v).flow + pathFlow;
          if(graph[v].edgeMap.containsKey(u) && graph[u].edgeMap.containsKey(v))
            graph[v].edgeMap.get(u).flow = -graph[u].edgeMap.get(v).flow;
          graph[u].edgeMap.get(v).restCapacity = graph[u].edgeMap.get(v).capacity - graph[u].edgeMap.get(v).flow;
          if(graph[v].edgeMap.containsKey(u) && graph[v].edgeMap.containsKey(u))
            graph[v].edgeMap.get(u).restCapacity = graph[v].edgeMap.get(u).capacity - graph[v].edgeMap.get(u).flow;

/*
          if(v!=t && u!=s ){
              //System.out.println(u +" : " +  v + "parent v");

              bipMatch.add(u);
              bipMatch.add(v);
          }*/
          //System.out.println(u +" : " +  v + " " + graph[v].edgeMap.get(u).capacity);
        }

        maxFlow += pathFlow;
      }

      maxMatch = maxFlow;
    //  System.out.println("BIG BRAIN LETS GO I BELIVE IN MIRACLES" + maxFlow);
      return maxFlow;
    }

    boolean bfs(int s, int t, int[] parent){
      boolean visited[] = new boolean[graph.length];
      for(int i=0; i < graph.length; i++ ){
        visited[i] = false;
      }
      LinkedList<Integer> queue = new LinkedList<Integer>();
      queue.add(graph[s].getNr());
      visited[s] = true;
      parent[s] = -1;
      while(queue.size() != 0){
        int u = queue.poll();
        HashMap<Integer, Edge> edgeList = graph[u].edgeMap;
        for(int node : edgeList.keySet()){
          if(visited[node] == false && edgeList.get(node).restCapacity > 0){
        //  if(visited[node] == false && edgeList.get(node).restCapacity > 0 && graph[node].edgeMap.get(u).restCapacity >= 0){
            queue.add(node);
            parent[node] = u;
            visited[node] = true;
          }

        }
      }
    //System.out.println("  " + (visited[t] == true));
      return (visited[t] == true);
    }




    void readBipartiteGraph() {
        // Läs antal hörn och kanter
        x = io.getInt();
        y = io.getInt();

        //lägger till s och t
        int v = x + y + 2;
        bipartGraph = new Node[v];

        //skapa ny nod-instans för alla noder
        for (int i = 0; i < v; i++) {
            bipartGraph[i] = new Node(i);
        }

        //lägg till kant från s till x-klassen
        for (int i = 1; i < x + 1; i++) {
            bipartGraph[0].addEdge(bipartGraph[i]);
        }
        //lägg till kant från y klassen till t
        for (int i = x + 1; i < v - 1; i++) {
            bipartGraph[i].addEdge(bipartGraph[v-1]);
        }


        e = io.getInt();

        // Läs in kanterna
        for (int i = 0; i < e; ++i) {
            int a = io.getInt();
            int b = io.getInt();

            bipartGraph[a].addEdge(bipartGraph[b]);
        }

        e += x + y;
    }

    List<Edge> readAndSolveFlowGraphBiparti() {
        // Läs in antal hörn, kanter, källa, sänka, och totalt flöde
        // (Antal hörn, källa och sänka borde vara samma som vi i grafen vi
        // skickade iväg)
        v = bipartGraph.length;
        s = 0;
        t = v - 1;

        Edge edge;
        Edge edge2;
        List<Edge> edges = new ArrayList<>(v);
        //Might be uneccesary

        graph = new Node[v];
        for (int i = 0; i < v; i++) {
            graph[i] = new Node(i);
        }
        for (int i = 0; i < v; i++) {
            for(Node nextNode : bipartGraph[i].neighborList){

              int toNode = nextNode.getNr();
                //            System.out.println(i+ " ->" +toNode);
              edge = new Edge(i, toNode, 1);
              edge2 = new Edge(toNode, i, 0);
              graph[i].addEdge(graph[toNode], edge);
              if(!graph[toNode].edgeMap.containsKey(i))
                graph[toNode].addEdge(graph[i], edge2);
            }
        }

        fordFulkerson(s,t);
        return edges;
    }

    void writeFlowGraphSolution(){
      // Skriv ut antal hörn och kanter samt källa och sänka
      io.println(v);
      int totFlow = 0;

      io.println((s) + " " + (t) + " " + maxMatch);
      int matchesFound = 0;
      StringBuilder stringBuilder = new StringBuilder();
      for (Node node : graph) {
        if(node != null)
          for (Map.Entry<Integer, Edge> edge : node.edgeMap.entrySet()){
            if(edge.getValue().fromNode != t
            && edge.getValue().toNode != s
            && edge.getValue().flow > 0){
              matchesFound++;
              stringBuilder.append(edge.getValue().printEdge() + " " + edge.getValue().getFlow() + "\n");

            }

        }
      }
      io.println(matchesFound);
      if(stringBuilder.length() > 0)
        io.println(stringBuilder.deleteCharAt(stringBuilder.length()-1));
      // Var noggrann med att flusha utdata när flödesgrafen skrivits ut!
      io.flush();

      // Debugutskrift
      System.err.println("Skickade iväg flödesgrafen");
    }

    void writeBipMatchSolution(List<Edge> edges) {

        // Skriv ut antal hörn och storleken på matchningen

        io.println(x + " " + y);
        io.println(maxMatch);

        for (Node node : graph) {
          for (Map.Entry<Integer, Edge> edge : node.edgeMap.entrySet()){
            if(edge.getValue().fromNode != 0 && edge.getValue().fromNode != graph.length -1
            && edge.getValue().toNode != graph.length -1 && edge.getValue().toNode != 0
            && edge.getValue().flow > 0){
                io.println(edge.getValue().printEdge());
            }

          }
        }


        io.flush();
    }

    BipRed() {
        io = new Kattio(System.in, System.out);

        readBipartiteGraph();
          //writeBipMatchSolution(readMaxFlowSolution());
          //readAndSolveFlowGraph();
          //writeFlowGraphSolution();
        // debugutskrift
        writeBipMatchSolution(readAndSolveFlowGraphBiparti());
        //System.err.println("Bipred avslutar\n");

        // Kom ihåg att stänga ner Kattio-klassen
        io.close();
    }
}