package Shared.Dtos;

import java.io.Serializable;

public class ReactionDto implements Serializable{
    private int userId;
    private int postId;
    private String reactionType;

    //constructor
    public ReactionDto(
        int userId,
        int postId,
        String reactionType
    ) {
        this.userId = userId;
        this.postId = postId;
        this.reactionType = reactionType;
    }

    public int getUserId() {
        return userId;
    }

    public int getPostId() {
        return postId;
    }

    public String getReactionType() {
        return reactionType;
    }
}
