package com.doit.jpa.services;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.UnexpectedRollbackException;

import com.doit.jpa.model.Preschool;
import com.doit.jpa.repositories.PreschoolRepository;
import com.doit.jpa.model.Teacher;

/*
 * 
 * DEFAULT TRANSACTIONAL HANDLING NOTES:
 * By default, @Transactional will set the propagation to REQUIRED, the readOnly flag to false, 
 * and the rollback only for unchecked exceptions. Also note that the isolation level is set to the database default; 
 * when using JPA, the isolation level is that of the underlying persistence provider. 
 * In the case of Hibernate, the isolation level of all transactions should be REPEATABLE_READ;
 * where PHANTOM reads may still occur, ie T2 inserted, not-yet-committed and T1 can see them later in same transaction
 * Note this level is stronger than READ_COMMITTED, and so is a superset of that.
 */

@Service
@Transactional(readOnly=true)
public class PreschoolDirectoryServiceImpl implements PreschoolDirectoryService {

    @Resource
    private PreschoolRepository preschoolRepository;

    @Override
    @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
    public Preschool create(Preschool preschool) {
        return preschoolRepository.save(preschool);
    }

    @Override
    public Preschool findById(long id) {
        return preschoolRepository.findOne(id);
    }
    
    @Override
    public Iterable<Preschool> findAll() {
    	return preschoolRepository.findAll();
    }

    @Override
    @Transactional(readOnly=false, propagation=Propagation.REQUIRED, rollbackFor=UnexpectedRollbackException.class)
    public Preschool delete(long id) {
    	
        Preschool preschoolToDelete = preschoolRepository.findOne(id);
        if (preschoolToDelete == null)
            throw new UnexpectedRollbackException("Unable to find existing preschool to delete.");

        preschoolRepository.delete(preschoolToDelete);
        return preschoolToDelete;
    }

    @Override
    @Transactional(readOnly=false, propagation=Propagation.REQUIRED, rollbackFor=UnexpectedRollbackException.class)
    public Preschool update(Preschool preschool) {
        Preschool updatedPreschool = preschoolRepository.findOne(preschool.getId());
        if (updatedPreschool == null)
            throw new UnexpectedRollbackException("Unable to find existing preschool to update.");    
        updatedPreschool.setName(preschool.getName());
        updatedPreschool.setProximity(preschool.getProximity());
        
        return updatedPreschool;
    }
    
    // TODO: TEST a BULK UPDATE of Preschool Style; 
    //       and ROLLBACK entire transaction if any ONE preschool could not be updated
    // TODO: research advantages of Iterable over List
    // @Modifying
    @Transactional(readOnly=false, propagation=Propagation.REQUIRED, rollbackFor=UnexpectedRollbackException.class)
    public boolean bulkUpdateStyleByIds(List<Long> idsForPreschoolsToUpdate, short updatedStyle) {
    	
    	int numSchoolsUpdated = preschoolRepository.bulkUpdateStyleByIdList(idsForPreschoolsToUpdate, updatedStyle);

    	// if no exceptions propagated out of here, then assumption is update was successful
        return true;
    }
    
    // @Modifying
    @Transactional(readOnly=false, propagation=Propagation.REQUIRED, rollbackFor=UnexpectedRollbackException.class)
	public void addTeachersToPreschool(long preschoolId, List<Teacher> teachers) {
		
		// Fetch preschool for id
    	Preschool preschool = preschoolRepository.findOne(preschoolId);
    	if (preschool == null) {
    		throw new UnexpectedRollbackException(String.format("Unable to find existing preschool to update for Id:  %d", preschoolId));    
    	}
		
		// Add teachers to Preschool
    	for (Teacher aTeacher:teachers) {
    		aTeacher.setParentPreschool(preschool);
    		preschool.getTeachers().add(aTeacher);
    	}
		
		// NOTE:  transactional commit boundary will cascade persistence changes
    	//        IFF OneToMany annotation is used!
	}
    
    public List<Teacher> getTeachersFromPreschool(long preschoolId) {
    	
    	// Fetch preschool for id
    	Preschool preschool = preschoolRepository.findOne(preschoolId);
    	if (preschool == null) {
    		throw new UnexpectedRollbackException(String.format("Unable to find existing preschool to update for Id:  %d", preschoolId));    
    	}
    	
    	// NOTE:  this eager join fetch is better than lazy-loading on access via
    	// preschool.getTeachers();
    	List<Teacher> teachers = preschoolRepository.batchFetchTeachersByPreschool(preschoolId);
    	
    	return teachers;
    }

}

