import java.util.ArrayDeque;


public class Reduce {

    private Kattio io;
    int bas_roller = 3;
    int bas_scener = 2;
    int bas_skadisar = 3;


    public Reduce(){
        io = new Kattio(System.in, System.out);
        graphReduction();
        io.flush();
        io.close();
    }

    //läser in hörn = roller, kanter = scener, färger = skådisar
    //skapar separat komponentgraf för minsta produktionen pga divorna, 3 roller, 2 scener, 3 skådisar
    //färgningen av denna komponentgraf påverkar inte indatagrafen
    private void graphReduction(){
        int from, to,nrLonelyNodes = 0;
        StringBuilder sb = new StringBuilder();

        int vertices = io.getInt(); //roller
        int edges = io.getInt(); // scener
        int colors = io.getInt(); // skådisar
        if(colors > vertices) colors = vertices;
        byte[] lonelyNodeCheckList = new byte[vertices+4];
        int[] indexAdjust = new int[vertices+4];
        ArrayDeque<Integer> queue = new ArrayDeque<>();


        //sparar alla kanter i kö för att senare kunna ta bort singlenoder/monologer
         for (int i = 4; i < edges+4; i++){
             from = io.getInt() + bas_roller;
             to = io.getInt() + bas_roller;
             lonelyNodeCheckList[from] = 1;
             lonelyNodeCheckList[to] = 1;
             queue.add(from);
             queue.add(to);

        }

        //ser hur hur många singlenoder som finns, 0:a står för singlenod, alltså ingen kant
        //alla noder justeras beroende på hur många singlenoder som finns framför de/som vi gått förbi
        //så i början har vi 0
        //men sen när vi gått förbi 1 så kommer vi ju behöva ta bort den och då måste vi ju skjuta 
        //tillbaka nästa nod som vi stöter på som inte är en singlenod ett steg tbx
        //om vi stöter på två st måste vi skjuta tbx den 2 steg
        //så i denna lista vet vi att noden p på plats p måste skjutas tillbaka 2 steg t.ex.
        //räknar också ut antal singlenoder
        for(int i = 4; i<lonelyNodeCheckList.length; i++){
            indexAdjust[i] = nrLonelyNodes;
            if(lonelyNodeCheckList[i] != 1) nrLonelyNodes++;
        }

        //antal vertices uppdateras så att alla singelnoder tas bort
        //printar ut inledande rader, antal roller, scener, skådisar
        vertices -= nrLonelyNodes;
        if(edges == 0){
            io.println(bas_roller);
        }else if(edges == 1){
            io.println(2 + bas_roller);
        }else {
            io.println(vertices + bas_roller);
        }
        io.println(edges + bas_scener);
        io.println(colors + bas_roller);

        //skriver ut rollista för basfallet 
        /*
        1 1
        1 2
        1 3
        */
        for (int i = 1; i <= bas_roller; i++){
            io.println(1 + " " + i);
        }

        //alla skådespelare får ha alla roller
        //skådisarna börjar dock från 4 då vi 1,2,3 är i en separat komponent av grafen
        //m 4 5 ... m+4
        //m 4 5 ... m+4
        sb.append(colors);
        for(int j = 4; j < (colors + 4); j++){
            sb.append(" ");
            sb.append(j);
        }
        for (int i = 0; i < vertices; i++){ //printar alla rader som för rollerna
            io.println(sb);
        }

        //basfall-scenerna skrivs ut
        /*
        2 1 3
        2 2 3
        */
        io.println(bas_scener + " " + 1 + " " + 3);
        io.println(bas_scener + " " + 2 + " " + 3);

        //gör scener utav de kanter som givits i input
        //måste dock justera index/nummer för deras noder då vi måste ta bort singlenoder
        //lägg sedan dessa i kö
        while(!queue.isEmpty()){
            from = queue.poll();
            from -= indexAdjust[from];
            to = queue.poll();
            to -= indexAdjust[to];
            io.println(2 + " " + from + " " + to);
        }
    }


    public static void main(String[] args){
        new Reduce();
    }
}