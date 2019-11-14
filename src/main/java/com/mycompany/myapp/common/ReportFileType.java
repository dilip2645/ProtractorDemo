package com.mycompany.myapp.common;

/**This class defines getter setter for report file type **/
import java.io.File;
import java.util.List;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class ReportFileType {
    private File reportFile;
    private HttpHeaders httpHeader;
    private List<String> filePath;

    public ReportFileType() {
        /* Default constructor */
    }

    /**
     * @return
     */
    public File getReportFile() {
        return reportFile;
    }

    /**
     * @param reportFile
     */
    public void setReportFile(File reportFile) {
        this.reportFile = reportFile;
    }

    /**
     * @return
     */
    public HttpHeaders getHttpHeader() {
        return httpHeader;
    }

    /**
     * @param httpHeader
     */
    public void setHttpHeader(HttpHeaders httpHeader) {
        this.httpHeader = httpHeader;
    }

    public List<String> getFilePath() {
        return filePath;
    }

    public void setFilePath(List<String> filePath) {
        this.filePath = filePath;
    }

}
