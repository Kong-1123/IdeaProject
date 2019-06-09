package cn.com.dhcc.creditquery.person.queryapi.util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import cn.com.dhcc.credit.platform.util.MD5;
import sun.misc.BASE64Encoder;
public class GzipUtils {

	    /**
	     * 
	     * 使用gzip进行压缩
	     */
	    public static String gzip(byte[] primStr) {
	        

	        ByteArrayOutputStream out = new ByteArrayOutputStream();

	        GZIPOutputStream gzip = null;
	        try {
	            gzip = new GZIPOutputStream(out);
	            gzip.write(primStr);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (gzip != null) {
	                try {
	                    gzip.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        String encodeBase64String = Base64.encodeBase64String(out.toByteArray());
	        return encodeBase64String;
	    }

	    /**
	     *
	     * <p>
	     * Description:使用gzip进行解压缩
	     * </p>
	     * 
	     * @param compressedStr
	     * @return
	     */
	    public static byte[] gunzip(String compressedStr) {
	        if (compressedStr == null) {
	            return null;
	        }

	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        ByteArrayInputStream in = null;
	        GZIPInputStream ginzip = null;
	        byte[] compressed = null;
	        byte[] decompressed = null;
	        try {
	            compressed = Base64.decodeBase64(compressedStr);
	            in = new ByteArrayInputStream(compressed);
	            ginzip = new GZIPInputStream(in);

	            byte[] buffer = new byte[1024];
	            int offset = -1;
	            while ((offset = ginzip.read(buffer)) != -1) {
	                out.write(buffer, 0, offset);
	            }
	            decompressed = out.toByteArray();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (ginzip != null) {
	                try {
	                    ginzip.close();
	                } catch (IOException e) {
	                }
	            }
	            if (in != null) {
	                try {
	                    in.close();
	                } catch (IOException e) {
	                }
	            }
	            if (out != null) {
	                try {
	                    out.close();
	                } catch (IOException e) {
	                }
	            }
	        }

	        return decompressed;
	    }

	    /**
	     * 使用zip进行压缩
	     * 
	     * @param str
	     *            压缩前的文本
	     * @return 返回压缩后的文本
	     */
	    public static final String zip(String str) {
	        if (str == null)
	            return null;
	        byte[] compressed;
	        ByteArrayOutputStream out = null;
	        ZipOutputStream zout = null;
	        String compressedStr = null;
	        try {
	            out = new ByteArrayOutputStream();
	            zout = new ZipOutputStream(out);
	            zout.putNextEntry(new ZipEntry("0"));
	            zout.write(str.getBytes());
	            zout.closeEntry();
	            compressed = out.toByteArray();
	            compressedStr = new sun.misc.BASE64Encoder().encodeBuffer(compressed);
	        } catch (IOException e) {
	            compressed = null;
	        } finally {
	            if (zout != null) {
	                try {
	                    zout.close();
	                } catch (IOException e) {
	                }
	            }
	            if (out != null) {
	                try {
	                    out.close();
	                } catch (IOException e) {
	                }
	            }
	        }
	        return compressedStr;
	    }

	    /**
	     * 使用zip进行解压缩
	     * 
	     * @param compressed
	     *            压缩后的文本
	     * @return 解压后的字符串
	     */
	    public static final String unzip(String compressedStr) {
	        if (compressedStr == null) {
	            return null;
	        }

	        ByteArrayOutputStream out = null;
	        ByteArrayInputStream in = null;
	        ZipInputStream zin = null;
	        String decompressed = null;
	        try {
	            byte[] compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
	            out = new ByteArrayOutputStream();
	            in = new ByteArrayInputStream(compressed);
	            zin = new ZipInputStream(in);
	            zin.getNextEntry();
	            byte[] buffer = new byte[1024];
	            int offset = -1;
	            while ((offset = zin.read(buffer)) != -1) {
	                out.write(buffer, 0, offset);
	            }
	            decompressed = out.toString();
	        } catch (IOException e) {
	            decompressed = null;
	        } finally {
	            if (zin != null) {
	                try {
	                    zin.close();
	                } catch (IOException e) {
	                }
	            }
	            if (in != null) {
	                try {
	                    in.close();
	                } catch (IOException e) {
	                }
	            }
	            if (out != null) {
	                try {
	                    out.close();
	                } catch (IOException e) {
	                }
	            }
	        }
	        return decompressed;
	    }
	    
	    /**
	     * @Description: 根据图片地址转换为base64编码字符串
	     * @Author: 
	     * @CreateTime: 
	     * @return
	     */
	    public static String getImageStr(String imgFile) {
	        InputStream inputStream = null;
	        byte[] data = null;
	        try {
	            inputStream = new FileInputStream(imgFile);
	            data = new byte[inputStream.available()];
	            inputStream.read(data);
	            inputStream.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        // 加密
	        BASE64Encoder encoder = new BASE64Encoder();
	        return encoder.encode(data);
	    }
	    
	    public static void main(String[] args) throws IOException {
	    	System.out.println("start===============");
//			String path = "C:\\Users\\trident\\Desktop\\faststone\\000.jpg";
			String path =  "E:\\download\\druid-master\\publications\\radstack\\figures\\tpch_scaling.pdf";
			File file = new File(path);
			byte[] readFileToByteArray = FileUtils.readFileToByteArray(file);
			
			String gzip = gzip(readFileToByteArray);
			System.out.println("gzip================");
			System.out.println(gzip);
			
			String md5String = MD5.getMd5String(gzip);
			System.out.println(md5String);
			System.out.println("md5====================");
		}
	     
}

