package com.example.guestbook.repository;

import com.example.guestbook.entity.GuestBook;
import com.example.guestbook.entity.QGuestBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;
/*
    @Test
    public void insertDummies(){
        IntStream.rangeClosed(1,300).forEach(i ->{
            GuestBook guestBook = GuestBook.builder()
                    .title("Title..")
                    .content("Content..")
                    .writer("User" + (i %10))
                    .build();
            System.out.println(guestbookRepository.save(guestBook));
        });
    }
*/
    @Test
    public void updateTest(){
        Optional<GuestBook> result= guestbookRepository.findById(100L);

        if(result.isPresent()){
            GuestBook guestbook =result.get();

            guestbook.changeTitle("changed title");
            guestbook.changeContent("changed Content");

            guestbookRepository.save(guestbook);

        }
    }

    //Querydsl 테스트용: title에 "1" 이라는 글자가 있는 엔티티 검색
    @Test
    public void testQuery1(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());

        QGuestBook qGuestBook = QGuestBook.guestBook;   //1.Q 도메인 클래스

        String keyword ="2";

        BooleanBuilder builder = new BooleanBuilder();  //2.where 에 들어가는 조건을 넣어주는 컨테이너

        BooleanExpression expression = qGuestBook.title.contains(keyword);  //3.필드값과 결합

        builder.and(expression);    //4.where 문에 AND 키워드와 결합

        Page<GuestBook> result = guestbookRepository.findAll(builder,pageable); //5.선택 findAll()

        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        });
    }

    // Querydsl 테스트 : 복합조건 처리 (title, content에 검색 키워드가 존재하고, gno가 0보다 큰 엔티티
    @Test
    public void testQuery2(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending()); //페이징 작업
        QGuestBook qGuestBook = QGuestBook.guestBook; //Q 도메인 클래스 얻기
        String keyword="1"; //검색할 키워드

        BooleanBuilder builder = new BooleanBuilder(); //where에 들어갈 조건 컨테이너
        BooleanExpression exTitle = qGuestBook.title.contains(keyword); //title 필드와 검색어 결합
        BooleanExpression exContent = qGuestBook.content.contains(keyword); //content 필드와 검색어 결합
        /*
         * 작성자도 추가 하고 싶을 경우, 즉 : 제목+내용+작성자 형식
         * BooleanExpression exWriter - qGuestbook.writer.contains(keyword); 를 추가하면된다.
         * */
        BooleanExpression exAll = exTitle.or(exContent); //title 또는 content
        //위와같이 작성자까지 추가하면 exAll에 작성자도 or 로 추가해줘야 한다.
        //BooleanExpression exAll = exTitle.or(exContent).or(exWriter)

        builder.and(exAll); //여기까지를 Builder에 추가
        builder.and(qGuestBook.gno.gt(0L)); //그리고 gno가 0보다 크다는 조건 추가

        Page<GuestBook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        });

    }


}
