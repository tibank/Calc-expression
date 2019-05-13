package hillel.calc.validator;

import hillel.calc.operations.Operation;
import hillel.calc.tokens.Token;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class SimpleValidator extends Validator {

    /**
     * check convert operand from string to double
     */
    private boolean isOperandValidNumber(String str) {
        try {
            double resultParse = Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * check count of parentheses
     */
    private boolean countOfParentheses(List<Token> list) {
        boolean result = true;
        Deque<Character> stack = new ArrayDeque<>();    // stack for checking opened and closed Parentheses
        String valueToken;
        int countOpened = 0;    // count opened Parentheses
        int position = 0;

        for (Token token : list) {
            valueToken = token.getValue();
            if (token.getClass().getSimpleName().equals("Token")) {
                if (valueToken.equals("(")) {
                    stack.addLast('(');
                } else if (valueToken.equals(")")) {
                    if (stack.isEmpty()) {
                        // error -  redundant Parentheses
                        System.out.println("Log: redundant Parentheses (token# " + position);
                        result = false;
                    } else {
                        stack.removeLast();
                    }
                }
            }
            position++;
        }

        if (!stack.isEmpty()) {
            System.out.println("Лишняя открывающая скобка");
            result = false;
        }

        return result;
    }

    /**
     * check operands - numbers of expression
     */
    private boolean checkOperands(List<Token> list) {
        boolean result = true;

        for (Token token : list) {
            if (token.getClass().getSimpleName().equals("Operand")) {
                if (!isOperandValidNumber(token.getValue())) {
                    // error of convert number in string to double
                    System.out.println("Operand " + token.getValue() + " isn't correct");
                    result = false;
                }
            }
        }

        return result;
    }

    /**
     * check function in the expression - if function contains
     * in the list of avaible function
     */
    private boolean checkFunctions(List<Token> list) {
        boolean result = true;
        String typeToken, valueToken;
        String lastStackToken = "";
        Deque<Token> stack = new ArrayDeque<>();

        for (Token token : list) {
            valueToken = token.getValue();
            typeToken = token.getClass().getSimpleName();

            if (typeToken.equals("Operation")) {
                // check existance this function
                if (!getMapFunc().containsKey(valueToken)) {
                    System.out.println("There is no function in list of allowable functions" + valueToken);
                    result = false;
                }
            }
        }

        return result;
    }

    /**
     * this nethod validate expression
     * check Parentheses, check numbers (every number is correct if it has no illegable symbol, one
     * digital point if exists, check operates and functions
     */
    //@Override
    public boolean validate() {
        Operation currentOperation;
        String typeToken, valueToken;
        Token prevToken = null;
        boolean result = true;
        List<Token> list = getListExp();          // original array
        List<Token> resultList = new ArrayList<Token>();    // correct array, token 'Operation' change on certain operations

        // check parentheses
        if (!countOfParentheses(list)) {
            return false;
        }

        if (!checkOperands(list)) {
            return false;
        }

        for (Token token : list) {
            typeToken = token.getClass().getSimpleName();
            valueToken = token.getValue();
            if (typeToken.equals("Operand")) {
                resultList.add(token);
            } else if (typeToken.equals("Token")) {
                if (valueToken.equals("(") || valueToken.equals(")")) {
                    resultList.add(token);
                }
            } else if (typeToken.equals("Operation")) {
                // check existance operation, if this is a first element of array or before it there is opened bracket '(',
                // it means it is sign of number unary operation - add symbol "U"
                if ((valueToken.equals("+") || valueToken.equals("-")) && ((prevToken == null) || prevToken.getValue().equals("("))) {
                    valueToken = valueToken + "U";  // this is unary +-
                }

                currentOperation = getMapOper().get(valueToken);
                if (currentOperation != null) {
                    resultList.add(currentOperation);
                } else {
                    resultList.add(token);  // operation not found may be it's function
                }
            }
            prevToken = token;
        }

        setListExp(resultList);

        return result;
    }
}
