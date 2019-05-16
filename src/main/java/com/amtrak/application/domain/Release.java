package com.amtrak.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.amtrak.application.domain.enumeration.Territory;

/**
 * A Release.
 */
@Entity
@Table(name = "release")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "release")
public class Release implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private Instant date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "territory", nullable = false)
    private Territory territory;

    @NotNull
    @Column(name = "build", nullable = false)
    private Integer build;

    @NotNull
    @Column(name = "release_letter", nullable = false)
    private String releaseLetter;

    @Column(name = "prefix_letter")
    private String prefixLetter;

    @NotNull
    @Column(name = "database_version", nullable = false)
    private String databaseVersion;

    @Column(name = "ws_version")
    private String wsVersion;

    @Column(name = "tma_version")
    private String tmaVersion;

    @Column(name = "port")
    private Integer port;

    @Lob
    @Column(name = "new_features")
    private String newFeatures;

    @Lob
    @Column(name = "updated_features")
    private String updatedFeatures;

    @NotNull
    @Column(name = "jhi_current", nullable = false)
    private Boolean current;

    @OneToMany(mappedBy = "release")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Spr> sprs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public Release date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Territory getTerritory() {
        return territory;
    }

    public Release territory(Territory territory) {
        this.territory = territory;
        return this;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

    public Integer getBuild() {
        return build;
    }

    public Release build(Integer build) {
        this.build = build;
        return this;
    }

    public void setBuild(Integer build) {
        this.build = build;
    }

    public String getReleaseLetter() {
        return releaseLetter;
    }

    public Release releaseLetter(String releaseLetter) {
        this.releaseLetter = releaseLetter;
        return this;
    }

    public void setReleaseLetter(String releaseLetter) {
        this.releaseLetter = releaseLetter;
    }

    public String getPrefixLetter() {
        return prefixLetter;
    }

    public Release prefixLetter(String prefixLetter) {
        this.prefixLetter = prefixLetter;
        return this;
    }

    public void setPrefixLetter(String prefixLetter) {
        this.prefixLetter = prefixLetter;
    }

    public String getDatabaseVersion() {
        return databaseVersion;
    }

    public Release databaseVersion(String databaseVersion) {
        this.databaseVersion = databaseVersion;
        return this;
    }

    public void setDatabaseVersion(String databaseVersion) {
        this.databaseVersion = databaseVersion;
    }

    public String getWsVersion() {
        return wsVersion;
    }

    public Release wsVersion(String wsVersion) {
        this.wsVersion = wsVersion;
        return this;
    }

    public void setWsVersion(String wsVersion) {
        this.wsVersion = wsVersion;
    }

    public String getTmaVersion() {
        return tmaVersion;
    }

    public Release tmaVersion(String tmaVersion) {
        this.tmaVersion = tmaVersion;
        return this;
    }

    public void setTmaVersion(String tmaVersion) {
        this.tmaVersion = tmaVersion;
    }

    public Integer getPort() {
        return port;
    }

    public Release port(Integer port) {
        this.port = port;
        return this;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getNewFeatures() {
        return newFeatures;
    }

    public Release newFeatures(String newFeatures) {
        this.newFeatures = newFeatures;
        return this;
    }

    public void setNewFeatures(String newFeatures) {
        this.newFeatures = newFeatures;
    }

    public String getUpdatedFeatures() {
        return updatedFeatures;
    }

    public Release updatedFeatures(String updatedFeatures) {
        this.updatedFeatures = updatedFeatures;
        return this;
    }

    public void setUpdatedFeatures(String updatedFeatures) {
        this.updatedFeatures = updatedFeatures;
    }

    public Boolean isCurrent() {
        return current;
    }

    public Release current(Boolean current) {
        this.current = current;
        return this;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public Set<Spr> getSprs() {
        return sprs;
    }

    public Release sprs(Set<Spr> sprs) {
        this.sprs = sprs;
        return this;
    }

    public Release addSpr(Spr spr) {
        this.sprs.add(spr);
        spr.setRelease(this);
        return this;
    }

    public Release removeSpr(Spr spr) {
        this.sprs.remove(spr);
        spr.setRelease(null);
        return this;
    }

    public void setSprs(Set<Spr> sprs) {
        this.sprs = sprs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Release)) {
            return false;
        }
        return id != null && id.equals(((Release) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Release{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", territory='" + getTerritory() + "'" +
            ", build=" + getBuild() +
            ", releaseLetter='" + getReleaseLetter() + "'" +
            ", prefixLetter='" + getPrefixLetter() + "'" +
            ", databaseVersion='" + getDatabaseVersion() + "'" +
            ", wsVersion='" + getWsVersion() + "'" +
            ", tmaVersion='" + getTmaVersion() + "'" +
            ", port=" + getPort() +
            ", newFeatures='" + getNewFeatures() + "'" +
            ", updatedFeatures='" + getUpdatedFeatures() + "'" +
            ", current='" + isCurrent() + "'" +
            "}";
    }
}
