package smarthomesimulator.presentation.consoleui;

import smarthomesimulator.presentation.Ansi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;

public final class ConsoleUI {
    private final Scanner scanner;

    public ConsoleUI(Scanner scanner) {
        this.scanner = Objects.requireNonNull(scanner, "scanner");
    }

    public void hr() {
        System.out.println(Ansi.wrap(Ansi.DIM, "------------------------------------------------------------"));
    }

    public void headline(String text) {
        System.out.println(Ansi.title(text));
    }

    public void info(String text) {
        System.out.println(Ansi.info(text));
    }

    public void success(String text) {
        System.out.println(Ansi.success(text));
    }

    public void warn(String text) {
        System.out.println(Ansi.warn(text));
    }

    public void error(String text) {
        System.out.println(Ansi.error(text));
    }

    public String prompt(String label, boolean required) {
        return prompt(label, required, Function.identity(), null);
    }

    public String prompt(String label, boolean required, Function<String, String> normalizer, Function<String, String> validator) {
        Objects.requireNonNull(normalizer, "normalizer");

        while (true) {
            System.out.print(Ansi.wrap(Ansi.CYAN, label) + (required ? Ansi.wrap(Ansi.DIM, " *") : "") + ": ");
            String raw = scanner.nextLine();
            String value = normalizer.apply(raw == null ? "" : raw);

            if (required && value.trim().isEmpty()) {
                error("Значення обов'язкове.");
                continue;
            }

            if (validator != null) {
                String err = validator.apply(value);
                if (err != null) {
                    error(err);
                    continue;
                }
            }

            return value;
        }
    }

    public String promptOptional(String label) {
        return prompt(label, false, Function.identity(), null);
    }

    public double promptDouble(String label, boolean required, Double minExclusive) {
        while (true) {
            String s = prompt(label, required);
            if (!required && s.trim().isEmpty()) {
                return Double.NaN;
            }
            try {
                double v = Double.parseDouble(s.trim());
                if (minExclusive != null && !(v > minExclusive)) {
                    error("Значення має бути > " + minExclusive);
                    continue;
                }
                return v;
            } catch (NumberFormatException ex) {
                error("Будь ласка, введіть коректне число.");
            }
        }
    }

    public long promptLong(String label, boolean required, Long minInclusive) {
        while (true) {
            String s = prompt(label, required);
            if (!required && s.trim().isEmpty()) {
                return Long.MIN_VALUE;
            }
            try {
                long v = Long.parseLong(s.trim());
                if (minInclusive != null && v < minInclusive) {
                    error("Значення має бути >= " + minInclusive);
                    continue;
                }
                return v;
            } catch (NumberFormatException ex) {
                error("Будь ласка, введіть коректне ціле число.");
            }
        }
    }

    public boolean confirm(String label, boolean defaultYes) {
        String suffix = defaultYes ? " [Т/н]" : " [т/Н]";
        while (true) {
            System.out.print(Ansi.wrap(Ansi.CYAN, label) + Ansi.wrap(Ansi.DIM, suffix) + ": ");
            String s = scanner.nextLine();
            if (s == null || s.trim().isEmpty()) {
                return defaultYes;
            }
            String v = s.trim().toLowerCase();
            if (v.equals("т") || v.equals("так") || v.equals("y") || v.equals("yes")) return true;
            if (v.equals("н") || v.equals("ні") || v.equals("n") || v.equals("no")) return false;
            error("Будь ласка, відповідайте т/н.");
        }
    }

    public int select(String title, List<String> options) {
        Objects.requireNonNull(options, "options");
        if (options.isEmpty()) {
            throw new IllegalArgumentException("Опції не можуть бути порожніми");
        }
        headline(title);
        for (int i = 0; i < options.size(); i++) {
            System.out.println(Ansi.wrap(Ansi.DIM, String.format("%2d", i + 1)) + "  " + options.get(i));
        }
        while (true) {
            String s = prompt("Виберіть (1-" + options.size() + ")", true);
            try {
                int idx = Integer.parseInt(s.trim());
                if (idx < 1 || idx > options.size()) {
                    error("Поза межами.");
                    continue;
                }
                return idx - 1;
            } catch (NumberFormatException ex) {
                error("Будь ласка, введіть число.");
            }
        }
    }

    public List<Integer> multiSelect(String title, List<String> options) {
        Objects.requireNonNull(options, "options");
        headline(title);
        if (options.isEmpty()) {
            warn("Немає доступних опцій.");
            return List.of();
        }
        for (int i = 0; i < options.size(); i++) {
            System.out.println(Ansi.wrap(Ansi.DIM, String.format("%2d", i + 1)) + "  " + options.get(i));
        }
        info("Введіть номери через кому (наприклад: 1,3,4). Введіть порожнє для відсутності.");
        while (true) {
            String s = promptOptional("Виберіть");
            if (s.trim().isEmpty()) {
                return List.of();
            }
            String[] parts = s.split(",");
            List<Integer> indices = new ArrayList<>();
            boolean ok = true;
            for (String p : parts) {
                try {
                    int idx = Integer.parseInt(p.trim());
                    if (idx < 1 || idx > options.size()) {
                        ok = false;
                        break;
                    }
                    indices.add(idx - 1);
                } catch (NumberFormatException ex) {
                    ok = false;
                    break;
                }
            }
            if (!ok) {
                error("Невірний вибір. Спробуйте знову.");
                continue;
            }
            return indices;
        }
    }
}
