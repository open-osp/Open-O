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

package ca.openosp.ai.rx.instruction.parser.impl;

import ca.openosp.ai.rx.instruction.parser.InstructionParser;
import ca.openosp.ai.rx.instruction.parser.InstructionSpecs;
import ca.openosp.ai.rx.instruction.parser.core.exception.EmptyInstruction;
import ca.openosp.ai.rx.instruction.parser.core.exception.ModelInitException;
import ca.openosp.ai.rx.instruction.parser.core.exception.ParsingException;
import ca.openosp.ai.rx.instruction.parser.core.tokenizer.InstructionTokenizer;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Implementation of the InstructionParser interface. This class uses OpenNLP to parse instructions.
 */
public class OpenNlpInstructionParser implements InstructionParser {

    private final TokenNameFinderModel nerModel;
    private NameFinderME nameFinder;
    private final DictionaryLemmatizer lemmatizer;

    /*
     * This constructor will initialize the model and dictionary using the provided model and dictionary paths.
     *
     * @param modelPath the path to the model file
     * @param dictionaryPath the path to the dictionary file
     * @throws ModelInitException if there is an error initializing the model
     */
    public OpenNlpInstructionParser(String modelPath, String dictionaryPath) throws ModelInitException, IOException {
        this(Files.newInputStream(Paths.get(modelPath)), Files.newInputStream(Paths.get(dictionaryPath)));
    }

    public OpenNlpInstructionParser(InputStream nerModelStream, InputStream dictionaryStream) throws ModelInitException {
        try {
            this.nerModel = new TokenNameFinderModel(nerModelStream);
            this.nameFinder = new NameFinderME(nerModel);

            byte[] data = toByteArray(dictionaryStream);

            InputStream stream1 = new ByteArrayInputStream(data);

            this.lemmatizer = new DictionaryLemmatizer(stream1);
        } catch (Exception e) {
            throw new ModelInitException("Error initializing model", e);
        }
    }


    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192]; // 8 KB buffer
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

    @Override
    public InstructionSpecs parse(String instruction) throws EmptyInstruction, ParsingException {
        if (Objects.isNull(this.nerModel)) {
            throw new ModelInitException("Model is not ready");
        } else if (instruction.isEmpty()) {
            throw new EmptyInstruction("Instruction is empty");
        }

        return this.parse(this.tokenize(instruction.toLowerCase()));
    }

    @Override
    public Tokenizer getTokenizer() {
        return InstructionTokenizer.INSTANCE;
    }

    @Override
    public String[] tokenize(String instruction) {
        return this.getTokenizer().tokenize(instruction);
    }

    /*
     * This method will return the POSTaggerME object.
     *
     * @return the POSTaggerME object
     * */
    private NameFinderME getNameFinder() {
        if (Objects.isNull(this.nameFinder)) {
            this.nameFinder = new NameFinderME(this.nerModel);
        }
        return this.nameFinder;
    }

    /*
     * This method will parse the tokenized instruction and return the InstructionSpecs object.
     *
     * @param tokenizedInstruction the tokenized instruction
     * @return the InstructionSpecs object
     * @throws ParsingException if there is an error parsing the instruction
     * */
    private InstructionSpecs parse(String[] tokenizedInstruction) throws ParsingException {

        NameFinderME tagger = this.getNameFinder();
        Span[] spans = tagger.find(tokenizedInstruction);

        if (Objects.isNull(spans) || spans.length == 0) {
            throw new ParsingException("Error parsing instruction");
        }

        String[] tagsArray = new String[tokenizedInstruction.length];
        Arrays.fill(tagsArray, "O");

        for (Span span : spans) {
            for (int i = span.getStart(); i < span.getEnd(); i++) {
                tagsArray[i] = span.getType();
            }
        }

        String[] lemmasArray = this.lemmatizer.lemmatize(tokenizedInstruction, tagsArray);
        for (int i = 0; i < lemmasArray.length; i++) {
            String lemma = lemmasArray[i];
            if (lemma.equals("O") && !(tagsArray[i].equals("?") || tagsArray[i].equals("TO"))) {
                lemmasArray[i] = tokenizedInstruction[i];
            }
        }

        Map<String, String> groupedEntities = new LinkedHashMap<>();
        List<InstructionSpecs.IdentifiedData> identifiedData = new ArrayList<>();

        for (Span span : spans) {
            String type = span.getType();
            String groupedText = String.join(" ",
                    Arrays.copyOfRange(tokenizedInstruction, span.getStart(), span.getEnd()));
            groupedEntities.put(type, groupedText);
            identifiedData.add(new InstructionSpecs.IdentifiedData(groupedText, type, this.lemmatizer.lemmatize(new String[]{groupedText}, new String[]{type})[0]));
        }

        return new InstructionSpecs(tokenizedInstruction, tagsArray, lemmasArray, groupedEntities, identifiedData);
    }

}
