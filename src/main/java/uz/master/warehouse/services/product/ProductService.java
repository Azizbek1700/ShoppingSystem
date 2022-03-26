package uz.master.warehouse.services.product;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.master.warehouse.dto.product.ProductCreateDto;
import uz.master.warehouse.dto.product.ProductDto;
import uz.master.warehouse.dto.product.ProductUpdateDto;
import uz.master.warehouse.dto.responce.DataDto;
import uz.master.warehouse.entity.product.Product;
import uz.master.warehouse.mapper.product.ProductMapper;
import uz.master.warehouse.repository.product.ProductRepository;
import uz.master.warehouse.services.AbstractService;
import uz.master.warehouse.services.GenericCrudService;
import uz.master.warehouse.validator.project.ProductValidator;

import java.util.List;

@Service
public class ProductService extends AbstractService<ProductRepository, ProductMapper, ProductValidator> implements GenericCrudService<Product, ProductDto, ProductCreateDto, ProductUpdateDto, Long> {
    public ProductService(ProductRepository repository,
                          ProductMapper mapper,
                          ProductValidator validator) {
        super(repository, mapper, validator);
    }

    @Override
    public DataDto<Long> create(ProductCreateDto createDto) {
        Product product = mapper.fromCreateDto(createDto);
        product.setColor(createDto.getColor());
        product.setDefault_price(createDto.getDefault_price());
        product.setModel(createDto.getModel());
        product.setFirmId(createDto.getFirmId());
        product.setItem_count(createDto.getItem_count());
        Product save = repository.save(product);
        return new DataDto<>(save.getId());
    }

    @Override
    public DataDto<Void> delete(Long id) {
        repository.deleteProduct(id);
        return new DataDto<>();
    }

    @Override
    public DataDto<Long> update(ProductUpdateDto updateDto) {
        Product product = mapper.fromUpdateDto(updateDto);
        product.setDefault_price(updateDto.getDefault_price());
        Product save = repository.save(product);
        return new DataDto<>(save.getId());
    }

    @Override
    public DataDto<List<ProductDto>> getAll() {
        List<Product> list = repository.findAllByDeletedFalse();
        return new DataDto<>(mapper.toDto(list));
    }

    @Override
    public DataDto<ProductDto> get(Long id) {
        Product product = repository.findByIdAndDeletedFalse(id).orElseThrow(() -> {
            throw new UsernameNotFoundException("Not found");
        });
        return new DataDto<>(mapper.toDto(product));
    }
}
