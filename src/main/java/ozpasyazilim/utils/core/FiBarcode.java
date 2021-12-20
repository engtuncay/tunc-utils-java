package ozpasyazilim.utils.core;

public class FiBarcode
{
  public FiBarcode()
  {
  }
  
  public static String Code128b( String DataToEncode )
  {
      char C128_Start = (char)204;
      char C128_Stop = (char)206;
      String Printable_string = "";
      String temp = "";
      char CurrentChar;
      int CurrentValue=0;
      int weightedTotal=0;
      int CheckDigitValue=0;
      char C128_CheckDigit='w';

      DataToEncode = DataToEncode.trim();
      weightedTotal = ((int)C128_Start) - 100;
      for( int i = 1; i <= DataToEncode.length(); i++ )
      {
         //get the value of each character
         CurrentChar = DataToEncode.charAt(i-1);
         if( ((int)CurrentChar) < 135 )
            CurrentValue = ((int)CurrentChar) - 32;
         if( ((int)CurrentChar) > 134 )
            CurrentValue = ((int)CurrentChar) - 100;
  
         CurrentValue = CurrentValue * i;
         weightedTotal = weightedTotal + CurrentValue;
      //added by tb, replaces the spaces with the 194 character
         if (CurrentChar == ' ')
         {
                      CurrentChar = (char) 194;
         }
                      temp = temp + CurrentChar;
      }
     //divide the WeightedTotal by 103 and get the remainder,
     //this is the CheckDigitValue
     CheckDigitValue = weightedTotal % 103;
     if( (CheckDigitValue < 95) && (CheckDigitValue > 0) )
       C128_CheckDigit = (char)(CheckDigitValue + 32);
     if( CheckDigitValue > 94 )
       C128_CheckDigit = (char)(CheckDigitValue + 100);
     if( CheckDigitValue == 0 ){
       C128_CheckDigit = (char)194;
       }
  
    //Printable_string = C128_Start + DataToEncode + C128_CheckDigit + C128_Stop + " ";
    Printable_string = C128_Start + temp + C128_CheckDigit + C128_Stop + " ";
    return Printable_string;
  }
}
