package br.com.leonardoferreira.jirareport.client;

import br.com.leonardoferreira.jirareport.domain.vo.GeoNamesWrapperVO;
import br.com.leonardoferreira.jirareport.domain.vo.HolidayVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by jfalbo
 */
@FeignClient(name = "geonames-client", url = "${geonames.url}")
public interface GeoNamesClient {

    @GetMapping()
    GeoNamesWrapperVO findAllChildrenByGeonameId(@RequestParam("geonameId") String geonameIdBrazil);

}
