package com.adhd.ad_hell.domain.advertise.command.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public enum Status {
    ACTIVATE,
    DEACTIVATED;

}