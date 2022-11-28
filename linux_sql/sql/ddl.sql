--Connect to host_agent database if not already connected
--postgres=# \c host_agent;

--Create table for hardware specs
--Check if table already exists
CREATE TABLE IF NOT EXISTS PUBLIC.host_info
    (
     id                 SERIAL NOT NULL,
     hostname           VARCHAR NOT NULL UNIQUE,
     cpu_number         INT NOT NULL,
     cpu_architecture   VARCHAR NOT NULL,
     cpu_model          VARCHAR NOT NULL,
     cpu_mhz            FLOAT NOT NULL,
     L2_cache           FLOAT NOT NULL,
     total_mem          FLOAT NOT NULL,
     "timestamp"        TIMESTAMP NOT NULL,
     CONSTRAINT host_id_pk PRIMARY KEY (id)
    );

--If it does not exist, create table for usage data
  CREATE TABLE IF NOT EXISTS PUBLIC.host_usage
    (
       "timestamp"      TIMESTAMP NOT NULL,
       host_id          SERIAL NOT NULL,
       memory_free      FLOAT NOT NULL,
       cpu_idle         INT NOT NULL,
       cpu_kernel       INT NOT NULL,
       disk_io          INT NOT NULL,
       disk_available   FLOAT NOT NULL,
       CONSTRAINT host_id_fk FOREIGN KEY (host_id) REFERENCES host_info(id)
    );