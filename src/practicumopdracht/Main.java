package practicumopdracht;

import javafx.application.Application;
import java.time.LocalDate;

public class Main {
    private static final String STUDENT_NAME = "Remzi Cavdar";
    private static final int STUDENT_NUMBER = 500714645;
    private static final boolean YES_I_ACCEPT = true;
    public static boolean launchedFromMain;

    public static void main(String[] args) {
        if(!YES_I_ACCEPT) {
            showDeclarationOfIntegrity();
            return;
        }
        launchedFromMain = true;
        Application.launch(MainApplication.class, args);
    }

    private static void showDeclarationOfIntegrity() {
        System.out.println("Integriteitsverklaring\n---");
        System.out.printf("Datum: %s%n", LocalDate.now());
        System.out.printf("Naam: %s%n", STUDENT_NAME);
        System.out.printf("Studentnummer: %s%n", STUDENT_NUMBER);
        System.out.println("---");

        String integriteitsVerklaring =
            "Ik verklaar naar eer en geweten dat ik deze practicumopdracht zelf zal maken en geen plagiaat zal plegen " +
            "door code van anderen over te nemen.\n\n" +

            "Ik ben me ervan bewust dat:\n"+
            "\t- Er (geautomatiseerd) op fraude wordt gescanned\n" +
            "\t- Verdachte situaties worden gemeld aan de examencommissie\n" +
            "\t- Fraude kan leiden tot het ongeldig verklaren van deze practicumopdracht voor alle studenten\n\n" +

            "Door 'YES_I_ACCEPT' in de Main-class op 'true' te zetten, onderteken ik deze verklaring.";

        System.out.println(integriteitsVerklaring);
    }

    // Getters
    public static String getStudentName() {
        return STUDENT_NAME;
    }
    public static int getStudentNumber() {
        return STUDENT_NUMBER;
    }
}