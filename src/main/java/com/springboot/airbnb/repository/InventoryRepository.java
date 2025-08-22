package com.springboot.airbnb.repository;

import com.springboot.airbnb.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}