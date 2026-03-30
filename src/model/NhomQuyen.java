
package model;

import java.util.Objects;

public class NhomQuyen {
    private int manhomquyen;
    private String tennhomquyen;

    public NhomQuyen() {
    }

    public NhomQuyen(int manhomquyen, String tennhomquyen) {
        this.manhomquyen = manhomquyen;
        this.tennhomquyen = tennhomquyen;
    }

    public int getManhomquyen() {
        return manhomquyen;
    }

    public void setManhomquyen(int manhomquyen) {
        this.manhomquyen = manhomquyen;
    }

    public String getTennhomquyen() {
        return tennhomquyen;
    }

    public void setTennhomquyen(String tennhomquyen) {
        this.tennhomquyen = tennhomquyen;
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
        final NhomQuyen other = (NhomQuyen) obj;
        if (this.manhomquyen != other.manhomquyen) {
            return false;
        }
        return Objects.equals(this.tennhomquyen, other.tennhomquyen);
    }

    @Override
    public String toString() {
        return "NhomQuyenDTO{" + "manhomquyen=" + manhomquyen + ", tennhomquyen=" + tennhomquyen + '}';
    }
    
}
