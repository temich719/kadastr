package com.example.kadastr.util;

public interface Mapper<T, S> {

    /**
     * maps DTO to model(entity)
     * @param s given DTO
     * @return model(entity) from DTO
     */
    T mapToModel(S s);

    /**
     * maps model(entity) to DTO
     * @param t given model
     * @return DTO from model(entity)
     */
    S mapToDto(T t);

}
