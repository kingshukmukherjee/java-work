package com.rabobank.statement.batchprocessing;

import com.rabobank.statement.model.CustomerStatementModel;
import com.rabobank.statement.validation.CustomerStatementModelProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static com.rabobank.statement.shared.CommonUtility.*;

/**
 * <h1>Batch Configure!</h1>
 * This batch configure programme will
 * setup the job os csv input file. And
 * It will process the file and then create
 * output csv file as an reconciliation report.
 *
 * @author Kingshuk Mukherjee - 233303
 * @version 1.0
 * @since 20/09/2019
 */

@EnableBatchProcessing
@Configuration
public class CsvFileProcessingConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    // begin reader, writer, and processor


    @Bean
    public FlatFileItemReader<CustomerStatementModel> reader() {
        FlatFileItemReader<CustomerStatementModel> reader = new FlatFileItemReader<CustomerStatementModel>();
        reader.setResource(inputResource);
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<CustomerStatementModel>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{REFERENCE, ACCOUNT_NUMBER, DESCRIPTION, START_BALANCE,
                        MUTATION, END_BALANCE});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<CustomerStatementModel>() {{
                setTargetType(CustomerStatementModel.class);
            }});
        }});
        return reader;
    }


    @Bean
    ItemProcessor<CustomerStatementModel, CustomerStatementModel> statementProcessor() {
        return new CustomerStatementModelProcessor();
    }

    @Bean
    public FlatFileItemWriter<CustomerStatementModel> writer() {

        //Create writer instance
        FlatFileItemWriter<CustomerStatementModel> writer = new FlatFileItemWriter<>();

        writer.setShouldDeleteIfExists(true);

        //Set output file location
        writer.setResource(outputResource);

        //All job repetitions should "append" to same output file
        writer.setAppendAllowed(false);

        //Name field values sequence based on object properties
        writer.setLineAggregator(new DelimitedLineAggregator<CustomerStatementModel>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<CustomerStatementModel>() {
                    {
                        setNames(new String[]{REFERENCE, DESCRIPTION});
                    }
                });
            }
        });
        return writer;
    }

    @Bean
    public Step csvFileProcessingStep() {
        return stepBuilderFactory.get("csvFileProcessingStep")
                .<CustomerStatementModel, CustomerStatementModel>chunk(1)
                .reader(reader())
                .processor(statementProcessor())
                .writer(writer())
                .build();
    }

    @Bean
    Job csvFileProcessingJob() {
        return jobBuilderFactory.get("csvFileProcessingJob")
                .incrementer(new RunIdIncrementer())
                .flow(csvFileProcessingStep())
                .end()
                .build();
    }
}
