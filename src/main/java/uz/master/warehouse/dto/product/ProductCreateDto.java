package uz.master.warehouse.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.master.warehouse.annotations.HaveFirm;
import uz.master.warehouse.annotations.HaveSector;
import uz.master.warehouse.dto.BaseDto;
import uz.master.warehouse.dto.GenericDto;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDto implements BaseDto {

    @NotBlank
    @Positive
    private Integer item_count;

    @NotBlank
    private String model;

    private String color;

    @NotNull
    @HaveFirm
    private Long firmId;

    @Positive
    private Double default_price;

    @HaveSector
    @NotBlank
    private Long sectorId;

}
