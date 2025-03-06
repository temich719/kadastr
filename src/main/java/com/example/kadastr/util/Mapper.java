package com.example.kadastr.util;

public interface Mapper<T, S> {

    T mapToModel(S s);

    S mapToDto(T t);

}
