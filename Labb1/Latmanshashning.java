//one/two off när det kommer till antal förekomster
//når inte slutet av filen

import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.EOFException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Latmanshashning{
  public long[] hashIndex;
  private RandomAccessFile randomAccessFile;
  private String currentWord;
  private long firstPointer;
  private RandomAccessFile bigBoyKorpus;

  public Latmanshashning(){
    try{

      randomAccessFile = new RandomAccessFile("/var/tmp/ut", "r");

      byte[] b = new byte[20000];
      //första fallet när vi tar in den första chunken så kollar vi bara i slutet att vi inte slutar på ett ord
      long fileSize = randomAccessFile.length();
      String text;
      boolean allLoaded = false;
      String[] splitWord;

      }


    catch(IOException e){
      e.printStackTrace();
    }

  }


  public Latmanshashning(Boolean create){
    hashIndex = new long[32000];
    firstPointer = 0;
    currentWord = "";
    int startPos = 0;

    try{

      randomAccessFile = new RandomAccessFile("/var/tmp/ut", "r");

      byte[] b = new byte[20000];
      //första fallet när vi tar in den första chunken så kollar vi bara i slutet att vi inte slutar på ett ord
      long fileSize = randomAccessFile.length();
      String text;
      boolean allLoaded = false;
      String[] splitWord;
      //CreateHashIndex(splitWord, 0);

      while(fileSize > firstPointer){
        randomAccessFile.seek(firstPointer);
        randomAccessFile.read(b);
        text = new String(b, "ISO-8859-1");

        splitWord = text.split("\\s+");
        if(firstPointer + 20000 > fileSize){
          allLoaded = true;
        }
        CreateHashIndex(splitWord, allLoaded);

      }


    }catch(IOException e){
      e.printStackTrace();
    }
  }





  private void CreateHashIndex(String[] splitWord, boolean allLoaded){
    int index = 0;
    int endDisplacement = 0;
    int start = 0;
    //RUN THROUGH THE EHOLW TEXT. :)
  //  System.out.println(splitWord[0] + "HÄR KANSKE?");
  //If letter increase dont look at last one,
    if(splitWord[0].equals("") || splitWord[splitWord.length-1].equals(" ")){
      start = 1;
    }
    else if(splitWord[splitWord.length-1].equals("")  || splitWord[splitWord.length-1].equals(" ")){
      endDisplacement = 1;
      if(LetterValue(splitWord[splitWord.length-2].substring(0,1)) > 0 ){
        endDisplacement = 2;
      }
      //If number is last dont look at last or second last
      else{
        endDisplacement = 3;
        if(allLoaded){
            endDisplacement = 0;
        }
      }
    }
    else if(LetterValue(splitWord[splitWord.length-1].substring(0,1)) > 0 ){
      endDisplacement = 1;
    }
    //If number is last dont look at last or second last
    else{

      endDisplacement = 2;
      if(allLoaded){
          endDisplacement = 0;
      }
    }

    for(int i = start; i < (splitWord.length-endDisplacement); i++)
    {

      if(LetterValue(splitWord[i].substring(0,1)) <= 0)
      {

          index += splitWord[i].length() + 1;
      }
      else
      {

        String s = splitWord[i];
        String tempWord = new String(s);
        //If new letter add to index.
        if(s.length() < 3){
            s = " " + s;
              if(s.length() < 3)
                  s = " " + s;
        }
        if(!currentWord.equals(s.substring(0,3))){

          currentWord = s.substring(0,3);
          //Add the placement of the word to the index.
          //Räknar ut index för de tre första karaktärerna och på det indexet i hashindex lägger vi in positionen i korpus för just de tre bokstäverna
          int indexValue = CalculateWordIndex(currentWord);

          if(hashIndex[indexValue] == 0){

            //System.out.println(splitWord[i]);
            //System.out.println(firstPointer + index);
            hashIndex[indexValue] = firstPointer + index;
                        //System.out.println(indexValue);
          }
        }
        index += splitWord[i].length() + 1;
      }
    }

    firstPointer += index;
    //Fix so first value starts at 0
    hashIndex[1] = 0;
  }

  public int CalculateWordIndex(String letters){
    int returnValue = 0;
    int checkValue = 0;

    if(letters.length() < 3){
      letters = " " + letters;
        if(letters.length() < 3)
            letters = " " + letters;
    }
    returnValue += LetterValue(letters.substring(2,3));
    checkValue = LetterValue(letters.substring(1,2));
    if(checkValue != 0){
            returnValue += 30*checkValue;
            checkValue = LetterValue(letters.substring(0,1));
      if(checkValue != 0){
              returnValue += 900*checkValue;
      }
    }
    //System.out.println("WORD " + letters);
      //System.out.println("indexvalue " + returnValue);
    return returnValue;
  }

  private int LetterValue(String word){
    int value = 0;
    if(word.equals("ö") || word.equals("Ö") ){
      return 29;
    }
    if(word.equals("ä") || word.equals("Ä") ){
      return 27;
    }
    if(word.equals("å") || word.equals("Å") ){
      return 26;
    }
    try{
      byte[] wordByte = word.getBytes("ISO-8859-1");
      value = wordByte[0];
    }catch(IOException e){
      e.printStackTrace();
    }
    if(value == 0x0020){
      return 0;
    }
    value = value - 0x0040;

    if(value > 0x0020){
      value = value - 0x0020;
    }
    return value;
  }


  public String MakeIndexWorthy(String word){
    String s = word;
    String tempWord = new String(s);
    if(tempWord.length() < 3){
      //System.out.println("word : "+ word + " " + word.length());
        tempWord = " " + tempWord;
          if(tempWord.length() < 3)
              tempWord = " " + tempWord;
    }else{
      tempWord = word.substring(0,3);
    }
    return tempWord;
  }

  public List<Long> FindAll(String word){
    return FindWordInOrdLista(word, MakeIndexWorthy(word), hashIndex[CalculateWordIndex(MakeIndexWorthy(word))]);
  }

  public List<Long> FindWordInOrdLista(String word, String indexLetters, long pointFirst){
    byte[] b;
    int byteSize = 2000;
    int index = 0;
    int countWords = 0;
    long pointStart = pointFirst;
    Scanner scannerObj = new Scanner(System.in);
    List<Long> listOutput = new ArrayList<Long>();
    b = new byte[byteSize];
    try{

      randomAccessFile.seek(pointFirst);
      randomAccessFile.read(b);
      long fileSize = randomAccessFile.length();
      String text = new String(b, "ISO-8859-1");
      String[] splitWord = text.split("\\s+");

      while(pointStart < fileSize && MakeIndexWorthy(splitWord[0]).equals(indexLetters)){
          //System.out.println(indexLetters + ":" + splitWord[0]);
          int k = 0;
          if(LetterValue(splitWord[splitWord.length-1].substring(0,1)) > 0){
            k = 3;
          }
          else if(pointStart +byteSize > fileSize){
            k = 1;
          }
          else{
            k = 2;
          }
          for(int i = 0; i < splitWord.length-k; i++){
            //Word
            //a 20000
            //a 2
            if(LetterValue(splitWord[i].substring(0,1)) > 0){
              //Number
              //System.out.println(splitWord[i] + Long.valueOf(splitWord[i+1]).longValue() + "\n");
              if(word.equals(splitWord[i])){

                  listOutput.add(Long.valueOf(splitWord[i+1]).longValue());
              }
            }
            //System.out.println(splitWord[i].length() +  " " + splitWord[i]);
            index += splitWord[i].length() + 1;
          }
          //Word

          if(MakeIndexWorthy(splitWord[splitWord.length-k]).equals(indexLetters)){
            //System.out.println(indexLetters + ":" + splitWord[splitWord.length-1]);
            pointStart += index;
            index = 0;
            randomAccessFile.seek(pointStart);
            randomAccessFile.read(b);
            text = new String(b, "ISO-8859-1");
            splitWord = text.split("\\s+");
          }
          else{
            break;
          }
      }

    }
    catch(IOException e){
      e.printStackTrace();

    }
    return listOutput;
  }



}
