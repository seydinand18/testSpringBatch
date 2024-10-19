package sn.dieuwrigne_tech.testSpringBatch.batch.config;

import org.springframework.batch.item.ItemProcessor;
import sn.dieuwrigne_tech.testSpringBatch.batch.student.Student;

public class StudentProcessor implements ItemProcessor<Student, Student> {

	@Override
	public Student process(Student student) throws Exception {
		student.setLastName(student.getLastName().toUpperCase());
		student.setFirstName(student.getFirstName().substring(0, 1).toUpperCase() + student.getFirstName().substring(1));
		return student;
	}
}
