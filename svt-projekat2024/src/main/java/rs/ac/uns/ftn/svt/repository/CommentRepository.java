package rs.ac.uns.ftn.svt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.svt.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
