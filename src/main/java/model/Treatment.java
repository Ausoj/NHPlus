package model;

import utils.DateConverter;

import java.time.LocalDate;
import java.time.LocalTime;

public class Treatment {
    private long tid;
    private long pid;
    private long cid;
    private LocalDate date;
    private LocalTime begin;
    private LocalTime end;
    private TreatmentType type;
    private String remarks;

    public Treatment(long pid, long cid, LocalDate date, LocalTime begin,
                     LocalTime end, TreatmentType type, String remarks) {
        this.pid = pid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.type = type;
        this.remarks = remarks;
        this.cid = cid;
    }

    public Treatment(long tid, long pid, LocalDate date, LocalTime begin,
                     LocalTime end, TreatmentType type, String remarks) {
        this.tid = tid;
        this.pid = pid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.type = type;
        this.remarks = remarks;
    }

    public long getTid() {
        return tid;
    }

    public long getPid() {
        return this.pid;
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

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String toString() {
        return "\nBehandlung" + "\nTID: " + this.tid +
                "\nPID: " + this.pid +
                "\nDate: " + this.date +
                "\nBegin: " + this.begin +
                "\nEnd: " + this.end +
                "\nType: " + this.type +
                "\nRemarks: " + this.remarks + "\n";
    }
}