package Shared.Requests;

import java.io.Serializable;

public class AddNewPeopleRequest implements Serializable{
    private int user2_id;

    public AddNewPeopleRequest(int user2_id) {
        this.user2_id = user2_id;
    }

    public int getUser2ID() {
        return user2_id;
    }
    
}
