package com.kchonov.someboxserver.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "ganres", schema = "public", catalog = "postgres")
public class GanresEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ganre_id")
    private Integer ganreId;
    @Basic
    @Column(name = "ganre")
    private String ganre;

    public Integer getGanreId() {
        return ganreId;
    }

    public void setGanreId(Integer ganreId) {
        this.ganreId = ganreId;
    }

    public String getGanre() {
        return ganre;
    }

    public void setGanre(String ganre) {
        this.ganre = ganre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GanresEntity that = (GanresEntity) o;
        return ganreId == that.ganreId && Objects.equals(ganre, that.ganre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ganreId, ganre);
    }
}
