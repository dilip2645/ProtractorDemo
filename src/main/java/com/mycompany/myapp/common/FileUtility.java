package com.mycompany.myapp.common;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class FileUtility {
	
	private static final int GENERATE_TWO_DIGIT_NUM = 2;
	
	@Value("${directory}")
    private  String directoryPath;
	
	 FileUtility(){
		/* Default Constructor */
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
	 public  synchronized File createFile(String fileName, String extension) {
	        File file;
	        boolean exists = true;
	        SecureRandom random;
	        long timeMilis;
	        String filePath;
	        File directory;
	        if(directoryPath != null && !directoryPath.isEmpty()){
	           directory = new File(directoryPath  + File.separator
	                + extension.replace(".", ""));
	        }else{
	        	
	        	 directory = new File(System.getProperty(Constants.USER_HOME)  + File.separator
	                     + extension.replace(".", ""));
	        }

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
	     * Convert base64 string of image in PNG format and resize image
	     * 
	     * @param base64
	     *            BASE64 string of image
	     * @param propertyFileName
	     *            Property file name
	     * @param fileName
	     *            file name
	     * @return signature return signature file of user
	     * @param width
	     * @param height
	     * @throws IOException
	     * @throws CustomException
	     */
	    public  File generateSignatureImage(String base64Image, int width, int height, 
	            String fileName) throws CustomException {

	        byte[] decodedBase64 = DatatypeConverter.parseBase64Binary(base64Image.substring(base64Image.indexOf(',')));
	        try (ByteArrayInputStream array = new ByteArrayInputStream(decodedBase64)) {
	            File signatureFileName = createFile(fileName,Constants.IMG_EXTENTION);
	            BufferedImage image = ImageIO.read(array);
	            ImageIO.write(image, Constants.IMG_FORMAT, signatureFileName);
	            BufferedImage originalImage = ImageIO.read(signatureFileName);
	            BufferedImage resizeImagePng = resizeImage(originalImage, width, height);
	            ImageIO.write(resizeImagePng, Constants.IMG_FORMAT, signatureFileName);
	            return signatureFileName;
	        } catch (IOException e) {
	            throw new CustomException("Error in converting Base64Url to image: " + e);
	        }
	    }
	    
	    /**
	     * This method will resize image
	     * 
	     * @param image
	     *            image to resize
	     * @param width
	     *            width
	     * @param height
	     *            height
	     * @return resizedImage return image
	     */
	    public  BufferedImage resizeImage(BufferedImage image, int width, int height) {
	        Image temporaryImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D graphics = resizedImage.createGraphics();
	        graphics.drawImage(temporaryImage, 0, 0, null);
	        graphics.dispose();
	        return resizedImage;
	    }
	
}
