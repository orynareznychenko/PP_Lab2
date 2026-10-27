public class Car {
    private String vin;
    private String brand;
    private String model;
    private int year;
    private Client owner;

    public Car(
            final String carVin,
            final String carBrand,
            final String carModel,
            final int carYear,
            final Client carOwner
    ) {
        setVin(carVin);
        setBrand(carBrand);
        setModel(carModel);
        setYear(carYear);
        setOwner(carOwner);
    }

    private void setVin(final String carVin) {
        validateVin(carVin);
        vin = carVin.strip();
    }

    public void setBrand(final String carBrand) {
        validateBrand(carBrand);
        brand = carBrand.strip();
    }

    public void setModel(final String carModel) {
        validateModel(carModel);
        model = carModel.strip();
    }

    public void setYear(final int carYear) {
        validateYear(carYear);
        year = carYear;
    }

    public void setOwner(final Client carOwner) {
        validateOwner(carOwner);
        owner = carOwner;
    }

    private void validateVin(final String carVin) {
        if (carVin == null) {
            throw new IllegalArgumentException("VIN cannot be empty");
        }
        final boolean blank = carVin.isBlank();
        if (blank) {
            throw new IllegalArgumentException("VIN cannot be empty");
        }
    }

    private void validateBrand(final String carBrand) {
        if (carBrand == null) {
            throw new IllegalArgumentException("Brand cannot be empty");
        }
        final boolean blank = carBrand.isBlank();
        if (blank) {
            throw new IllegalArgumentException("Brand cannot be empty");
        }
    }

    private void validateModel(final String carModel) {
        if (carModel == null) {
            throw new IllegalArgumentException("Model cannot be empty");
        }
        final boolean blank = carModel.isBlank();
        if (blank) {
            throw new IllegalArgumentException("Model cannot be empty");
        }
    }

    private void validateYear(final int carYear) {
        final int minYear = 1894;
        final int maxYear = 2026;
        final boolean tooOld = carYear < minYear;
        final boolean tooNew = carYear > maxYear;
        if (tooOld || tooNew) {
            throw new IllegalArgumentException("Invalid car manufacture year");
        }
    }

    private void validateOwner(final Client carOwner) {
        if (carOwner == null) {
            throw new IllegalArgumentException("Car owner cannot be null");
        }
    }

    public String getVin() {
        return vin;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public Client getOwner() {
        return owner;
    }

    public String formatInfo() {
        return String.format("%s %s (%s), year: %d", brand, model, vin, year);
    }
}