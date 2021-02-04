package engine.dao;

import engine.entity.QuizCompletion;
import engine.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;

public interface QuizCompletionRepository extends PagingAndSortingRepository<QuizCompletion, Date> {

    Page findAllByUser (Pageable pageable, User user);

}
