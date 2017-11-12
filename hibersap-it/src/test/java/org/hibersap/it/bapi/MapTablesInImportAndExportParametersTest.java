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
import javax.annotation.Generated;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Changing;
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
        Car alfaRomeo = new Car("Alfa Romeo", "Spider", 2);
        Car kiaCeed = new Car("Kia", "cee'd", 5);
        CarListBapi bapi = new CarListBapi().addCarImport(alfaRomeo).addCarImport(kiaCeed);

        session.execute(bapi);

        assertThat(bapi.carsExport).containsExactly(alfaRomeo, kiaCeed);
    }

    @Test
    public void bapiWithTableInStructurePassingEmptyImportParameter() throws Exception {

        CarListBapi bapi = new CarListBapi();

        session.execute(bapi);

        assertThat(bapi.carsExport).isEmpty();
    }

    @Test
    public void bapiWithDeepTableInStructurePassingEmptyManufacturerList() throws Exception {
        CarManufacturerBapi bapi = new CarManufacturerBapi();

        session.execute(bapi);

        assertThat(bapi.manufacturersExport).isEmpty();
    }

    @Test
    public void bapiWithDeepTableInStructurePassingEmptyCarList() throws Exception {
        CarManufacturerBapi bapi = new CarManufacturerBapi();

        session.execute(bapi);

        assertThat(bapi.manufacturersExport).isEmpty();
    }

    @Test
    public void changingStructureContainsOnlyEntriesFromSapWhenPassingEmptyManufacturersList() throws Exception {
        CarManufacturerBapi bapi = new CarManufacturerBapi();

        session.execute(bapi);

        assertThat(bapi.manufacturersChanging).onProperty("name").containsOnly("Honda", "Alfa Romeo");
    }

    @Test
    public void changingStructureContainsEntriesPassedInAndEntriesAddedBySap() throws Exception {
        Manufacturer tata = new Manufacturer("Tata");

        CarManufacturerBapi bapi = new CarManufacturerBapi();
        bapi.addManufacturerToChanging(tata);

        session.execute(bapi);

        assertThat(bapi.manufacturersChanging).onProperty("name").containsOnly("Tata", "Honda", "Alfa Romeo");
    }

    @Test
    public void changinsStructureContainsAllDataProvdidedKeepingOrder() throws Exception {
        Manufacturer alfaRomeo = new Manufacturer("Alfa Romeo").addCar(new Car("Alfa Romeo", "Spider", 2));
        Manufacturer honda = new Manufacturer("Honda").addCar(new Car("Honda", "Accord", 5)).addCar(new Car("Honda", "Civic", 4));
        Manufacturer tata = new Manufacturer("Tata").addCar(new Car("Tata", "Nano", 4)).addCar(new Car("Tata", "Aria", 5));

        CarManufacturerBapi bapi = new CarManufacturerBapi();
        bapi.addManufacturerToChanging(tata);

        session.execute(bapi);

        assertThat(bapi.manufacturersChanging.get(0)).isEqualTo(tata);
        assertThat(bapi.manufacturersChanging.get(1)).isEqualTo(alfaRomeo);
        assertThat(bapi.manufacturersChanging.get(2)).isEqualTo(honda);
    }

    @Test
    public void bapiWithDeepTableInStructure() throws Exception {
        Manufacturer kia = new Manufacturer("Kia")
                .addCar(new Car("Kia", "cee'd", 5))
                .addCar(new Car("Kia", "Rio", 4));
        Manufacturer zastava = new Manufacturer("Zastava")
                .addCar(new Car("Zastava", "128", 4))
                .addCar(new Car("Yugo", "45", 4));
        CarManufacturerBapi bapi = new CarManufacturerBapi();
        bapi.addManufacturerToImport(kia);
        bapi.addManufacturerToImport(zastava);

        session.execute(bapi);

        assertThat(bapi.manufacturersExport)
                .hasSize(2)
                .containsExactly(kia, zastava);
    }

    @Bapi("Z_CAR_MANUFACTURER_LIST")
    public static class CarManufacturerBapi {

        @Import
        @Parameter(value = "I_MANUFACTURERS", type = TABLE_STRUCTURE)
        private List<Manufacturer> manufacturersImport = new ArrayList<Manufacturer>();

        @Export
        @Parameter(value = "E_MANUFACTURERS", type = TABLE_STRUCTURE)
        private List<Manufacturer> manufacturersExport;

        @Changing
        @Parameter(value = "C_MANUFACTURERS", type = TABLE_STRUCTURE)
        private List<Manufacturer> manufacturersChanging = new ArrayList<Manufacturer>();

        void addManufacturerToImport(final Manufacturer manufacturer) {
            manufacturersImport.add(manufacturer);
        }

        void addManufacturerToChanging(final Manufacturer manufacturer) {
            manufacturersChanging.add(manufacturer);
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

        CarListBapi addCarImport(final Car car) {
            carsImport.add(car);
            return this;
        }
    }

    @BapiStructure
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

        Car(final String manufacturer, final String model, final int numberOfSeats) {
            this.manufacturer = manufacturer;
            this.model = model;
            this.numberOfSeats = numberOfSeats;
        }

        @Override
        @Generated("IntelliJ")
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Car car = (Car) o;

            if (numberOfSeats != car.numberOfSeats) {
                return false;
            }
            if (manufacturer != null ? !manufacturer.equals(car.manufacturer) : car.manufacturer != null) {
                return false;
            }
            return model != null ? model.equals(car.model) : car.model == null;
        }

        @Override
        @Generated("IntelliJ")
        public int hashCode() {
            int result = manufacturer != null ? manufacturer.hashCode() : 0;
            result = 31 * result + (model != null ? model.hashCode() : 0);
            result = 31 * result + numberOfSeats;
            return result;
        }

        @Override
        public String toString() {
            return "Car{" +
                    "manufacturer='" + manufacturer + '\'' +
                    ", model='" + model + '\'' +
                    ", numberOfSeats=" + numberOfSeats +
                    '}';
        }
    }

    @BapiStructure
    public static class Manufacturer {

        @Parameter("NAME")
        private String name;

        @Parameter(value = "CARS", type = TABLE_STRUCTURE)
        private List<Car> cars = new ArrayList<Car>();

        public Manufacturer() {
            // needed by Hibersap
        }

        Manufacturer(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        Manufacturer addCar(final Car car) {
            cars.add(car);
            return this;
        }

        @Override
        @Generated("IntelliJ")
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Manufacturer that = (Manufacturer) o;

            if (name != null ? !name.equals(that.name) : that.name != null) {
                return false;
            }
            return cars != null ? cars.equals(that.cars) : that.cars == null;
        }

        @Override
        @Generated("IntelliJ")
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (cars != null ? cars.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Manufacturer{" +
                    "name='" + name + '\'' +
                    ", cars=" + cars +
                    '}';
        }
    }
}
