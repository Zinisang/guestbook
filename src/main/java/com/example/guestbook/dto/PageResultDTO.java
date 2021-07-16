package com.example.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {   // 1.
    // DTO 리스트
    private List<DTO> dtoList;

    // 총 페이지
    private int totalPage;

    // 현재 페이지 번호
    private int page;

    // 목록 사이즈
    private int size;

    // 시작 페이지 번호
    private int start;

    // 끝 페이지 번호
    private int end;

    // 페이지 번호 목록
    private List<Integer> pageList;

    // 이전 버튼 링크 여부
    private boolean prev;

    // 다음 버튼 링크 여부
    private boolean next;

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){    // 2.
        dtoList = result.stream().map(fn).collect(Collectors.toList()); // DTO 리스트
        totalPage = result.getTotalPages(); // 전체 페이지
        makePageList(result.getPageable());
    }

    // 페이지 목록 메소드
    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber() + 1;   // 실제로는 0부터 시작하므로 1을 더해서 실제화면에서는 1로 출력될 수 있게
        this.size = pageable.getPageSize();

        int tempEnd = (int)(Math.ceil(page/10.0))*10;       // 임시 페이지 끝 번호

        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

    }
}
/*
 *   1. PageResultDTO는 다양한 곳에서 사용할 수 있도록 제네릭 타입을 이용하여 DTO, EN이라는 타입을 지정
 *       DTO : DTO 타입, EN : Entity 타입을 의미.
 *
 *   2. Function<EN, DTO> fn : 엔티티 객체들을 DTO로 변환 해주는 기능.
 *
 * => 나중에 어떤 종류의 Page<E> 타입이 생성되더라도 PageResultDTO를 이용하여 처리 가능하도록 범용 설계된 클래스
 *       즉, 위와 같이 제네릭 방식으로 적영해두면 나중에 추가적인 클래스를 작성하지 않고도 목록 데이터를 처리할 수 있도록 설계됨
 *
 * PageResultDTo는 List<DTO> 타입으로 DTO 객체들을 보관.
 * Page<Entity>의 엔티티 객체를 DTO로 변환하는 기능이 필요 => Function<EN, DTO> fn
 * */
