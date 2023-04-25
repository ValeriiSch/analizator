package com.company;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static final BlockingQueue<String> texts1 = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> texts2 = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> texts3 = new ArrayBlockingQueue<>(100);
    public static final int lenght_text = 100_000;
    public static final int count_text = 10_000;

    public static void main(String[] args) {
        String[] text = new String[count_text];

        new Thread(() -> {
            for (int i = 0; i < count_text; i++) {
                text[i] = generateText("abc", lenght_text);
                try {
                    texts1.put(text[i]);
                    texts2.put(text[i]);
                    texts3.put(text[i]);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        new Thread(() -> calc_max('a', texts1)).start();
        new Thread(() -> calc_max('b', texts2)).start();
        new Thread(() -> calc_max('c', texts3)).start();
    }

    public static void calc_max(char letter, BlockingQueue<String> texts) {
        int counter = 0;
        int counterMax = 0;
        for (int i = 0; i < count_text; i++) {
            try {
                String text = texts.take();
                for (int j = 0; j < text.length(); j++) {
                    if (text.charAt(j) == letter) {
                        ++counter;
                    }
                }
                if (counter > counterMax) {
                    counterMax = counter;
                }
                counter = 0;
            } catch (InterruptedException e) {
                return;
            }
        }
        System.out.println("максимальное количество символов " + letter + ": " + counterMax);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}

