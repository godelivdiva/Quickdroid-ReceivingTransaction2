package com.quick.receivingtransaction;

public class listData {
    String KodeBarang;
    String Penyerahan;
    String _id;
    String Locator;
    String Subinv;
    String Produk;
    String Keterangan;
    String Exp;
    String no_id;
    String Permintaan;
    String Serah;
    String Satuan;
    String Cabang;
    String Lokasi;
    String Seksi;

    public listData(String KodeBarang, String Penyerahan,
                    String _id, String Subinv, String Locator, String Produk, String Keterangan, String Exp, String no_id, String Permintaan, String Serah, String Satuan, String Cabang, String Lokasi, String Seksi) {

        this.KodeBarang = KodeBarang;
        this.Penyerahan = Penyerahan;
        this._id = _id;
        this.Subinv = Subinv;
        this.Locator = Locator;
        this.Produk = Produk;
        this.Keterangan = Keterangan;
        this.Exp = Exp;
        this.no_id = no_id;
        this.Permintaan = Permintaan;
        this.Serah = Serah;
        this.Satuan = Satuan;
        this.Cabang = Cabang;
        this.Lokasi = Lokasi;
        this.Seksi = Seksi;
    }

    public String getKodeBarang() {
        return KodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        KodeBarang = kodeBarang;
    }

    public String getPenyerahan() {
        return Penyerahan;
    }

    public void setPenyerahan(String penyerahan) {
        Penyerahan = penyerahan;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSubinv() {
        return Subinv;
    }

    public void setSubinv(String subinv) {
        Subinv = subinv;
    }

    public String getLocator() {
        return Locator;
    }

    public void setLocator(String locator) {
        Locator = locator;
    }

    public String getProduk() {
        return Produk;
    }

    public void setProduk(String produk) {

        Produk = produk;
    }

    public String getKeterangan() {
        return Keterangan;
    }

    public void setKeterangan(String keterangan) {
        Keterangan = keterangan;
    }

    public String getExp() {
        return Exp;
    }

    public void setExp(String exp) {
        Exp = exp;
    }

    public String getNo_id() {
        return no_id;
    }

    public void setNo_id(String no_id) {
        this.no_id = no_id;
    }

    public String getPermintaan() {
        return Permintaan;
    }

    public void setPermintaan(String permintaan) {
        Permintaan = permintaan;
    }

    public String getSerah() {
        return Serah;
    }

    public void setSerah(String serah) {
        Serah = serah;
    }

    public String getSatuan() {
        return Satuan;
    }

    public void setSatuan(String satuan) {
        Satuan = satuan;
    }
    public String getCabang() {
        return Cabang;
    }

    public void setCabang(String cabang) {
        Cabang = cabang;
    }

    public String getLokasi() {
        return Lokasi;
    }

    public void setLokasi(String lokasi) {
        Lokasi = lokasi;
    }

    public String getSeksi() {
        return Seksi;
    }

    public void setSeksi(String seksi) {
        this.Seksi = seksi;
    }
}
