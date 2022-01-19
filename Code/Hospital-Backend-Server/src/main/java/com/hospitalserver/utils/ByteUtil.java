package com.hospitalserver.utils;

import java.nio.ByteBuffer;

/**
 * @author https://stackoverflow.com/questions/4485128/how-do-i-convert-long-to-byte-and-back-in-java#4485196
 * Metodo que passa long para Byte[] e Byte[] para um long.
 */
public class ByteUtil {
	
	private ByteUtil() {}
    
	private static ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);    

    public static byte[] longToBytes(long x) {
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();
        return buffer.getLong();
    }
}
