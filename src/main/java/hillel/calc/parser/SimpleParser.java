package hillel.calc.parser;


import hillel.calc.operations.Operation;
import hillel.calc.tokens.Operand;
import hillel.calc.tokens.Token;
import hillel.calc.tokens.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class SimpleParser extends Parser<List<Token>> {

    @Override
    public List<Token> parsing(String str) {
        List<Token> list = new ArrayList<Token>();
        TypeToken typeBuffer = null;   // тип токена символов считанных символов в буфере
        TypeToken typeExpr;     // возможный тип токена считанного символа
        String buffer = "";
        char ch;

        System.out.println("Parsing " + str);
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if ( ((int) ch) < 33 || ((int) ch) > 126 ) {
                continue;
            }

            typeExpr = getTypeTokenChar(typeBuffer, ch);
            // определяем считали полностью токен или очередной символ текущего (в этом случае просто добавляем символ в буфер)
            // если это не скобки, то добавляем в буфер
            // если скобка, она считается отдельным токеном и сразу добавляется в буфер

            // если это первый символ, просто добавляем его в буфер
            if (typeBuffer == null) {
                buffer += ch;
                typeBuffer = typeExpr;
            } else if (typeBuffer == typeExpr) {
                // если тип токена в буфере и тип токена совпадают, просто добавляем в буфер
                if (ch == '(' || ch ==')' || ch ==',') {
                    AddList( list, typeBuffer, buffer);
                } else {
                    buffer += ch;
                }
            } else {
                // считан символ нового токена, строку символов из буфера превращаем в токен и добавляем в массив токенов
                AddList( list, typeBuffer, buffer);
                // буфер очищаем и записываем символ нового токена, типу токена в буфере присваиваем тип токена символа
                buffer = String.valueOf(ch);
                typeBuffer = typeExpr;
            }
        }

        // добавляем токен из того, что осталось в буфере
        AddList( list, typeBuffer, buffer);

        return list;

    }

    /* ============================================================
        add token of expression in the array of tokens
    ============================================================ */
    private void AddList(List<Token> list, TypeToken en, String str) {
        int priority = 0;
        int arity = 0;

        String key;
        Token token = null;

        if (en == TypeToken.OPERAND) {
            token = new Operand(str);
        } else if (en == TypeToken.OPEN_BRACKET || en == TypeToken.CLOSE_BRACKET || en == TypeToken.DEVIDER) {
            token = new Token(str);
        } else if (en == TypeToken.OPERATION || en == TypeToken.FUNCTION) {
            token = new Operation(str);
        }
        list.add(token);
    }

    /* =========================================================================
        вернуть тип токена для символа
        ========================================================================= */
    private static TypeToken getTypeTokenChar(TypeToken typeBuffer, char ch) {
        TypeToken result;
        if (Character.isDigit(ch) || ch == '.') {
            result = TypeToken.OPERAND;
        } else if (Character.isLetter(ch)) {
            if (ch == 'E' || ch == 'e') {
                if (typeBuffer == TypeToken.OPERAND) {
                    // это экспоненциальная форма числа
                    result = TypeToken.OPERAND;
                } else {
                    result = TypeToken.FUNCTION;
                }
            } else {
                result = TypeToken.FUNCTION;
            }
        } else if (ch == '(') {
            result = TypeToken.OPEN_BRACKET;
        } else if (ch == ')') {
            result = TypeToken.CLOSE_BRACKET;
        } else if (ch == ',') {
            result = TypeToken.DEVIDER;
        } else {
            result =  TypeToken.OPERATION;
        }
        return result;
    }
}
