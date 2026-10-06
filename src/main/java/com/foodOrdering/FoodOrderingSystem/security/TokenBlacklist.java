package com.foodOrdering.FoodOrderingSystem.security;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TokenBlacklist {

    private Set<String> blacklistedTokens = new HashSet<>();

    public void addToken(String token) {  //blacklist mai token add jb koi user logout krta h to
        blacklistedTokens.add(token);
    }

    public boolean isBlacklisted(String token) {  // check kya blacklist mai token h ye
        return blacklistedTokens.contains(token);
    }
}