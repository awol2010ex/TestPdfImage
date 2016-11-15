package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
//获取购买方名称 和销售方名称
public class Test2 {

	public static void main(String[] args) throws IOException,
			DocumentException {
		String path = "G:\\新建文件夹\\46261.pdf";

		getKeyWords(path);

	}

	/*
	 * 返回关键字所在的坐标和页数 float[0] >> X float[1] >> Y float[2] >> page
	 */
	public static void getKeyWords(String filePath) {

		final List<Object[]> list = new ArrayList<Object[]>();
		try {
			PdfReader pdfReader = new PdfReader(filePath);
			int pageNum = pdfReader.getNumberOfPages();
			PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(
					pdfReader);

			// 下标从1开始
			for (int i = 1; i <= pageNum; i++) {
				pdfReaderContentParser.processContent(i, new RenderListener() {

					@Override
					public void renderText(TextRenderInfo textRenderInfo) {
						String text = textRenderInfo.getText();
						/*
						System.out.println("start");
						System.out.println("line----"
								+ textRenderInfo.getBaseline()
										.getBoundingRectange());
						System.out.println("text---" + text);
						System.out.println("end");
*/
						list.add(new Object[] {
								textRenderInfo.getBaseline()
										.getBoundingRectange().x,
								textRenderInfo.getBaseline()
										.getBoundingRectange().y, text });
						;

					}

					@Override
					public void renderImage(ImageRenderInfo arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void endTextBlock() {
						// TODO Auto-generated method stub

					}

					@Override
					public void beginTextBlock() {
						// TODO Auto-generated method stub

					}
				});
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Collections.sort(list, new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				Object[] obj1 = (Object[]) o1;
				Object[] obj2 = (Object[]) o2;
				if ((Float) obj1[1] < (Float) obj2[1]) {
					return 1;
				} else if ((Float) obj1[1] > (Float) obj2[1]) {
					return -1;
				} else if ((Float) obj1[1] == (Float) obj2[1]) {
					if ((Float) obj1[0] < (Float) obj2[0]) {
						return 1;
					} else if ((Float) obj1[0] > (Float) obj2[0]) {
						return -1;
					}
				}
				return 0;
			}
		});
		List<String> nameList =new ArrayList<String>();
		for (int i=0,s=list.size();i<s;i++) {
		  Object[] obj =list.get(i);
          float x =(Float)obj[0];
          float y =(Float)obj[1];
          String text =((String) obj[2]).trim();
          //System.out.println(x+","+y+","+text);
        
          if("名".equals(text)){
        	     String lasttext = ((String)list.get(i-1)[2]).trim();
        	     float lastx =(Float)list.get(i-1)[0];
        	     if("称".equals(lasttext)){
        	    	    for( int k=i-10; k<i+10 ;k++  ){
        	    	    	if(k!=i-1){
        	    	    	   Object[] t =list.get(k);
        	    	    	   String temptext =((String)t[2]).trim();
        	    	    	   if(  !"".equals(temptext) && isChineseByREG(temptext)  &&  (Float)t[0]>lastx&&  Math.abs((Float)t[0]-lastx)<20   &&  Math.abs(y-(Float)t[1])<10      ){
        	    	    		   nameList.add(temptext);
        	    	    		   break ;
        	    	    	   }
        	    	    	}
        	    	    }
        	     }
          }
          
		}
		
		for(String name :nameList){
			System.out.println(name);
		}
	}

	// 只能判断部分CJK字符（CJK统一汉字）
    public static boolean isChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str.trim()).find();
    }
}
