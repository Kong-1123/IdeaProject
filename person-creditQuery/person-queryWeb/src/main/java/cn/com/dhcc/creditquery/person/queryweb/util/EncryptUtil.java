package cn.com.dhcc.creditquery.person.queryweb.util;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptUtil {
	
	private static Logger log = LoggerFactory.getLogger(EncryptUtil.class);
	
	private static String sale = "E81B4E4FECE753A48AB8E5A1396A391C909A0F9127418A5D41A2150FB1D885A4";


	public static String encryptByRC4(String content, String password) {
		log.info("encrypt RC4 start");
        try {
        	String ciphertext = encrypt(content, password);
        	String b64str = Base64.encodeBase64String(StringUtils.getBytesUtf8(ciphertext));
			log.info("encrypt RC4 end");
			return b64str;
		} catch (Exception e) {
			log.error("encrypt RC4 error:", e);
			return null;
		}
	}
	
	private static String encrypt(String content, String password){
		Integer[] S = new Integer[256]; // S盒
		Character[] keySchedul = new Character[content.length()]; // 生成的密钥流
		StringBuilder ciphertext = new StringBuilder();

		ksa(S, password);
		rpga(S, keySchedul, content.length());

		for (int i = 0; i < content.length(); ++i) {
		    ciphertext.append((char) (content.charAt(i) ^ keySchedul[i]));
		}
		return ciphertext.toString();
	}
	
	public static String decryptByRC4(String content, String password) {
		log.info("decrypt RC4 start");
        try {
        	String ciphertext = new String(Base64.decodeBase64(content), Charsets.UTF_8);
        	String decrypt = encrypt(ciphertext, password);
        	log.info("decrypt RC4 end");
        	return decrypt;
		} catch (Exception e) {
			log.error("decrypt RC4 error:", e);
			return null;
		}
	}
	
	//解密
		public static String decryptByRC4(String content) {
			log.info("decrypt RC4 start");
	        try {
	        	String ciphertext = new String(Base64.decodeBase64(content), Charsets.UTF_8);
	        	String decrypt = encrypt(ciphertext, sale);
	        	log.info("decrypt RC4 end");
	        	return decrypt;
			} catch (Exception e) {
				log.error("decrypt RC4 error:", e);
				return null;
			}
		}
		
	
    // 1.1 KSA--密钥调度算法--利用key来对S盒做一个置换，也就是对S盒重新排列
    private static void ksa(Integer[] s, String key) {
        for (int i = 0; i < 256; ++i) {
            s[i] = i;
        }

        int j = 0;
        for (int i = 0; i < 256; ++i) {
            j = (j + s[i] + key.charAt(i % key.length())) % 256;
            swap(s, i, j);
        }
    }

    // 1.2 RPGA--伪随机生成算法--利用上面重新排列的S盒来产生任意长度的密钥流
    private static void rpga(Integer[] s, Character[] keySchedul, int plaintextLength) {
        int i = 0, j = 0;
        for (int k = 0; k < plaintextLength; ++k) {
            i = (i + 1) % 256;
            j = (j + s[i]) % 256;
            swap(s, i, j);
            keySchedul[k] = (char) (s[(s[i] + s[j]) % 256]).intValue();
        }
    }

    // 1.3 置换
    private static void swap(Integer[] s, int i, int j) {
        Integer mTemp = s[i];
        s[i] = s[j];
        s[j] = mTemp;
    }
    
	public static void main(String[] args) throws Exception {
		String text = "<html><body>2333333</body></html>";
		String key = "233";
		String enc = encryptByRC4(text, key);
		System.out.println(enc);
		String dec = decryptByRC4(enc, key);
		System.out.println(dec);
	}
}
