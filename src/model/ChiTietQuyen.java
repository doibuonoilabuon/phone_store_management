
package model;

import java.util.Objects;


public class ChiTietQuyen {
    private int manhomquyen;
    private String machucnang;
    private String hanhdong;

    public ChiTietQuyen() {
    }

    public ChiTietQuyen(int manhomquyen, String machucnang, String hanhdong) {
        this.manhomquyen = manhomquyen;
        this.machucnang = machucnang;
        this.hanhdong = hanhdong;
    }

    public int getManhomquyen() {
        return manhomquyen;
    }

    public void setManhomquyen(int manhomquyen) {
        this.manhomquyen = manhomquyen;
    }

    public String getMachucnang() {
        return machucnang;
    }

    public void setMachucnang(String machucnang) {
        this.machucnang = machucnang;
    }

    public String getHanhdong() {
        return hanhdong;
    }

    public void setHanhdong(String hanhdong) {
        this.hanhdong = hanhdong;
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
        final ChiTietQuyen other = (ChiTietQuyen) obj;
        if (!Objects.equals(this.manhomquyen, other.manhomquyen)) {
            return false;
        }
        if (!Objects.equals(this.machucnang, other.machucnang)) {
            return false;
        }
        return Objects.equals(this.hanhdong, other.hanhdong);
    }

    @Override
    public String toString() {
        return "ChiTietQuyen{" + "manhomquyen=" + manhomquyen + ", machucnang=" + machucnang + ", hanhdong=" + hanhdong + '}';
    }

    
    
}
