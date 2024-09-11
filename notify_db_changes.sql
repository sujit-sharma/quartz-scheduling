CREATE OR REPLACE FUNCTION notify_job_details_update()
RETURNS trigger AS $$
BEGIN
    PERFORM pg_notify('job_details_update', NEW.job_key ||',' || NEW.expression);
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER job_details_update_trigger
    AFTER UPDATE ON job_details
    FOR EACH ROW
    EXECUTE FUNCTION notify_job_details_update();