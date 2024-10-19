package sn.dieuwrigne_tech.testSpringBatch.batch.config;

import lombok.RequiredArgsConstructor;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import sn.dieuwrigne_tech.testSpringBatch.batch.repository.StudentRepository;
import sn.dieuwrigne_tech.testSpringBatch.batch.student.Student;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final StudentRepository studentRepository;

	@Bean
	public FlatFileItemReader<Student> itemReader() {
		FlatFileItemReader<Student> itemReader = new FlatFileItemReader<>();
		itemReader.setResource(new ClassPathResource("students.csv"));
		itemReader.setName("Student-Reader");
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(lineMapper());
		return itemReader;
	}

	private LineMapper<Student> lineMapper() {
		DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("id", "firstName", "lastName", "age");

		BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Student.class);

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}

	@Bean
	StudentProcessor itemProcessor() {
		return new StudentProcessor();
	}

	@Bean
	RepositoryItemWriter<Student> itemWriter() {
		RepositoryItemWriter<Student> itemWriter = new RepositoryItemWriter<>();
		itemWriter.setRepository(studentRepository);
		itemWriter.setRepository(studentRepository);
		itemWriter.setMethodName("save");
		return itemWriter;
	}

	@Bean
	public Step importStep() {
		return new StepBuilder("importStep", jobRepository)
			.<Student, Student>chunk(10, platformTransactionManager)
			.reader(itemReader())
			.processor(itemProcessor())
			.writer(itemWriter())
			.build();
	}

	@Bean
	public Job runJob() {
		return new JobBuilder("importStudents", jobRepository)
			.start(importStep())
			.build();
	}

}
