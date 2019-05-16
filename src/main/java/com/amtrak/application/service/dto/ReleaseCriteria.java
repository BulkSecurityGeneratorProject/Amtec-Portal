package com.amtrak.application.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.amtrak.application.domain.enumeration.Territory;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.amtrak.application.domain.Release} entity. This class is used
 * in {@link com.amtrak.application.web.rest.ReleaseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /releases?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReleaseCriteria implements Serializable, Criteria {
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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter date;

    private TerritoryFilter territory;

    private IntegerFilter build;

    private StringFilter releaseLetter;

    private StringFilter prefixLetter;

    private StringFilter databaseVersion;

    private StringFilter wsVersion;

    private StringFilter tmaVersion;

    private IntegerFilter port;

    private BooleanFilter current;

    private LongFilter sprId;

    public ReleaseCriteria(){
    }

    public ReleaseCriteria(ReleaseCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.territory = other.territory == null ? null : other.territory.copy();
        this.build = other.build == null ? null : other.build.copy();
        this.releaseLetter = other.releaseLetter == null ? null : other.releaseLetter.copy();
        this.prefixLetter = other.prefixLetter == null ? null : other.prefixLetter.copy();
        this.databaseVersion = other.databaseVersion == null ? null : other.databaseVersion.copy();
        this.wsVersion = other.wsVersion == null ? null : other.wsVersion.copy();
        this.tmaVersion = other.tmaVersion == null ? null : other.tmaVersion.copy();
        this.port = other.port == null ? null : other.port.copy();
        this.current = other.current == null ? null : other.current.copy();
        this.sprId = other.sprId == null ? null : other.sprId.copy();
    }

    @Override
    public ReleaseCriteria copy() {
        return new ReleaseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDate() {
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }

    public TerritoryFilter getTerritory() {
        return territory;
    }

    public void setTerritory(TerritoryFilter territory) {
        this.territory = territory;
    }

    public IntegerFilter getBuild() {
        return build;
    }

    public void setBuild(IntegerFilter build) {
        this.build = build;
    }

    public StringFilter getReleaseLetter() {
        return releaseLetter;
    }

    public void setReleaseLetter(StringFilter releaseLetter) {
        this.releaseLetter = releaseLetter;
    }

    public StringFilter getPrefixLetter() {
        return prefixLetter;
    }

    public void setPrefixLetter(StringFilter prefixLetter) {
        this.prefixLetter = prefixLetter;
    }

    public StringFilter getDatabaseVersion() {
        return databaseVersion;
    }

    public void setDatabaseVersion(StringFilter databaseVersion) {
        this.databaseVersion = databaseVersion;
    }

    public StringFilter getWsVersion() {
        return wsVersion;
    }

    public void setWsVersion(StringFilter wsVersion) {
        this.wsVersion = wsVersion;
    }

    public StringFilter getTmaVersion() {
        return tmaVersion;
    }

    public void setTmaVersion(StringFilter tmaVersion) {
        this.tmaVersion = tmaVersion;
    }

    public IntegerFilter getPort() {
        return port;
    }

    public void setPort(IntegerFilter port) {
        this.port = port;
    }

    public BooleanFilter getCurrent() {
        return current;
    }

    public void setCurrent(BooleanFilter current) {
        this.current = current;
    }

    public LongFilter getSprId() {
        return sprId;
    }

    public void setSprId(LongFilter sprId) {
        this.sprId = sprId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReleaseCriteria that = (ReleaseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(territory, that.territory) &&
            Objects.equals(build, that.build) &&
            Objects.equals(releaseLetter, that.releaseLetter) &&
            Objects.equals(prefixLetter, that.prefixLetter) &&
            Objects.equals(databaseVersion, that.databaseVersion) &&
            Objects.equals(wsVersion, that.wsVersion) &&
            Objects.equals(tmaVersion, that.tmaVersion) &&
            Objects.equals(port, that.port) &&
            Objects.equals(current, that.current) &&
            Objects.equals(sprId, that.sprId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        date,
        territory,
        build,
        releaseLetter,
        prefixLetter,
        databaseVersion,
        wsVersion,
        tmaVersion,
        port,
        current,
        sprId
        );
    }

    @Override
    public String toString() {
        return "ReleaseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (territory != null ? "territory=" + territory + ", " : "") +
                (build != null ? "build=" + build + ", " : "") +
                (releaseLetter != null ? "releaseLetter=" + releaseLetter + ", " : "") +
                (prefixLetter != null ? "prefixLetter=" + prefixLetter + ", " : "") +
                (databaseVersion != null ? "databaseVersion=" + databaseVersion + ", " : "") +
                (wsVersion != null ? "wsVersion=" + wsVersion + ", " : "") +
                (tmaVersion != null ? "tmaVersion=" + tmaVersion + ", " : "") +
                (port != null ? "port=" + port + ", " : "") +
                (current != null ? "current=" + current + ", " : "") +
                (sprId != null ? "sprId=" + sprId + ", " : "") +
            "}";
    }

}
