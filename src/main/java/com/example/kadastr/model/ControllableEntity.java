package com.example.kadastr.model;

import java.util.UUID;

public interface ControllableEntity {

    /**
     * finds uuid of user who is entity controller
     * @return uuid of user who is entity controller
     */
    UUID getEntityControllerUuid();

}
