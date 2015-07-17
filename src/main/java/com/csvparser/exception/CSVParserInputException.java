package com.csvparser.exception;

public class CSVParserInputException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public CSVParserInputException(){
    	System.out.println("Invalid Input pass. Two arguments needed first filepath and second class Name");
    }

}
