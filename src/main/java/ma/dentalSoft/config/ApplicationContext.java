package ma.dentalSoft.config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import ma.dentalSoft.repository.api.PatientRepo;
import ma.dentalSoft.service.api.PatientService;
import ma.dentalSoft.ui.controllers.PatientController;

/**
 * Conteneur IoC (Factory / Singleton) qui instancie les composants par réflexion.
 */
public class ApplicationContext {

    private static final ApplicationContext INSTANCE = new ApplicationContext();

    private final Map<Class<?>, Object> beansByClass = new HashMap<>();
    private final Map<String, Object> beans = new HashMap<>();
    private Connection connection;

    private ApplicationContext() {
        var cl = Thread.currentThread().getContextClassLoader();
        try (var configFile = cl.getResourceAsStream("application.properties")) {
            if (configFile == null) {
                throw new IOException("Fichier application.properties introuvable dans le classpath");
            }

            Properties props = new Properties();
            props.load(configFile);

            var patientRepoClassName = props.getProperty("dao.patientDao");
            var patientServiceClassName = props.getProperty("service.patientService");
            var patientControllerClassName = props.getProperty("controller.patientController");

            var cPatientRepo = Class.forName(patientRepoClassName);
            var cPatientService = Class.forName(patientServiceClassName);
            var cPatientCtr = Class.forName(patientControllerClassName);

            connection = JdbcConnectionFactory.createConnection(props);

            PatientRepo repo = instantiateRepo(cPatientRepo, connection);
            PatientService service = (PatientService) cPatientService
                    .getDeclaredConstructor(PatientRepo.class)
                    .newInstance(repo);
            PatientController controller = (PatientController) cPatientCtr
                    .getDeclaredConstructor(PatientService.class)
                    .newInstance(service);

            beansByClass.put(Connection.class, connection);
            beansByClass.put(PatientRepo.class, repo);
            beansByClass.put(PatientService.class, service);
            beansByClass.put(PatientController.class, controller);

            beans.put("connection", connection);
            beans.put("repo", repo);
            beans.put("service", service);
            beans.put("controller", controller);

            Runtime.getRuntime().addShutdownHook(new Thread(this::closeConnection));
        } catch (IOException e) {
            System.err.println("Error loading data from properties file : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error while loading Class (Class Name may be wrong) : " + e.getMessage());
        } catch (NoSuchMethodException e) {
            System.err.println("Error while calling Constructor : " + e.getMessage());
        } catch (InstantiationException e) {
            System.err.println("Error while creating instance : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error : " + e.getMessage());
        }
    }

    private static PatientRepo instantiateRepo(Class<?> repoClass, Connection connection)
            throws ReflectiveOperationException {
        try {
            return (PatientRepo) repoClass
                    .getDeclaredConstructor(Connection.class)
                    .newInstance(connection);
        } catch (NoSuchMethodException ignored) {
            return (PatientRepo) repoClass
                    .getDeclaredConstructor()
                    .newInstance();
        }
    }

    public static ApplicationContext getInstance() {
        return INSTANCE;
    }

    public <T> T getBean(Class<T> beanClass) {
        return beanClass.cast(beansByClass.get(beanClass));
    }

    public Object getBean(String beanName) {
        return beans.get(beanName);
    }

    private void closeConnection() {
        if (connection == null) {
            return;
        }
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error while closing JDBC connection : " + e.getMessage());
        }
    }
}
