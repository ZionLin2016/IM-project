package cn.nio.client.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;


public abstract class CharactorUtils {
    
	// 解码器 ： 用 utf-16 进行解码 
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
