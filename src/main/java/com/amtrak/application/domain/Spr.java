package com.amtrak.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

import com.amtrak.application.domain.enumeration.Territory;

import com.amtrak.application.domain.enumeration.Priority;

import com.amtrak.application.domain.enumeration.Resolution;

/**
 * A Spr.
 */
@Entity
@Table(name = "spr")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "spr")
public class Spr implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "territory", nullable = false)
    private Territory territory;

    @NotNull
    @Column(name = "jhi_number", nullable = false, unique = true)
    private Integer number;

    @NotNull
    @Column(name = "full_number", nullable = false, unique = true)
    private String fullNumber;

    @Lob
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "resolution")
    private Resolution resolution;

    @Column(name = "jira_link")
    private String jiraLink;

    @Column(name = "reviewer_id")
    private Long reviewerId;

    @ManyToOne
    @JsonIgnoreProperties("sprs")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("sprs")
    private Release release;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Territory getTerritory() {
        return territory;
    }

    public Spr territory(Territory territory) {
        this.territory = territory;
        return this;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

    public Integer getNumber() {
        return number;
    }

    public Spr number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getFullNumber() {
        return fullNumber;
    }

    public Spr fullNumber(String fullNumber) {
        this.fullNumber = fullNumber;
        return this;
    }

    public void setFullNumber(String fullNumber) {
        this.fullNumber = fullNumber;
    }

    public String getDescription() {
        return description;
    }

    public Spr description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public Spr priority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public Spr resolution(Resolution resolution) {
        this.resolution = resolution;
        return this;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public String getJiraLink() {
        return jiraLink;
    }

    public Spr jiraLink(String jiraLink) {
        this.jiraLink = jiraLink;
        return this;
    }

    public void setJiraLink(String jiraLink) {
        this.jiraLink = jiraLink;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public Spr reviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
        return this;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public User getUser() {
        return user;
    }

    public Spr user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Release getRelease() {
        return release;
    }

    public Spr release(Release release) {
        this.release = release;
        return this;
    }

    public void setRelease(Release release) {
        this.release = release;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Spr)) {
            return false;
        }
        return id != null && id.equals(((Spr) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Spr{" +
            "id=" + getId() +
            ", territory='" + getTerritory() + "'" +
            ", number=" + getNumber() +
            ", fullNumber='" + getFullNumber() + "'" +
            ", description='" + getDescription() + "'" +
            ", priority='" + getPriority() + "'" +
            ", resolution='" + getResolution() + "'" +
            ", jiraLink='" + getJiraLink() + "'" +
            ", reviewerId=" + getReviewerId() +
            "}";
    }
}
