package com.kmk.imageboard.model;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "request_data")
public class RequestData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "session_id", unique = true)
    private String sessionId;

    @Column(name = "browser")
    private String browser;

    @Column(name = "os")
    private String os;

    @Column(name = "time_of_request")
    private LocalTime time;

    public RequestData() {
    }

    public RequestData(String sessionId, String browser, String os, LocalTime time) {
        this.sessionId = sessionId;
        this.browser = browser;
        this.os = os;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
