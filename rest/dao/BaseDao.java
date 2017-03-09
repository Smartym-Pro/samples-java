package pro.smartum.reptracker.gateway.dao;

import java.io.Serializable;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import pro.smartum.reptracker.gateway.web.beans.FetchLimits;

interface BaseDao<T, PK extends Serializable> {

    void persist(T obj);

    @NotNull
    PK save(T obj);

    @Nullable
    T get(PK id);

    @Nullable
    T getDetailed(PK id);

    void delete(T obj);

    void update(T obj);

    long countAll();

    @NotNull
    List<T> list(FetchLimits fetchLimits);

    @NotNull
    List<PK> listIds(FetchLimits fetchLimits);

    void flush();
}
