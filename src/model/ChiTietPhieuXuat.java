package model;

public class ChiTietPhieuXuat {
    private int maphieu;
    private String masp;
    private int soluong;
    private long dongia;

    public ChiTietPhieuXuat() {
    }

    public ChiTietPhieuXuat(int maphieu, String masp, int soluong, long dongia) {
        this.maphieu = maphieu;
        this.masp = masp;
        this.soluong = soluong;
        this.dongia = dongia;
    }

    public int getMaphieu() {
        return maphieu;
    }

    public void setMaphieu(int maphieu) {
        this.maphieu = maphieu;
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public long getDongia() {
        return dongia;
    }

    public void setDongia(long dongia) {
        this.dongia = dongia;
    }

    @Override
    public String toString() {
        return "ChiTietPhieuXuat{" + "maphieu=" + maphieu + ", masp=" + masp + ", soluong=" + soluong + ", dongia=" + dongia + '}';
    }
}