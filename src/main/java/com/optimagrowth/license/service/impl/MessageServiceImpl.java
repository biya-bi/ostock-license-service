package com.optimagrowth.license.service.impl;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.optimagrowth.license.service.MessageService;

@Service
class MessageServiceImpl implements MessageService {
    private final MessageSource messageSource;

    MessageServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(String key, Locale locale, Object... args) {
        return messageSource.getMessage(key, args, locale);
    }

}
