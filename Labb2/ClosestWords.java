import java.util.LinkedList;
import java.util.List;

public class ClosestWords {
  LinkedList<String> closestWords = null;
  int closestDistance = -1;

  int[][] m = new int[40][40];
  

  void makeMatrix(){
    for(int i = 0; i<40; i++){
      m[i][0] = i;
    }
    for(int i = 1; i<40; i++){
      m[0][i] = i;
    }

  }


  int partDist(String w1, String w2, int w1len, int w2len, int same){
    
    
    for(int i = same; i<w2len+1; i++){
      for (int j = 1; j<w1len+1; j++){
        int res = m[j-1][i-1] + (w1.charAt(j-1)==w2.charAt(i-1) ? 0 : 1);
        int addLetter = m[j-1][i]+1;
        if(addLetter<res){
          res = addLetter;
        }
        int removeLetter = m[j][i-1]+1;
        if(removeLetter < res){
          res = removeLetter;
        }
        m[j][i] = res;
      }


    }
    return m[w1len][w2len];
  }



  int Distance(String w1, String w2, int p) {

    return partDist(w1, w2, w1.length(), w2.length(), p+1);
  }

  

  public ClosestWords(String w, List<String> wordList) {
    makeMatrix();
   
    int wlen = w.length();

    String oldWord = "";
    
    for (String s : wordList) {

      if (closestDistance != -1 && Math.abs(s.length()-wlen) > closestDistance) {
        continue;
      }

      int p = 0;
      for(int i = 0; i<oldWord.length()&& i<s.length();i++){
        if(oldWord.charAt(i)==s.charAt(i)){
          p++;
        }else{
          break;
        }
      }
     
      int dist = Distance(w, s,p);
      
      if (dist < closestDistance || closestDistance == -1) {
        closestDistance = dist;
        closestWords = new LinkedList<String>();
        closestWords.add(s);
      }
      else if (dist == closestDistance){
        closestWords.add(s);
      }
        oldWord = s;
    
}
  }

  int getMinDistance() {
    return closestDistance;
  }

  List<String> getClosestWords() {
    return closestWords;
  }
}

