package by.intro.dms.service.office;

import by.intro.dms.mapper.OfficeMapper;
import by.intro.dms.model.office.Office;
import by.intro.dms.model.office.OfficePage;
import by.intro.dms.model.response.OfficesListResponse;
import by.intro.dms.repository.office.OfficeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static by.intro.dms.service.PaginationUtils.buildPaginationInfo;

@Service
public class OfficeService {

    private final OfficeRepository officeRepository;
    private final OfficeMapper officeMapper;

    public OfficeService(OfficeRepository officeRepository, OfficeMapper officeMapper) {
        this.officeRepository = officeRepository;
        this.officeMapper = officeMapper;
    }

    public Page<Office> getOfficesPage(OfficePage officePage) {
        int pageNumber = officePage.getPageNumber();
        int pageSize = officePage.getPageSize();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return officeRepository.findAll(pageRequest);
    }

    public OfficesListResponse getOffices(OfficePage officePage) {
        Page<Office> officesPage = getOfficesPage(officePage);
        return OfficesListResponse.builder()
                .offices(officeMapper.toDtoList(officesPage.toList()))
                .paginationInfo(buildPaginationInfo(officesPage))
                .build();
    }
}
