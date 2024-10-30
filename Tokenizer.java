public class Tokenizer {
    private final String input;
    private int currentPosition = 0;

    public Tokenizer(String input) {
        this.input = input;
    }

    public Token getNextToken() {
        skipWhitespace();

        if (currentPosition >= input.length()) {
            return new Token(TokenType.EOF, "", currentPosition);
        }

        char currentChar = input.charAt(currentPosition);

        switch (currentChar) {
            case '{': currentPosition++; return new Token(TokenType.LEFT_BRACE, "{", currentPosition - 1);
            case '}': currentPosition++; return new Token(TokenType.RIGHT_BRACE, "}", currentPosition - 1);
            case '[': currentPosition++; return new Token(TokenType.LEFT_BRACKET, "[", currentPosition - 1);
            case ']': currentPosition++; return new Token(TokenType.RIGHT_BRACKET, "]", currentPosition - 1);
            case ':': currentPosition++; return new Token(TokenType.COLON, ":", currentPosition - 1);
            case ',': currentPosition++; return new Token(TokenType.COMMA, ",", currentPosition - 1);
            case '"': return readString();
            default:
                if (Character.isDigit(currentChar) || currentChar == '-') {
                    return readNumber();
                } else if (startsWith("true")) {
                    currentPosition += 4;
                    return new Token(TokenType.TRUE, "true", currentPosition - 4);
                } else if (startsWith("false")) {
                    currentPosition += 5;
                    return new Token(TokenType.FALSE, "false", currentPosition - 5);
                } else if (startsWith("null")) {
                    currentPosition += 4;
                    return new Token(TokenType.NULL, "null", currentPosition - 4);
                } else {
                    throw new IllegalArgumentException("Unexpected character '" + currentChar + "' at position " + currentPosition);
                }
        }
    }

    private void skipWhitespace() {
        while (currentPosition < input.length() && Character.isWhitespace(input.charAt(currentPosition))) {
            currentPosition++;
        }
    }

    private boolean startsWith(String keyword) {
        return input.startsWith(keyword, currentPosition);
    }

    private Token readString() {
        int start = ++currentPosition;
        StringBuilder sb = new StringBuilder();
        while (currentPosition < input.length()) {
            char currentChar = input.charAt(currentPosition);

            if (currentChar == '"') {
                currentPosition++;
                return new Token(TokenType.STRING, sb.toString(), start - 1);
            } else if (currentChar == '\\') {
                currentPosition++;
                if (currentPosition >= input.length()) {
                    throw new IllegalArgumentException("Unexpected end of input in string literal");
                }
                currentChar = input.charAt(currentPosition);
                switch (currentChar) {
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case '/': sb.append('/'); break;
                    case 'b': sb.append('\b'); break;
                    case 'f': sb.append('\f'); break;
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    default: throw new IllegalArgumentException("Invalid escape sequence \\" + currentChar + " at position " + currentPosition);
                }
            } else {
                sb.append(currentChar);
            }
            currentPosition++;
        }
        throw new IllegalArgumentException("Unterminated string literal starting at position " + start);
    }


    private Token readNumber() {
        int start = currentPosition;
        boolean isDecimal = false;
        boolean hasExponent = false;

        if (input.charAt(currentPosition) == '-') {
            currentPosition++;
        }
    
        while (currentPosition < input.length()) {
            char currentChar = input.charAt(currentPosition);
    
            if (Character.isDigit(currentChar)) {
                currentPosition++;
            } else if (currentChar == '.' && !isDecimal) {
                isDecimal = true;
                currentPosition++;
            } else if ((currentChar == 'e' || currentChar == 'E') && !hasExponent) {
                hasExponent = true;
                currentPosition++;
                if (currentPosition < input.length() && 
                    (input.charAt(currentPosition) == '+' || input.charAt(currentPosition) == '-')) {
                    currentPosition++;
                }
            } else {
                break;
            }
        }
    
        String value = input.substring(start, currentPosition);
    
        try {
            if (isDecimal || hasExponent) {
                Double.parseDouble(value);
            } else {
                Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            // System.out.println(e);
        }
    
        return new Token(TokenType.NUMBER, value, start);
    }
}
