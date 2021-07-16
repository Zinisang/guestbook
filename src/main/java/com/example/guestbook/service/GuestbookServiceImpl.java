package com.example.guestbook.service;

import com.example.guestbook.dto.GuestbookDTO;
import com.example.guestbook.dto.PageRequestDTO;
import com.example.guestbook.dto.PageResultDTO;
import com.example.guestbook.entity.GuestBook;
import com.example.guestbook.entity.QGuestBook;
import com.example.guestbook.repository.GuestbookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor    // 자동 주입
public class GuestbookServiceImpl implements GuestbookService{


    private final GuestbookRepository repository;


    @Override
    public Long register(GuestbookDTO dto){
        log.info("DTO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        log.info(dto);

        GuestBook entity = dtoToEntity(dto);
        log.info(entity); //나중에 repository.save(entity)

        repository.save(entity);

        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, GuestBook> getList(PageRequestDTO requestDTO){
        //정렬방식 설정
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        //검색 조건 처리 : getSearch() 의 반환타입 = BooleanBuilder 타입
        BooleanBuilder booleanBuilder = getSearch(requestDTO);

        //Querydsl 사용하기 위한 수정
         Page<GuestBook> result = repository.findAll(booleanBuilder,pageable);

        Function<GuestBook, GuestbookDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }


    // 조회 관련
    @Override
    public GuestbookDTO read(Long gno) {
        Optional<GuestBook> result = repository.findById(gno);


        return result.isPresent() ? entityToDto(result.get()) : null;
        // 조회된 결과가 있으면 DTO로 변환 그렇지 않으면 null을 리턴턴
    }

    // 수정 관련
    @Override
    public void modify(GuestbookDTO dto) {
        // 업데이트 항목 : 제목 / 내용
        Optional<GuestBook> result = repository.findById(dto.getGno());

        //만약 찾은 결과 값이 있으면
        if(result.isPresent()){
            GuestBook entity = result.get();
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repository.save(entity);
        }
    }

    // 삭제 관련
    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    //검색관련 : GuestbookServiceImpl 자체 선언 메서드
    //검색 메서드는 현 클래스에서만 사용 : 즉, getList() 내에서 사용
    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        //검색 타입
        String type = requestDTO.getType();

        //BooleanBuilder 객체 생성
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestBook qGuestBook = QGuestBook.guestBook;

        //검색어
        String keyword = requestDTO.getKeyword();

        //import com.querydsl.core.types.dsl.BooleanExpression
        BooleanExpression expression = qGuestBook.gno.gt(0L);
        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0){

            return booleanBuilder;
        }

        //검색조건 존재
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")){     //제목 검색
            conditionBuilder.or(qGuestBook.title.contains(keyword));
        }
        if(type.contains("c")){     //제목 검색
            conditionBuilder.or(qGuestBook.content.contains(keyword));
        }
        if(type.contains("w")){     //제목 검색
            conditionBuilder.or(qGuestBook.writer.contains(keyword));
        }

        //모든조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }


}