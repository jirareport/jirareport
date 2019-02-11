package br.com.jiratorio.client;

import br.com.jiratorio.domain.vo.HolidayVO;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "holiday-client", url = "${holiday.url}")
public interface HolidayClient {

    @GetMapping
    @Cacheable("findAllHolidaysInCity")
    List<HolidayVO> findAllHolidaysInCity(@RequestParam("ano") Integer year,
                                          @RequestParam("estado") String state,
                                          @RequestParam("cidade") String city,
                                          @RequestParam("token") String token);
}
