package com.ananotherrpg.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ananotherrpg.IQueryable;

public class IOManager {

    private static Scanner s = new Scanner(System.in);

    public enum ListType {
        BULLET, NUMBERED, ONE_LINE
    };

    public enum SelectionMethod {
        NUMBERED, TEXT
    }

    // Yes == TRUE, No == FALSE
    public static Boolean askYesOrNoQuestion(String question){ 
        println(question + " (Y/N)");

        String input;

        do{
            println("Enter Y or N");
            input = s.nextLine();
        }while(!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N"));
        
        if (input.equalsIgnoreCase("Y")) {
            return true;
        } 
        
        return false;

    }

    public static List<String> extractIQueryableListForms(List<? extends IQueryable> list) {
        return list.stream().map(IQueryable::getListForm).collect(Collectors.toList());
    }

    public static void listStrings(List<String> data, ListType type) {
        if (data.isEmpty()) {
            println("There's nothing to show");
        } else {
            if (type == ListType.BULLET) {
                for (int i = 0; i < data.size(); i++) {
                    println("*" + data.get(i));
                }
            } else if (type == ListType.NUMBERED) {
                for (int i = 0; i < data.size(); i++) {
                    println((i + 1) + ". " + data.get(i));
                }
            } else if (type == ListType.ONE_LINE) {
                for (int i = 0; i < data.size() - 1; i++) {
                    print(data.get(i) + ", ");
                }
                println(data.get(data.size() - 1));
            }
        }
    }

    public static void listIQueryables(List<? extends IQueryable> list, ListType type) {
        listStrings(extractIQueryableListForms(list), type);
    }

    public static Optional<String> queryUserInputAgainstStrings(List<String> data, SelectionMethod selectionMethod, boolean canExit) {
        if (data.isEmpty()) {
            return Optional.empty();
        } else {
            String validatedInput = "";
            if (selectionMethod == SelectionMethod.NUMBERED) {
                print("Enter choice number: ");

                String[] intToStringMap = data.toArray(new String[0]);

                int input = -1;

                while ((!(input >= 1) && (input <= intToStringMap.length))) {
                    String inputData = s.nextLine().trim();
                    if (inputData.equalsIgnoreCase("Exit") && canExit) {
                        return Optional.empty();
                    } else {
                        try {
                            input = Integer.parseInt(inputData.trim());
                        } catch (NumberFormatException e) {
                            println("That's not a valid option!");
                        }
                    }
                }
                validatedInput = intToStringMap[input - 1];
            } else if (selectionMethod == SelectionMethod.TEXT) {
                String input = s.nextLine().trim();

                while (!data.contains(input)) {
                    if (input.equalsIgnoreCase("Exit") && canExit) {
                        return Optional.empty();
                    }
                    println("That's not a valid option");
                    input = s.nextLine().trim();
                }

                validatedInput = input;
            }

            return Optional.of(validatedInput);
        }
    }

    public static <T extends IQueryable> Optional<T> queryUserInputAgainstIQueryable(List<T> data,
            SelectionMethod selectionMethod, boolean canExit) {
        Map<String, T> options = data.stream().collect(Collectors.toMap(T::getName, Function.identity()));

        List<String> optionNames = data.stream().map(T::getName).collect(Collectors.toList());

        Optional<String> optionalData = queryUserInputAgainstStrings(new ArrayList<String>(optionNames),
                selectionMethod, canExit);

        if (optionalData.isPresent()) {
            try {

                return Optional.of(options.get(optionalData.orElseThrow()));
            } catch (NoSuchElementException e) {
                println("Catastrophic error in user query");
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }

    }

    public static <T> Optional<T> queryUserInputAgainstCustomMap(List<T> data, Function<? super T, ? extends String> keyMapper,
            SelectionMethod selectionMethod, boolean canExit) {
        Map<String, T> options = data.stream().collect(Collectors.toMap(keyMapper, Function.identity()));

        Optional<String> optionalData = queryUserInputAgainstStrings(new ArrayList<String>(options.keySet()),
                selectionMethod, canExit);

        if (optionalData.isPresent()) {
            try {

                return Optional.of(options.get(optionalData.orElseThrow()));
            } catch (NoSuchElementException e) {
                println("Catastrophic error in user query");
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }

    }

    public static Optional<String> listAndQueryUserInputAgainstStrings(List<String> data, ListType type,
            SelectionMethod method, boolean canExit) {
        listStrings(data, type);
        return queryUserInputAgainstStrings(data, method, canExit);
    }

    public static <T extends IQueryable> Optional<T> listAndQueryUserInputAgainstIQueryables(List<T> data, ListType type,
            SelectionMethod method,boolean canExit) {
        listIQueryables(data, type);
        return queryUserInputAgainstIQueryable(data, method, canExit);
    }

    public static void println(String text) {
        System.out.println(text);
    }

    public static void print(String text) {
        System.out.print(text);
    }
    
}