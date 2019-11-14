package com.mycompany.myapp.common;


/** Used to create directory and file **/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;


public class FileOperation extends FileInputStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileOperation.class);
    private File file;
    private static final int GENERATE_TWO_DIGIT_NUM = 2;

    public FileOperation(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }

    /**
     * This method close file InputStream then delete file which pass into
     * constructor
     * 
     */
    @Override
    public void close() throws IOException {
        super.close();
        if (file.delete()) {
            LOGGER.info("File deleted successfully");
        }

    }

    /**
     * Get file path from Properties file
     * 
     * @param propFile
     *            property file
     * @param proprtyKey
     *            property key name
     * @return directory
     * @throws IOException
     * @throws CustomException
     */
    public static String getPropertyValue(File propFile, String proprtyKey) throws CustomException {
        String directory = null;

        try (InputStream input = new FileInputStream(propFile)) {
            Properties prop = new Properties();
            prop.load(input);
            directory = prop.getProperty(proprtyKey);
            return directory;
        } catch (IOException e) {
            throw new CustomException("Path Not Found : " + e);
        }
    }

    /**
     * create file with given extension
     * 
     * @param extension
     *            file extension
     * @param propertyFileName
     *            property file name
     * @param fileName
     *            file name
     * 
     * @return file return file
     * @throws IOException
     * @throws CustomException
     */
    public static synchronized File createFile(String extension, String propertyFileName, String fileName)
            throws CustomException {
        File file;
        boolean exists = true;
        SecureRandom random;
        long timeMilis;
        String filePath;
        File directory;
        String path;
        File propertyFile = getPropertyFile(propertyFileName);
        if (propertyFile == null) {
            throw new CustomException("Error reading reports property file, file is null ");
        }
        path = getPropertyValue(propertyFile, Constants.DIRECTORY);
        directory = new File(System.getProperty(Constants.USER_HOME) + File.separator + path + File.separator
                + extension.replace(".", ""));
        if (!directory.exists()) {
            directory.mkdirs();
        }
        filePath = directory.getAbsolutePath();
        file = new File(filePath + File.separator + fileName + extension);
        while (exists) {
            if (file.exists()) {
                timeMilis = System.currentTimeMillis();
                random = new SecureRandom();
                file = new File(filePath + File.separator + fileName + "_" + timeMilis
                        + random.nextInt(GENERATE_TWO_DIGIT_NUM) + extension);
                if (file.exists()) {
                    exists = true;
                } else {
                    exists = false;
                }
            } else {
                exists = false;
            }

        }

        return file;

    }

    /**
     * This method reads property file
     * 
     * @param filename
     * @throws CustomException
     ***/
    public static File getPropertyFile(String filename) throws CustomException {
        try {
            DefaultResourceLoader loader = new DefaultResourceLoader();
            Resource resource = loader.getResource("classpath:" + filename);
            return resource.getFile();
        } catch (IOException e) {
            throw new CustomException("Error reading file : " + filename + " " + e);
        }
    }
}
