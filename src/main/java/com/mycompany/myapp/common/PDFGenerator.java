package com.mycompany.myapp.common;

/** This class defines method to generate Pdf file**/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;


@Component
public class PDFGenerator {

    private FileUtility fileUtil;

    @Autowired
    public PDFGenerator(FileUtility fileUtil) {
        super();
        this.fileUtil = fileUtil;
    }

    /**
     * This method generates PDF report file
     * 
     * @param data
     *            Report data
     * @param reportTitle
     * @param xslFile
     *            XSL file
     * @param logo
     *            Application Logo
     * @param propertyFileName
     *            property file name
     * @param fileName
     * @param signerName
     * @param userName
     * @return file returns generated PDF file
     * 
     * @throws URISyntaxException
     * @throws IOException
     * @throws FOPException
     * @throws TransformerException
     * @throws CustomException
     * @throws FileNotFoundException
     * 
     */

    public File createPDF(
            String data,
            File xslFile,
            String logo,
            String userName,
            DigitalSignatureBean signatureBean) throws CustomException, IOException {
        String inputXML;
        File outputFile = fileUtil
                .createFile(signatureBean.getFileName(), Constants.PDF_EXTENTION);
        try (OutputStream out = new FileOutputStream(outputFile)) {
            inputXML = CreateXML.setJsonToXml(
                    data,
                    signatureBean.getReportTitle(),
                    logo,
                    signatureBean.getSignerName(),
                    userName,
                    signatureBean.getUserSignature());
            StringReader reader = new StringReader(inputXML);
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslFile));
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(new StreamSource(reader), res);
            return outputFile;
        } catch (IOException | FOPException | TransformerException e) {
            throw new CustomException("Error in PDF creation: " + e);
        }
    }

    /**
     * This method generates PDF report file for listing reports
     * 
     * @param data
     *            Report data object This data object will be converted into
     *            ordered set of xml data using XmlMapper
     * @param xslFile
     *            XSL file as stream
     * @param logo
     *            Application Logo
     * @param fileName
     * @param signerName
     * @param userName
     * @return file returns generated PDF file
     * @throws CustomException
     * @throws IOException
     **/
    public File createPDF(
            List<?> data,
            InputStream xslFile,
            String logo,
            String userName,
            DigitalSignatureBean signatureBean,
            List<String> headers,
            List<String> excludeFields) throws CustomException, IOException {
        String inputXML;
        ReportData reportData;
        Transformer transformer;
        File outputFile = fileUtil.createFile(signatureBean.getFileName(), Constants.PDF_EXTENTION);
        try (OutputStream out = new FileOutputStream(outputFile)) {

            reportData = new ReportData();
            reportData.setSignerName(signatureBean.getSignerName());
            reportData.setReportTitle(signatureBean.getReportTitle());
            reportData.setLogo("file:///" + logo.replace("\\", "/"));
            reportData.setData(data);
            reportData.setUserName(userName);
            if (signatureBean.getUserSignature() != null) {
                reportData.setUserSignature(signatureBean.getUserSignature());
            }
            reportData.setHeaders(headers);
            reportData.setExcludeFields(excludeFields);
            inputXML = CreateXML.objectToXml(reportData);
            StringReader reader = new StringReader(inputXML);
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            TransformerFactory factory = TransformerFactory.newInstance();

            if (xslFile != null) {
                transformer = factory.newTransformer(new StreamSource(xslFile));
            } else {
                transformer = factory.newTransformer(
                        new StreamSource(new ClassPathResource("default-report.xsl").getInputStream()));
            }
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(new StreamSource(reader), res);
            return outputFile;
        } catch (IOException | FOPException | TransformerException e) {
            throw new CustomException("Error in PDF creation: " + e);
        }
    }
    /**
     * This method generates PDF report file for listing reports
     * 
     * @param data
     *            Report data object This data object will be converted into
     *            ordered set of xml data using XmlMapper
     * @param xslFile
     *            XSL file
     * @param logo
     *            Application Logo
     * @param fileName
     * @param signerName
     * @param userName
     * @return file returns generated PDF file
     * @throws CustomException
     * @throws IOException
     **/
    public File createPDF(
            List<?> data,
            File xslFile,
            String logo,
            String userName,
            DigitalSignatureBean signatureBean,
            List<String> headers,
            List<String> excludeFields) throws CustomException, IOException {
        String inputXML;
        ReportData reportData;
        Transformer transformer;
        File outputFile = fileUtil.createFile(signatureBean.getFileName(), Constants.PDF_EXTENTION);
        try (OutputStream out = new FileOutputStream(outputFile)) {

            reportData = new ReportData();
            reportData.setSignerName(signatureBean.getSignerName());
            reportData.setReportTitle(signatureBean.getReportTitle());
            reportData.setLogo("file:///" + logo.replace("\\", "/"));
            reportData.setData(data);
            reportData.setUserName(userName);
            if (signatureBean.getUserSignature() != null) {
                reportData.setUserSignature(signatureBean.getUserSignature());
            }
            reportData.setHeaders(headers);
            reportData.setExcludeFields(excludeFields);
            inputXML = CreateXML.objectToXml(reportData);
            StringReader reader = new StringReader(inputXML);
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            TransformerFactory factory = TransformerFactory.newInstance();

            if (xslFile != null && xslFile.exists()) {
                transformer = factory.newTransformer(new StreamSource(xslFile));
            } else {
                transformer = factory.newTransformer(
                        new StreamSource(new ClassPathResource("default-report.xsl").getInputStream()));
         }
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(new StreamSource(reader), res);
            return outputFile;
        } catch (IOException | FOPException | TransformerException e) {
            throw new CustomException("Error in PDF creation: " + e);
        }
    }
}
