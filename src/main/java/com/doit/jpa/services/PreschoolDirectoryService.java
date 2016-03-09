package com.doit.jpa.services;

import java.util.List;

import com.doit.jpa.model.Preschool;
import com.doit.jpa.model.Teacher;

public interface PreschoolDirectoryService {
	
	  public Preschool create(Preschool school);
	  public Preschool findById(long id);
	  public Iterable<Preschool> findAll();
	  public Preschool delete(long id);
	  public Preschool update(Preschool preschoold);
	 
	  public boolean bulkUpdateStyleByIds(List<Long> preschoolsIdsUpdate, short updatedStyle);

	  public void addTeachersToPreschool(long preschoolId, List<Teacher> teachers);
	  public List<Teacher> getTeachersFromPreschool(long preschoolId);

}
