package model;

public class SanPham {
    private String maSP;
    private String tenSP;
    private String thuongHieu;
    private double donGia;
    private int soLuongTon;
    private String mauSac;
    private String dungLuong;
    private String ram;

    public SanPham() {
    }

    public SanPham(String maSP, String tenSP, String thuongHieu, double donGia, int soLuongTon, String mauSac, String dungLuong, String ram) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.thuongHieu = thuongHieu;
        this.donGia = donGia;
        this.soLuongTon = soLuongTon;
        this.mauSac = mauSac;
        this.dungLuong = dungLuong;
        this.ram = ram;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getThuongHieu() {
        return thuongHieu;
    }

    public void setThuongHieu(String thuongHieu) {
        this.thuongHieu = thuongHieu;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    public String getDungLuong() {
        return dungLuong;
    }

    public void setDungLuong(String dungLuong) {
        this.dungLuong = dungLuong;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }
}
