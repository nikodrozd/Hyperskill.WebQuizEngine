package engine.controller;

import engine.entity.Quiz;
import engine.exception.QuizNotFoundException;
import engine.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
class QuizRestController {

    @Autowired
    private QuizService service;

    @PostMapping(value = "/api/quizzes/{id}/solve", produces = MediaType.APPLICATION_JSON_VALUE)
    public String postApiQuizByIdToSolve(@PathVariable int id, @RequestBody String answer) {
        return nullCheck(service.solveQuizById(id, answer));
    }

    @PostMapping(value = "/api/quizzes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Quiz postApiQuizzes(@Valid @RequestBody Quiz newQuiz) {
        service.addNewQuiz(newQuiz);
        return newQuiz;
    }

    @GetMapping(value = "/api/quizzes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getApiQuizById(@PathVariable int id) {
        return nullCheck(service.getQuizById(id));
    }

    @GetMapping(value = "/api/quizzes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Quiz>> getApiQuizzes(@RequestParam(defaultValue = "0") int page) throws QuizNotFoundException {
        return new ResponseEntity<Page<Quiz>>(service.getAllQuizzes(page), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "/api/quizzes/completed", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Quiz>> getApiQuizzesCompleted(@RequestParam(defaultValue = "0") int page) throws QuizNotFoundException {
        return new ResponseEntity<Page<Quiz>>(service.getAllQuizzesSolvedByUser(page), new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/quizzes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteApiQuizzesById (@PathVariable int id) {
        if (service.deleteQuiz(id)) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    private String nullCheck(String response) throws QuizNotFoundException {
        if (response == null) {
            throw new QuizNotFoundException();
        } else {
            return response;
        }
    }

}
