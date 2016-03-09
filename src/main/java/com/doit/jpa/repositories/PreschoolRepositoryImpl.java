package com.doit.jpa.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.doit.jpa.model.Preschool;
import com.doit.jpa.model.Teacher;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/*
 * NOTE:
 * - Spring Data/Boot somehow provides implementation of STANDARD interfaces at runtime;
 *   so to extend implementation at compile time now for non-boilerplate custom HQL queries,
 *   we're here adding ADDING an INDEPENDENT custom interface implementation 
 * - PereistenceContext injection info here:
 *   http://tomee.apache.org/examples-trunk/injection-of-entitymanager/README.html
 */
public class PreschoolRepositoryImpl implements PreschoolRepositoryCustom {	
	
	// NOTE:  DI to inject handle to entityManager from Spring auto-configured persistence context
	@PersistenceContext
	private EntityManager entityManager;
	
	/* 
	 * - access to EntityManager from here:
	 * http://stackoverflow.com/questions/11880924/how-to-add-custom-method-to-spring-data-jpa
	 * http://hop2croft.wordpress.com/2011/07/06/jpa-basic-example-with-entitymanager-spring-and-maven/
	 * 
	 */
 
	/*
	 * from StackOverflow here:
     * http://stackoverflow.com/questions/10220262/updating-boolean-value-in-spring-data-jpa-using-query-with-hibernate
     
     
       TOFIND: performance impact of precompiled queries given DYNAMIC VARIABLE query here
	
	   TOFIND: performance impact of adding index on style varchar-typed column in Preschool table; 
	           OR Java long EnumerationMap converting Style string to RDBMS short representation 
	   
     */
	public int bulkUpdateStyleByIdList(List<Long> paramIds, short updatedStyle) {
		
	   // input business logic validation of proper style type
	   // TODO:  need to make this an EnumerationMap or externalize validation methods to separate class
	   if ((updatedStyle < 1) || (updatedStyle > 3)) {
		   throw new  InvalidDataAccessApiUsageException ("Input Style for update must be between 1 and 3.");
	   }
	   
	   int numberOfSchoolsInput = paramIds.size();
	   Query query = entityManager.createQuery(PreschoolRepositoryCustom.BULK_UPDATE_STYLE_BY_ID_LIST);
	   query.setParameter("updatedStyle", updatedStyle);
	   query.setParameter("paramIds", paramIds); //paramIds is List<Long>
	   int numberOfSchoolsUpdated = query.executeUpdate();
	   if (numberOfSchoolsInput != numberOfSchoolsUpdated) {
		   throw new DataRetrievalFailureException("Unable to find all Preschools by Ids input to update Style.");
	   }
	   
	   return numberOfSchoolsUpdated;
	   
	 }
	
	public List<Teacher> batchFetchTeachersByPreschool(long preschoolId) {
		
		 Query query = entityManager.createQuery(BATCH1_FETCH_TEACHERS_BY_PRESCHOOL);
		 // NOTE:  following parameter placeholder name must exactly match what's in HQL query string!
		 query.setParameter("paramId", preschoolId);
		 // NOTE:  MUST fetch PARENT holding collection FIRST, as handle into EAGER JOIN FETCH of children
		 List<Preschool> preschools = query.getResultList();
		 // TODO:  investigate why this is returning essentially a SQL join result set WITHOUT eliminating duplicate parent entities
		 //        i.e. 3 teacher elements for 1 parent preschool returns 3 preschool elements with first preschool info duplicated
		 /*
			 if (preschools.size() != 1) {
				 throw new DataRetrievalFailureException(String.format("Unable to find Preschool for given Id:  %d", preschoolId));
			 }
		 */
		 if (preschools.size() < 1) {
			 throw new DataRetrievalFailureException(String.format("Unable to find Preschool for given Id:  %d", preschoolId));
		 }
		 List<Teacher> teachers = new ArrayList<Teacher>(preschools.get(0).getTeachers());
		 return teachers;
		 
	}
	
}
