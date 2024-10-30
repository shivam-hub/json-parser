import java.util.*;
import java.math.*;

public class Parser {
    private final Tokenizer tokenizer;
    private Token currentToken;

    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        this.currentToken = tokenizer.getNextToken();
    }

    private void next() {
        currentToken = tokenizer.getNextToken();
    }

    private void expect(TokenType type) {
        if (currentToken.getType() != type) {
            throw new IllegalArgumentException("Expected " + type + " but found " + currentToken.getType()
                    + " at position " + currentToken.getPosition());
        }
        next();
    }

    public Map<String, Object> parseObject() {
        Map<String, Object> jsonObject = new HashMap<>();

        expect(TokenType.LEFT_BRACE);
        while (currentToken.getType() != TokenType.RIGHT_BRACE) {
            if (currentToken.getType() != TokenType.STRING) {
                throw new IllegalArgumentException("Expected STRING key but found " + currentToken.getType());
            }
            String key = currentToken.getValue();
            next();

            expect(TokenType.COLON);

            Object value = parseValue();
            jsonObject.put(key, value);

            if (currentToken.getType() == TokenType.COMMA) {
                next();
            } else {
                break;
            }
        }
        expect(TokenType.RIGHT_BRACE);
        return jsonObject;
    }

    public List<Object> parseArray() {
        List<Object> array = new ArrayList<>();
        expect(TokenType.LEFT_BRACKET);
        if (currentToken.getType() == TokenType.RIGHT_BRACKET) {
            next();
            return array;
        }

        while (true) {
            array.add(parseValue());
            if (currentToken.getType() == TokenType.COMMA) {
                next();
                if (currentToken.getType() == TokenType.RIGHT_BRACKET) {
                    throw new IllegalArgumentException(
                            "Unexpected comma before closing bracket at position " + currentToken.getPosition());
                }
            } else if (currentToken.getType() == TokenType.RIGHT_BRACKET) {
                next();
                if (currentToken.getType() == TokenType.COMMA || currentToken.getType() != TokenType.EOF) {
                    throw new IllegalArgumentException(
                            "Unexpected token after array closing bracket at position " + currentToken.getPosition());
                }
                break;
            } else {
                throw new IllegalArgumentException(
                        "Expected comma or closing bracket in array at position " + currentToken.getPosition());
            }
        }

        return array;
    }

    public Object parseValue() {
        switch (currentToken.getType()) {
            case STRING:
                String stringValue = currentToken.getValue();
                next();
                return stringValue;
            case NUMBER:
                String numberValue = currentToken.getValue();
                next();
                if (numberValue.contains("e") || numberValue.contains("E")) {
                    try {
                        return new BigDecimal(numberValue);
                    } catch (Exception e) {
                        return numberValue;
                    }
                } else if (numberValue.contains(".")) {
                    return Double.parseDouble(numberValue);
                } else {
                    try {
                        return Integer.parseInt(numberValue);
                    } catch (Exception e) {
                        return numberValue;
                    }
                }
            case LEFT_BRACE:
                return parseObject();
            case LEFT_BRACKET:
                return parseArray();
            case TRUE:
                next();
                return true;
            case FALSE:
                next();
                return false;
            case NULL:
                next();
                return null;
            default:
                throw new IllegalArgumentException(
                        "Unexpected token " + currentToken.getType() + " at position " + currentToken.getPosition());
        }
    }

    public Object parse() {
        if (currentToken.getType() == TokenType.LEFT_BRACE) {
            return parseObject();
        } else if (currentToken.getType() == TokenType.LEFT_BRACKET) {
            return parseArray();
        } else {
            throw new IllegalArgumentException("JSON must start with '{' or '['");
        }
    }
}
