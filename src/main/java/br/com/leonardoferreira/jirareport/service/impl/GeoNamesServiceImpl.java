package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.client.GeoNamesClient;
import br.com.leonardoferreira.jirareport.domain.vo.GeoNamesWrapperVO;
import br.com.leonardoferreira.jirareport.service.GeoNamesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GeoNamesServiceImpl implements GeoNamesService {

    @Autowired
    private GeoNamesClient geoNamesClient;

    @Override
    public GeoNamesWrapperVO findAllStatesOfBrazil() {
        return geoNamesClient.findAllChildrenByGeonameId("3469034");
    }

    @Override
    public GeoNamesWrapperVO findAllCitiesByState(String geonameIdState) {
        return geoNamesClient.findAllChildrenByGeonameId(geonameIdState);
    }

}
