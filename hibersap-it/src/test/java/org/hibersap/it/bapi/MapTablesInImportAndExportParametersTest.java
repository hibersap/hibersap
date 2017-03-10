/*
 * Copyright (c) 2008-2017 akquinet tech@spree GmbH
 *
 * This file is part of Hibersap.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hibersap.it.bapi;

import java.util.ArrayList;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.it.AbstractBapiTest;
import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;
import static org.hibersap.annotations.ParameterType.TABLE_STRUCTURE;

public class MapTablesInImportAndExportParametersTest extends AbstractBapiTest {

    @Test
    public void bapiWithTableInStructure() throws Exception {

        CarListBapi bapi = new CarListBapi();
        bapi.addCarImport("Alfa Romeo", "Spider", 2);
        bapi.addCarImport("Kia", "cee'd", 5);

        session.execute(bapi);

        assertThat(bapi.carsExport).onProperty("manufacturer").containsExactly("Alfa Romeo", "Kia");
        assertThat(bapi.carsExport).onProperty("model").containsExactly("Spider", "cee'd");
        assertThat(bapi.carsExport).onProperty("numberOfSeats").containsExactly(2, 5);
    }

    @Test
    public void bapiWithDeepTableInStructure() throws Exception {
        Manufacturer kia = new Manufacturer("Kia");
        kia.addCar(new Car("Kia", "cee'd", 5));
        kia.addCar(new Car("Kia", "Rio", 4));
        CarManufacturerBapi bapi = new CarManufacturerBapi();
        bapi.addManufacturer(kia);

        session.execute(bapi);

        assertThat(bapi.manufacturersExport).hasSize(1);
        Manufacturer manufacturer = bapi.manufacturersExport.get(0);
        assertThat(kia.name).isEqualTo("Kia");
        assertThat(kia.cars).hasSize(2);
        assertThat(kia.cars.get(0).manufacturer).isEqualTo("Kia");
        assertThat(kia.cars.get(0).model).isEqualTo("cee'd");
        assertThat(kia.cars.get(0).numberOfSeats).isEqualTo(5);
        assertThat(kia.cars.get(1).manufacturer).isEqualTo("Kia");
        assertThat(kia.cars.get(1).model).isEqualTo("Rio");
        assertThat(kia.cars.get(1).numberOfSeats).isEqualTo(4);
    }

    @Bapi("Z_CAR_MANUFACTURER_LIST")
    public static class CarManufacturerBapi {

        @Import
        @Parameter(value = "I_MANUFACTURERS", type = TABLE_STRUCTURE)
        private List<Manufacturer> manufacturersImport = new ArrayList<Manufacturer>();

        @Export
        @Parameter(value = "E_MANUFACTURERS", type = TABLE_STRUCTURE)
        private List<Manufacturer> manufacturersExport;

        public void addManufacturer(final Manufacturer manufacturer) {
            manufacturersImport.add(manufacturer);
        }
    }

    @Bapi("Z_CAR_LIST")
    public static class CarListBapi {

        @Import
        @Parameter(value = "I_CARS", type = TABLE_STRUCTURE)
        private List<Car> carsImport = new ArrayList<Car>();

        @Export
        @Parameter(value = "E_CARS", type = TABLE_STRUCTURE)
        private List<Car> carsExport;

        public void addCarImport(final String manufacturer, final String model, final int numberOfSeats) {
            carsImport.add(new Car(manufacturer, model, numberOfSeats));
        }
    }

    public static class Car {

        @Parameter("MANUFACTURER")
        private String manufacturer;

        @Parameter("MODEL")
        private String model;

        @Parameter("NUM_SEATS")
        private int numberOfSeats;

        private Car() {
            // needed by Hibersap
        }

        public Car(final String manufacturer, final String model, final int numberOfSeats) {

            this.manufacturer = manufacturer;
            this.model = model;
            this.numberOfSeats = numberOfSeats;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public String getModel() {
            return model;
        }

        public int getNumberOfSeats() {
            return numberOfSeats;
        }
    }

    public static class Manufacturer {

        @Parameter("NAME")
        private String name;
        @Parameter(value = "CARS", type = TABLE_STRUCTURE)
        private List<Car> cars = new ArrayList<Car>();

        public Manufacturer() {
            // needed by Hibersap
        }

        public Manufacturer(final String name) {
            this.name = name;
            this.cars = cars;
        }

        public void addCar(final Car car) {
            cars.add(car);
        }
    }
}
