package model;

import utils.DateConverter;

import java.time.LocalDate;
import java.time.LocalTime;

public class Treatment {
    private long id;
    private long patientId;
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
        LocalDate date = DateConverter.convertStringToLocalDate(s_date);
        this.date = date;
    }

    public void setBegin(String begin) {
        LocalTime time = DateConverter.convertStringToLocalTime(begin);
        this.begin = time;
    }

    public void setEnd(String end) {
        LocalTime time = DateConverter.convertStringToLocalTime(end);
        this.end = time;
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