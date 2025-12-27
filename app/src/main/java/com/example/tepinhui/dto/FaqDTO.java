package com.example.tepinhui.dto;

public class FaqDTO {
    private Integer id;
    private String question;
    private String answer;
    private String category;

    // 默认构造函数（用于JSON解析）
    public FaqDTO() {
    }

    // 带参数的构造函数（用于直接创建对象）
    public FaqDTO(Integer id, String question, String answer, String category) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.category = category;
    }

    // Getter和Setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}