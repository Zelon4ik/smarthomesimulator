package smarthomesimulator.presentation;

public final class Console {
    private Console() {}

    public static void title(String text) {
        System.out.println(Ansi.title(text));
    }

    public static void info(String text) {
        System.out.println(Ansi.info(text));
    }

    public static void success(String text) {
        System.out.println(Ansi.success(text));
    }

    public static void warn(String text) {
        System.out.println(Ansi.warn(text));
    }

    public static void error(String text) {
        System.out.println(Ansi.error(text));
    }

    public static void plain(String text) {
        System.out.println(text);
    }

    public static void blankLine() {
        System.out.println();
    }
}
