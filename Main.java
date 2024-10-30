import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Main <path_to_json_file>");
            System.exit(1);
            return;
        }
        
        String jsonFilePath = args[0];

        try {
            String json = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            Tokenizer tokenizer = new Tokenizer(json);
            Parser parser = new Parser(tokenizer);
            Object result = parser.parse();
            System.out.println("Parsed Output:");
            System.out.println(result);
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Error reading or parsing JSON: " + e.getMessage());
            System.exit(1);
        }
    }
}


// public class Main {
//     public static void main(String[] args) {
//         String json = "[\"x\"],";

//         Tokenizer tokenizer = new Tokenizer(json);

//         // Token token;
//         // System.out.println("Tokens:");
//         // while ((token = tokenizer.getNextToken()).getType() != TokenType.EOF) {
//         //     System.out.println(token);
//         // }

//         Parser parser = new Parser(tokenizer);

//         Object result = parser.parse();
//         System.out.println(result);
//     }
// }
