package com.taemin.user.dto.response;

import jakarta.validation.constraints.NotBlank;

public record LoginResponse(@NotBlank String accessToken) {
}