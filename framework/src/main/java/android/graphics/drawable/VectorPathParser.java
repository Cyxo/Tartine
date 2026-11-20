package android.graphics.drawable;

import android.graphics.Path;

/**
 * Parses the SVG path commands used by vector drawables into the framework
 * {@link Path} model. Commands may be separated by whitespace, commas, or
 * nothing at all; a command letter stays in effect for subsequent coordinate
 * sets, and a moveto's later pairs are implicit lineto commands.
 */
final class VectorPathParser {

    private VectorPathParser() {
    }

    static Path parse(String data) {
        if (data == null) {
            throw new IllegalArgumentException("pathData must not be null");
        }
        Cursor input = new Cursor(data);
        Path path = new Path();
        float currentX = 0.0f;
        float currentY = 0.0f;
        float startX = 0.0f;
        float startY = 0.0f;
        float controlX = 0.0f;
        float controlY = 0.0f;
        char command = 0;
        char previousCommand = 0;

        while (true) {
            input.skipSeparators();
            if (!input.hasMore()) {
                return path;
            }
            if (isCommand(input.peek())) {
                command = input.read();
                if (command == 'Z' || command == 'z') {
                    path.close();
                    currentX = startX;
                    currentY = startY;
                    previousCommand = command;
                    command = 0;
                    continue;
                }
            }
            if (command == 0) {
                throw input.error("expected a path command");
            }
            boolean relative = Character.isLowerCase(command);
            switch (command) {
                case 'M':
                case 'm': {
                    float x = input.nextFloat();
                    float y = input.nextFloat();
                    if (relative) {
                        x += currentX;
                        y += currentY;
                    }
                    path.moveTo(x, y);
                    currentX = x;
                    currentY = y;
                    startX = x;
                    startY = y;
                    command = relative ? 'l' : 'L';
                    break;
                }
                case 'L':
                case 'l': {
                    float x = input.nextFloat();
                    float y = input.nextFloat();
                    if (relative) {
                        x += currentX;
                        y += currentY;
                    }
                    path.lineTo(x, y);
                    currentX = x;
                    currentY = y;
                    break;
                }
                case 'H':
                case 'h': {
                    float x = input.nextFloat();
                    if (relative) {
                        x += currentX;
                    }
                    path.lineTo(x, currentY);
                    currentX = x;
                    break;
                }
                case 'V':
                case 'v': {
                    float y = input.nextFloat();
                    if (relative) {
                        y += currentY;
                    }
                    path.lineTo(currentX, y);
                    currentY = y;
                    break;
                }
                case 'C':
                case 'c': {
                    float x1 = input.nextFloat();
                    float y1 = input.nextFloat();
                    float x2 = input.nextFloat();
                    float y2 = input.nextFloat();
                    float x = input.nextFloat();
                    float y = input.nextFloat();
                    if (relative) {
                        x1 += currentX;
                        y1 += currentY;
                        x2 += currentX;
                        y2 += currentY;
                        x += currentX;
                        y += currentY;
                    }
                    path.cubicTo(x1, y1, x2, y2, x, y);
                    controlX = x2;
                    controlY = y2;
                    currentX = x;
                    currentY = y;
                    break;
                }
                case 'S':
                case 's': {
                    float x1 = currentX;
                    float y1 = currentY;
                    if (previousCommand == 'C' || previousCommand == 'c'
                            || previousCommand == 'S' || previousCommand == 's') {
                        x1 = 2.0f * currentX - controlX;
                        y1 = 2.0f * currentY - controlY;
                    }
                    float x2 = input.nextFloat();
                    float y2 = input.nextFloat();
                    float x = input.nextFloat();
                    float y = input.nextFloat();
                    if (relative) {
                        x2 += currentX;
                        y2 += currentY;
                        x += currentX;
                        y += currentY;
                    }
                    path.cubicTo(x1, y1, x2, y2, x, y);
                    controlX = x2;
                    controlY = y2;
                    currentX = x;
                    currentY = y;
                    break;
                }
                case 'Q':
                case 'q': {
                    float x1 = input.nextFloat();
                    float y1 = input.nextFloat();
                    float x = input.nextFloat();
                    float y = input.nextFloat();
                    if (relative) {
                        x1 += currentX;
                        y1 += currentY;
                        x += currentX;
                        y += currentY;
                    }
                    path.quadTo(x1, y1, x, y);
                    controlX = x1;
                    controlY = y1;
                    currentX = x;
                    currentY = y;
                    break;
                }
                case 'T':
                case 't': {
                    float x1 = currentX;
                    float y1 = currentY;
                    if (previousCommand == 'Q' || previousCommand == 'q'
                            || previousCommand == 'T' || previousCommand == 't') {
                        x1 = 2.0f * currentX - controlX;
                        y1 = 2.0f * currentY - controlY;
                    }
                    float x = input.nextFloat();
                    float y = input.nextFloat();
                    if (relative) {
                        x += currentX;
                        y += currentY;
                    }
                    path.quadTo(x1, y1, x, y);
                    controlX = x1;
                    controlY = y1;
                    currentX = x;
                    currentY = y;
                    break;
                }
                default:
                    throw input.error("unsupported path command " + command);
            }
            previousCommand = command;
        }
    }

    private static boolean isCommand(char value) {
        return "MmZzLlHhVvCcSsQqTtAa".indexOf(value) >= 0;
    }

    /** Character cursor that recognizes SVG's compact number syntax. */
    private static final class Cursor {
        private final String data;
        private int at;

        private Cursor(String data) {
            this.data = data;
        }

        private boolean hasMore() {
            return at < data.length();
        }

        private char peek() {
            return data.charAt(at);
        }

        private char read() {
            return data.charAt(at++);
        }

        private void skipSeparators() {
            while (hasMore()) {
                char value = peek();
                if (!Character.isWhitespace(value) && value != ',') {
                    return;
                }
                at++;
            }
        }

        private float nextFloat() {
            skipSeparators();
            int start = at;
            if (hasMore() && (peek() == '+' || peek() == '-')) {
                at++;
            }
            while (hasMore() && Character.isDigit(peek())) {
                at++;
            }
            if (hasMore() && peek() == '.') {
                at++;
                while (hasMore() && Character.isDigit(peek())) {
                    at++;
                }
            }
            if (hasMore() && (peek() == 'e' || peek() == 'E')) {
                int exponent = at++;
                if (hasMore() && (peek() == '+' || peek() == '-')) {
                    at++;
                }
                int digits = at;
                while (hasMore() && Character.isDigit(peek())) {
                    at++;
                }
                if (digits == at) {
                    at = exponent;
                }
            }
            if (start == at) {
                throw error("expected a number");
            }
            try {
                return Float.parseFloat(data.substring(start, at));
            } catch (NumberFormatException e) {
                throw error("invalid number", e);
            }
        }

        private IllegalArgumentException error(String message) {
            return new IllegalArgumentException(
                    message + " at pathData index " + at);
        }

        private IllegalArgumentException error(String message, Throwable cause) {
            return new IllegalArgumentException(
                    message + " at pathData index " + at, cause);
        }
    }
}
