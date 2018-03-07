package vay.enterwind.auto2000samarinda.models;

/**
 * Created by novay on 07/03/18.
 */

public class Plan {

    private String nama, alamat, status, uuid;

    public Plan(String nama, String alamat, String status, String uuid) {
        this.nama = nama;
        this.alamat = alamat;
        this.status = status;
        this.uuid = uuid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
