package br.com.leonardoferreira.jirareport.client;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.vo.HolidayVO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by jfalbo
 */
@FeignClient(name = "holiday-client", url = "${holiday.url}")
public interface HolidayClient {

    @GetMapping
    @Cacheable("findAllHolidaysInCity")
    List<HolidayVO> findAllHolidaysInCity(@RequestParam("ano") Integer year,
                                          @RequestParam("estado") String state,
                                          @RequestParam("cidade") String city,
                                          @RequestParam("token") String token);
}
