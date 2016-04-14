package com.toughegg.andytools.http.cache;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Andy on 15/7/8.
 */
public  class CountingInputStream extends FilterInputStream {
    private int bytesRead = 0;

    public CountingInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        int result = super.read();
        if (result != -1) {
            bytesRead++;
        }
        return result;
    }

    @Override
    public int read(byte[] buffer, int offset, int count) throws IOException {
        int result = super.read(buffer, offset, count);
        if (result != -1) {
            bytesRead += result;
        }
        return result;
    }
}

