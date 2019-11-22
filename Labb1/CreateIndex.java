import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
public class CreateIndex {

  private byte[] hashIndex;
  private static RandomAccessFile randomAccessFile;
  private String recentWord;
  public static void main(String[] args) {
    try{

      PrintWriter writer = new PrintWriter("/var/tmp/Latmanshash.txt", "ISO-8859-1");
      Latmanshashning latmanshashning = new Latmanshashning(true);

      long[] wordList = latmanshashning.hashIndex;
        for(int i = 0; i < wordList.length; i++){
          if(wordList[i] == 0 && i != 1){
              writer.println("i"+ i + " " + -1);
          }
          else{
              writer.println("i"+ i + " " + wordList[i]);
          }


        }
        writer.close();

      }
      catch(IOException e){
        e.printStackTrace();
      }
    }

/*
    Scanner scannerObj = new Scanner(System.in);
    System.out.println("Skriv in ett ord tack");
    String word = scannerObj.nextLine();
    List<Long> wordList = latmanshashning.FindAll(word);





    System.out.println("Det finns " + wordList.size() + " förekomster av ordet");
    if(wordList.size() > 24){
      System.out.println("Det finns fler än 25 förekomster, fortsätta? Ja/Nej");
      String inputWord = scannerObj.nextLine();
      if(!inputWord.equals("Ja")){
      }
      else{
        latmanshashning.PrintTexts(wordList);
      }
    }
    else{
      latmanshashning.PrintTexts(wordList);
    }
  */
  }




/*

  private void GetWordList(){
      randomAccessFile.seek(i);
  }
*/
