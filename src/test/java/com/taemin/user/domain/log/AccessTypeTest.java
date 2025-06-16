package com.taemin.user.domain.log;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccessTypeTest {

    @Test
    @DisplayName("AccessType enum이 LOGIN과 LOGOUT 값을 가지는지 확인")
    void checkAccessTypeValues() {
        // when
        AccessType[] accessTypes = AccessType.values();

        // then
        assertThat(accessTypes).hasSize(2);
        assertThat(accessTypes).contains(AccessType.LOGIN, AccessType.LOGOUT);
    }
}