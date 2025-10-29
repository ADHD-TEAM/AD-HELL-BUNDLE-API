package com.adhd.ad_hell.domain.user.command.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public enum Role {
    USER,
    ADMIN;

}
