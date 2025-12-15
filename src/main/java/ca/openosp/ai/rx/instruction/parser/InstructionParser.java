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

package ca.openosp.ai.rx.instruction.parser;

import opennlp.tools.tokenize.Tokenizer;


/**
 * This interface is responsible for parsing the instruction string and returning the
 * {@link InstructionSpecs} object.
 */
public interface InstructionParser {

    /**
     * This method will parse the given instruction string and return the {@link InstructionSpecs} object.
     *
     * @param instruction the instruction string to be parsed
     * @return the {@link InstructionSpecs} object
     */
    InstructionSpecs parse(String instruction);

    /**
     * This method will return the {@link Tokenizer} object.
     *
     * @return the {@link Tokenizer} object
     */
    Tokenizer getTokenizer();

    /**
     * This method will tokenize the given instruction string and return the array of tokens.
     *
     * @param instruction the instruction string to be tokenized
     * @return the array of tokens
     */
    String[] tokenize(String instruction);

}
