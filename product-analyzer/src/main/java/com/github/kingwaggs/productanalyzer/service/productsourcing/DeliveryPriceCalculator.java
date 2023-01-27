package com.github.kingwaggs.productanalyzer.service.productsourcing;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author waggs
 * @see <a href="https://www.ohmyzip.com/how-it-works/shipping-fee/price-chart/">Ohmyzip price chart</a>
 */

@Slf4j
@Service
public class DeliveryPriceCalculator {
    private static final String DELIVERY_PRICE_LIST_FILE_PATH = "static/ohmyzip_delivery_price_list.xlsx";

    private static final int DE_SHEET_INDEX = 0;
    private static final int CA_SHEET_INDEX = 1;

    private static final int FIRST_ROW_INDEX = 0;
    private static final int WEIGHT_INDEX = 0;
    private static final int PRICE_INDEX = 1;

    private Map<Double, Double> DE_FAST_DELIVERY_PRICE_MAP;
    private Map<Double, Double> CA_DELIVERY_PRICE_MAP;

    public Double getDEFastDeliveryPrice(Double weight) {
        if (DE_FAST_DELIVERY_PRICE_MAP == null) {
            DE_FAST_DELIVERY_PRICE_MAP = getPriceMap(DE_SHEET_INDEX);
        }
        return DE_FAST_DELIVERY_PRICE_MAP.get(weight);
    }

    public Double getCADeliveryPrice(Double weight) {
        if (CA_DELIVERY_PRICE_MAP == null) {
            CA_DELIVERY_PRICE_MAP = getPriceMap(CA_SHEET_INDEX);
        }
        return CA_DELIVERY_PRICE_MAP.get(weight);
    }

    private Map<Double, Double> getPriceMap(int sheetIndex) {
        try {
            Map<Double, Double> fastDeliveryPriceMap = new LinkedHashMap<>();
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(DELIVERY_PRICE_LIST_FILE_PATH);
            Workbook workbook = new XSSFWorkbook(Objects.requireNonNull(inputStream));
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            for (Row row : sheet) {
                if (row.getRowNum() == FIRST_ROW_INDEX) {
                    continue;
                }
                fastDeliveryPriceMap.put(row.getCell(WEIGHT_INDEX).getNumericCellValue(), row.getCell(PRICE_INDEX).getNumericCellValue());
            }
            return fastDeliveryPriceMap;
        } catch (IOException e) {
            log.error("Error occured while reading Fast Delivery Price Map. Empty map returned.", e);
            return new LinkedHashMap<>();
        }
    }

}
