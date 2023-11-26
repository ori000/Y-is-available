package Shared.Requests;

import java.io.Serializable;

public class AddPostRequest implements Serializable {
    private String postText;
    private String mediaUrl;

    public AddPostRequest(String postContent, String mediaUrl) {
        this.postText = postContent;
        this.mediaUrl = mediaUrl != null ? mediaUrl : "";
    }

    public String getPostText() {
        return postText;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }
}
