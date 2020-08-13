/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package com.indivica.olis.parameters;

import org.oscarehr.olis.OLISUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Test Result Code
 * @author jen
 *
 */
public class OBX3 implements Parameter {

	private List<String> codes = new ArrayList<>();
	private String nameOfCodingSystem;
	
	public OBX3(String nameOfCodingSystem) {
	    this.nameOfCodingSystem = nameOfCodingSystem;
    }

	@Override
	public String toOlisString() {
		return OLISUtils.createQueryStringForCodes(getQueryCode(), codes, nameOfCodingSystem);
	}

	public void addValue(String requestCode) {
		codes.add(requestCode);
	}

	public void addAllValues(List<String> requestCodes) {
		codes.addAll(requestCodes);
	}
	
	@Override
	public void setValue(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(Integer part, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(Integer part, Integer part2, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getQueryCode() {
		return "@OBX.3";
	}
	
	public boolean hasCodes() {
		return !codes.isEmpty();
	}

}
