package sn.dieuwrigne_tech.testSpringBatch.batch.student;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/student")
public class StudentController {

	private static final Logger logger = LoggerFactory.getLogger(StudentController.class); // Logger pour la classe StudentController.

	private final JobLauncher jobLauncher; // Lanceur de job.
	private final Job job; // Instance du job à exécuter.

	@GetMapping("/import-student")
	public void importStudentCsvToDbJob() {
		JobParameters jobParameter = new JobParametersBuilder()
			.addLong("startAt", System.currentTimeMillis())
			.toJobParameters();

		logger.info("Début du job d'importation des étudiants à {}", jobParameter.getParameters().get("startAt"));

		try {
			jobLauncher.run(job, jobParameter); // Exécution du job avec les paramètres définis.
			logger.info("Le job d'importation des étudiants a été lancé avec succès.");
		} catch (JobExecutionAlreadyRunningException e) {
			logger.error("Le job est déjà en cours d'exécution.", e);
		} catch (JobParametersInvalidException e) {
			logger.error("Les paramètres du job sont invalides.", e);
		} catch (JobInstanceAlreadyCompleteException e) {
			logger.error("Une instance de ce job a déjà été complétée.", e);
		} catch (JobRestartException e) {
			logger.error("Erreur lors du redémarrage du job.", e);
		}
	}
}
