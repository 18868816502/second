package com.beihui.market.helper.updatehelper;


import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadHelper {

    public static boolean download(String url, String fileName, ProgressResponseListener listener) {
        boolean res = false;
        if (url != null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            OkHttpClient client = ProgressHelper.addProgressResponseListener(builder, listener);
            FileOutputStream fos = null;
            try {
                Response response = client.newCall(request).execute();
                if (response.code() == 200) {
                    res = true;
                    File dir = new File(Environment.getExternalStorageDirectory() + "/temp");
                    if (!dir.exists()) {
                        //noinspection ResultOfMethodCallIgnored
                        dir.mkdirs();
                    }
                    File file = new File(Environment.getExternalStorageDirectory() + "/temp", fileName + ".apk");
                    if (!file.exists()) {
                        res = file.createNewFile();
                    }
                    fos = new FileOutputStream(file);
                    fos.write(response.body().bytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return res;
    }
}
