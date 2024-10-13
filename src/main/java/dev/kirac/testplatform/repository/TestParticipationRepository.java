package dev.kirac.testplatform.repository;

import dev.kirac.testplatform.entity.TestParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestParticipationRepository extends JpaRepository<TestParticipation, Long> {

    Optional<TestParticipation> findByStudentIdAndTestId(Long studentId, Long testId);
}
