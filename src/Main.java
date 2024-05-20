import java.util.Scanner;

public class Main {
    private static final int[] arabicValues = {100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] romanValues = {"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Введите арифметическое выражение (или 'exit' для выхода):");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                String result = calc(input);
                System.out.println("Результат: " + result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String calc(String input) throws Exception {

        input = input.replaceAll("\\s+", "");


        char operator = findOperator(input);

        String[] parts = input.split("[+\\-*/]");
        if (parts.length != 2) {
            throw new Exception("Неправильный формат выражения");
        }

        String operand1 = parts[0];
        String operand2 = parts[1];


        boolean isRoman = isRoman(operand1) && isRoman(operand2);
        boolean isArabic = isArabic(operand1) && isArabic(operand2);

        if (!isRoman && !isArabic) {
            throw new Exception("Используйте либо только арабские, либо только римские числа");
        }

        int num1, num2;
        if (isRoman) {
            num1 = romanToInt(operand1);
            num2 = romanToInt(operand2);
        } else {
            num1 = Integer.parseInt(operand1);
            num2 = Integer.parseInt(operand2);
        }

        if (num1 < 1 || num1 > 10 || num2 < 1 || num2 > 10) {
            throw new Exception("Числа должны быть от 1 до 10 включительно");
        }


        int result;
        switch (operator) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                if (num2 == 0) {
                    throw new Exception("Деление на ноль невозможно");
                }
                result = num1 / num2;
                break;
            default:
                throw new Exception("Неподдерживаемая операция");
        }
        if (isRoman) {
            if (result < 1) {
                throw new Exception("Результат римского числа не может быть меньше единицы");
            }
            return intToRoman(result);
        } else {
            return String.valueOf(result);
        }
    }

    private static char findOperator(String input) throws Exception {
        for (char c : input.toCharArray()) {
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                return c;
            }
        }
        throw new Exception("Оператор не найден");
    }

    private static boolean isRoman(String input) {
        return input.matches("[IVXLCDM]+");
    }

    private static boolean isArabic(String input) {
        return input.matches("\\d+");
    }

    private static int romanToInt(String roman) {
        int result = 0;
        int i = 0;
        while (i < roman.length()) {
            String current = roman.substring(i, i + 1);
            String next = (i + 1 < roman.length()) ? roman.substring(i + 1, i + 2) : "";

            int currentIndex = indexOfRoman(current);
            int nextIndex = indexOfRoman(next);

            if (nextIndex != -1 && arabicValues[currentIndex] < arabicValues[nextIndex]) {
                result += arabicValues[nextIndex] - arabicValues[currentIndex];
                i += 2;
            } else {
                result += arabicValues[currentIndex];
                i++;
            }
        }
        return result;
    }

    private static int indexOfRoman(String roman) {
        for (int i = 0; i < romanValues.length; i++) {
            if (romanValues[i].equals(roman)) {
                return i;
            }
        }
        return -1;
    }

    private static String intToRoman(int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arabicValues.length; i++) {
            while (num >= arabicValues[i]) {
                sb.append(romanValues[i]);
                num -= arabicValues[i];
            }
        }
        return sb.toString();
    }
}
