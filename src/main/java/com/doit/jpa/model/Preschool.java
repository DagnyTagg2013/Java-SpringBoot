package com.doit.jpa.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Preschool", schema = "kidstuff")
public class Preschool {

	// auto-generated id on positive integer long
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    private String name;
    
    // TODO: convert following into Enumeration Type
    // Proximity Values:  1 (close), 2 (farther, in same city as home)
    private short proximity;
    // Style Values:  1 (play-based), 2 (montessori-based), 3 (program-structured)
    private short style;
    
    // subset unordered unique collection of Teachers for Preschool 
    @OneToMany(mappedBy = "parentPreschool", 	// Secondary requirement for bidirectional assoc & nav, parent entity name property on child entity
               fetch = FetchType.LAZY,  // The default
               cascade = CascadeType.PERSIST, // Cascades child persistence from parent on txn commit
               orphanRemoval = true)  // includes CascadeType.REMOVE BUT with inefficiency of per-instance removal; 
                                      // NOT a BULK DELETE
    protected Set<Teacher> teachers = new HashSet<Teacher>();
    
    // TODO:  add the following specialized type attributes to test ORM support for
    // private BigDecimal monthlyFees;
    // private JodaDateTime earliestEnrollmentDate;

    // default protected ctor used for JPA only 
    protected Preschool() {}

    public Preschool(String name, short proximity, short style) {
        this.name = name;
        this.proximity = proximity;
        this.style = style;
    }

  
	@Override
    public String toString() {
        return String.format(
                "Preschool[id=%d, name='%s', proximity='%d', style='%d']",
                id, name, proximity, style);
    }

	public long getId() {
		return id;
	}

	/*
	public void setId(long id) {
		this.id = id;
	}
	*/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getProximity() {
		return proximity;
	}

	public void setProximity(short proximity) {
		this.proximity = proximity;
	}

	public short getStyle() {
		return style;
	}

	public void setStyle(short style) {
		this.style = style;
	}
	
	public Set<Teacher> getTeachers() {
		return teachers;
	}

	/*
	public void setTeachers(Set<Teacher> teachers) {
		this.teachers = teachers;
	}
	*/

}


