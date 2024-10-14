package frames;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileValidator {
    public static boolean isValidDataFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            line = br.readLine();
            if (line == null || !line.matches("\\d+")) {
                return false; 
            }
            
            int numberOfVertices = Integer.parseInt(line.trim());
            
            if (numberOfVertices <= 0) {
                return false; 
            }
         
            while ((line = br.readLine()) != null){
                String[] parts = line.trim().split("\\s+");
                
                if (parts.length != 3) {
                    return false;
                }
                
                if (!isNumeric(parts[0])){
                    return false;
                } 

                int weight = Integer.parseInt(parts[0]);
                
                if (weight < 0) {
                    return false; 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (NumberFormatException e) {
            return false; 
        }
        
        return true; 
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false; 
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
