package sn.dieuwrigne_tech.testSpringBatch.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.dieuwrigne_tech.testSpringBatch.batch.student.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
