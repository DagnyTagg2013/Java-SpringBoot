------------------------------------------------------------------
--  TABLE Preschool w InnoDB for transaction support
------------------------------------------------------------------

CREATE TABLE `Preschool`
(
   id          bigint(20) NOT NULL AUTO_INCREMENT,
   name        varchar(255) NOT NULL,
   proximity   smallint(6) NOT NULL,
   style       smallint(6) NOT NULL,
   PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

------------------------------------------------------------------
--  TABLE Teacher w InnoDB for transaction support
------------------------------------------------------------------

CREATE TABLE `Teacher`
(
   id          		bigint(20) NOT NULL AUTO_INCREMENT,
   preschool_id     bigint(20) NOT NULL,
   name        		varchar(255) NOT NULL,
   certification    varchar(255) NOT NULL,
   PRIMARY KEY (id),
   FOREIGN KEY (preschool_id) 
   REFERENCES Preschool(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

------------------------------------------------------------------
--  CLEAN Script in OPPOSITE order of creation dependency due to Foreign Key constraints
------------------------------------------------------------------

truncate Teacher;
truncate Preschool;

drop table Teacher;
drop table Preschool;

select * from Preschool;
select * from Teacher;