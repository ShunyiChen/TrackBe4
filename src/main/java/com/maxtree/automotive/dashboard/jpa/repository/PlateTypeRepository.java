package com.maxtree.automotive.dashboard.jpa.repository;

import com.maxtree.automotive.dashboard.jpa.entity.PlateType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PlateTypeRepository extends CrudRepository<PlateType, Long>, PagingAndSortingRepository<PlateType, Long>, JpaSpecificationExecutor<PlateType> {
}
