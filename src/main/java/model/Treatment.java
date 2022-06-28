package model;

import utils.DateConverter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This class represents a treatment.
 */
public class Treatment {
    private long id;
    private final long patientId;
    private long caregiverId;
    private LocalDate date;
    private LocalTime begin;
    private LocalTime end;
    private TreatmentType type;
    private String remarks;

    /**
     * @param patientId   the id of the patient
     * @param caregiverId the id of the caregiver
     * @param date        the date of the treatment
     * @param begin       the beginning time of the treatment
     * @param end         the ending time of the treatment
     * @param type        the type of the treatment
     * @param remarks     the remarks of the treatment
     */
    public Treatment(long patientId, long caregiverId, LocalDate date, LocalTime begin,
                     LocalTime end, TreatmentType type, String remarks) {
        this.patientId = patientId;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.type = type;
        this.remarks = remarks;
        if (remarks.equals("")) throw new IllegalArgumentException("Bemerkungen müssen angegeben werden!");
        this.caregiverId = caregiverId;

    }

    /**
     * @param id          the id of the treatment
     * @param patientId   the id of the patient
     * @param caregiverId the id of the caregiver
     * @param date        the date of the treatment
     * @param begin       the beginning time of the treatment
     * @param end         the ending time of the treatment
     * @param type        the type of the treatment
     * @param remarks     the remarks of the treatment
     */
    public Treatment(long id, long patientId, long caregiverId, LocalDate date, LocalTime begin,
                     LocalTime end, TreatmentType type, String remarks) {
        this.id = id;
        this.patientId = patientId;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.type = type;
        this.remarks = remarks;
        this.caregiverId = caregiverId;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the patientId
     */
    public long getPatientId() {
        return this.patientId;
    }

    /**
     * @return the caregiverId
     */
    public String getDate() {
        return date.toString();
    }

    /**
     * @return the begin
     */
    public String getBegin() {
        return begin.toString();
    }

    /**
     * @return the end
     */
    public String getEnd() {
        return end.toString();
    }

    /**
     * @param s_date the date to set
     */
    public void setDate(String s_date) {
        if (s_date == null || s_date.equals("")) throw new IllegalArgumentException("Datum darf nicht leer sein.");

        this.date = DateConverter.convertStringToLocalDate(s_date);

    }

    /**
     * @param begin the beginning date to set
     */
    public void setBegin(String begin) {
        if (begin.equals("")) throw new IllegalArgumentException("Begin darf nicht leer sein.");

        this.begin = DateConverter.convertStringToLocalTime(begin);
    }

    /**
     * @param end the ending date to set
     */
    public void setEnd(String end) {
        if (end.equals("")) throw new IllegalArgumentException("Ende darf nicht leer sein.");

        this.end = DateConverter.convertStringToLocalTime(end);

    }

    /**
     * @return the {@link TreatmentType}
     */
    public TreatmentType getType() {
        return type;
    }

    /**
     * @param type the {@link TreatmentType} to set
     */
    public void setType(TreatmentType type) {
        this.type = type;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        if (remarks.equals("")) throw new IllegalArgumentException("Bemerkungen müssen angegeben werden.");

        this.remarks = remarks;
    }

    /**
     * @return the caregiverId
     */
    public long getCaregiverId() {
        return caregiverId;
    }

    /**
     * @param caregiverId the caregiverId to set
     */
    public void setCaregiverId(long caregiverId) {
        this.caregiverId = caregiverId;
    }

    /**
     * @return the string representation of the treatment
     */
    public String toString() {
        return "\nBehandlung" + "\nTID: " + this.id +
                "\nPID: " + this.patientId +
                "\nDate: " + this.date +
                "\nBegin: " + this.begin +
                "\nEnd: " + this.end +
                "\nType: " + this.type +
                "\nRemarks: " + this.remarks + "\n";
    }
}