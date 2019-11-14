package com.mycompany.myapp.common;



import java.math.BigDecimal;

public final class Constants {

    public static final String SCHEMA = "dbo";
    public static final String CATALOG = "prod01";
    public static final String USER_CACHE = "UserCache";
    public static final String NA = "0";
    public static final String EMPTY_STRING = "";
    public static final String IMAGE_NA = "";
    public static final String DECIMAL_NA = "0.00";
    public static final Double DOUBLE_DEFAULT = 0.00;
    public static final BigDecimal BIG_DECIMAL_DEFAULT = BigDecimal.ZERO;
    public static final String AUTHORIZATION_HEADER = "PHX-Authorization";
   
    /******************** File ********************/

    public static final String PDF_EXTENTION = ".pdf";
    public static final String XML_EXTENTION = ".xml";
    public static final String XLSX_EXTENTION = ".xlsx";
    public static final String CSV_EXTENTION = ".csv";
    public static final String PDF = "pdf";
    public static final String XLS = "xls";
    public static final String CSV = "csv";
    public static final String ZIP_EXTENTION = ".zip";
    public static final String IMG_EXTENTION = ".png";
    public static final String WORKBOOK_NAME = "report";
    public static final String IMG_FORMAT = "png";
    public static final String MEDIA_TYPE_CSV = "text/csv";
    public static final String MEDIA_TYPE_XLS = "application/vnd.ms-excel";
    public static final String FILE = "File";
    public static final String DIRECTORY = "DIRECTORY";
    public static final String USER_HOME = "user.home";
    public static final String DEFAULT_FILE = "default";
    public static final int TOKEN_EXPIRATION_TIME = 3600;
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_MM_DD_YY = "MM/dd/yy";
    public static final String DATE_FORMAT_DD_MM_YY_TIMESTAMP ="dd/MM/yy HH:mm:ss";
    public static final String DATE_FORMAT_DD_MM_YYYY_TIMESTAMP ="dd/MM/yyyy HH:mm:ss";
    public static final String DATE_FORMAT_DD_MMM_YY = "dd MMM yyyy";
    public static final String DEFAULT_DATE_FORMAT = "EEE MMM dd HH:mm:ss z yyyy";
    public static final String DATE_FORMAT_MM_DD_YYYY = "MM/dd/yyyy";
    public static final String DATE_FORMAT_YYYYMMDD= "yyyyMMdd";
    private Constants() {
        /** Default constructor **/
        
    }

}
