package com.maxtree.automotive.dashboard.jpa.repository;

import com.maxtree.automotive.dashboard.jpa.entity.Material;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MaterialRepository extends CrudRepository<Material, Long>, PagingAndSortingRepository<Material, Long>, JpaSpecificationExecutor<Material> {
}
