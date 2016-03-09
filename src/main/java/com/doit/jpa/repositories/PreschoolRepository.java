package com.doit.jpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.doit.jpa.model.Preschool;

/* 
 * IMPORTANT TO NOTE HERE!
 * 
 * - extend with Sring CrudRepository type overrides for Entity and Id types; where 
 * Spring Data framework will provide an implementation for standard interfaces at runtime injection
 * - extend with DISJOINT custom repository interface for overriding with CUSTOM HQL Bulk updates;
 * i.e. for BULK update and delete transactions
 * - PagingAndSortingRepository is a SUPERSET of the above, with paging and sorting capabilities
 * 
 */
public interface PreschoolRepository extends PagingAndSortingRepository<Preschool, Long>, PreschoolRepositoryCustom {
	
   // sample standard methods generated at runtime below
   // Preschool findOne(Long Id);
   // List<Preschool> findByName(String Name);
    
}

