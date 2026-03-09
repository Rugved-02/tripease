package com.tripease.auth.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalSecurityStore {
    public static final Map<String, String[]> store = new ConcurrentHashMap<>();
}
