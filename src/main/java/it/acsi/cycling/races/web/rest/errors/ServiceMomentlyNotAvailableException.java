package it.acsi.cycling.races.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class ServiceMomentlyNotAvailableException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public ServiceMomentlyNotAvailableException() {
        super(ErrorConstants.SERVICE_MOMENTLY_NOT_AVAILABLE,
            "Ci scusiamo ma, il servizio è momentaneamente non disponibile, riprovate più tardi",
            Status.INTERNAL_SERVER_ERROR);
    }
}
