package com.tripease.hotels.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalSecurityStore {
    public static final Map<String, String[]> store = new ConcurrentHashMap<>();
//    GlobalSecurityStore.store.put("authData",new String[]{userId, roles, secret});
}
