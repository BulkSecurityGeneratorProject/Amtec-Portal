package com.amtrak.application.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.amtrak.application.domain.TeamEvent} entity. This class is used
 * in {@link com.amtrak.application.web.rest.TeamEventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /team-events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TeamEventCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter start;

    private InstantFilter end;

    public TeamEventCriteria(){
    }

    public TeamEventCriteria(TeamEventCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.start = other.start == null ? null : other.start.copy();
        this.end = other.end == null ? null : other.end.copy();
    }

    @Override
    public TeamEventCriteria copy() {
        return new TeamEventCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getStart() {
        return start;
    }

    public void setStart(InstantFilter start) {
        this.start = start;
    }

    public InstantFilter getEnd() {
        return end;
    }

    public void setEnd(InstantFilter end) {
        this.end = end;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TeamEventCriteria that = (TeamEventCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(start, that.start) &&
            Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        start,
        end
        );
    }

    @Override
    public String toString() {
        return "TeamEventCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (start != null ? "start=" + start + ", " : "") +
                (end != null ? "end=" + end + ", " : "") +
            "}";
    }

}
