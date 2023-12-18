package com.joko.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener{
	
	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	  private final JdbcTemplate jdbcTemplate;

	  public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
	    this.jdbcTemplate = jdbcTemplate;
	  }

	  @Override
	  public void afterJob(JobExecution jobExecution) {
	    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
	      log.info("!!! JOB FINISHED! Time to verify the results");

			/*
			 * jdbcTemplate .query("SELECT first_name, last_name FROM people", new
			 * DataClassRowMapper<>(Person.class)) .forEach(person ->
			 * log.info("Found <{{}}> in the database.", person));
			 */
	      
	   // Check if the job was for processing Person entities
          if (jobExecution.getJobInstance().getJobName().equals("personJob")) {
              jdbcTemplate.query("SELECT first_name, last_name FROM people", new DataClassRowMapper<>(Person.class))
                      .forEach(person -> log.info("Found <{}> in the database.", person));
          }

          // Check if the job was for processing OrderEntity entities
          if (jobExecution.getJobInstance().getJobName().equals("orderEntityJob")) {
              jdbcTemplate.query("SELECT Order_ID, Order_Date, Ship_Date, Ship_Mode, Customer_ID, " +
                      "Customer_Name, Segment, Country, City, State, Postal_Code, Region, Product_ID, " +
                      "Category, Sub_Category, Product_Name, Sales, Quantity, Discount, Profit " +
                      "FROM orders", new DataClassRowMapper<>(OrderEntity.class))
                      .forEach(orderEntity -> log.info("Found <{}> in the database.", orderEntity));
          }
	    }
	  }
	  
	  
	
}
