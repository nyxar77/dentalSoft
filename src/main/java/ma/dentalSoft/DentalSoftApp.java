package ma.dentalSoft;

import ma.dentalSoft.config.ApplicationContext;
import ma.dentalSoft.exceptions.RepositoryException;
import ma.dentalSoft.ui.controllers.PatientController;

public class DentalSoftApp {
    public static void main(String[] args) {
        var ctx = ApplicationContext.getInstance();
        var controller = ctx.getBean(PatientController.class);
        if (controller == null) {
            System.err.println("Impossible d'initialiser PatientController.");
            return;
        }

        try {
            controller.getAllPatients();
        } catch (RepositoryException e) {
            System.err.println(e.getSummary());
        }
    }
}
