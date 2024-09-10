package com.taemin.user.domain.user;

import com.taemin.user.external.dto.response.KakaoUserResponse;
import com.taemin.user.external.dto.response.GoogleUserResponse;
import com.taemin.user.external.dto.response.NaverUserResponse;
import com.taemin.user.type.OAuthProvider;
import com.taemin.user.type.Role;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record OAuth2User(
        String oAuthId,
        String name,
        String email,
        String profile
) {
    public static OAuth2User of(GoogleUserResponse googleUser) {
        return new OAuth2User(googleUser.getSub(), googleUser.getName(), googleUser.getEmail(), googleUser.getPicture());
    }

    public static OAuth2User of(KakaoUserResponse kakaoUserResponse) {
        return new OAuth2User(String.valueOf(kakaoUserResponse.getId()), kakaoUserResponse.getNickName(), kakaoUserResponse.getEmail(), kakaoUserResponse.getProfileImageUrl());
    }

    public static OAuth2User of(NaverUserResponse naverUserResponse) {
        return new OAuth2User(naverUserResponse.getResponse().getId(), naverUserResponse.getResponse().getName(), naverUserResponse.getResponse().getEmail(), naverUserResponse.getResponse().getProfileImage());
    }

    public User toEntity(OAuthProvider oAuthProvider) {
        return User.of(
                Name.of(name),
                Email.of(email),
                Profile.of(profile),
                Role.USER,
                oAuthProvider,
                OAuthId.of(oAuthId)
        );
    }
}