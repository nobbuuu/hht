package com.booyue.net.request.retrofit.upload;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * @author: wangxinhua
 * @date: 2019/4/11
 * @description :
 */
public class FileUploadRequestBody extends RequestBody{
    public interface UploadListener {
        void onProgress(int progress );
    }
    // okio.Segment.SIZE
    public static final int SEGMENT_SIZE = 2*1024;

    protected File file;
    protected UploadListener listener;
//    protected String contentType;
    public FileUploadRequestBody(File file, UploadListener listener) {
        this.file = file;
//        this.contentType = contentType;
        this.listener = listener;
    }

    protected FileUploadRequestBody() {

    }

    @Override
    public long contentLength() {
        return file.length();
    }

    @Override
    public MediaType contentType() {
        return MultipartBody.FORM;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(file);
            long total = 0;
            int progress = 0;
            long read;
            while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                total += read;
                progress = (int) (total * 100f/contentLength());
                sink.flush();
                this.listener.onProgress(progress);
            }

        } finally {
            Util.closeQuietly(source);
        }
    }
}
