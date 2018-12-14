package cn.nio.server.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;


public abstract class CharactorUtils {
    
	// ������ �� �� utf-16 ���н��� 
    public static CharsetDecoder utf16Decoder = Charset.forName("utf-16").newDecoder();
    
    public static byte[] utf16Encode(String s) {
        
        try {
            return s.getBytes("utf-16");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
