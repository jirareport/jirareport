package br.com.leonardoferreira.jirareport.client;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.vo.HolidayVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by jfalbo
 */
@FeignClient(name = "holiday-client", url = "${holiday.url}")
public interface HolidayClient {

    @GetMapping()
    List<HolidayVO> findAllHolidaysInCity(@RequestParam("ano") String year, @RequestParam("estado") String state, @RequestParam("cidade") String city);

}
