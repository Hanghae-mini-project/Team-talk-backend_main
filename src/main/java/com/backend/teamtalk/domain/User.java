package com.backend.teamtalk.domain;

import com.backend.teamtalk.dto.SignupRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Table(name = "user")
@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    /*
     * 실수: cascade 처리를 하지 않고, user 를 탈퇴시키니 참조키 제약조건 위반에 걸려서 에러 생김
     * 미처 생각하지 못했음.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @Column(nullable = true)
    @JsonManagedReference
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    /*
     * 의문: Custom User Details Service 에서 user 가 활성화 상태인지 체크를 하는데
     * 이게 언제 설정 되는 거지?
     */
    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;

    /*
     * 실무에서는 @ManyToMany 사용하지 않음
     * 해야할 것: 일대다, 다대일로 어떻게 만들면 좋을지 그려볼 것.
     *
     * @JoinColumn name 을 단순히 id 라고 한 이유:
     *  user_id 라고 하고 싶었는데, 그렇게 하면 user_id 를 참조하고
     *  있는 board 에서는 column name 이 user_user_id 라고 표기가 되는데 이걸 board 에서만 따로 name 을
     *  지정할 수 있는지 방법을 찾지 못했기 때문.
     *
     * 유의: name = id 는 user_id 를 지칭하는 것.
     */
    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;


    //프론트에서 필요한 데이터. 변수명을 skill 로 맞춤.
    @Column(nullable = true)
    private String skill;


    public User(SignupRequestDto signupRequestDto) {
        this.username = signupRequestDto.getUsername();
        this.password = signupRequestDto.getPassword();
        this.skill = signupRequestDto.getSkill();
    }

}
