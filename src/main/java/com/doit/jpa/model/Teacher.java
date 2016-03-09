package com.doit.jpa.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.doit.jpa.model.Preschool;

@Entity
@Table(name = "Teacher", schema = "kidstuff")
public class Teacher {
	
	// auto-generated id on positive integer long
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    private String name;
    
    private String certification;
    
    @ManyToOne(fetch = FetchType.LAZY) // Defaults to EAGER, so need to OVERRIDE to lazy
    @JoinColumn(name = "PRESCHOOL_ID", nullable = false)  // Preschool REQUIRED for Teacher, Teacher tbl fk
    protected Preschool parentPreschool;
    
    // default protected ctor used for JPA only 
    protected Teacher() {}

    public Teacher(String name, String certification) {
        this.name = name;
        this.certification = certification;
    }

    @Override
    public String toString() {
        return String.format(
                "Teacher[id=%d, name='%s', certification='%s']",
                id, name, certification);
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public Preschool getParentPreschool() {
		return parentPreschool;
	}

	public void setParentPreschool(Preschool parentPreschool) {
		this.parentPreschool = parentPreschool;
	}
	
}
