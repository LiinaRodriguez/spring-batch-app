package com.example.springbatch.config;

import com.example.springbatch.listener.JobListener;
import com.example.springbatch.repository.CustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springbatch.entity.Customer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private JobListener jobListener;
    //Reader
    @Bean
    public FlatFileItemReader<Customer> customerReader(){
        System.out.println("Lector");
        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
        itemReader.setName("csv-reader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Customer> lineMapper() {
        System.out.println("Mapper");
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(true);
        lineTokenizer.setNames("id", "firstName", "lastName","email", "gender", "contactNo", "country", "dob");

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
    //Processor CustomerProcessor class
    @Bean
    public CustomerProcessor customerProcessor(){
        System.out.println("Procesador");

        return new CustomerProcessor();
    }

    //Writer
    @Bean
    public RepositoryItemWriter<Customer> customerWriter(){
        System.out.println("Escritor");
        RepositoryItemWriter<Customer>  repositoryWriter= new RepositoryItemWriter<>();
        repositoryWriter.setRepository(customerRepo);
        repositoryWriter.setMethodName("save");
        return repositoryWriter;
    }
    //Step
    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        System.out.println("Step1");
        return new StepBuilder("csv-step", jobRepository)
                .<Customer, Customer>chunk(10, transactionManager)
                .reader(customerReader())
                .processor(customerProcessor())
                .writer(customerWriter())
                .allowStartIfComplete(true)
                .build();
    }

    //Job
    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        System.out.println("Job");
        return new JobBuilder("customer-job", jobRepository)
                .listener(jobListener)
                .flow(step(jobRepository, transactionManager))
                .end()
                .build();
    }
}
