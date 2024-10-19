package sn.dieuwrigne_tech.testSpringBatch.batch.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class); // Logger pour la classe BatchConfig.

	private final JobRepository jobRepository; // Gestion des jobs batch.
	private final PlatformTransactionManager platformTransactionManager; // Gestion des transactions pour les étapes.
	private final StudentRepository studentRepository; // Accès au dépôt des étudiants.

	@Bean
	public FlatFileItemReader<Student> itemReader() {
		logger.info("Initialisation du FlatFileItemReader pour lire le fichier CSV.");
		FlatFileItemReader<Student> itemReader = new FlatFileItemReader<>();
		itemReader.setResource(new ClassPathResource("students.csv")); // Chemin du fichier source dans les ressources du classpath.
		itemReader.setName("Student-Reader"); // Nom du lecteur pour l'identification.
		itemReader.setLinesToSkip(1); // Saute la première ligne (généralement l'en-tête).
		itemReader.setLineMapper(lineMapper()); // Utilise un LineMapper pour mapper chaque ligne en objet Student.
		logger.info("FlatFileItemReader initialisé avec succès.");
		return itemReader;
	}

	private LineMapper<Student> lineMapper() {
		logger.info("Configuration du LineMapper pour mapper les lignes du fichier CSV en objets Student.");
		DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<>(); // Mapper par défaut.

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(","); // Utilise la virgule comme délimiteur.
		lineTokenizer.setStrict(false); // Ne lève pas d'exception si le nombre de champs est incorrect.
		lineTokenizer.setNames("id", "firstName", "lastName", "age"); // Noms des colonnes du CSV mappées aux champs de l'objet.

		BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Student.class); // Spécifie le type cible pour le mappage.

		lineMapper.setLineTokenizer(lineTokenizer); // Définit le tokenizer pour découper chaque ligne.
		lineMapper.setFieldSetMapper(fieldSetMapper); // Associe le mapper de champs pour créer les objets Student.

		logger.info("LineMapper configuré avec succès.");
		return lineMapper;
	}

	@Bean
	StudentProcessor itemProcessor() {
		logger.info("Initialisation du StudentProcessor.");
		return new StudentProcessor();
	}

	@Bean
	RepositoryItemWriter<Student> itemWriter() {
		logger.info("Initialisation du RepositoryItemWriter pour persister les objets Student.");
		RepositoryItemWriter<Student> itemWriter = new RepositoryItemWriter<>();
		itemWriter.setRepository(studentRepository); // Définir le repository utilisé.
		itemWriter.setMethodName("save"); // Méthode du repository à appeler pour enregistrer chaque item.
		logger.info("RepositoryItemWriter initialisé avec succès.");
		return itemWriter;
	}

	@Bean
	public Step importStep() {
		logger.info("Création de l'étape 'importStep'.");
		Step step = new StepBuilder("importStep", jobRepository)
			.<Student, Student>chunk(10, platformTransactionManager) // Traitement par lots de 10 enregistrements.
			.reader(itemReader()) // Utilise le lecteur défini pour lire les données.
			.processor(itemProcessor()) // Utilise le processeur défini pour transformer les données.
			.writer(itemWriter()) // Utilise le writer défini pour persister les données.
			.build();
		logger.info("L'étape 'importStep' a été créée avec succès.");
		return step;
	}

	@Bean
	public Job runJob() {
		logger.info("Initialisation du job 'importStudents'.");
		Job job = new JobBuilder("importStudents", jobRepository)
			.start(importStep()) // Commence par l'étape importStep.
			.build();
		logger.info("Le job 'importStudents' a été initialisé avec succès.");
		return job;
	}
}
