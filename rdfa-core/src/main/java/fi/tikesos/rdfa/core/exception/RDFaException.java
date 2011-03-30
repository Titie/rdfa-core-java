package fi.tikesos.rdfa.core.exception;

import fi.tikesos.rdfa.core.datatype.Location;

public class RDFaException extends Exception implements Location {
	private static final long serialVersionUID = 1L;
	private long line;
	private long column;

	/**
	 * Constructor
	 * 
	 * @param line
	 * @param column
	 */
	public RDFaException(long line, long column) {
		super();
		this.line = line;
		this.column = column;
	}

	/**
	 * Constructor
	 * 
	 * @param cause
	 * @param line
	 * @param column
	 */
	public RDFaException(Throwable cause, long line, long column) {
		super(cause);
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

}
