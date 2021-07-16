package com.example.guestbook.service;

import com.example.guestbook.dto.GuestbookDTO;
import com.example.guestbook.dto.PageRequestDTO;
import com.example.guestbook.dto.PageResultDTO;
import com.example.guestbook.entity.GuestBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GuestbookServiceTest {

    @Autowired
    private GuestbookService service;

    @Test
    public void testRegister(){

        GuestbookDTO dto = GuestbookDTO.builder()
                .title("Test Title")
                .content("Test Content")
                .writer("testU0")
                .build();

        System.out.println(service.register((dto)));
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(11).size(10).build();

        PageResultDTO<GuestbookDTO, GuestBook> resultDTO =  service.getList(pageRequestDTO);

        System.out.println("PREV :" + resultDTO.isPrev());
        System.out.println("NEXT :" + resultDTO.isNext());
        System.out.println("TOTAL :" + resultDTO.getTotalPage());

        System.out.println("--------------------------");
        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()){
            System.out.println(guestbookDTO);
        }
        System.out.println("--------------------------");

        resultDTO.getPageList().forEach(i -> System.out.println(i));
        //페이지 번호 목록 출력확인..
    }

    @Test
    public void testSearch(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).type("tc").keyword("11").build();

        PageResultDTO<GuestbookDTO, GuestBook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV :" + resultDTO.isPrev());
        System.out.println("NEXT :" + resultDTO.isNext());
        System.out.println("TOTAL :" + resultDTO.getTotalPage());

        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()){
            System.out.println(guestbookDTO);   //DTO 객체 목록
        }

        resultDTO.getPageList().forEach(i -> System.out.println(i));    //페이지 번호 목록
    }



}
