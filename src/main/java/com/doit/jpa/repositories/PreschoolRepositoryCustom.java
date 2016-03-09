package com.doit.jpa.repositories;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.doit.jpa.model.Teacher;

public interface PreschoolRepositoryCustom {
	
	public static String BULK_UPDATE_STYLE_BY_ID_LIST = "UPDATE Preschool school SET school.style = :updatedStyle WHERE school.id in (:paramIds)";
	// NOTE:  if a simple query is to be mapped to params, then use this annotation; but if implementation is to be 
	//        customized further, then it's necessary to get a handle to EntityManager to handle custom behavior.
	//        otherwise, @Query annotation on method is sufficient for Spring to supply vanilla implementation
	//@Query("UPDATE school Preschool SET school.style = :updatedStyle WHERE school.id in (:paramIds)")
	int bulkUpdateStyleByIdList(@Param("paramIds")List<Long> paramIds, @Param("updatedStyle")short updatedStyle);
	
	// NOTE:  Entity names and properties; rather than tables names are referred to in an HQL query,
	//        along with JOIN FETCH strategy for EAGER fetching
	public static String BATCH1_FETCH_TEACHERS_BY_PRESCHOOL =  "SELECT p"
			                                                + " FROM Preschool as p"
															+ " LEFT JOIN FETCH p.teachers"
															+ " WHERE p.id = (:paramId)";
	
	List<Teacher> batchFetchTeachersByPreschool(long preschoolId);
	
}
