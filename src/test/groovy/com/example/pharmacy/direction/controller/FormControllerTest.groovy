package com.example.pharmacy.direction.controller

import com.example.pharmacy.direction.dto.OutputDto
import com.example.pharmacy.domain.service.PharmacyRecommendationService
import org.hibernate.result.Output
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

class FormControllerTest extends Specification {

    private MockMvc mockMvc
    private PharmacyRecommendationService pharmacyRecommendationService = Mock()
    private List<OutputDto> outputDtoList

    def setup() {
        // FormController MockMvc 객체로 만든다.
        mockMvc = MockMvcBuilders.standaloneSetup(new FormController(pharmacyRecommendationService)).build()
        // standaloneSetup : 테스트할 컨트롤러를 수동으로 주입하는 것. 한 컨트롤러에 집중하여 테스트하는 용도로 사용 -> 유닛 테스트와 유사

        outputDtoList = new ArrayList<>()
        outputDtoList.addAll(
                OutputDto.builder()
                        .pharmacyName("pharmacy1")
                        .build(),
                OutputDto.builder()
                        .pharmacyName("pharmacy2")
                        .build()
        )
    }
    def "GET /"(){
        expect:
        // FormController 의 "/" URI를 get 방식으로 호출한다.
        mockMvc.perform(get("/")) // perform : 요청을 전송하는 역할. 결과로 ResultActions 객체를 받는다.
        // andExpect : 응답을 검증하는 역할. andExpect 가 1개라도 실패하면 테스트는 실패한다.
                .andExpect(handler().handlerType(FormController.class)) // handler() : 요청에 매핑된 컨트롤러 검증
                .andExpect(status().isOk()) // status() : 상태코드 검증
                .andExpect(view().name("main")) // 리턴하는 뷰 이름 검증
                .andDo(log()) // 요청/응답 전체 메시지를 확인할 수 있다. 실행 결과를 디버길 레벨로 출력
    }

    def "POST /search"(){
        given:
        String inputAddress = "부산 사상구 모라동"

        when:
        def resultActions = mockMvc.perform(post("/search")
                .param("address", inputAddress))
        // param : 키=값의 파라미터를 전달할 수 있다. 여러 개일 떄는 params(), 하나일 떄는 param() 사용

        then:
        1 * pharmacyRecommendationService.recommendPharmacyList(argument -> {
            assert argument == inputAddress // mock 객체의 argument 검증
        }) >> outputDtoList

        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("output"))
        // model : 컨트롤러에서 저장한 모델들의 정보 검증
                .andExpect(model().attributeExists("outputFormList")) // outputFormList 에 해당하는 데이터가 model 에 있는지 검증
                .andExpect(model().attribute("outputFormList", outputDtoList)) // outputFormList 에 해당하는 데이터가 value 객체인지 검증
                .andDo(print()) // 실행 결과를 지정해준 대상으로 출력해준다.
    }

}
