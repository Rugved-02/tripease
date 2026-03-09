package com.tripease.analytics.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalSecurityStore {
    public static final Map<String, String[]> store = new ConcurrentHashMap<>();
}
