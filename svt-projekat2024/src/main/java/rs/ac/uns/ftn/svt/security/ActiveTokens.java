package rs.ac.uns.ftn.svt.security;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ActiveTokens {

    private final Map<String, String> userTokenMap = new ConcurrentHashMap<>();

    public void addTokenForUser(String userEmail, String token) {
        userTokenMap.put(userEmail, token);
    }

    public void removeToken(String token) {
        userTokenMap.values().remove(token);
    }

    public boolean isTokenListedForUser(String userEmail) {
        return userTokenMap.containsKey(userEmail);
    }

    public String getTokenForUser(String userEmail) {
        return userTokenMap.get(userEmail);
    }
}
