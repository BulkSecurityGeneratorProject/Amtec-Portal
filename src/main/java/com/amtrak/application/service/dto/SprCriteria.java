package com.amtrak.application.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.amtrak.application.domain.enumeration.Territory;
import com.amtrak.application.domain.enumeration.Priority;
import com.amtrak.application.domain.enumeration.Resolution;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.amtrak.application.domain.Spr} entity. This class is used
 * in {@link com.amtrak.application.web.rest.SprResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sprs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SprCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Territory
     */
    public static class TerritoryFilter extends Filter<Territory> {

        public TerritoryFilter() {
        }

        public TerritoryFilter(TerritoryFilter filter) {
            super(filter);
        }

        @Override
        public TerritoryFilter copy() {
            return new TerritoryFilter(this);
        }

    }
    /**
     * Class for filtering Priority
     */
    public static class PriorityFilter extends Filter<Priority> {

        public PriorityFilter() {
        }

        public PriorityFilter(PriorityFilter filter) {
            super(filter);
        }

        @Override
        public PriorityFilter copy() {
            return new PriorityFilter(this);
        }

    }
    /**
     * Class for filtering Resolution
     */
    public static class ResolutionFilter extends Filter<Resolution> {

        public ResolutionFilter() {
        }

        public ResolutionFilter(ResolutionFilter filter) {
            super(filter);
        }

        @Override
        public ResolutionFilter copy() {
            return new ResolutionFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TerritoryFilter territory;

    private IntegerFilter number;

    private StringFilter fullNumber;

    private PriorityFilter priority;

    private ResolutionFilter resolution;

    private StringFilter jiraLink;

    private LongFilter reviewerId;

    private LongFilter userId;

    private LongFilter releaseId;

    public SprCriteria(){
    }

    public SprCriteria(SprCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.territory = other.territory == null ? null : other.territory.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.fullNumber = other.fullNumber == null ? null : other.fullNumber.copy();
        this.priority = other.priority == null ? null : other.priority.copy();
        this.resolution = other.resolution == null ? null : other.resolution.copy();
        this.jiraLink = other.jiraLink == null ? null : other.jiraLink.copy();
        this.reviewerId = other.reviewerId == null ? null : other.reviewerId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.releaseId = other.releaseId == null ? null : other.releaseId.copy();
    }

    @Override
    public SprCriteria copy() {
        return new SprCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public TerritoryFilter getTerritory() {
        return territory;
    }

    public void setTerritory(TerritoryFilter territory) {
        this.territory = territory;
    }

    public IntegerFilter getNumber() {
        return number;
    }

    public void setNumber(IntegerFilter number) {
        this.number = number;
    }

    public StringFilter getFullNumber() {
        return fullNumber;
    }

    public void setFullNumber(StringFilter fullNumber) {
        this.fullNumber = fullNumber;
    }

    public PriorityFilter getPriority() {
        return priority;
    }

    public void setPriority(PriorityFilter priority) {
        this.priority = priority;
    }

    public ResolutionFilter getResolution() {
        return resolution;
    }

    public void setResolution(ResolutionFilter resolution) {
        this.resolution = resolution;
    }

    public StringFilter getJiraLink() {
        return jiraLink;
    }

    public void setJiraLink(StringFilter jiraLink) {
        this.jiraLink = jiraLink;
    }

    public LongFilter getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(LongFilter reviewerId) {
        this.reviewerId = reviewerId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(LongFilter releaseId) {
        this.releaseId = releaseId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SprCriteria that = (SprCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(territory, that.territory) &&
            Objects.equals(number, that.number) &&
            Objects.equals(fullNumber, that.fullNumber) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(resolution, that.resolution) &&
            Objects.equals(jiraLink, that.jiraLink) &&
            Objects.equals(reviewerId, that.reviewerId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(releaseId, that.releaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        territory,
        number,
        fullNumber,
        priority,
        resolution,
        jiraLink,
        reviewerId,
        userId,
        releaseId
        );
    }

    @Override
    public String toString() {
        return "SprCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (territory != null ? "territory=" + territory + ", " : "") +
                (number != null ? "number=" + number + ", " : "") +
                (fullNumber != null ? "fullNumber=" + fullNumber + ", " : "") +
                (priority != null ? "priority=" + priority + ", " : "") +
                (resolution != null ? "resolution=" + resolution + ", " : "") +
                (jiraLink != null ? "jiraLink=" + jiraLink + ", " : "") +
                (reviewerId != null ? "reviewerId=" + reviewerId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (releaseId != null ? "releaseId=" + releaseId + ", " : "") +
            "}";
    }

}
