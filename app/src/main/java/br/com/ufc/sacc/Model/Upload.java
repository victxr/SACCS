package br.com.ufc.sacc.Model;

import android.net.Uri;
import com.google.android.gms.tasks.Task;

public class Upload {
    private String mName;
    private String mImageUrl;
    private Task<Uri> downloadUrl;


    public Upload(String name, String imageUrl) {
        mName = name;
        mImageUrl = imageUrl;
    }

    public void Upload() {

    }

    public Task<Uri> getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(Task<Uri> downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
