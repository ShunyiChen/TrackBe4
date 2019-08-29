package com.maxtree.automotive.dashboard.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "suggestions")
public class Suggestion {

    /** 自增ID */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /** 审核后遇到的问题 */
    private String problem;
    /** 建议 */
    private String suggestion;

    public Suggestion() {}

    public Suggestion(String problem, String suggestion) {
        this.problem = problem;
        this.suggestion = suggestion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "id=" + id +
                ", problem='" + problem + '\'' +
                ", suggestion='" + suggestion + '\'' +
                '}';
    }
}
