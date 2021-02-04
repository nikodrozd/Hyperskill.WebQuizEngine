package engine.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class QuizCompletion {

    @ManyToOne
    @JoinColumn(name="email")
    @JsonIgnore
    private User user;

    private int id;

    @Id
    private Date completedAt;

    public QuizCompletion(User user, int id){
        this.user = user;
        this.id = id;
        this.completedAt = new Date();
    }

    public QuizCompletion(){

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
