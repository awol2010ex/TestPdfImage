package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfImageObject;

public class Test {

	public static void main(String[] args) throws IOException, DocumentException {
		// TODO Auto-generated method stub
		PdfReader reader = new PdfReader("G:\\新建文件夹\\46261.pdf");
		PRStream prStream = null;
		PdfImageObject pdfImageObject = null;
		PdfObject pdfObject = null;
		
		// Number of objects in pdf document
		int pdfObjectCount = reader.getXrefSize(); 
		int extractedImageCount = 0;
		for(int i = 0; i < pdfObjectCount; i++) {
			
			// Get the object at the index i in the objects collection
			pdfObject = reader.getPdfObject(i);
			
			// Object not found so continue
			if(pdfObject == null || !pdfObject.isStream())
			    continue;
			
			// Cast object to stream
			prStream = (PRStream)pdfObject;
			
			// Get the object type
			PdfObject type = prStream.get(PdfName.SUBTYPE);
			// Check if the object is the image type object
			if(type != null && type.toString().equals(PdfName.IMAGE.toString())) {
				
				// Get the image
				pdfImageObject = new PdfImageObject(prStream);
				
				// Convert the image to buffered image
			    BufferedImage bufferedImage = pdfImageObject.getBufferedImage();
			    
			    // Write the buffered image to local disk
			    File file =new File("G:\\新建文件夹\\46261-"+i+".jpg");
			    ImageIO.write(bufferedImage, "jpg", file);
			    Result result =null;
			    try{
			    result =scanningImage(file);
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
			    if(result!=null)
			    System.out.println(result.getText());
			    
			    file.deleteOnExit();
			    extractedImageCount++;
			}
		}
	}
	
	
	public static  Result scanningImage(File file) throws IOException {  
		  //get the data from the input stream  
        BufferedImage image = ImageIO.read(file);  
  
        //convert the image to a binary bitmap source  
        LuminanceSource source = new BufferedImageLuminanceSource(image);  
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source)); 
        
        QRCodeReader reader = new QRCodeReader();  
        try {  
  
            return reader.decode(bitmap) ;
  
        } catch (NotFoundException e) {  
  
            //e.printStackTrace();  
  
        } catch (ChecksumException e) {  
  
            //e.printStackTrace();  
  
        } catch (FormatException e) {  
  
            //e.printStackTrace();  
  
        }  
  
        return null;  
  
    }  

}
