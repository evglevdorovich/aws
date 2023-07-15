package com.example.warehouse.repository;


import com.example.warehouse.model.WarehouseLog;
import com.example.warehouse.model.WarehouseView;

import java.util.List;
import java.util.Set;

public interface WarehouseRepository {

    WarehouseView getViewById(String productId);
    Set<WarehouseView> getViewsByIds(List<String> productId);
    void updateWarehouseWithViews(Set<WarehouseView> views, Set<WarehouseLog> logs);

    List<WarehouseView> getAll();
}
