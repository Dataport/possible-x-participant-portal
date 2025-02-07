package eu.possiblex.participantportal.application.control;

import eu.possiblex.participantportal.application.entity.CreateDataOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.CreateServiceOfferingRequestTO;
import eu.possiblex.participantportal.application.entity.PrefillFieldsTO;
import eu.possiblex.participantportal.business.entity.CreateDataOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.CreateServiceOfferingRequestBE;
import eu.possiblex.participantportal.business.entity.PrefillFieldsBE;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = { ProviderApiMapperTest.TestConfig.class })
class ProviderApiMapperTest {

    @Autowired
    private ProviderApiMapper providerApiMapper;

    @Test
    void mapGetCreateServiceOfferingRequestBE() {

        CreateServiceOfferingRequestTO to = CreateServiceOfferingRequestTO.builder().build();
        // TODO
        CreateServiceOfferingRequestBE be = providerApiMapper.getCreateOfferingRequestBE(to);

    }

    @Test
    void mapGetDataCreateOfferingRequestBE() {

        CreateDataOfferingRequestTO to = CreateDataOfferingRequestTO.builder().build();
        // TODO
        CreateDataOfferingRequestBE be = providerApiMapper.getCreateOfferingRequestBE(to);

    }

    @Test
    void mapGetPrefillFieldsTO() {

        PrefillFieldsBE be = PrefillFieldsBE.builder().build();
        // TODO
        PrefillFieldsTO to = providerApiMapper.getPrefillFieldsTO(be);

    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ProviderApiMapper providerApiMapper() {

            return Mappers.getMapper(ProviderApiMapper.class);
        }

    }
}
