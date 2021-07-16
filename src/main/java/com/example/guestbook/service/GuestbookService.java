package com.example.guestbook.service;

import com.example.guestbook.dto.GuestbookDTO;
import com.example.guestbook.dto.PageRequestDTO;
import com.example.guestbook.dto.PageResultDTO;
import com.example.guestbook.entity.GuestBook;

public interface GuestbookService {

    // 등록 관련
    Long register(GuestbookDTO dto);

    // 목록 관련
    // 추가 내용 : GuestbookServiceImpl에 해당 메서드 추가(Override)되어야 함.
    PageResultDTO<GuestbookDTO, GuestBook> getList(PageRequestDTO requestDTO);

    // 조회 관련
    GuestbookDTO read(Long gno);

    // 수정 관련
    void modify(GuestbookDTO dto);

    // 삭제 관련
    void remove(Long gno);

    // DTO -> Entity45
    default GuestBook dtoToEntity(GuestbookDTO dto) {
        GuestBook entity = GuestBook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    // Entity -> DTO
    default GuestbookDTO entityToDto(GuestBook entity){
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }

}