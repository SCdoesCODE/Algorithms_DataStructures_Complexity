import java.util.*;



public class Labb5Test{
	Kattio io;
	int roles;
  	int scenes;
  	int actors;
  	int superActor;
  	int nrFinalActors;

  	//ActorTest[] actorClassArray;
  	List<ActorTest> actorClassList;

  	int[] roleToActorArray; //uppdaterar denna när vi tillsatt en roll

  	HashSet<Integer> nonUsedRoles; //när vi tillsatt en roll tar vi bort den härifrån
  	HashMap<Integer, List<Integer>> sceneToRolesMap; //roller som finns i en scen


  	public Labb5Test(){

  		io = new Kattio(System.in, System.out);
  		roles = io.getInt();
    	scenes = io.getInt();
    	actors = io.getInt();
    	superActor = actors + 1;

    	actorClassList = new ArrayList<ActorTest>();
    	nonUsedRoles = new HashSet<Integer>();

    	sceneToRolesMap = new HashMap<Integer,List<Integer>>();
    	roleToActorArray = new int[roles+1]; //på index 10 finns den skådisen som fått roll 10

    	//ifyllnadsactor för att vi måste börja på 0 i en lista
    	actorClassList.add(0,new ActorTest(-1));

    	for(int i = 1;i<=actors;i++){
    		actorClassList.add(i,new ActorTest(i));
    	}

    	//fyller nonUsedRoles set som vi ska ta bort från senare vid tillsättning av roller
	    for (int i = 1; i <= roles; i++) {
	      	nonUsedRoles.add(i);
	      	

	      	//fyll i information om vilka skådisar som kan spela vilka roller
		     int nrPossibleActorsForRole = io.getInt(); //denna roll kan spelas av så många skådisar
		    
		     for (int j = 0; j < nrPossibleActorsForRole; j++) {
		       int actorNr = io.getInt(); 
		       actorClassList.get(actorNr).possibleRoles.add(i);


		     }
		     
		     
		  
		}
		//fyll i info om vilka roller som är i vilka scener
		for (int i = 1; i <= scenes; i++) {
			int nrRolesInScene = io.getInt();
			List<Integer> rolesList = new ArrayList<Integer>();
			for(int j = 0; j<nrRolesInScene;j++){
				int roleNum = io.getInt();
				rolesList.add(roleNum);
			}
			sceneToRolesMap.put(i,rolesList);
			

		}
		giveRolesToDivas();
		giveRolesToRest();
		giveRolesToSuperActors();

		printOutput();

		io.flush();
    	io.close();


	}

  	public void giveRolesToDivas(){

	  	//ge diva1 en roll
	  	int tempDiva1 = 0;
	  	for (int role : actorClassList.get(1).possibleRoles) {

	  		if(possibleToGiveRoleToActor(role,1)){
	  			tempDiva1 = role;
	  			giveRoleToActor(role,1);
	  			break;
	  		}
	  	}

	  	//försök ge diva2 en roll
	  	//funkar inte alltid
	  	//men därför kör vi om hela programmet när detta inte går
	  	//borde finnas bättre lösning
	  	boolean bothDivasCasted = false;
	    for (int role : actorClassList.get(2).possibleRoles) {
	      if (possibleToGiveRoleToActor(role,2)) {
	      	bothDivasCasted = true;
	        giveRoleToActor(role,2);
	        break;
	      }
	    }

	    //om det är omöjligt att ge diva2 en roll när diva1 har fått denna roll
	    //så vill vi alltså inte att diva1 ska få denna roll
	    //och tar därför bort denna roll som möjlig roll från diva1:s möjliga roller

	    if (!bothDivasCasted) {
	      actorClassList.get(1).possibleRoles.remove(tempDiva1);
	      actorClassList.get(1).givenRoles.remove(tempDiva1);
	      roleToActorArray[tempDiva1] = 0;

	      nrFinalActors = 0;
	      giveRolesToDivas();
	    }

  	}

