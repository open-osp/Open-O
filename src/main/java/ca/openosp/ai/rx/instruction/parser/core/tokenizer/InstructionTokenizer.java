/**
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package ca.openosp.ai.rx.instruction.parser.core.tokenizer;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;
import opennlp.tools.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class InstructionTokenizer implements Tokenizer {

    public static final InstructionTokenizer INSTANCE;

    static {
        INSTANCE = new InstructionTokenizer();
    }

    @Override
    public String[] tokenize(String s) {
        List<String> tokens = new ArrayList<>();

        for (Span span : tokenizePos(s)) {
            String token = s.substring(span.getStart(), span.getEnd());

            if (token.contains("-")) {
                if (isNumericHyphenRange(token)) {
                    int hyphenIndex = token.indexOf('-');
                    String left = token.substring(0, hyphenIndex);
                    String right = token.substring(hyphenIndex + 1);
                    tokens.add(left);
                    tokens.add("-");
                    tokens.add(right);
                } else {
                    tokens.add(token);
                }
            } else {
                tokens.add(token);
            }
        }

        return tokens.toArray(new String[0]);
    }

    @Override
    public Span[] tokenizePos(String s) {
        CharacterEnum charType = CharacterEnum.WHITESPACE;
        CharacterEnum state = charType;

        List<Span> tokens = new ArrayList<>();
        int sl = s.length();
        int start = -1;
        char pc = 0;
        for (int ci = 0; ci < sl; ci++) {
            char c = s.charAt(ci);
            if (StringUtil.isWhitespace(c)) {
                charType = CharacterEnum.WHITESPACE;
            } else if (Character.isLetter(c) || c == '-') {
                charType = CharacterEnum.ALPHABETIC;
            } else if (Character.isDigit(c)) {
                charType = CharacterEnum.NUMERIC;
            } else {
                charType = CharacterEnum.OTHER;
            }
            if (state == CharacterEnum.WHITESPACE) {
                if (charType != CharacterEnum.WHITESPACE) {
                    start = ci;
                }
            } else {
                if (charType != state || charType == CharacterEnum.OTHER && c != pc) {
                    tokens.add(new Span(start, ci));
                    start = ci;
                }
            }
            state = charType;
            pc = c;
        }
        if (charType != CharacterEnum.WHITESPACE) {
            tokens.add(new Span(start, sl));
        }
        return tokens.toArray(new Span[0]);
    }

    private boolean isNumericHyphenRange(String token) {
        if (!token.contains("-")) return false;

        String[] parts = token.split("-");
        return parts.length == 2 && isNumeric(parts[0]) && isNumeric(parts[1]);
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+(\\.\\d+)?");
    }

    static class CharacterEnum {
        static final CharacterEnum WHITESPACE = new CharacterEnum("whitespace");
        static final CharacterEnum ALPHABETIC = new CharacterEnum("alphabetic");
        static final CharacterEnum NUMERIC = new CharacterEnum("numeric");
        static final CharacterEnum OTHER = new CharacterEnum("other");

        private final String name;

        private CharacterEnum(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
