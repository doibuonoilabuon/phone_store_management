package model;



import java.sql.Timestamp;
import java.util.Objects;


public class Phieu {
    private int maphieu;
    private int manguoitao;
    private Timestamp thoigiantao;
    private long tongTien;
    private int trangthai;

    public Phieu() {
    }

    public Phieu(int maphieu, int manguoitao, Timestamp thoigiantao, long tongTien, int trangthai) {
        this.maphieu = maphieu;
        this.manguoitao = manguoitao;
        this.thoigiantao = thoigiantao;
        this.tongTien = tongTien;
        this.trangthai = trangthai;
    }
    
    public int getMaphieu() {
        return maphieu;
    }

    public void setMaphieu(int maphieu) {
        this.maphieu = maphieu;
    }

    public int getManguoitao() {
        return manguoitao;
    }

    public void setManguoitao(int manguoitao) {
        this.manguoitao = manguoitao;
    }

    public Timestamp getThoigiantao() {
        return thoigiantao;
    }

    public void setThoigiantao(Timestamp thoigiantao) {
        this.thoigiantao = thoigiantao;
    }

    public long getTongTien() {
        return tongTien;
    }

    public void setTongTien(long tongTien) {
        this.tongTien = tongTien;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Phieu other = (Phieu) obj;
        if (this.maphieu != other.maphieu) {
            return false;
        }
        if (this.manguoitao != other.manguoitao) {
            return false;
        }
        if (this.tongTien != other.tongTien) {
            return false;
        }
        if (this.trangthai != other.trangthai) {
            return false;
        }
        return Objects.equals(this.thoigiantao, other.thoigiantao);
    }

    @Override
    public String toString() {
        return "Phieu{" + "maphieu=" + maphieu + ", manguoitao=" + manguoitao + ", thoigiantao=" + thoigiantao + ", tongTien=" + tongTien + ", trangthai=" + trangthai + '}';
    }

    
}
