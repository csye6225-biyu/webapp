package com.yan.webapp.service;

public record AccountUpdateRequest(
        String firstName,
        String lastName,
        String password
) {}
