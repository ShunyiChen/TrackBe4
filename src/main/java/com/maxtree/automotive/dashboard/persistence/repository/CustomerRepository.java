package com.maxtree.automotive.dashboard.persistence.repository;

import java.util.List;

import com.maxtree.automotive.dashboard.persistence.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);
}
