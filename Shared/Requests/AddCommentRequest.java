package Shared.Requests;

import java.io.Serializable;

public class AddCommentRequest implements Serializable{
    private int postID;
    private String comment;

    public AddCommentRequest(int postID, String comment) {
        this.postID = postID;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public int getPostID() {
        return postID;
    }
    
}
