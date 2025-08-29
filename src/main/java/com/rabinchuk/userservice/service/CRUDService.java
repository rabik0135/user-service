package com.rabinchuk.userservice.service;

import java.util.List;

public interface CRUDService<T, U> {
    List<T> getAll();

    T getById(Long id);

    List<T> getByIds(List<Long> ids);

    T create(U u);

    T updateById(Long id, U u);

    void deleteById(Long id);
}
