package Shared.Requests;

import java.io.Serializable;

public class BaseRequest<T> implements Serializable{
    private String token;
    private T payload;

    public BaseRequest(String token, T payload) {
        this.token = token;
        this.payload = payload;
    }

    public String getToken() {
        return token;
    }

    public T getPayLoad() {
        return payload;
    }
    
}
