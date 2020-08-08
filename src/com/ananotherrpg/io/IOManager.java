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
/**
 * Manages all printing to the console, and getting user input from the console
 */
public class IOManager {

    private static Scanner s = new Scanner(System.in);

    /**
     * Different ways to display a list.
     */
    public enum ListType {
        BULLET, NUMBERED, ONE_LINE
    };

    /**
     * Different methods of user selection
     */
    public enum SelectionMethod {
        NUMBERED, TEXT
    }

    /**
     * Asks a yes or no question from the user
     * @param question The question to be asked
     * @return true if YES, false if NO
     */
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

    
    private static List<String> extractIQueryableListForms(List<? extends IQueryable> list) {
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
                String[] intToStringMap = data.toArray(new String[0]);

                int input = -1;

                boolean validInput = false;
                while (!validInput) {
                    print("Enter choice number: ");
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

                    if(((input >= 1) && (input <= intToStringMap.length))){
                        validInput = true;
                    }else{
                        println("That's not a valid option!");
                    }
                }
                validatedInput = intToStringMap[input - 1];
            } else if (selectionMethod == SelectionMethod.TEXT) {
                String input = s.nextLine().trim();

                // Looks for a match ignoring case
                while (!data.stream().anyMatch(input::equalsIgnoreCase)) {
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

                return Optional.of(options.get(optionalData.get()));
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
        List<String> stringData = data.stream().map(keyMapper).collect(Collectors.toList());
        Map<String, T> options = data.stream().collect(Collectors.toMap(keyMapper, Function.identity()));

        Optional<String> optionalData = queryUserInputAgainstStrings(stringData,
                selectionMethod, canExit);

        if (optionalData.isPresent()) {
            try {

                return Optional.of(options.get(optionalData.get()));
            } catch (NoSuchElementException e) {
                println("Catastrophic error in user query");
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }

    }

    public static <T> Optional<T> listAndQueryUserInputAgainstCustomMap(Map<String, T> customMap, ListType type,
            SelectionMethod selectionMethod, boolean canExit) {
                List<String> stringData = new ArrayList<String>(customMap.keySet());

                listStrings(stringData, type);
                Optional<String> optionalData = queryUserInputAgainstStrings(stringData,
                        selectionMethod, canExit);

                if (optionalData.isPresent()) {
                    try {

                        return Optional.of(customMap.get(optionalData.get()));
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

    
    public static <T> Optional<T> listAndQueryUserInputAgainstCustomMap(List<T> data, Function<? super T, ? extends String> keyMapper,
    ListType type, SelectionMethod selectionMethod, boolean canExit) {
        List<String> stringData = data.stream().map(keyMapper).collect(Collectors.toList());
        listStrings(stringData, type);

        return queryUserInputAgainstCustomMap(data, keyMapper, selectionMethod, canExit);

    }

    public static void println(String text) {
        System.out.println(text);
    }

    public static void print(String text) {
        System.out.print(text);
    }

	public static String getInput(String input) {
        IOManager.println(input);
		return s.nextLine().trim();
	}
    
}