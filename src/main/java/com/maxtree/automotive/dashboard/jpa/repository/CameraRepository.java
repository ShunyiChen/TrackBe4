package com.maxtree.automotive.dashboard.jpa.repository;

import com.maxtree.automotive.dashboard.jpa.entity.Camera;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CameraRepository extends CrudRepository<Camera, Long>, PagingAndSortingRepository<Camera, Long>, JpaSpecificationExecutor<Camera> {
}
