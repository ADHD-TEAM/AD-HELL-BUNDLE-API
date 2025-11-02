package com.adhd.ad_hell.advertise.command.domain.aggregate;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public enum AdStatus {
    ACTIVATE,
    DEACTIVATED;

}