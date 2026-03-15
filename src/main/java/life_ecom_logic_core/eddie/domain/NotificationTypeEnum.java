package life_ecom_logic_core.eddie.domain;

/**
 * PostgreSQL native enum mapped to the notification_type database type.
 * Values must remain lowercase to match the DB enum definition.
 */
public enum NotificationTypeEnum {
    payment_reminder,
    recurring_payment,
    system
}
