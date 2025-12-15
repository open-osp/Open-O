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

import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public record InstructionSpecs(String[] tokens, String[] tags, String[] lemmas, Map<String, String> groupedEntities,
                               List<IdentifiedData> identifiedData) {

    public void print() {
        System.out.println("---------------------------");
        this.identifiedData.forEach(IdentifiedData::print);
        System.out.println("---------------------------");
    }

    @Override
    public @NonNull String toString() {

        return "InstructionSpecs{tokens=" +
                Arrays.toString(tokens) +
                ", tags=" +
                Arrays.toString(tags) +
                ", lemmas=" +
                Arrays.toString(lemmas) +
                "}\n";
    }


    public record IdentifiedData(String token, String tag, String lemma) {

        public void print() {
            System.out.println(this.tag + "\t\t" + this.token + "\t\t" + this.lemma);
        }
    }
}
