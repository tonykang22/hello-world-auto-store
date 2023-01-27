package com.github.kingwaggs.ordermanager.coupangsdk.domain;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.*;

import java.io.IOException;

public class ProgressResponseBody extends ResponseBody {
    private final ResponseBody responseBody;
    private final ProgressResponseBody.ProgressListener progressListener;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, ProgressResponseBody.ProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    public MediaType contentType() {
        return this.responseBody.contentType();
    }

    public long contentLength() {
        return this.responseBody.contentLength();
    }

    public BufferedSource source() {
        if (this.bufferedSource == null) {
            this.bufferedSource = Okio.buffer(this.source(this.responseBody.source()));
        }

        return this.bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                this.totalBytesRead += bytesRead != -1L ? bytesRead : 0L;
                ProgressResponseBody.this.progressListener.update(this.totalBytesRead, ProgressResponseBody.this.responseBody.contentLength(), bytesRead == -1L);
                return bytesRead;
            }
        };
    }

    public interface ProgressListener {
        void update(long var1, long var3, boolean var5);
    }
}
