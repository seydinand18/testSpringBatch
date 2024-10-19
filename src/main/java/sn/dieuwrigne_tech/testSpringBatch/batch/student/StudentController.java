package sn.dieuwrigne_tech.testSpringBatch.batch.student;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/student")
public class StudentController {

	private final JobLauncher jobLauncher;
	private final Job job;

	@RequestMapping("/import-student")
	public void importStudentCsvToDbJob() {
		JobParameters jobParameter = new JobParametersBuilder()
			.addLong("startAt", System.currentTimeMillis())
			.toJobParameters();

		try {
			jobLauncher.run(job, jobParameter);
		} catch (JobExecutionAlreadyRunningException
				 | JobParametersInvalidException
				 | JobInstanceAlreadyCompleteException
				 | JobRestartException e) {
			e.printStackTrace();
		}
	}
}
