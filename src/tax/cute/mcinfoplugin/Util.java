package tax.cute.mcinfoplugin;

public class Util {
    public static boolean isUuid(String s) {
        return s.replace("-","").length() == 32;
    }
}
