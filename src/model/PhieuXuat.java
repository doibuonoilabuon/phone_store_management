package model;

import java.sql.Timestamp;
import java.util.Objects;

public class PhieuXuat extends Phieu {
    
    private String makh;

    public PhieuXuat() {
    }

    public PhieuXuat(String makh) {
        this.makh = makh;
    }

    // Đã đổi double tongTien thành long tongTien để khớp với lớp cha Phieu
    public PhieuXuat(String makh, int maphieu, int manguoitao, Timestamp thoigiantao, long tongTien, int trangthai) {
        super(maphieu, manguoitao, thoigiantao, tongTien, trangthai);
        this.makh = makh;
    }

    public String getMakh() {
        return makh;
    }

    public void setMakh(String makh) {
        this.makh = makh;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.makh);
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
        final PhieuXuat other = (PhieuXuat) obj;
        return Objects.equals(this.makh, other.makh);
    }

    @Override
    public String toString() {
        return "PhieuXuat{" + "makh=" + makh + '}' + super.toString();
    }
}