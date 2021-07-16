package com.example.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

    private int page;   // 화면에서 전달되는 페이지 번호
    private int size;   // 페이지 당 목록의 갯수
    //이 두 가지는 기본값을 설정

    private String type;    //검색 타입
    private String keyword; //사용자 입력 검색어

    public PageRequestDTO(){        // 객체가 생성될 때 기본값 설정
        this.page = 1;
        this.size = 10;
    }
    public Pageable getPageable(Sort sort){
        return PageRequest.of(page -1, size, sort);
        // page -1
        // 화면에서는 1 페이지로 시작하지만,
        // 실제 내부에서는 0이 첫 페이지기 때문
    }
}
