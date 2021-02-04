package engine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import engine.dao.QuizCompletionRepository;
import engine.dao.QuizRepository;
import engine.entity.Quiz;
import engine.entity.QuizCompletion;
import engine.entity.User;
import engine.exception.QuizNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizCompletionRepository quizCompletionRepository;

    public void addNewQuiz (Quiz newQuiz) {
        newQuiz.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        quizRepository.save(newQuiz);
    }

    public String getQuizById(int id) {
        try {
            Optional<Quiz> quiz = quizRepository.findById(id);
            if (quiz.isPresent()) {
                return new ObjectMapper().writeValueAsString(quiz.get());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Page<Quiz> getAllQuizzes(int page) {
        return quizRepository.findAll(PageRequest.of(page, 10));
    }

    public Page<Quiz> getAllQuizzesSolvedByUser(int page) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return quizCompletionRepository.findAllByUser(PageRequest.of(page, 10, Sort.by("completedAt").descending()), currentUser);
    }

    public String solveQuizById(int id, String answer) {
        Optional<Quiz> quizOpt = quizRepository.findById(id);
        if (quizOpt.isPresent()) {
            ObjectMapper responseMapper = new ObjectMapper();
            ObjectMapper requestMapper = new ObjectMapper();
            Quiz quiz = quizOpt.get();
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            try {
                int[] answersList = requestMapper.readValue(requestMapper.readTree(answer).withArray("answer").toString(), int[].class);
                ObjectNode node = responseMapper.createObjectNode();
                if (Arrays.equals(answersList, quiz.getAnswer())) {
                    quizCompletionRepository.save(new QuizCompletion(currentUser, quiz.getId()));
                    node.put("success", true);
                    node.put("feedback", "Congratulations, you're right!");
                } else {
                    node.put("success", false);
                    node.put("feedback", "Wrong answer! Please, try again.");
                }
                return responseMapper.writeValueAsString(node);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean deleteQuiz (int id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = currentUser.getEmail();
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isPresent()) {
            if (userEmail.equals(quiz.get().getUser().getEmail())) {
                quizRepository.deleteById(id);
            } else {
                return false;
            }
        } else {
            throw new QuizNotFoundException();
        }
        return true;
    }

}
