package com.springboot.airbnb.service;

import com.springboot.airbnb.entity.Room;

public interface InventoryService {

    void initializeInventoryForAYear(Room room);

    void deleteInventory(Room room);
}
