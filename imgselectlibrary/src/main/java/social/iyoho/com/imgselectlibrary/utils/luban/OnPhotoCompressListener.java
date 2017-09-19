package social.iyoho.com.imgselectlibrary.utils.luban;

import java.util.ArrayList;

public interface OnPhotoCompressListener {

    /**
     * Fired when the compression is started, override to handle in your own code
     */
    void complete(ArrayList<String> imgs);

}
