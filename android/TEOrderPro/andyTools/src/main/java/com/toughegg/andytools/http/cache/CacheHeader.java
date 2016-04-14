package com.toughegg.andytools.http.cache;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 15/7/8.
 */
public class CacheHeader {
    public long size; // 缓存头大小
    public String key;
    public String etag;
    public long serverDate;
    public long ttl;
    public long softTtl;
    public Map<String, String> responseHeaders;
    private static final int CACHE_MAGIC = 0x20150423;

    private CacheHeader() {
    }

    public CacheHeader(String key, Entry entry) {
        this.key = key;
        this.size = entry.data.length;
        this.etag = entry.etag;
        this.serverDate = entry.serverDate;
        this.ttl = entry.ttl;
        this.responseHeaders = entry.responseHeaders;
    }

    /**
     * Reads the header off of an InputStream and returns a CacheHeader
     * object.
     *
     * @param is The InputStream to read from.
     * @throws IOException
     */
    public static CacheHeader readHeader(InputStream is) throws IOException {
        CacheHeader entry = new CacheHeader();
        int magic = readInt(is);
        if (magic != CACHE_MAGIC) {
            // don't bother deleting, it'll get pruned eventually
            throw new IOException();
        }
        entry.key = readString(is);
        entry.etag = readString(is);
        if (entry.etag.equals("")) {
            entry.etag = null;
        }
        entry.serverDate = readLong(is);
        entry.ttl = readLong(is);
        entry.softTtl = readLong(is);
        entry.responseHeaders = readStringStringMap(is);
        return entry;
    }

    /**
     * Creates a cache entry for the specified data.
     */
    public Entry toCacheEntry(byte[] data) {
        Entry e = new Entry();
        e.data = data;
        e.etag = etag;
        e.serverDate = serverDate;
        e.ttl = ttl;
        e.responseHeaders = responseHeaders;
        return e;
    }

    /**
     * Writes the contents of this CacheHeader to the specified
     * OutputStream.
     */
    public boolean writeHeader(OutputStream os) {
        try {
            writeInt(os, CACHE_MAGIC);
            writeString(os, key);
            writeString(os, etag == null ? "" : etag);
            writeLong(os, serverDate);
            writeLong(os, ttl);
            writeLong(os, softTtl);
            writeStringStringMap(responseHeaders, os);
            os.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static int read(InputStream is) throws IOException {
        int b = is.read();
        if (b == -1) {
            throw new EOFException();
        }
        return b;
    }

    public static void writeInt(OutputStream os, int n) throws IOException {
        os.write((n >> 0) & 0xff);
        os.write((n >> 8) & 0xff);
        os.write((n >> 16) & 0xff);
        os.write((n >> 24) & 0xff);
    }

    public static int readInt(InputStream is) throws IOException {
        int n = 0;
        n |= (read(is) << 0);
        n |= (read(is) << 8);
        n |= (read(is) << 16);
        n |= (read(is) << 24);
        return n;
    }

    static void writeLong(OutputStream os, long n) throws IOException {
        os.write((byte) (n >>> 0));
        os.write((byte) (n >>> 8));
        os.write((byte) (n >>> 16));
        os.write((byte) (n >>> 24));
        os.write((byte) (n >>> 32));
        os.write((byte) (n >>> 40));
        os.write((byte) (n >>> 48));
        os.write((byte) (n >>> 56));
    }

    static long readLong(InputStream is) throws IOException {
        long n = 0;
        n |= ((read(is) & 0xFFL) << 0);
        n |= ((read(is) & 0xFFL) << 8);
        n |= ((read(is) & 0xFFL) << 16);
        n |= ((read(is) & 0xFFL) << 24);
        n |= ((read(is) & 0xFFL) << 32);
        n |= ((read(is) & 0xFFL) << 40);
        n |= ((read(is) & 0xFFL) << 48);
        n |= ((read(is) & 0xFFL) << 56);
        return n;
    }

    static void writeString(OutputStream os, String s) throws IOException {
        byte[] b = s.getBytes("UTF-8");
        writeLong(os, b.length);
        os.write(b, 0, b.length);
    }

    static String readString(InputStream is) throws IOException {
        int n = (int) readLong(is);
        byte[] b = streamToBytes(is, n);
        return new String(b, "UTF-8");
    }

    static void writeStringStringMap(Map<String, String> map, OutputStream os) throws IOException {
        if (map != null) {
            writeInt(os, map.size());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                writeString(os, entry.getKey());
                writeString(os, entry.getValue());
            }
        } else {
            writeInt(os, 0);
        }
    }

    static Map<String, String> readStringStringMap(InputStream is) throws IOException {
        int size = readInt(is);
        Map<String, String> result = (size == 0) ? Collections.<String, String>emptyMap() : new HashMap<String, String>(size);
        for (int i = 0; i < size; i++) {
            String key = readString(is).intern();
            String value = readString(is).intern();
            result.put(key, value);
        }
        return result;
    }
    /**
     * Reads the contents of an InputStream into a byte[].
     * */
    private static byte[] streamToBytes(InputStream in, int length) throws IOException {
        byte[] bytes = new byte[length];
        int count;
        int pos = 0;
        while (pos < length && ((count = in.read(bytes, pos, length - pos)) != -1)) {
            pos += count;
        }
        if (pos != length) {
            throw new IOException("Expected " + length + " bytes, read " + pos + " bytes");
        }
        return bytes;
    }

}


