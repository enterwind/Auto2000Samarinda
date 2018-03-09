package vay.enterwind.auto2000samarinda.models;

/**
 * Created by novay on 09/03/18.
 */

public class Reference {

    private String uuid, nama, telepon, alamat, catatan, jenis, status, oleh, kepada;

    public Reference() {
    }

    public Reference(String uuid, String nama, String telepon, String alamat, String catatan, String jenis, String status, String oleh, String kepada) {
        this.uuid = uuid;
        this.nama = nama;
        this.telepon = telepon;
        this.alamat = alamat;
        this.catatan = catatan;
        this.jenis = jenis;
        this.status = status;
        this.oleh = oleh;
        this.kepada = kepada;
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

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOleh() {
        return oleh;
    }

    public void setOleh(String oleh) {
        this.oleh = oleh;
    }

    public String getKepada() {
        return kepada;
    }

    public void setKepada(String kepada) {
        this.kepada = kepada;
    }
}
