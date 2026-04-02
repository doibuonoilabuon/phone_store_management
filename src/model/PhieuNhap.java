package model;

import java.sql.Timestamp;
import java.util.Objects;

public class PhieuNhap extends Phieu {
    
    private String manhacungcap; 

    public PhieuNhap() {
    }

    public PhieuNhap(String manhacungcap) {
        this.manhacungcap = manhacungcap;
    }

    public PhieuNhap(String manhacungcap, int maphieu, int manguoitao, Timestamp thoigiantao, long tongTien, int trangthai) {
        super(maphieu, manguoitao, thoigiantao, tongTien, trangthai);
        this.manhacungcap = manhacungcap;
    }

    public String getManhacungcap() {
        return manhacungcap;
    }

    public void setManhacungcap(String manhacungcap) {
        this.manhacungcap = manhacungcap;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.manhacungcap);
        return hash;
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
        final PhieuNhap other = (PhieuNhap) obj;
        return Objects.equals(this.manhacungcap, other.manhacungcap);
    }

    @Override
    public String toString() {
        return "PhieuNhap{" + "manhacungcap=" + manhacungcap + '}' + super.toString();
    }
}