  	public void giveRolesToRest(){
  		//System.out.println("tjoooooo" + nonUsedRoles.size()+ " " + nonUsedRoles);
  		for (int i = 3; i <= actors; i++) {
  			//kollar att det finns möjliga roller för denna skådis
  			if(actorClassList.get(i).possibleRoles!=null){
  				//för varje möjlig roll
  				for(int role : actorClassList.get(i).possibleRoles){
  					//kollar om det är möjligt att kasta denna roll till denna skådis
  					if(possibleToGiveRoleToActor(role,i)){
  						giveRoleToActor(role,i);
  					}
  				}
  			}

  	}
  }
  

  public void giveRolesToSuperActors(){
  	//Integer[] lastRoles = nonUsedRoles.toArray(new Integer[nonUsedRoles.size()]);
  	//System.out.println("tjoooooo" + nonUsedRoles.size()+ " " + nonUsedRoles);
  	//System.out.println("arraysize " + roleToActorArray.length);

  	for (final java.util.Iterator<Integer> nonUsedRole = nonUsedRoles.iterator(); nonUsedRole.hasNext();) {
	    	final Integer currentItrRole = nonUsedRole.next();
	    	nonUsedRole.remove();
	    	nrFinalActors++;
	    	roleToActorArray[currentItrRole] = superActor;
	    	actorClassList.add(superActor,new ActorTest(superActor));
	    	actorClassList.get(superActor).givenRoles.add(currentItrRole);
	    	superActor++;
	}


  }


  	public boolean possibleToGiveRoleToActor(int roleNr,int actorNr){

  		//redan upptagen 
  		if(roleToActorArray[roleNr] != 0){
  			return false;
  		}

  		//kolla att diva-villkoret möts
  		if(actorNr == 1 || actorNr == 2){
  			//basically gå igenom varje scen och se om de innehåller roller som givits till diva 1 och diva 2
  			for (int i = 1; i <= scenes; i++) {
  				for (int diva1Role : actorClassList.get(1).givenRoles) {
  				//så om vi stöter på en scen som innehåller denna roll vi vill casta och en roll som redan givits till diva1
  				//så kan vi inte lägga varken diva1 eller diva2 där
  				//den andra for-loopen säger basically samma sak
  				//bara att vi går igenom roller som givits till diva2
		          if (sceneToRolesMap.get(i).contains(roleNr) && sceneToRolesMap.get(i).contains(diva1Role)) {
		            return false;
		          }
		        }
		        for (int diva2Role : actorClassList.get(2).givenRoles) {
          			if (sceneToRolesMap.get(i).contains(roleNr) && sceneToRolesMap.get(i).contains(diva2Role)) {
		            return false;
		          }
		        }
  			}
  		}else{
  		//för alla andra som inte är divor
  		//kollar så att de inte hamnar i scener med sig själva
  		//om vi stöter på en scen där skådespelaren redan tilldelats en roll och denna roll förekommer
  		//går ej
  			for (int i = 1; i <= scenes; i++) {
		        for (int role : actorClassList.get(actorNr).givenRoles) {
		          if (sceneToRolesMap.get(i).contains(roleNr) && sceneToRolesMap.get(i).contains(role)) {
		            return false;
		          }
		        }
		      }


  		}

  		//om inget av detta händer är det OK att tillsätta denna skådis till denna roll :))

  		return true;


  	}

  	public void giveRoleToActor(int roleNr, int actorNr){
  		//om det är den första rollen vi ger till denna skådis kan vi inkrementera antal final actors
  		if (actorClassList.get(actorNr).givenRoles.isEmpty()) {
	      nrFinalActors++;
	    }

  		nonUsedRoles.remove(roleNr);
  		roleToActorArray[roleNr] = actorNr;
  		actorClassList.get(actorNr).givenRoles.add(roleNr);
  		//actorToGivenRolesCollection.add(actorNr,roleNr);
  		

  	}

  	  public void printOutput() {
	    StringBuilder sb;
	    //int totalActorCount = nrFinalActors + nonUsedRoles.size();
	    io.println(nrFinalActors);
	    for (int i = 1; i <actorClassList.size(); i++) {
	      if (!actorClassList.get(i).givenRoles.isEmpty()) {
	        sb = new StringBuilder();
	        sb.append(i + " " + actorClassList.get(i).givenRoles.size());
	        for (int role : actorClassList.get(i).givenRoles) {
	          sb.append(" " + role);
	        }
	        io.println(sb.toString());
	      }
	    }

	  }


	 

	  public static void main(String args[]) {
	    new Labb5Test();
	  }


}