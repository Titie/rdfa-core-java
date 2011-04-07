/**
 * Copyright (C) 2011 ssakorho <sami.s.korhonen@uef.fi>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fi.tikesos.rdfa.core.exception;

import fi.tikesos.rdfa.core.datatype.Location;

public class RDFaException extends Exception implements Location {
	private static final long serialVersionUID = 1L;
	private long line;
	private long column;
	private String elementName;

	/**
	 * Constructor
	 * 
	 * @param elementName
	 * @param line
	 * @param column
	 */
	public RDFaException(String elementName, long line, long column) {
		super();
		this.elementName = elementName;
		this.line = line;
		this.column = column;
	}

	/**
	 * Constructor
	 * 
	 * @param elementName
	 * @param cause
	 * @param line
	 * @param column
	 */
	public RDFaException(String elementName, Throwable cause, long line, long column) {
		super(cause);
		this.elementName = elementName;
		this.line = line;
		this.column = column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fi.tikesos.rdfa.core.datatype.Location#getLine()
	 */
	public long getLine() {
		return line;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fi.tikesos.rdfa.core.datatype.Location#getColumn()
	 */
	public long getColumn() {
		return column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fi.tikesos.rdfa.core.datatype.Location#setLocation(long, long)
	 */
	public void setLocation(long line, long column) {
		this.line = line;
		this.column = column;
	}
	
	/**
	 * @return
	 */
	public String getElementName() {
		return elementName;
	}

}
