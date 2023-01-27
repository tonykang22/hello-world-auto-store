package com.github.kingwaggs.productanalyzer.service.productsourcing;

import com.github.kingwaggs.productanalyzer.config.TestConfig;
import com.github.kingwaggs.productanalyzer.exception.DeliveryPriceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(properties = "spring.profiles.active:local", classes = TestConfig.class)
class PapagoTextTranslatorIntegrationTest {

//    @Autowired
    private PapagoTextTranslator papagoTextTranslator;

    @Test
    void translateInternalError() {
        // given
        String word =
//                "ence and some small creativity, also for research and development about how to apply it to life. Really cool and perfect for you if you want to get into electrical engineering.\n" +
//                "Specifications:\n" +
//                "Color: Multicolor\n" +
//                "Type: Diode, Triode, Metal Film Resistance, Aluminum Electrolytic Capacitor, Ceramic Chip Capacitor\n" +
//                "Package Size:\n" +
//                "Package Weight:\n" +
//                "Package List:\n" +
                "820PCS 1/4W 1% Metal Film Resistor: Each value 20PCS, all 41 values in one package\n" +
                "1Ω, 10Ω, 20Ω, 33Ω, 47Ω, 51Ω, 68Ω, 75Ω, 82Ω, 91Ω, 100Ω, 200Ω, 220Ω, 330Ω, 470Ω, 510Ω, 680Ω, 750Ω, 820Ω, 910Ω, 1KΩ, 10KΩ, 20KΩ, 33KΩ, 47KΩ, 51KΩ, 68KΩ, 75KΩ, 82KΩ, 91KΩ, 100KΩ, 200KΩ, 220KΩ, 330KΩ, 470KΩ, 510KΩ, 680KΩ, 750KΩ, 820KΩ, 910KΩ, 1MΩ\n" +
                "300PCS Ceramic Capacitors (50V): Each value 10PCS, all 30 values in one package\n" +
                "2P, 3P, 5P, 10P, 15P, 22P, 30P, 33P, 47P, 68P, 75P, 82P, 101, 151, 221, 331, 471, 681, 102, 152, 222, 332, 472, 682, 103, 153, 223, 473, 683, 104\n" +
                "120 PCS Aluminum Electrolytic Capacitor: Each value 10PCS, all 12 values in one package\n" +
                "50V 0.22uF, 50V 0.47uF, 50V 1uF, 50V 2.2uF, 50V 4.7uF, 25V 10uF, 16V 22uF, 16V 33uF, 16V 47uF, 16V 100uF, 16V 220uF, 16V 470uF\n" +
                "180PCS TO-92 Transistor: Each value 10PCS, all 18 values in one package\n" +
                "59012, 59013, 59014, 59015, 59018, A1015, C1815, 58050, 58550, A42, 2N5401, 2N5551, A733, C945, 2N3906, 2N3904, 2N2222, A92\n";
//                "100PCS 3mm LED: Each color 20PCS, all 5 colors in one package\n" +
//                "Red, Yellow, Blue, White, Green\n" +
//                "100PCS 5mm LED: Each color 20PCS, all 5 colors in one package\n" +
//                "Red, Yellow, Blue, White, Green\n" +
//                "100 PCS Diode: all 8 values in one package\n" +
//                "1N4007 25PCS, 1N4148 25PCS, 1N5399 10PCS, 1N5819 10PCS, FR107 10PCS, FR207 10PCS, 1N5822 5PCS, 1N5408 5PCS\n" +
//                "4PCS Prototype PCBs: Each value 1PCS, all 4 values in one package";
        String source = "en";
        String target = "ko";

        // when
        String translatedText = papagoTextTranslator.translateText(word, source, target);
        System.out.println(translatedText);

        // then

    }

    @Test
    void test() {
        // given
        int a = 1;

        // when
        try {
            throwExceptionWhenOne(a);
        } catch (DeliveryPriceException e) {
            StackTraceElement[] stackTrace = e.getStackTrace();

//            Arrays.stream(stackTrace)
//                    .forEach(System.out::println);

            Throwable[] suppressed = e.getSuppressed();
            Arrays.stream(suppressed)
                    .forEach(System.out::println);

        }

        Box box = new Box();
        box.setObject(new DeliveryPriceException("조졌네요"));

        // then


    }

    public static class Box {

        private Object object;

        Object getObject() {
            return this.object;
        }

        void setObject(Object object) {
            this.object = object;
        }
    }

    private void throwExceptionWhenOne(int a) throws DeliveryPriceException {
        if (a == 1) {
            throw new DeliveryPriceException("뭔가 웃겨요");
        }
    }

}