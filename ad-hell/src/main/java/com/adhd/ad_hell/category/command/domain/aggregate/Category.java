package com.adhd.ad_hell.category.command.domain.aggregate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    private String name;
    private String description;
    private CategoryStatus status;

    @Builder
    private Category(Category parent, String name, String description, CategoryStatus status) {
        this.parent = parent;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public void updateInfo(String name, String description) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
    }

    public void changeStatus(CategoryStatus status) {
        this.status = status;
    }
}
