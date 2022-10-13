package practicumopdracht.models;

import java.time.LocalDate;
import static practicumopdracht.MainApplication.getDateTimeFormatter;

/**
 * Person model - MasterModel
 * @author Remzi Cavdar - remzi.cavdar@hva.nl
 */

public class Person {
    private String name, sex, birthplace, nationality, documentNumber;
    private LocalDate birthdate;
    private int SSN; // Social Security Number (SSN)

    // Name, Sex, Birthdate, Birthplace, Nationality, SSN, Document number
    public Person(String name, String sex, LocalDate birthdate, String birthplace, String nationality, int SSN,
                  String documentNumber) {
        this.name = name;
        this.sex = sex;
        this.birthdate = birthdate;
        this.birthplace = birthplace;
        this.nationality = nationality;
        this.SSN = SSN;
        this.documentNumber = documentNumber;
    }

    @Override
    public String toString() {
        return String.format("Naam: %s - Geslacht: %s - Geboortedatum: %s - Geboorteplaats: %s - " +
                        "Nationaliteit: %s - BSN: %d - Documentnummer: %s]",
                name, sex, getDateTimeFormatter().format(birthdate), birthplace, nationality, SSN, documentNumber);
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public int getSSN() {
        return SSN;
    }

    public void setSSN(int SSN) {
        this.SSN = SSN;
    }
}