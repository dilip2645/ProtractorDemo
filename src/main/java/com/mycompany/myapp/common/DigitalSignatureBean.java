package com.mycompany.myapp.common;


import java.io.Serializable;

public class DigitalSignatureBean implements Serializable {

    private static final long serialVersionUID = 1237525602076028401L;

    private String fileName;
    private String reportTitle;
    private int xCoordinate;
    private int yCoordinate;
    private int zoomPercentage;
    private int pageNo;
    private String signerName;
    private String userSignature;

    public DigitalSignatureBean() {
        /* Default constructor */
    }

   /**
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return
     */
    public String getReportTitle() {
        return reportTitle;
    }

    /**
     * @param reportTitle
     */
    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    /**
     * @return
     */
    public int getxCoordinate() {
        return xCoordinate;
    }

    /**
     * @param xCoordinate
     */
    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    /**
     * @return
     */
    public int getyCoordinate() {
        return yCoordinate;
    }

    /**
     * @param yCoordinate
     */
    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    /**
     * @return
     */
    public int getZoomPercentage() {
        return zoomPercentage;
    }

    /**
     * @param zoomPercentage
     */
    public void setZoomPercentage(int zoomPercentage) {
        this.zoomPercentage = zoomPercentage;
    }

    /**
     * @return
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * @param pageNo
     */
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * @return
     */
    public String getSignerName() {
        return signerName;
    }

    /**
     * @param signerName
     */
    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    /**
     * @return
     */
    public String getUserSignature() {
        return userSignature;
    }

    /**
     * @param userSignature
     */
    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }

}
