

OBJECTIVES:
* use of SpringData support for auto-generating entity-level CRUD and Paging and Sorting data access per 
standards for JPA Persistence
* use of Java-driven table schema generation
* use of externalized environment properties file
* use of DB configuration to MySQL DB provider
* use of customized Repository implementation to support bulk update HQL queries
* use of business service transactional updates through Service layer 
* use of bi-directional ManyToOne and OneToMany queries to enable cascading persistence, navigation, and customizable
eager fetch HQL query plans

TO RUN:
* Apple/System Preferences, startup MySQL Server
* Toad/DB Connection to database 'kidstuff' as root user with specified password:
root@localhost/kidstuff
jdbc:mysql://localhost:3306/kidstuff?characterEncoding=utf8
* run schema.sql in Toad
* comment out appropriate schema-generation in Application.java/jpaVendorAdapter()
hibernateJpaVendorAdapter.setGenerateDdl(true);
* from terminal command-line in project jpa directory:
mvn clean install
* from STS IDE; rclick Application.java; 
RunAs Java Application and F8 through breakpoints

TO EXTEND:
* find out how to make sl4j or log4j log levels configurable
* find out how to make version upgrades configurable for Hibernate and JDBC connection pooling providers
* add tests for support on JodaDateTime and BigDecimal types
* SL4J Logging externalized log-level support
* JUnit and Mockito Support
* MVC Support
* REST Support
* MyBatis integration for further custom queries

TO SAVE:
* from terminal command-line in project jpa directory:
git add src
git add docs
git add pom.xml
git status
git commit
'i' comment; ESC wq!



