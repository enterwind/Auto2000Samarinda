package vay.enterwind.auto2000samarinda.models;

/**
 * Created by novay on 07/03/18.
 */

public class Plan {

    private String nama, telepon, alamat, status, uuid, longitude, latitude;

    public Plan(String uuid, String nama, String telepon, String alamat, String status, String longitude, String latitude) {
        this.uuid = uuid;
        this.nama = nama;
        this.telepon = telepon;
        this.alamat = alamat;
        this.status = status;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

}
