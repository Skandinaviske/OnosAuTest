public class test {
    public static void main(String[] args) throws Exception {

        String projectPath = ShellUtils.getrelativePath();
        ShellUtils.execShell("cp "+ projectPath + "/src/main/resources/OneNodeDemo "+projectPath+"/src/main/resources/oneNodeDemo");
        ShellUtils.replaceSelected("????","IPadd1");
        ShellUtils.replaceSelected("Crazy","IPadd2");
    }
}
