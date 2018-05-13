package br.com.leonardoferreira.jirareport.client;

import br.com.leonardoferreira.jirareport.domain.vo.HolidayVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by jfalbo
 */
@FeignClient(name = "holiday-client", url = "${holiday.url} + ${holiday.token}")
public interface HolidayClient {

    @GetMapping()
    List<HolidayVO> findAllHolidaysInCity(@RequestParam("ano") final String year,
                                          @RequestParam("estado") final String state,
                                          @RequestParam("cidade") final String city);

}
