package com.message.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "commonQuestion")
@Getter
@Setter
public class CommonQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commonQuestionGenerator")
    @SequenceGenerator(name = "commonQuestionGenerator", sequenceName = "commonQuestion_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(length = 50)
    private String qlsxId;

    @Column(length = 50)
    private String qlInnerCode;//权力唯一编码

    //问题名称
    @Column(nullable = true)
    @Lob
    private String question;

    //解答
    @Column(nullable = true)
    @Lob
    private String answer;


    public CommonQuestion() {
    }

    public CommonQuestion(String qlInnerCode, String question, String answer) {
        this.qlInnerCode = qlInnerCode;
        this.question = question;
        this.answer = answer;
    }
}
