//package com.backend.teamtalk.domain;
//
//import com.backend.teamtalk.dto.SignupRequestDto;
//import com.backend.teamtalk.repository.BoardRepository;
//import com.backend.teamtalk.repository.UserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class UserTest {
//
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    BoardRepository boardRepository;
//
//    @Test
//    @DisplayName("유저 생성")
//    void createUser() throws Exception {
//        //given
//        User userM = new User(1L, "bubble", "asdf");
//        User userT = new User(2L, "marc", "asdf");
//
//        //when
//        userRepository.save(userM);
//        userRepository.save(userT);
//
//        //then
//        Board board1 = new Board(1L, "test하고 있어요!", userM);
//        Board savedBoardNew = boardRepository.save(board1);
//
////        System.out.println("savedBoardNew = " + savedBoardNew);
//
//
//
//
//
//    }
//
//
//}