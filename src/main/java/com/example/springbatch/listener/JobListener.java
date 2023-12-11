package com.example.springbatch.listener;

import com.example.springbatch.service.EmailService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JobListener implements JobExecutionListener {

    private final EmailService emailService;
    @Autowired
    public JobListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void afterJob(JobExecution jobExecution){
        System.out.println("Se ejecutó el trabajo con éxito");

        String to = "linnrodriguez25@gmail.com";
        String subject = "Spring batch job - STATUS: " + jobExecution.getStatus().toString() ;

        List<StepExecution> stepExecutions = new ArrayList<>(jobExecution.getStepExecutions());
        StringBuilder textBuilder = new StringBuilder();

        for (StepExecution stepExecution : stepExecutions) {
            String stepInfo = "Step Name: " + stepExecution.getStepName() +
                    ", Read Count: " + stepExecution.getReadCount() +
                    ", Write Count: " + stepExecution.getWriteCount();

            System.out.println(stepInfo);
            textBuilder.append(stepInfo).append("\n");
        }

        String TEXT = textBuilder.toString();


        if (emailService == null){
            System.out.println("Null email service");
        }else {
            emailService.sendEmail(to,subject,TEXT);
            System.out.println("correo enviado con éxito");}
    }
}
