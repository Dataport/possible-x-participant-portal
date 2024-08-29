package eu.possiblex.participantportal.business.entity.edc.transfer;

public enum TransferProcessState {
    INITIAL,
    PROVISIONING,
    PROVISIONING_REQUESTED,
    PROVISIONED,
    REQUESTING,
    REQUESTED,
    STARTING,
    STARTED,
    SUSPENDING,
    SUSPENDED,
    COMPLETING,
    COMPLETED,
    TERMINATING,
    TERMINATED,
    DEPROVISIONING,
    DEPROVISIONING_REQUESTED,
    DEPROVISIONED;
}
