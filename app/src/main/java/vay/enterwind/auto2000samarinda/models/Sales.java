package vay.enterwind.auto2000samarinda.models;

/**
 * Created by novay on 11/03/18.
 */

public class Sales {

    String email, foto, nama, telepon, alamat, status, planning, reported, remains;

    public Sales() {

    }

    public Sales(String email, String foto, String nama, String telepon, String alamat, String status, String planning, String reported, String remains) {
        this.email = email;
        this.foto = foto;
        this.nama = nama;
        this.telepon = telepon;
        this.alamat = alamat;
        this.status = status;
        this.planning = planning;
        this.reported = reported;
        this.remains = remains;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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

    public String getPlanning() {
        return planning;
    }

    public void setPlanning(String planning) {
        this.planning = planning;
    }

    public String getReported() {
        return reported;
    }

    public void setReported(String reported) {
        this.reported = reported;
    }

    public String getRemains() {
        return remains;
    }

    public void setRemains(String remains) {
        this.remains = remains;
    }
}
