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


import opennlp.tools.namefind.NameSample;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Span;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class ObjectStreamNameSampleImpl implements ObjectStream<NameSample> {

    private final ObjectStream<String> lineStream;

    public ObjectStreamNameSampleImpl(ObjectStream<String> lineStream) {
        this.lineStream = lineStream;
    }

    @Override
    public NameSample read() throws IOException {
        String line;

        // Read the next non-empty line
        while ((line = lineStream.read()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                break;
            }
        }

        if (line == null) {
            return null; // end of file
        }

        // Split line by spaces to get token_tag pairs
        String[] tokenTagPairs = line.split("\\s+");

        List<String> tokens = new ArrayList<>();
        List<String> tags = new ArrayList<>();

        for (String pair : tokenTagPairs) {
            String[] parts = pair.split("\\|", 2); // split only on the first underscore
            if (parts.length != 2) {
                throw new IOException("Invalid token-tag format: " + pair);
            }
            tokens.add(parts[0]);
            tags.add(parts[1]);
        }

        // Build spans based on tags
        List<Span> spans = new ArrayList<>();

        for (int i = 0; i < tags.size(); i++) {
            String tag = tags.get(i);

            if (tag.equals("O")) {
                continue; // skip non-entities
            }

            String[] parts = tag.split("-", 2);
            if (parts.length != 2) continue;

            String prefix = parts[0]; // U, B, I, L
            String type = parts[1];   // e.g., METHOD, ROUTE, etc.

            switch (prefix) {
                case "U":
                    spans.add(new Span(i, i + 1, type));
                    break;
                case "B": {
                    int start = i;
                    int end = i + 1;
                    while (end < tags.size()) {
                        String next = tags.get(end);
                        if (next.startsWith("L-") && next.endsWith(type)) {
                            spans.add(new Span(start, end + 1, type));
                            i = end;
                            break;
                        }
                        end++;
                    }
                    break;
                }
                default:
                    break;
            }
        }

        return new NameSample(
                tokens.toArray(new String[0]),
                spans.toArray(new Span[0]),
                false
        );
    }

    @Override
    public void reset() throws IOException {
        lineStream.reset();
    }

    @Override
    public void close() throws IOException {
        lineStream.close();
    }
}
