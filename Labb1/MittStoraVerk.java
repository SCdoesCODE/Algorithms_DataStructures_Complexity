import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.EOFException;
public class MittStoraVerk{
  private long[] hashIndex;
  private static RandomAccessFile randomAccessFile;
  private String currentWord;
  private long firstPointer;
  public MittStoraVerk(){
    hashIndex = new long[27000];
    firstPointer = 0;
    currentWord = "";
    int startPos = 0;
    try{
      randomAccessFile = new RandomAccessFile("/var/tmp/ut", "r");
      byte[] b = new byte[100000];
      //första fallet när vi tar in den första chunken så kollar vi bara i slutet att vi inte slutar på ett ord
      randomAccessFile.seek(firstPointer);
      randomAccessFile.read(b);

      String text = new String(b, "ISO-8859-1");

      String[] splitWord = text.split("\\W+");
      CreateHashIndex(splitWord, 0);

      while(true){
        try{
          firstPointer += 75000;

          randomAccessFile.seek(firstPointer);
          randomAccessFile.read(b);
          text = new String(b, "ISO-8859-1");

          splitWord = text.split("\\W+");
          if(splitWord[0].length() < 3){
              splitWord[0] = " " + splitWord[0];
                if(splitWord[0].length() < 3)
                    splitWord[0] = " " + splitWord[0];

          }
          if(LetterValue(splitWord[0])>0){
            startPos = 2;
          }
          else{
            startPos = 1;
          }
          CreateHashIndex(splitWord, startPos);

        }catch(EOFException eof){
            break;
        }

      }

      for(long i : hashIndex){

      //  if(i != 0)
          //System.out.pfprintrintln("hashindex " + i);
      }
    }catch(IOException e){
      e.printStackTrace();
    }
  }

  private void CreateHashIndex(String[] splitWord, int startPos){
    int index = 0;

    //RUN THROUGH THE EHOLW TEXT. :)

    for(int i = startPos; i < splitWord.length-3; i += 2)
    {

      //If new letter add to index.
      if(splitWord[i].length() < 3){
          splitWord[i] = " " + splitWord[i];
            if(splitWord[i].length() < 3)
                splitWord[i] = " " + splitWord[i];

      }
      if(!currentWord.equals(splitWord[i].substring(0,3))){
        currentWord = splitWord[i].substring(0,3);
        //Add the placement of the word to the index.
        //Räknar ut index för de tre första karaktärerna och på det indexet i hashindex lägger vi in positionen i korpus för just de tre bokstäverna
        int indexValue = CalculateWordIndex(splitWord[i]);
        if(indexValue < 0){
          i++;
        }
        else {
          //Long longIndex = new Long(index);
          //Long longIndex = Long.valueOf(index);
          System.out.println("ord " + splitWord[i]);
          hashIndex[indexValue] = firstPointer + index;
        }
      }
      //index += ord + mellanrum, nummer + ny rad

      index += splitWord[i].length() + 1 + splitWord[i+1].length() + 1;
    }
  }

  private int CalculateWordIndex(String letters){
    int returnValue = 0;
    int checkValue = 0;
    /*
    if(letters.length() < 3){
      letters = " " + letters;
        if(letters.length() < 3)
            letters = " " + letters;
    }*/
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
}
