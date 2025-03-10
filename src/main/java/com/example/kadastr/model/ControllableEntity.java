package com.example.kadastr.model;

import java.util.UUID;

//Marks entity that can has the controller(definite user)
public interface ControllableEntity {

    /**
     * finds uuid of user who is entity controller
     * @return uuid of user who is entity controller
     */
    UUID getEntityControllerUuid();

}
