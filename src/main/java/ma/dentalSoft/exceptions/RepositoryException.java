package ma.dentalSoft.exceptions;

/**
 * Signale une erreur survenue dans la couche d'accès aux données.
 */
public class RepositoryException extends RuntimeException {

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Retourne une description courte adaptée à l'affichage, sans imprimer
     * toute la pile d'exécution.
     *
     * @return le type de l'exception, le message métier et la cause technique
     */
    public String getSummary() {
        var causeMessage = getCause() == null
                ? "Cause technique non disponible"
                : getCause().getMessage();

        return "[%s] %s — Cause : %s".formatted(
                getClass().getSimpleName(),
                getMessage(),
                causeMessage
        );
    }
}
