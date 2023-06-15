package com.galvanize;

public interface Driver {

	public String quoteColumn(String column);
	public String quoteTable(String table);
	public String quoteValue(String value);

}
