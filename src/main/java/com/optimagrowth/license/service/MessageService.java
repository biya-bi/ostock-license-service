package com.optimagrowth.license.service;

import java.util.Locale;

public interface MessageService {
    String getMessage(String key, Locale locale, Object... args);
}
