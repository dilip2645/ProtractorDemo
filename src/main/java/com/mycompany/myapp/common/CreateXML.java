package com.mycompany.myapp.common;


/**This class is used to create XML input for FOP PDF generator**/

import org.json.JSONObject;
import org.json.XML;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class CreateXML {

    CreateXML() {
        /** private constructor **/
    }

    /**
     * This method create XML
     * 
     * @param data
     *            string of Report data
     * @param logo
     *            application LOGO
     * @param name
     *            Signer Name
     * @param userName
     *            Logged User Name
     * @return XML Returns data in XML format
     * 
     */
    public static String setJsonToXml(
            String data,
            String title,
            String logo,
            String name,
            String userName,
            String userSignature) {
        JSONObject input = new JSONObject();
        JSONObject report = new JSONObject();
        String xml;

        report.put("signerName", name);
        report.put("reportTitle", title);
        report.put("logo", "file:///" + logo.replace("\\", "/"));
        report.put("data", new JSONObject(data));
        report.put("userName", userName);

        if (userSignature != null) {
            report.put("userSignature", "file:///" + userSignature.replace("\\", "/"));
        }
        input.put("report", report);
        xml = XML.toString(input);
        return xml;

    }

    /**
     * Method converts object into Xml string
     * 
     * @param reportData
     * @return String of xml
     * @throws JsonProcessingException
     **/
    public static String objectToXml(ReportData reportData) throws JsonProcessingException {
        return new XmlMapper().writeValueAsString(reportData);
    }
}
