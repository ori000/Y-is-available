package Shared.Requests;
import java.io.Serializable;

import Shared.Enums.ReactionType;

public class AddReactionRequest implements Serializable {
    private int postId;
    private ReactionType reactionType;

    public AddReactionRequest(int postId, ReactionType reactionType) {
        this.postId = postId;
        this.reactionType = reactionType;
    }

    public int getPostId() {
        return postId;
    }

    public ReactionType getReactionType() {
        return reactionType;
    }
    
}
