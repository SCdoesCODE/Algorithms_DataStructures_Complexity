import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.lang.*;
import java.lang.Iterable;
public class Konkordans {


  static Latmanshashning latmanshashning;
  RandomAccessFile bigBoyKorpus;
  byte[] b;
  RandomAccessFile randomAccessFile;
  String[] splitHash = new String[200];
  public Konkordans(String word){
    char[] letters = word.toCharArray();
    for(char letter : letters) {
    if(!Character.isLetter(letter)){
      System.out.println("Skriv in ett giltigt ord nästa gång tack");
      System.exit(0);
  }
}
    long indexWordPosition = 0;
    b = new byte[200];

    try{
      randomAccessFile = new RandomAccessFile("/var/tmp/Latmanshash.txt", "r");
      long high = randomAccessFile.length();
      long low = 0;
      long mid = (high-low)/2;
      indexWordPosition = BinSearch(latmanshashning.CalculateWordIndex(word), high, low);
    }catch(IOException e){
      e.printStackTrace();
    }
    List<Long> wordList = new ArrayList<Long>();
    if(indexWordPosition>=0){
            wordList = latmanshashning.FindWordInOrdLista(word,
            latmanshashning.MakeIndexWorthy(word),
            indexWordPosition);
    }






    System.out.println("Det finns " + wordList.size() + " förekomster av ordet");
    Scanner scannerObj = new Scanner(System.in);
    if(wordList.size() > 24){
      System.out.println("Det finns fler än 25 förekomster, fortsätta? Ja/Nej");
      String inputWord = scannerObj.nextLine();
      if(!inputWord.equals("ja")){
      }
      else{
        LoadBigBoyKorpus();
        PrintTexts(wordList);
      }
    }
    else{
      LoadBigBoyKorpus();
      PrintTexts(wordList);
    }
  }

  public Long BinSearch(long word, long high, long low){
    long mid = (high + low)/2;

    //System.out.println("high: " + high + "mid: " + mid + "low: " + low);
    try{
      randomAccessFile.seek(mid);
      randomAccessFile.read(b);
      String text = new String(b, "ISO-8859-1");
      splitHash = text.split("\\s+");
      //Check if highest value in splithash is higher than what we are looking for and lowest is lwoer than value, Then to array search.
      for(int i = 0; i < 3; i++){
        if(splitHash[i].length() > 0 && splitHash[i].substring(0,1).equals("i")){

            Long value = Long.valueOf(splitHash[i].substring(1,splitHash[i].length())).longValue();

            if(value > word){
              return BinSearch(word, mid, low);
            }
            else if(value < word){

              return BinSearch(word, high, mid);
            }
            else{
              if(splitHash[i+1].substring(0,1).equals("n")){
                return null;
              }else{
                return Long.valueOf(splitHash[i+1]).longValue();
              }
            }
          }
      }

    }catch(IOException e){
      e.printStackTrace();
    }
    return null;
  }


  public void PrintTexts(List<Long> listORD){

    for(Long places : listORD){
        System.out.println(FindTextInKorpus(places));
    }

  }
  public static void main(String[] args){


      latmanshashning = new Latmanshashning();
//System.out.println(args[0].toLowerCase());
      Konkordans konkordans = new Konkordans(args[0].toLowerCase());

  }

  public void LoadBigBoyKorpus(){
    try{
      bigBoyKorpus = new RandomAccessFile("/info/adk19/labb1/korpus", "r");
      }catch(IOException e){
         e.printStackTrace();
      }
  }

  private String FindTextInKorpus(long point){
    byte[] b = new byte[40];
    String text = "NU BLEV DET FEL";
    try{
      if(point>=20){
        bigBoyKorpus.seek(point-20);
      }else{
        bigBoyKorpus.seek(point);
      }

      bigBoyKorpus.read(b);
      text = new String(b, "ISO-8859-1");
      //System.out.println(text);

    }
    catch(IOException e){
      e.printStackTrace();
    }

    return text;
  }
}
