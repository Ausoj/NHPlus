package model;

import utils.DateConverter;

import java.time.LocalDate;
import java.time.LocalTime;

public class Treatment {
    private long id;
    private final long patientId;
    private long caregiverId;
    private LocalDate date;
    private LocalTime begin;
    private LocalTime end;
    private TreatmentType type;
    private String remarks;

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

    public long getId() {
        return id;
    }

    public long getPatientId() {
        return this.patientId;
    }

    public String getDate() {
        return date.toString();
    }

    public String getBegin() {
        return begin.toString();
    }

    public String getEnd() {
        return end.toString();
    }

    public void setDate(String s_date) {
        if (s_date == null || s_date.equals("")) throw new IllegalArgumentException("Datum darf nicht leer sein.");

        this.date = DateConverter.convertStringToLocalDate(s_date);

    }

    public void setBegin(String begin) {
        if (begin.equals("")) throw new IllegalArgumentException("Begin darf nicht leer sein.");

        this.begin = DateConverter.convertStringToLocalTime(begin);
    }

    public void setEnd(String end) {
        if (end.equals("")) throw new IllegalArgumentException("Ende darf nicht leer sein.");

        this.end = DateConverter.convertStringToLocalTime(end);

    }

    public TreatmentType getType() {
        return type;
    }

    public void setType(TreatmentType type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        if (remarks.equals("")) throw new IllegalArgumentException("Bemerkungen müssen angegeben werden.");

        this.remarks = remarks;
    }

    public long getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(long caregiverId) {
        this.caregiverId = caregiverId;
    }

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