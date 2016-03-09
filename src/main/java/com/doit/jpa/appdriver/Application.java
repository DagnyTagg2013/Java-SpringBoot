package com.doit.jpa.appdriver;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
// TODO:  extend for MVC support later
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.view.JstlView;
//import org.springframework.web.servlet.view.UrlBasedViewResolver;
import java.util.List;

import com.doit.jpa.model.Preschool;
import com.doit.jpa.model.Teacher;
import com.doit.jpa.repositories.PreschoolRepository;
import com.doit.jpa.services.PreschoolDirectoryService;

@Configuration
// TODO:  extend for MVC support later
//@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("com.doit.jpa")
@PropertySource("classpath:environment.properties")
@EnableJpaRepositories("com.doit.jpa.repositories")

public class Application {

    private static final String PROPERTY_NAME_DATABASE_DRIVER = "db.driver";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
    private static final String PROPERTY_NAME_DATABASE_URL = "db.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";

    private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";

    @Resource
    private Environment env;

    @Bean
    public DataSource dataSource() {
    	
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));

        dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));

        dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));

        dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));

        return dataSource;

    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setDataSource(dataSource());

        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        
        entityManagerFactoryBean.setPackagesToScan(env.getRequiredProperty(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN));
        
        return entityManagerFactoryBean;
        
    }
    
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(Boolean.getBoolean(env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL)));
        // NOTE:  run with this option set to TRUE the FIRST time for schema-generation;
        // but then annotate Entity class with @Table to make more maintainable
        // hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

 // TODO:  extend for MVC support later
/*
    @Bean
    public UrlBasedViewResolver setupViewResolver() {

        UrlBasedViewResolver resolver = new UrlBasedViewResolver();

        resolver.setPrefix("/WEB-INF/pages/");

        resolver.setSuffix(".jsp");

        resolver.setViewClass(JstlView.class);

        return resolver;

    }
*/
    
    
/*
 * 
 * TODO:  ADD JUnit and Mockito support later; but using command-line tests from main for now!
 * 
 */
    
    public static void TEST1(PreschoolRepository repository) {
    	
    	  // save three Preschools with varied proximities, but then DEFAULT Styles all to play-based at 1
          repository.save(new Preschool("Challenge", (short)2, (short)1));
          repository.save(new Preschool("Odyssey Montessori", (short)2, (short)1));
          repository.save(new Preschool("All Are Friends", (short)1, (short)1));
          
          // fetch all Preschools
          Iterable<Preschool> preschools = repository.findAll();
          System.out.println("Preschools found with findAll():");
          System.out.println("-------------------------------");
          for (Preschool preschool : preschools) {
              System.out.println(preschool);
          }
          System.out.println();

          // fetch an individual Preschool by ID
          Preschool preschool = repository.findOne(1L);
          System.out.println("Preschool found with findOne(1L):");
          System.out.println("--------------------------------");
          System.out.println(preschool);
          System.out.println();

    }
    
    public static void TEST2(PreschoolDirectoryService service) {
    
       // testing for initial persisted state
       // EXPECT third preschool to have initial style of 1
       Preschool thirdSchool = service.findById(3L);
       System.out.println(String.format("Found third preschool on listing with default Style of:  %d.", thirdSchool.getStyle()));
       
       // testing for SUCCESSFUL transactional update to a valid Style FROM default Play-based (1) to Montessori (2)
       // EXPECT second and third preschool styles to CHANGE to 2 from 1 above
       System.out.println("Updating second and third preschool Styles in BULK update to Montessori (2).");
       List<Long> montessoriSchoolIds = new ArrayList<Long>();
       montessoriSchoolIds.add(2L);
       montessoriSchoolIds.add(3L);
       service.bulkUpdateStyleByIds(montessoriSchoolIds, (short)2);
       
       Preschool secondPreschool = service.findById(2L);
       Preschool thirdPreschool = service.findById(3L);
       System.out.println(String.format("Updated Style for second preschool is:  %d", secondPreschool.getStyle()));
       System.out.println(String.format("Updated Style for third preschool is:  %d", thirdPreschool.getStyle()));
       
       // testing for UNSUCCESSFUL transactional update to an invalid Style of 999, 
       // EXPECT first preschool style to REMAIN unchanged at 1 from initialization
       List<Long> criminalStyleSchoolIds = new ArrayList<Long>();
       criminalStyleSchoolIds.add(1L);
       criminalStyleSchoolIds.add(2L);
       criminalStyleSchoolIds.add(3L);
       try {
    	   service.bulkUpdateStyleByIds(criminalStyleSchoolIds, (short)999);
       } catch (UnexpectedRollbackException tex) {
    	   System.out.println("TRANSACTION EXCEPTION");
    	   System.out.println(tex.getMessage());
       } catch (DataAccessException dex) {
    	   System.out.println("DATA EXCEPTION");
    	   System.out.println(dex.getMessage());
       }
       
       Preschool firstPreschool = service.findById(1L);
       System.out.println(String.format("Updated Style for first preschool is:  %d", firstPreschool.getStyle()));
       
       // testing for UNSUCCESSFUL transactional update on a Preschool which does not already exist with Id of 13
       // EXPECT second preschool style to REMAIN unchanged at 2 from above
       List<Long> unknownSchoolIds = new ArrayList<Long>();
       unknownSchoolIds.add(13L);
       try {
    	   service.bulkUpdateStyleByIds(unknownSchoolIds, (short)3);
       } catch (UnexpectedRollbackException tex) {
    	   System.out.println("TRANSACTION EXCEPTION");
    	   System.out.println(tex.getMessage());
       } catch (DataAccessException dex) {
    	   System.out.println("DATA EXCEPTION");
    	   System.out.println(dex.getMessage());
       }
       
       secondPreschool = service.findById(2L);
       System.out.println(String.format("Updated Style for second preschool is:  %d", secondPreschool.getStyle()));
    
      
    }
    
    public static void TEST3(PreschoolDirectoryService service) {
    	
    	// INITIAL state EXPECTED is 0 teachers in preschool with Id of 1L
    	List<Teacher> teachers = service.getTeachersFromPreschool(1L);
    	System.out.println(String.format("Number of teachers retrieved for Preschool with Id 1 is:  %d", teachers.size()));
    
    	System.out.println("Adding three new teachers to preschool.");
    	List<Teacher> newTeachers = new ArrayList<Teacher>(); 
    	newTeachers.add(new Teacher("Jackie Chan", "AMS Montessori"));
    	newTeachers.add(new Teacher("Ruchi Gupta", "AMS Montessori"));
    	newTeachers.add(new Teacher("Kate Middleton", "Aide"));
    	service.addTeachersToPreschool(1L, newTeachers);
   
    	// EXPECT to retrieve total of 3 teachers now
        teachers = service.getTeachersFromPreschool(1L);
    	System.out.println(String.format("Number of teachers retrieved for Preschool with Id 1 is:  %d", teachers.size()));
    	for (Teacher teacher : teachers) {
            System.out.println(teacher);
        }
    }
    
	public static void main(String[] args) {
		
		// BASELINE test for context loading
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        PreschoolRepository repository = context.getBean(PreschoolRepository.class);
        PreschoolDirectoryService service = context.getBean(PreschoolDirectoryService.class);
		
		// TEST1:
		// - Repository DB connectivity
		Application.TEST1(repository);
		
		// TEST2:
		// - Service transaction commit
		// - Service transaction rollback
		Application.TEST2(service);
		
		// TEST3:
		// - transactional child collection update
		// - dynamic custom eager fetch of child collection 
		Application.TEST3(service);
		
		// closing application context;
		context.close();
		
	}

}

