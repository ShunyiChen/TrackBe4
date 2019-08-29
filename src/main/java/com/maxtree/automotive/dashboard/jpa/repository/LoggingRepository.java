package com.maxtree.automotive.dashboard.jpa.repository;

import com.maxtree.automotive.dashboard.jpa.entity.Logging;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LoggingRepository extends CrudRepository<Logging, Long>, PagingAndSortingRepository<Logging, Long>, JpaSpecificationExecutor<Logging> {
}
