package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.client.GeoNamesClient;
import br.com.leonardoferreira.jirareport.domain.vo.GeoNamesWrapperVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geonames")
public class GeoNamesRestController {

    @Autowired
    private GeoNamesClient geoNamesClient;

    @GetMapping("/findAllCitiesByState/{stateId}")
    public ResponseEntity<GeoNamesWrapperVO> findAllCitiesByState(@PathVariable String stateId) {
        GeoNamesWrapperVO allChildrenByGeonameId = geoNamesClient.findAllChildrenByGeonameId(stateId);
        return ResponseEntity.ok(allChildrenByGeonameId);
    }

}
