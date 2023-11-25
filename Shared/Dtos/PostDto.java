package Shared.Dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostDto implements Serializable{
    private int postId;
    private int userId;
    private String postText;
    private String postDate;
    private List<CommentDto> comments; // Assuming a post can have multiple comments
    private List<Integer> likes; // Assuming a post can have multiple likes
    private List<MediaDto> media; // Assuming a post can have multiple media items

    //constructor
    public PostDto(
        int postId,
        int userId,
        String postText,
        String postDate,
        List<CommentDto> comments,
        List<Integer> likes,
        List<MediaDto> media
    ) {
        this.postId = postId;
        this.userId = userId;
        this.postText = postText;
        this.postDate = postDate;
        this.comments = comments != null ? comments : new ArrayList<CommentDto>();
        this.likes = likes != null ? likes : new ArrayList<Integer>();
        this.media = media != null ? media : new ArrayList<MediaDto>();
    }

    //getters
    public int getPostId() {
        return postId;
    }

    public int getUserId() {
        return userId;
    }

    public String getPostText() {
        return postText;
    }

    public String getPostDate() {
        return postDate;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public List<Integer> getLikes() {
        return likes;
    }

    public List<MediaDto> getMedia() {
        return media;
    }
}