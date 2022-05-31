package main;

import java.util.*;

public class Main {
    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    public static int precedence(char op) {
        if (op == '+' || op == '-') {
            return 11;
        } else if (op == '*' || op == '/') {
            return 12;
        } else if (op == '^') {
            return 13;
        }
        return 0;
    }

    public static boolean hasLeftAssociativity(char op) {
        if (isOperator(op)) {
            if (op == '^') {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static String ShuntingYardAlg(String operation) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < operation.length(); i++) {
            char c = operation.charAt(i);

            if (Character.isDigit(c)) {
                postfix.append(c + " ");
            } else if (precedence(c) > 0) {
                while (!stack.isEmpty() && stack.peek() != '(' &&
                        (precedence(c) < precedence(stack.peek()) ||
                                (precedence(c) == precedence(stack.peek()) && hasLeftAssociativity(stack.peek())))) {

                    postfix.append(stack.pop() + " ");

                }
                stack.push(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (stack.peek() != '(') {
                    postfix.append(stack.pop() + " ");
                }
                if (stack.isEmpty()) {
                    return "Error: invalid parenthesis";
                }
                stack.pop(); // = '('
            }

        }

        while (!stack.isEmpty()) {
            char o = stack.pop();
            postfix.append(o + " ");
            if (o == '(') {
                return "Error: invalid parenthesis";
            }
        }

        return postfix.toString();
    }

    public static String postfixToInfix(String postfix) {
        Stack<String> stack = new Stack<>();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < postfix.length(); i++) {
            String c = Character.toString(postfix.charAt(i));
            if (c.equals(" ")) {
                continue;
            }
            if (Character.isDigit(c.charAt(0))) {
                stack.push(c);
            } else {
                if (stack.size() < 2) {
                    return "Error: invalid postfix expression";
                }
                String op1 = stack.pop();
                String op2 = stack.pop();

                String res = "(" + op2 + c + op1 + ")";
                result.delete(0, result.length());
                result.append(res);
                stack.push(res);
            }
        }
        result.delete(0, result.length());
        result.append(stack.pop());

        if (!stack.isEmpty()) {
            return "Error: invalid postfix expression";
        }

        return result.toString();
    }

    public static int postfixEvaluation(String postfix) {
        Stack<String> stack = new Stack<>();
        int result;

        for (int i = 0; i < postfix.length(); i++) {
            char c = postfix.charAt(i);
            if (c == ' ') {
                continue;
            }
            if (Character.isDigit(c)) {
                stack.push(c + "");
            } else {
                if (stack.size() < 2) {
                    System.out.println("Error: invalid postfix expression - stack.size() < 2");
                    return -1;
                }
                int op1 = Integer.parseInt(stack.pop());
                int op2 = Integer.parseInt(stack.pop());

                switch (c) {
                    case '+':
                        result = op2 + op1;
                        break;
                    case '-':
                        result = op2 - op1;
                        break;
                    case '*':
                        result = op2 * op1;
                        break;
                    case '/':
                        result = op2 / op1;
                        break;
                    case '^':
                        result = (int) Math.pow(op2, op1);
                        break;
                    default:
                        result = -1;
                }

                stack.push(result + "");
            }
        }
        result = Integer.parseInt(stack.pop());

        if (!stack.isEmpty()) {
            System.out.println("Error: invalid postfix expression - stack is not empty; stack.size = " + stack.size());
            return -1;
        }

        return result;
    }

    public static ArrayList<Integer> radixSortAsc(ArrayList<Integer> nums) {

        Queue<String> queueNums = new LinkedList<>();
        int maxNumLength = String.valueOf(Collections.max(nums)).length();

        // adding zeros before numbers where needed:
        for (Integer num : nums) {
            StringBuilder zeros = new StringBuilder();
            if (String.valueOf(num).length() < maxNumLength) {
                for (int j = 0; j < maxNumLength - String.valueOf(num).length(); j++) {
                    zeros.append("0");
                }
            }
            queueNums.offer(zeros.toString() + num);
        }


        // 10 queues for each digit
        ArrayList<Queue<String>> queuesByDigit = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            queuesByDigit.add(new LinkedList<>());
        }


        for (int digitPosition = 0; digitPosition < maxNumLength; digitPosition++) { // add to queues for each digit location

            // clear digit stacks before each loop
            for (int i = 0; i < 10; i++) {
                while (!queuesByDigit.get(i).isEmpty()) {
                    queuesByDigit.get(i).remove();
                }
            }

            // put each number in the corresponding digit queue
            for (String number : queueNums) {
                char digit = number.charAt(maxNumLength - 1 - digitPosition); // check digit value
                queuesByDigit.get(Character.getNumericValue(digit)).offer(number); // add number to proper queue
            }

            // clear and rewrite the final sorted queue
            while (!queueNums.isEmpty()) {
                queueNums.remove();
            }
            for (int i = 0; i < 10; i++) {
                for (String number : queuesByDigit.get(i)) {
                    queueNums.offer(number);
                }
            }

        }


        // final solution:
        ArrayList<Integer> sortedNums = new ArrayList<>();
        while (!queueNums.isEmpty()) {
            sortedNums.add(Integer.parseInt(queueNums.poll()));
        }

        return sortedNums;
    }


