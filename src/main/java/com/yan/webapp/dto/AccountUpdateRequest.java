package com.yan.webapp.dto;

public record AccountUpdateRequest(
        String firstName,
        String lastName,
        String password
) {}
