package com.mycompany.myapp.common;


/** This class defines method to generate CSV file **/
import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CSVGenerator {

	private FileUtility fileUtil;
	
	@Autowired
    public CSVGenerator(FileUtility fileUtil) {
		super();
		this.fileUtil = fileUtil;
	}

	/**
     * This method generate csv file
     * 
     * @param data
     *            JSON array of Report data
     * @param headers
     *            report header name
     * @param propertyFileName
     *            property file name
     * @param fileName
     * @return outputFile Returns generated CSV file
     * 
     * @throws IOException
     * @throws CustomException
     * 
     */
	@SuppressWarnings("deprecation")
	public File createCSV(JSONArray data, Map<String, String> headers, String fileName)
            throws  IOException {

        File outputFile = fileUtil.createFile(fileName,Constants.CSV_EXTENTION);

        StringBuilder sb = new StringBuilder();
        if (data.length() <= 0) {
            sb.append("No record Found");
        } else {
            for (String header : headers.values()) {
                sb.append(header);
                sb.append(",");
            }
            sb.append(System.lineSeparator());
            for (int i = 0; i < data.length(); i++) {
                JSONObject column = data.getJSONObject(i);
                for (String header : headers.keySet()) {
                    Object columnValue = column.get(header);
                    sb.append(columnValue.toString().replace(",", ""));
                    sb.append(",");
                }
                sb.append(System.lineSeparator());
            }
        }
        FileUtils.writeStringToFile(outputFile, sb.toString());
        return outputFile;

    }

}
