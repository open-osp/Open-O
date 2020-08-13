/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package oscar.oscarLab.ca.all.pageUtil;

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import org.oscarehr.olis.OLISUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OLISLabPDFUtils {

    public enum Hl7EncodedSpan {
        HIGHLIGHT("H", "N") {
            @Override
            public Chunk createChunk(String chunkText) {
                Chunk highlightChunk = new Chunk(chunkText);
                highlightChunk.setBackground(Color.YELLOW);
                return highlightChunk;
            }
        };

        public static final String TAG_REGEX = "\\\\\\.%s\\\\(.+?)\\\\\\.%s\\\\";
        
        private String startCharacter;
        private String endCharacter;
        public abstract Chunk createChunk(String text);

        Hl7EncodedSpan(String startCharacter, String endCharacter) {
            this.startCharacter = startCharacter;
            this.endCharacter = endCharacter;
        }

        public String getStartCharacter() {
            return startCharacter;
        }
        public String getEndCharacter() {
            return endCharacter;
        }

        public static List<Chunk> createChunksFromText(String hl7Text) {
            List<Chunk> formattedTextChunks = new ArrayList<Chunk>();
            // find instances of the span start characters, create chunk for text prior to it
            // then find the end character and create a chunk with the special formatting
            for (Hl7EncodedSpan hl7EncodedSpan : Hl7EncodedSpan.values()) {
                String regex = String.format(Hl7EncodedSpan.TAG_REGEX, hl7EncodedSpan.getStartCharacter(), hl7EncodedSpan.getEndCharacter());
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(hl7Text);
                while(matcher.find()) {
                    String spanContent = matcher.group(1);
                    String beforeSpan = hl7Text.substring(0, matcher.start());
                    String afterSpan = hl7Text.substring(matcher.end());

                    formattedTextChunks.add(new Chunk(beforeSpan));
                    formattedTextChunks.add(hl7EncodedSpan.createChunk(spanContent));
                    hl7Text = afterSpan;
                }
            }
            formattedTextChunks.add(new Chunk(hl7Text));
            return formattedTextChunks;
        }
    }

    /**
     * Creates a Phrase from a string of text, adding span level markup like highlighting
     * @param hl7Text the HL7 text to parse
     * @param font the pdf font to use as a base
     * @return A phrase containing Chunks with appropriate markup
     */
    public static Phrase createPhraseFromHl7(String hl7Text, Font font) {

        Phrase phrase = new Phrase();
        phrase.setFont(font);
        // A phrase is a list of chunks, with each chunk having its own formatting
        phrase.addAll(Hl7EncodedSpan.createChunksFromText(hl7Text));

        return phrase;
    }
    
    public static List<PdfPCell> createCellsFromHl7(String hl7Text, Font font, PdfPCell templateCell) {
        List<PdfPCell> formattedCells = new ArrayList<PdfPCell>();
        PdfPCell cell;

        // Replace repeatable encoded characters with their pdf equivalent replacements
        hl7Text = OLISUtils.Hl7EncodedRepeatableCharacter.performReplacement(hl7Text, false);
        
        // Split comment on \.ce\ (center tag span) markup, due to the fact that adding centered text requires cell-level alignment
        Pattern pattern = Pattern.compile("\\\\\\.ce\\\\(.+?)(?:\n|$)");
        Matcher matcher = pattern.matcher(hl7Text);
        while (matcher.find()) {
            String beforeSpan = hl7Text.substring(0, matcher.start());
            String spanContent = matcher.group(1);
            String afterSpan = hl7Text.substring(matcher.end());

            // Create cell for comment before center tag
            cell = createCellWithCopiedProperties(templateCell);
            cell.setPhrase(OLISLabPDFUtils.createPhraseFromHl7(beforeSpan, font));
            formattedCells.add(cell);

            // Create cell for comment within center tag
            cell = createCellWithCopiedProperties(templateCell);
            cell.setPhrase(OLISLabPDFUtils.createPhraseFromHl7(spanContent, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            formattedCells.add(cell);

            // Set comment to remaining comment text
            hl7Text = afterSpan;
        }

        cell = createCellWithCopiedProperties(templateCell);
        cell.setPhrase(OLISLabPDFUtils.createPhraseFromHl7(hl7Text, font));
        formattedCells.add(cell);
        
        return formattedCells;
    }
    
    public static void addAllCellsToTable(PdfPTable table, List<PdfPCell> cells) {
        for (PdfPCell cell : cells) {
            table.addCell(cell);
        }
    }
    
    public static PdfPCell createCellWithCopiedProperties(PdfPCell templateCell) {
        PdfPCell cell = new PdfPCell();
        cell.setColspan(templateCell.getColspan());
        cell.setRowspan(templateCell.getRowspan());
        cell.setBorder(templateCell.getBorder());
        cell.setPaddingLeft(templateCell.getPaddingLeft());
        cell.setPaddingRight(templateCell.getPaddingRight());
        cell.setPaddingTop(templateCell.getPaddingTop());
        cell.setPaddingBottom(templateCell.getPaddingBottom());
        cell.setFixedHeight(templateCell.getFixedHeight());
        cell.setHorizontalAlignment(templateCell.getHorizontalAlignment());
        cell.setVerticalAlignment(templateCell.getVerticalAlignment());
        cell.setBackgroundColor(templateCell.getBackgroundColor());
        cell.setBorderColor(templateCell.getBorderColor());
        return cell;
    }
}