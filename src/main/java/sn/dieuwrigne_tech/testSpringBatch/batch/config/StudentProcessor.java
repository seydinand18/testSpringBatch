package sn.dieuwrigne_tech.testSpringBatch.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import sn.dieuwrigne_tech.testSpringBatch.batch.student.Student;

public class StudentProcessor implements ItemProcessor<Student, Student> {

	private static final Logger logger = LoggerFactory.getLogger(StudentProcessor.class); // Logger pour la classe StudentProcessor.

	@Override
	public Student process(Student student) {
		logger.info("Traitement de l'étudiant avec ID: {}", student.getId());

		// Transformation des données de l'étudiant.
		student.setLastName(student.getLastName().toUpperCase()); // Mettre le nom de famille en majuscules.
		student.setFirstName(
			student.getFirstName().substring(0, 1).toUpperCase() + student.getFirstName().substring(1).toLowerCase()
		); // Mettre la première lettre du prénom en majuscule et le reste en minuscule.

		logger.info("Étudiant transformé: ID={}, Prénom={}, Nom={}",
			student.getId(), student.getFirstName(), student.getLastName());

		return student;
	}
}