    public static ArrayList<Integer> radixSortDesc(ArrayList<Integer> nums) {
        // DIFFERENCE:
        // queueNums was changed to Stack<String> stackNums

        Stack<String> stackNums = new Stack<>();
        int maxNumLength = String.valueOf(Collections.max(nums)).length();

        // adding zeros before numbers where needed:
        for (Integer num : nums) {
            StringBuilder zeros = new StringBuilder();
            if (String.valueOf(num).length() < maxNumLength) {
                for (int j = 0; j < maxNumLength - String.valueOf(num).length(); j++) {
                    zeros.append("0");
                }
            }
            stackNums.push(zeros.toString() + num);
        }


        // 10 queues for each digit
        ArrayList<Queue<String>> queuesByDigit = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            queuesByDigit.add(new LinkedList<>());
        }


        for (int digitPosition = 0; digitPosition < maxNumLength; digitPosition++) { // add to queues for each digit location

            // clear digit stacks before each loop
            for (int i = 0; i < 10; i++) {
                while (!queuesByDigit.get(i).isEmpty()) {
                    queuesByDigit.get(i).remove();
                }
            }

            // put each number in the corresponding digit queue
            for (String number : stackNums) {
                char digit = number.charAt(maxNumLength - 1 - digitPosition); // check digit value
                queuesByDigit.get(Character.getNumericValue(digit)).offer(number); // add number to proper queue
            }

            // clear and rewrite the final sorted queue
            while (!stackNums.isEmpty()) {
                stackNums.pop();
            }
            for (int i = 0; i < 10; i++) {
                for (String number : queuesByDigit.get(i)) {
                    stackNums.push(number);
                }
            }

        }

        // final solution:
        ArrayList<Integer> sortedNums = new ArrayList<>();
        while (!stackNums.isEmpty()) {
            sortedNums.add(Integer.parseInt(stackNums.pop()));
        }

        return sortedNums;
    }


    public static void main(String[] args) {

        System.out.println("\n\t1. Evaluatorul de expresii matematice");
        System.out.println("\n-> intoarce rezultatul corect pentru orice expresie matematica, introdusa in forma infixata\n");

        System.out.println("\nTest 1\n");
        String infix = "3+(2+1)*2^3^2-8/(5-1*2/2)";
        System.out.println("Forma infixata: " + infix + "\n");

        // Postfix Form - Shunting-Yard Algorithm
        String postfix = ShuntingYardAlg(infix);
        System.out.println("Algoritm Shunting-Yard: infixata -> postfixata : " + postfix);
        System.out.println("\npostfixata -> infixata : " + postfixToInfix(postfix));
        System.out.println("\nEvaluarea expresiei postfixate: " + postfixEvaluation(postfix));
        System.out.println("\nAsadar, evaluarea expresiei infixate este: " + postfixEvaluation(ShuntingYardAlg(infix)));

        Scanner sc = new Scanner(System.in);
        System.out.println("\n\nVom evalua expresii infixate date de la tastatura.");

        while (true) {
            System.out.println("\nIntroduceti o expresie infixata: ");
            infix = sc.next();
            postfix = ShuntingYardAlg(infix);
            System.out.println("\t-> forma postfixata: " + postfix);
            System.out.println("\t-> rezultat: " + postfixEvaluation(postfix));
            System.out.println("\nDoriti sa mai testati o expresie? (daca nu, tastati \"nu\"):");
            String s = sc.next();
            if (s.equals("nu")) {
                break;
            }
        }


        System.out.println("\n\n\t2. Radix Sort");
        System.out.println("\n-> sorteaza numere naturale\n");
        System.out.println("\nTest: [1000, 4, 25, 319, 88, 51, 3430, 8471, 701, 1, 2989, 657, 713]");

        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1000, 4, 25, 319, 88, 51, 3430, 8471, 701, 1, 2989, 657, 713));

        System.out.println("\nSortare in ordine ascendenta:");
        ArrayList<Integer> sortedAsc = radixSortAsc(numbers);
        System.out.println();
        for (int i = 0; i < sortedAsc.size(); i++) {
            System.out.print(sortedAsc.get(i) + " ");
        }

        System.out.println("\n\nSortare in ordine descendenta:");
        ArrayList<Integer> sortedDesc = radixSortDesc(numbers);
        System.out.println();
        for (int i = 0; i < sortedDesc.size(); i++) {
            System.out.print(sortedDesc.get(i) + " ");
        }


        System.out.println("\n\nVom ordona numere date de la tastatura.");

        while (true) {
            numbers.clear();
            sortedAsc.clear();
            sortedDesc.clear();
            System.out.print("\nIntroduceti un sir de numere (apasand Enter dupa fiecare numar). ");
            System.out.println("Cand ati terminat, tastati o litera:");
            while (sc.hasNextInt()) {
                int n = sc.nextInt();
                numbers.add(n);
            }
            sc.next();

            System.out.println("\nSortare in ordine ascendenta:");
            sortedAsc = radixSortAsc(numbers);
            for (int i = 0; i < sortedAsc.size(); i++) {
                System.out.print(sortedAsc.get(i) + " ");
            }

            System.out.println("\n\nSortare in ordine descendenta:");
            sortedDesc = radixSortDesc(numbers);
            for (int i = 0; i < sortedDesc.size(); i++) {
                System.out.print(sortedDesc.get(i) + " ");
            }
            System.out.println();

            System.out.println("\nDoriti sa mai ordonati un sir? (daca nu, tastati \"nu\"):");

            String s = sc.next();
            if (s.equals("nu")) {
                break;
            }
        }


        System.out.println("\n\nProgram incheiat!");
        sc.close();


    }
}
