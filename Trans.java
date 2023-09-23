import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trans{
    
    public static void main(String[] args) {
        
        int keyLength = Integer.parseInt(args[0]);
        String key = args[1];
        String inputFile = args[2];
        String outputFile = args[3];
        String type = args[4];

        String inputString = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));

            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            inputString = sb.toString();

            reader.close();
            
        } catch (IOException e) {
           System.err.println("Input file is not valid");
           return;
        }

        if(!isInputValid(key, keyLength, inputString)){
            return;
        }

        String outputString = "";

        if(type.equalsIgnoreCase("enc")){
            outputString = encrypt(inputString, key, keyLength);
        }else if(type.equalsIgnoreCase("dec")){
            outputString = decrypt(inputString, key, keyLength);
        }else{
            System.err.println("Only enc/dec operations are supported");
            return;
        }

        try{
            FileWriter writer = new FileWriter(outputFile);
            writer.write(outputString);

            writer.close();
        }catch(IOException ex){
            System.err.println("Error while writing to output file");
            return;
        }

        //System.out.println(isInputValid("12845376", 8, "sdgs$23224"));


        /*String cipherText = encrypt("attackpostponeduntiltwoam", "3421567", 7);

        String decryptedText = decrypt("ATHNIERIPTSISORPNSOCZ", "3421567", 7);

        System.out.println(cipherText);
        System.out.println(decryptedText);*/
    }

    private static String encrypt(String plainText, String key, int keyLength){

        StringBuilder cipherText = new StringBuilder();
        int columns = keyLength, rows = Math.ceilDiv(plainText.length(), keyLength);

        char[][] charMatrix = new char[rows][columns];

        int index = 0;

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){

                if(index >= plainText.length()){
                    charMatrix[i][j] = 'z';
                    continue;
                }

                charMatrix[i][j] = plainText.charAt(index);
                index++;
            }
        }

        for(int i = 0; i < keyLength; i++){

            int col = key.charAt(i) - '0' - 1;

            for(int j = 0; j < rows; j++){

                cipherText.append(charMatrix[j][col]);
            }
        }

        return cipherText.toString();
    }

    private static String decrypt(String cipherText, String key, int keyLength){

        StringBuilder decryptedText = new StringBuilder();

        int columns = keyLength, rows = cipherText.length() / keyLength;

        char[][] charMatrix = new char[rows][columns];

        int index = 0;

        for(int i = 0; i < keyLength; i++){
           
            int col = key.charAt(i) - '0' - 1;

            for(int j = 0; j < rows; j++){

                charMatrix[j][col] = cipherText.charAt(index);

                index++;
            }
        }

         for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){

                decryptedText.append(charMatrix[i][j]);
            }
        }

        return decryptedText.toString();
    }

    private static boolean isInputValid(String key, int keyLength, String inpuString){

        if(key.length() != keyLength){
            System.err.println("<keylength> must match the length of <key>");
            return false;
        }

        if(!isKeyValid(key, keyLength)){
            System.err.println("<key> must include all digits from 1 to <keylength> with each digit occurring exactly once");
            return false;
        }

        if(!isInputFileValid(inpuString)){
            System.err.println("<inputfile> must contain only lowercase letters (a-z) or digits (0-9)");
            return false;
        }

        return true;
    }

    private static boolean isKeyValid(String key, int keyLength){

        String regex = "^(?!.*(.).*\\1)[1-" + keyLength + "]" + "{" + keyLength+ "}" + "$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(key);

        return matcher.matches();

    }

    private static boolean isInputFileValid(String inpuString){

        String regex = "^[a-z0-9]+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inpuString);

        return matcher.matches();

    }

}