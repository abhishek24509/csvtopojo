package com.csvparser.iface;

import java.util.List;

public interface ICSVParser {

	public List<Object> parse(String pFilePath,String pClassName);
}
