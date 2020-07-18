import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ShellUtils {

    public static void execShell(String shell){
        try {
            Runtime.getRuntime().exec(shell);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static void replaceSelected(String replaceWith, String replacedContent) {

        Path path = Paths.get(System.getProperty("user.dir") + "/sevenNodeDemo");
        Charset charset = StandardCharsets.UTF_8;
        try {

            String content = new String(Files.readAllBytes(path), charset);
            content = content.replaceAll(replacedContent, replaceWith);
            Path path0 = Files.write(path, content.getBytes(charset));
            System.out.println(path0.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getrelativePath() {
        String relativelyPath = System.getProperty("user.dir");
        //System.out.println(relativelyPath);
        return relativelyPath;
    }
}
