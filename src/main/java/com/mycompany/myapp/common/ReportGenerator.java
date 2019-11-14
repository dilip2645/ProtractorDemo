package com.mycompany.myapp.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ReportGenerator {

    private final CSVGenerator csvGenerator;
    private final PDFGenerator pdfGenerator;
    private final XLSGeneretorLocal xlsGenerator;

    @Autowired
    public ReportGenerator(
            CSVGenerator csvGenerator,
            PDFGenerator pdfGenerator,
            XLSGeneretorLocal xlsGenerator) {
        this.csvGenerator = csvGenerator;
        this.pdfGenerator = pdfGenerator;
        this.xlsGenerator = xlsGenerator;
    }

    /***
     * This method use to generate listing report, for PDF report it required
     * generic XSL
     *
     * @param reportData
     *            list of records to be display in report file
     * @param exportType
     *            Export type
     * @param reportHeader
     *            report data headers
     * @param workBookName
     *            Workbook name
     * @param fileName
     *            File name
     * @param reportTitle
     *            Report Title for PDF report
     * @param excludeHeaders
     *            key name which needs to exclude
     * @param xslFile
     *            XSL File for PDF report generation if null then default XSL
     *            file will use
     * @return report
     * @throws URISyntaxException
     */
    public ReportFileType generateListingReport(
            List<?> reportData,
            String exportType,
            Map<String, String> reportHeader,
            String workBookName,
            String fileName,
            String reportTitle,
            List<String> excludeHeaders,
            InputStream xslFile) {

        HttpHeaders httpHeaders = new HttpHeaders();
        File outputFile = null;
        ReportFileType report;
        InputStream xslfile=null;
        try {
            if (Constants.PDF.equals(exportType)) {
            	File logoAsFile = File.createTempFile("sun_logo", ".jpg");
				FileUtils.copyInputStreamToFile(new ClassPathResource("sun_logo.jpg").getInputStream(), logoAsFile);
                DigitalSignatureBean signatureBean = new DigitalSignatureBean();
                signatureBean.setFileName(fileName);
                signatureBean.setReportTitle(reportTitle);
                outputFile = pdfGenerator.createPDF(
                        reportData,
                        xslfile,
                        logoAsFile.getAbsolutePath(),
                        null,
                        signatureBean,
                        reportHeader.values().stream().collect(Collectors.toList()),
                        excludeHeaders);
                httpHeaders.setContentType(MediaType.APPLICATION_PDF);

            } else if (Constants.XLS.equals(exportType)) {

                ObjectMapper mapper = new ObjectMapper();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                mapper.writeValue(outStream, reportData);

                outputFile = xlsGenerator.createExcel(
                        new JSONArray(new String(outStream.toByteArray())),
                        reportHeader,
                        fileName,
                        workBookName);
                httpHeaders.setContentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_XLS));

            } else if (Constants.CSV.equals(exportType)) {

                ObjectMapper mapper = new ObjectMapper();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                mapper.writeValue(outStream, reportData);
                outputFile = csvGenerator
                        .createCSV(
                                new JSONArray(new String(outStream.toByteArray())),
                                reportHeader,
                                fileName);
                httpHeaders.setContentType(MediaType.parseMediaType(Constants.MEDIA_TYPE_CSV));

            }
            if (outputFile == null) {
                throw new CustomException("Error in generating " + exportType + " report.");
            } else {
                httpHeaders.setContentDispositionFormData(Constants.FILE, outputFile.getName());
            }
            report = new ReportFileType();
            report.setHttpHeader(httpHeaders);
            report.setReportFile(outputFile);
            return report;
        } catch (IOException | CustomException e) {
            throw new RuntimeException("Error while generating report file.", e);
        }
    }

}
