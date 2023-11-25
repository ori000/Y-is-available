package Shared.Dtos;

import java.io.Serializable;

public class CommentDto implements Serializable{
    public int commentId;
    public int postId;
    public int userId;
    public String commentText;
    public String commentDate;

    //constructor
    public CommentDto(
        int commentId,
        int postId,
        int userId,
        String commentText,
        String commentDate
    ) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.commentText = commentText;
        this.commentDate = commentDate;
    }

    //getters
    public int getCommentId() {
        return commentId;
    }

    public int getPostId() {
        return postId;
    }

    public int getUserId() {
        return userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getCommentDate() {
        return commentDate;
    }
}
