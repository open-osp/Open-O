//CHECKSTYLE:OFF
/**
 * Copyright (c) 2008-2012 Indivica Inc.
 * <p>
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package ca.openosp.openo.olis1;

import ca.openosp.openo.commn.model.Provider;

import ca.openosp.openo.olis1.queries.Query;
import ca.openosp.openo.olis1.segments.DSCSegment;
import ca.openosp.openo.olis1.segments.MSHSegment;
import ca.openosp.openo.olis1.segments.SPRSegment;
import ca.openosp.openo.olis1.segments.ZSHSegment;

public class OLISMessage {

    private MSHSegment mshSegment;
    private ZSHSegment zshSegment;
    private SPRSegment sprSegment;
    private DSCSegment dscSegment = null;

    public OLISMessage() {

    }

    public OLISMessage(Provider provider, Query query) {
        mshSegment = new MSHSegment(query.getQueryType());
        zshSegment = new ZSHSegment(provider);
        sprSegment = new SPRSegment(query.getQueryType(), query);
    }

    public OLISMessage(Provider provider, Query query, String continuationPointer) {
        mshSegment = new MSHSegment(query.getQueryType());
        zshSegment = new ZSHSegment(provider);
        sprSegment = new SPRSegment(query.getQueryType(), query);
        dscSegment = new DSCSegment(continuationPointer);
    }

    public String getTransactionId() {
        return mshSegment.getUuidString();
    }

    public String getOlisHL7String() {
        String output = "";

        output += mshSegment.getSegmentHL7String() + "\r";
        output += zshSegment.getSegmentHL7String() + "\r";
        output += sprSegment.getSegmentHL7String();

        if (dscSegment != null)
            output += "\r" + dscSegment.getSegmentHL7String();

        return output;
    }

}
