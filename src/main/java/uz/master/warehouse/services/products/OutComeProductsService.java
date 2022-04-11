package uz.master.warehouse.services.products;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.master.warehouse.criteria.GenericCriteria;
import uz.master.warehouse.dto.outComeProducts.OutComeProductsCreateDto;
import uz.master.warehouse.dto.outComeProducts.OutComeProductsDto;
import uz.master.warehouse.dto.outComeProducts.OutComeProductsUpdateDto;
import uz.master.warehouse.dto.responce.AppErrorDto;
import uz.master.warehouse.dto.responce.DataDto;
import uz.master.warehouse.entity.products.OutComeProducts;
import uz.master.warehouse.mapper.products.OutComeProductsMapper;
import uz.master.warehouse.repository.products.OutComeProductsRepository;
import uz.master.warehouse.services.AbstractService;
import uz.master.warehouse.services.GenericCrudService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class OutComeProductsService extends AbstractService<OutComeProductsRepository, OutComeProductsMapper>
        implements GenericCrudService<OutComeProducts, OutComeProductsDto, OutComeProductsCreateDto, OutComeProductsUpdateDto, GenericCriteria, Long> {

    private final WareHouseProductsService service;
    private final WareHouseProductsService wareHouseProductsService;

    public OutComeProductsService(OutComeProductsRepository repository, @Qualifier("outComeProductsMapperImpl") OutComeProductsMapper mapper, WareHouseProductsService service, WareHouseProductsService wareHouseProductsService) {
        super(repository, mapper);
        this.service = service;
        this.wareHouseProductsService = wareHouseProductsService;
    }


    @Override
    public DataDto<Long> create(OutComeProductsCreateDto createDto) {
        OutComeProducts outComeProducts = mapper.fromCreateDto(createDto);
        service.checkCount(outComeProducts.getProductId(), createDto.getCount());
        return new DataDto<>(repository.saveOutCome(createDto.getClientBarId(), createDto.getCount(), createDto.getProductId(), createDto.getProductPrice()));
    }

    @Override
    public DataDto<Void> delete(Long clientId) {
        repository.deleteAllByClientBarId(clientId);
        return new DataDto<>();
    }

    @Override
    public DataDto<Long> update(OutComeProductsUpdateDto updateDto) {
        Optional<OutComeProducts> optional = repository.findById(updateDto.getId());
        if (optional.isPresent()) {
            OutComeProducts products = optional.get();
            mapper.updateModel(updateDto, products);
            return new DataDto<>(repository.save(products).getId());
        }
        throw new RuntimeException("Bad Request");
    }

    @Override
    public DataDto<List<OutComeProductsDto>> getAll() {
        return null;
    }

    public DataDto<List<OutComeProductsDto>> getAll(Long clientId) {
        List<OutComeProducts> products = repository.findAllByClientBarId(clientId);
        List<OutComeProductsDto> dtos = mapper.toDto(products);
        return new DataDto<>(dtos);
    }


    @Override
    public DataDto<OutComeProductsDto> get(Long id) {
        Optional<OutComeProducts> outComeProductsOptional = repository.findById(id);
        if (outComeProductsOptional.isPresent()) {
            return new DataDto<>(mapper.toDto(outComeProductsOptional.get()));

        }
        throw new RuntimeException("Not found");
    }

    @Override
    public DataDto<List<OutComeProductsDto>> getWithCriteria(GenericCriteria criteria) {
        return null;
    }


    public DataDto<Boolean> updateTaken(Long id) {
        Optional<OutComeProducts> outComeProductsOptional = repository.findById(id);
        if (outComeProductsOptional.isPresent()) {
            OutComeProducts found = outComeProductsOptional.get();
            boolean res = wareHouseProductsService.outcomeProducts(found);

            if (!res) return new DataDto<>(Boolean.FALSE);
            found.setTaken(true);
            repository.save(found);
            return new DataDto<>(Boolean.TRUE);
        }
        return new DataDto<>(Boolean.FALSE);


    }

    public DataDto<List<OutComeProductsDto>> getByDate(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            List<OutComeProducts> allByCreatedAt_date = repository.findAllByCreatedAt_Date(localDate);
            return new DataDto<>(mapper.toDto(allByCreatedAt_date));
        } catch (DateTimeParseException e) {
            return new DataDto<>(new AppErrorDto("UnValid Date format", HttpStatus.BAD_REQUEST));
        }
    }
}
