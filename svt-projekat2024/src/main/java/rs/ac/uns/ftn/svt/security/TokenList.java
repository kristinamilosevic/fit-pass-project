package rs.ac.uns.ftn.svt.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class TokenList {
    private final Set<String> list = ConcurrentHashMap.newKeySet();
    private final ActiveTokens activeTokens;

    public void listToken(String token) {
        list.add(token);
        activeTokens.removeToken(token);
    }

    public boolean isTokenListed(String token) {
        return list.contains(token);
    }
}
