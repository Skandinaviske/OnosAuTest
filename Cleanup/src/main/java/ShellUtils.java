import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//Shell command utils.

public class ShellUtils {

    //Execute shell commands under current user
    public static void execShell(String shell){
        try {
            Runtime.getRuntime().exec(shell);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Return the project path
    public static void replaceSelected(String replaceWith, String replacedContent) throws IOException {

        Path path = Paths.get(System.getProperty("user.dir") + "/oneNodeDemo");
        Charset charset = StandardCharsets.UTF_8;

        String content = new String(Files.readAllBytes(path), charset);
        content = content.replaceAll(replacedContent, replaceWith);
        Files.write(path, content.getBytes(charset));
    }

    //Return the project path
    public static String getrelativePath() {
        String relativelyPath = System.getProperty("user.dir");
        //System.out.println(relativelyPath);
        return relativelyPath;
    }
}
